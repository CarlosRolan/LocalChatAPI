package com;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.Msg.MsgType;

public class Connection extends Thread implements ApiCodes {

	private long mId;
	private Socket mSocket = null;
	private String mNick = "Nameless";
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	private String hostName = "localhost";
	private int port = 8080;

	// Getters
	public String getConId() {
		return String.valueOf(mId);
	}

	public String getNick() {
		return mNick;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	// Setters
	public void setConId(String id) {
		mId = Long.parseLong(id);
	}

	public void setConId(Long id) {
		mId = id;
	}

	public void setNick(String nick) {
		mNick = nick;
	}

	// Constructors
	public Connection(String nick) {
		mNick = nick;
		try {
			initConnection();
		} catch (IOException e) {
			reconnect();
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

	// Private Methods
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

	private void presentation() {
		if (presentToServer()) {
			System.out.println(INFO_CONECXION_ACCEPTED);
			System.out.println("Listening on PORT: " + port);
		} else {
			System.out.println(INFO_CONECXION_REJECTED);
		}
	}

	private boolean presentToServer() {
		Msg presentation = new Msg(MsgType.REQUEST);
		presentation.setAction(PRESENT);
		presentation.setEmisor(getNick());

		writeMessage(presentation);

		Msg presentationResponse = null;
		try {
			presentationResponse = readMessage();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("COULD NOT RECIEVE CONFIRMATION");
		}

		if (INFO_PRESENTATION_SUCCES.equals(presentationResponse.getAction())) {
			mId = Integer.parseInt(presentationResponse.getReceptor());
			return true;
		} else {
			return false;
		}
	}

	// Public Methods
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
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Msg readMessage() throws ClassNotFoundException, IOException, EOFException {
		return (Msg) ois.readObject();
	}

}
