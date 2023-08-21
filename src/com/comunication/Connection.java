package com.comunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.chat.Member;
import com.comunication.MSG.MsgType;
import com.comunication.handlers.IMsgHandler;
import com.comunication.handlers.IPckgHandler;

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

	private IMsgHandler pMSGHANDLER;
	private IPckgHandler pPCKGHANDLER;

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

	public void setMsgHandler(IMsgHandler msgHandler) {
		pMSGHANDLER = msgHandler;
	}

	public void setPckgHandler(IPckgHandler pckgHandler) {
		pPCKGHANDLER = pckgHandler;
	}

	/* CONSTRUCTORS */
	/**
	 * Instance a connection with hostname = 'localhost' and port =8080
	 * 
	 * @param nick of the user of the connection
	 */
	public Connection(String nick, IMsgHandler msgHandler, IPckgHandler pckgHandler) {
		mNick = nick;
		pMSGHANDLER = msgHandler;
		pPCKGHANDLER = pckgHandler;
		try {
			initConnection();
		} catch (IOException e) {
			reconnect();
		}
	}

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
	public Connection(String nick, final String HOSTNAME, final int PORT, IMsgHandler msgHandler,
			IPckgHandler pckgHandler) {
		mNick = nick;
		hostName = HOSTNAME;
		port = PORT;
		pMSGHANDLER = msgHandler;
		pPCKGHANDLER = pckgHandler;
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
	public Connection(Socket socket, IMsgHandler msgHandler, IPckgHandler pckgHandler) {
		mSocket = socket;
		pMSGHANDLER = msgHandler;
		pPCKGHANDLER = pckgHandler;
		try {
			oos = new ObjectOutputStream(mSocket.getOutputStream());
			ois = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		MSG presentation = new MSG(MsgType.REQUEST);
		presentation.setAction(REQ_PRESENT);
		presentation.setEmisor(getNick());

		writeMessage(presentation);

		MSG presentationResponse = readMessage();

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

	public void write(Object obj) {
		if (obj instanceof MSG) {
			System.out.println("MSG_OUT==>" + obj.toString());
			writeMessage((MSG) obj);
		}
		if (obj instanceof PKG) {
			System.out.println("PCKG_OUT==>" + obj.toString());
			writePackage((PKG) obj);
		}
	}

	public void writeMessage(MSG msg) {
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

	public void writePackage(PKG pckg) {
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

	public Object read() {
		try {
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			IExceptionListener.onException(e);
		} catch (IOException e) {
			IExceptionListener.onException(e);
		}

		return null;
	}

	/**
	 * @return Msg
	 * 
	 */
	public MSG readMessage() {
		try {
			return (MSG) ois.readObject();
		} catch (ClassNotFoundException e) {
			IExceptionListener.onException(e);
		} catch (IOException e) {
			IExceptionListener.onException(e);
		}

		return null;
	}

	public PKG readPackage() {
		try {
			return (PKG) ois.readObject();
		} catch (ClassNotFoundException e) {
			IExceptionListener.onException(e);
		} catch (IOException e) {
			IExceptionListener.onException(e);
		}

		return null;
	}

	public void listenMsg(MSG msg) {
		pMSGHANDLER.handleMsg(msg);
	}

	public void listenPckg(PKG pckg) {
		pPCKGHANDLER.handlePckg(pckg);
	}

	public void listen() {

		Object o = read();

		if (o instanceof MSG) {
			System.out.println("<==MSG_IN_" + o.toString());
			MSG msg = (MSG) o;
			listenMsg(msg);
		}

		if (o instanceof PKG) {
			System.out.println("<==PKG_IN_" + o.toString());
			PKG pckg = (PKG) o;
			listenPckg(pckg);
		}

	}

	public interface IExceptionListener {
		static void onException(Exception e) {
			System.out.println("Could not read the MSG" + e.getClass());
		}
	}

	public Member asMember() {
		return Member.regular(String.valueOf(mId), mNick);
	}

	public String toXML() {
		return "<connection id=" + mId + " nick=" + mNick + ">";
	}

}
