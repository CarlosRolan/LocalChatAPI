package com.comunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.comunication.Msg.MsgType;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Connection extends Thread implements ApiCodes {

	private long mId;
	private Socket mSocket = null;
	private String mNick = "Nameless";
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	private String hostName = "localhost";
	private int port = 8080;

	/* GETTERS */
	/**
	 * 
	 * @return the ID of the connection parsed as a String
	 */
	public String getConId() {
		return String.valueOf(mId);
	}

	/**
	 * 
	 * @return nickName of the user of the connection
	 */
	public String getNick() {
		return mNick;
	}

	/**
	 * 
	 * @return Connection's ObjectInPutStream
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * 
	 * @return Connection's ObjectOutPutStream
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * @param id the conection ID as a String
	 */
	/* SETTERS */
	public void setConId(String id) {
		mId = Long.parseLong(id);
	}

	public void setConId(long id) {
		mId = id;
	}

	public void setConId(Long id) {
		mId = id;
	}

	public void setNick(String nick) {
		mNick = nick;
	}

	/* CONSTRUCTORS */
	/**
	 * Instance a connection with hostname = 'localhost' and port =8080
	 * 
	 * @param nick of the user of the connection
	 */
	public Connection(String nick) {
		mNick = nick;
		try {
			initConnection();
		} catch (IOException e) {
			reconnect();
		}
	}

	/**
	 * Used to instance a connection from a client endpoint with the given params
	 * with an OutPut and InPut stream
	 * 
	 * @param nick     of the user of the connection
	 * @param HOSTNAME of the connection
	 * @param PORT     default 8080
	 * 
	 */
	public Connection(String nick, final String HOSTNAME, final int PORT) {
		mNick = nick;
		hostName = HOSTNAME;
		port = PORT;
		try {
			initConnection(hostName, port);
		} catch (IOException e) {
			reconnect();
		}
	}

	/**
	 * Used to instance a connection from a SERVER-SIDE-ServerSocket listener
	 * 
	 * @param socket recieved from a ServerSocket.accept();
	 */
	public Connection(Socket socket) {
		mSocket = socket;
		try {
			oos = new ObjectOutputStream(mSocket.getOutputStream());
			ois = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* PRVATE METHODS */
	private void initConnection() throws IOException {
		mSocket = new Socket("localhost", 8080);
		oos = new ObjectOutputStream(mSocket.getOutputStream());
		ois = new ObjectInputStream(mSocket.getInputStream());
		presentation();
	}

	private void initConnection(final String HOSTNAME, final int PORT) throws IOException {
		mSocket = new Socket(HOSTNAME, PORT);
		oos = new ObjectOutputStream(mSocket.getOutputStream());
		ois = new ObjectInputStream(mSocket.getInputStream());
		presentation();
	}

	/**
	 * This method encapsulates the presentation process from CLIENT to SERVER as
	 * well his response to the
	 * client side
	 */
	public void presentation() {
		if (presentToServer()) {
			System.out.println(INFO_CONECXION_ACCEPTED);
			System.out.println("Listening on PORT: " + port);
		} else {
			System.out.println(INFO_CONECXION_REJECTED);
		}
	}

	/**
	 * @return TRUE if the presentation has beeen suscessfull. FALSE if could no
	 *         get the confirmation from the server
	 */
	private boolean presentToServer() {
		Msg presentation = new Msg(MsgType.REQUEST);
		presentation.setAction(REQ_PRESENT);
		presentation.setEmisor(getNick());

		writeMessage(presentation);

		Msg presentationResponse = readMessage();

		if (presentationResponse == null) {
			System.out.println("COULD NOT RECIEVE CONFIRMATION");
			return false;
		} else {
			if (INFO_PRESENTATION_SUCCES.equals(presentationResponse.getAction())) {
				mId = Integer.parseInt(presentationResponse.getReceptor());
				return true;
			} else {
				return false;
			}
		}
	}

	/* PUBLIC METHODS */
	public void reconnect() {
		String dots = "";
		while (true) {
			try {
				initConnection();
			} catch (IOException e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ignored) {
				}
				System.out.print("\033[H\033[2J");
				System.out.println("Unable to coonect to the server. Trying." + dots);
				dots = dots.concat(".");
				if (dots.length() == 3) {
					dots = "";
				}
				System.out.flush();
				continue;
			}
			break;
		}
		System.out.print("\033[H\033[2J");
		System.out.println("RECONNECTED");
	}

	public void writeMessage(Msg msg) {
		if (msg != null) {
			try {
				oos.writeObject(msg);
				oos.flush();
			} catch (SocketException e) {
				IExceptionListener.onException(e);
			} catch (IOException e) {
				IExceptionListener.onException(e);
			}
		}
	}

	public void writePackage(Pckg pckg) {
		if (pckg != null) {
			try {
				oos.writeObject(pckg);
				oos.flush();
			} catch (SocketException e) {
				IExceptionListener.onException(e);
			} catch (IOException e) {
				IExceptionListener.onException(e);
			}
		}
	}

	/**
	 * @return Msg
	 * 
	 */
	public Msg readMessage() {
		try {
			return (Msg) ois.readObject();
		} catch (ClassNotFoundException e) {
			IExceptionListener.onException(e);
		} catch (IOException e) {
			IExceptionListener.onException(e);
		}

		return null;
	}

	public Pckg readPackage() {
		try {
			return (Pckg) ois.readObject();
		} catch (ClassNotFoundException e) {
			IExceptionListener.onException(e);
		} catch (IOException e) {
			IExceptionListener.onException(e);
		}

		return null;
	}

	public interface IExceptionListener {

		void onReadMsgException();

		void onWriteMsgException();

		void onPresentation();

		static void onException(Exception e) {
			System.out.println("Could not read the MSG" + e.getClass());
		}
	}

}
