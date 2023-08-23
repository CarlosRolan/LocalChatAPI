package com.comunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.chat.Chat;
import com.chat.Member;
import com.comunication.MSG.Type;
import com.comunication.handlers.IMSGHandler;
import com.comunication.handlers.IPKGHandler;

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
	private ObjectInputStream pOis = null;
	private ObjectOutputStream pOos = null;

	private IMSGHandler pMSGHANDLER;
	private IPKGHandler pPKGHANDLER;

	private String pHostName = "localhost";
	private int pPort = 8080;

	private List<String> chatsRefList;

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
	public ObjectInputStream getpOis() {
		return pOis;
	}

	/**
	 * 
	 * @return Connection's ObjectOutPutStream
	 */
	public ObjectOutputStream getpOos() {
		return pOos;
	}

	public List<Chat> getChats() {
		List<Chat> toret = new ArrayList<>();

		for (String chatRef : chatsRefList) {
			Chat fromRef = Chat.makeFromRef(chatRef);
			toret.add(fromRef);
		}

		return toret;
	}

	public String[] getChatsRef() {
		return (String[]) chatsRefList.toArray();
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

	public void setMsgHandler(IMSGHandler msgHandler) {
		pMSGHANDLER = msgHandler;
	}

	public void setPckgHandler(IPKGHandler pckgHandler) {
		pPKGHANDLER = pckgHandler;
	}

	/* CONSTRUCTORS */
	/**
	 * Instance a connection with hostname = 'localhost' and port =8080
	 * 
	 * @param nick of the user of the connection
	 */
	public Connection(String nick, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
		mNick = nick;
		pMSGHANDLER = msgHandler;
		pPKGHANDLER = pckgHandler;
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
	public Connection(String nick, final String HOSTNAME, final int PORT, IMSGHandler msgHandler,
			IPKGHandler pckgHandler) {
		mNick = nick;
		pHostName = HOSTNAME;
		pPort = PORT;
		pMSGHANDLER = msgHandler;
		pPKGHANDLER = pckgHandler;
		try {
			initConnection(pHostName, pPort);
		} catch (IOException e) {
			reconnect();
		}
	}

	/**
	 * Used to instance a connection from a SERVER-SIDE-ServerSocket listener
	 * 
	 * @param socket recieved from a ServerSocket.accept();
	 */
	public Connection(Socket socket, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
		mSocket = socket;
		pMSGHANDLER = msgHandler;
		pPKGHANDLER = pckgHandler;
		try {
			pOos = new ObjectOutputStream(mSocket.getOutputStream());
			pOis = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Connection(Socket socket) {
		mSocket = socket;
		try {
			pOos = new ObjectOutputStream(mSocket.getOutputStream());
			pOis = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* PRVATE METHODS */
	private void initConnection() throws IOException {
		mSocket = new Socket("localhost", 8080);
		pOos = new ObjectOutputStream(mSocket.getOutputStream());
		pOis = new ObjectInputStream(mSocket.getInputStream());
		presentation();
	}

	private void initConnection(final String HOSTNAME, final int PORT) throws IOException {
		mSocket = new Socket(HOSTNAME, PORT);
		pOos = new ObjectOutputStream(mSocket.getOutputStream());
		pOis = new ObjectInputStream(mSocket.getInputStream());
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
			System.out.println("Listening on PORT: " + pPort);
		} else {
			System.out.println(INFO_CONECXION_REJECTED);
		}
	}

	/**
	 * @return TRUE if the presentation has beeen suscessfull. FALSE if could no
	 *         get the confirmation from the server
	 */
	private boolean presentToServer() {
		MSG presentation = new MSG(Type.REQUEST);
		presentation.setAction(REQ_PRESENT);
		presentation.setEmisor(getNick());

		try {
			writeMessage(presentation);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MSG presentationResponse = null;
		try {
			presentationResponse = readMessage();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public void write(Object obj) throws SocketException, IOException {
		if (obj instanceof MSG) {
			System.out.println("MSG_OUT==>" + obj.toString());
			writeMessage((MSG) obj);
		}
		if (obj instanceof PKG) {
			System.out.println("PCKG_OUT==>" + obj.toString());
			writePackage((PKG) obj);
		}
	}

	public void writeMessage(MSG msg) throws IOException, NullPointerException {
		pOos.writeObject(msg);
		pOos.flush();
	}

	public void writePackage(PKG pckg) throws IOException, NullPointerException {
		pOos.writeObject(pckg);
		pOos.flush();
	}

	public Object read() throws ClassNotFoundException, IOException {
		return pOis.readObject();
	}

	/**
	 * @return Msg
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 */
	public MSG readMessage() throws ClassNotFoundException, IOException {
		return (MSG) pOis.readObject();
	}

	public PKG readPackage() throws ClassNotFoundException, IOException {
		return (PKG) pOis.readObject();
	}

	public void listenMsg(MSG msg) {
		pMSGHANDLER.handleMsg(msg);
	}

	public void listenPckg(PKG pckg) {
		pPKGHANDLER.handlePckg(pckg);
	}

	public void listen() throws ClassNotFoundException, IOException {

		Object o = null;

		o = read();

		if (o != null) {
			if (o instanceof MSG) {
				System.out.println("<==MSG_IN_" + o.toString());
				MSG msg = (MSG) o;
				listenMsg(msg);
			}

			if (o instanceof PKG) {
				System.out.println("<==PKG_IN_" + o.toString());
				PKG pkg = (PKG) o;
				listenPckg(pkg);
			}
		}

	}

	/* PARSERS */
	public Member asMember() {
		return Member.regular(String.valueOf(mId), mNick);
	}

	public String toXML() {
		return "<connection id=" + mId + " nick=" + mNick + ">";
	}

}
