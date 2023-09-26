package com.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.api.Codes;
import com.controller.handlers.IMSGHandler;
import com.controller.handlers.IPKGHandler;
import com.data.MSG;
import com.data.MSG.Type;
import com.data.PKG;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public abstract class Connection extends Thread implements Codes {

	private long mId;
	private Socket mSocket = null;
	private String mNick = "Nameless";
	private ObjectInputStream mOis = null;
	private ObjectOutputStream mOos = null;

	private IMSGHandler mMSGHandler;
	private IPKGHandler mPKGHandler;

	private String mHostName = "localhost";
	private int mPort = 8080;

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
	public Connection(String nick, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
		mNick = nick;
		mMSGHandler = msgHandler;
		mPKGHandler = pckgHandler;
		try {
			initConnection();
		} catch (IOException e) {
			reconnect();
		}
	}

	/**
	 * 
	 * @param nick
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
	public Connection(String nick, final String HOSTNAME, final int PORT, IMSGHandler msgHandler,
			IPKGHandler pckgHandler) {
		mNick = nick;
		mHostName = HOSTNAME;
		mPort = PORT;
		mMSGHandler = msgHandler;
		mPKGHandler = pckgHandler;
		try {
			initConnection(mHostName, mPort);
		} catch (IOException e) {
			reconnect();
		}
	}

	/**
	 * Used to instance a connection from a SERVER-SIDE-ServerSocket listener
	 * 
	 * @param socket      recieved from a ServerSocket.accept();
	 * @param msgHandler
	 * @param pckgHandler
	 */
	public Connection(Socket socket, IMSGHandler msgHandler, IPKGHandler pckgHandler) {
		mSocket = socket;
		mMSGHandler = msgHandler;
		mPKGHandler = pckgHandler;
		try {
			mOos = new ObjectOutputStream(mSocket.getOutputStream());
			mOis = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param socket
	 */
	public Connection(Socket socket) {
		mSocket = socket;
		try {
			mOos = new ObjectOutputStream(mSocket.getOutputStream());
			mOis = new ObjectInputStream(mSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* PRVATE METHODS */
	/**
	 * 
	 * @throws IOException
	 */
	private void initConnection() throws IOException {
		mSocket = new Socket("localhost", 8080);
		mOos = new ObjectOutputStream(mSocket.getOutputStream());
		mOis = new ObjectInputStream(mSocket.getInputStream());
		presentation();
	}

	/**
	 * 
	 * @param HOSTNAME
	 * @param PORT
	 * @throws IOException
	 */
	private void initConnection(final String HOSTNAME, final int PORT) throws IOException {
		mSocket = new Socket(HOSTNAME, PORT);
		mOos = new ObjectOutputStream(mSocket.getOutputStream());
		mOis = new ObjectInputStream(mSocket.getInputStream());
		presentation();
	}

	/** */
	public void presentation() {
		if (presentToServer()) {
			System.out.println(INFO_CONECXION_ACCEPTED);
			System.out.println("Listening on PORT: " + mPort);
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
			mOos.writeObject(presentation);
		} catch (NullPointerException e) {
			System.err.println("Null pointer Exception Could not write presentation");
		} catch (IOException e) {
			System.err.println("IO exception could not write presentation");
		}

		MSG presentationResponse = null;
		try {
			presentationResponse = (MSG) mOis.readObject();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found in presentation Response");
		} catch (IOException e) {
			System.err.println("IO exception at reading preseentation response");
		}

		if (presentationResponse == null) {
			System.out.println("Could not recieve presentation, presentationResponse = null");
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
		}
		if (obj instanceof PKG) {
			System.out.println("PCKG_OUT==>" + obj.toString());
		}
		mOos.writeObject(obj);
	}

	public Object read() throws ClassNotFoundException, IOException {
		return mOis.readObject();
	}

	private void listenMsg(MSG msg) {
		switch (msg.MSG_TYPE) {

			case REQUEST:
				mMSGHandler.handleRequest(msg);
				break;

			case MESSAGE:
				mMSGHandler.handleMessage(msg);
				break;

			case ERROR:
				mMSGHandler.handleError(msg);
				break;

			default:
				mMSGHandler.unHandledMSG(msg);
				break;
		}
	}

	private void listenPckg(PKG pkg) {
		switch (pkg.PACKAGE_TYPE) {

			case MIXED:
				mPKGHandler.handleMixed(pkg);
				break;

			case COLLECTION:
				mPKGHandler.handleCollection(pkg);
				break;

			default:
				mPKGHandler.unHandledPKG(pkg);
				break;

		}
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

	public String toXML() {
		return "<connection id=" + mId + " nick=" + mNick + ">";
	}

	public String getReference() {
		return mId + "_" + mNick;
	}
}
