package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Lily Schwarz
 */
public class ChatServer extends ChatWindow {

	private ClientHandler handler;
	public ChatServer(){
		super();
		this.setTitle("Chat Server");
		this.setLocation(80,80);

		try {
			// Create a listening service for connections
			// at the designated port number.
			ServerSocket server = new ServerSocket(2113);

			while (true)
			{
				// The method accept() blocks until a client connects.
				printMsg("Waiting for a connection");
				Socket socket = server.accept();
				handler = new ClientHandler(socket);
				handler.handleConnection();
			}

		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	/** This inner class handles communication to/from one client. */
	class ClientHandler implements Runnable{
		private PrintWriter writer;
		private BufferedReader reader;
		public String clientName = "Client Name: ";

		public ClientHandler(Socket socket) {
			try
			{
				InetAddress serverIP = socket.getInetAddress();
				printMsg("Connection made to " + serverIP);
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch (IOException e)
			{
				printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
			}
		}
		public void handleConnection() {
			try {
				while(true)
				{
					// read a message from the client
					String s = readMsg();
					sendMsg(s);
				}
			}
			catch (IOException e){
				printMsg("\nERROR Server:" + e.getLocalizedMessage() + "\n");
			}
		}

		/** Receive and display a message */
		public String readMsg() throws IOException
		{
			String s = reader.readLine();
			String t = "";
			String newName= "";
			if(s.length()>=7)
			{
				t = s.substring(0,5);
				newName = s.substring(6);
			}

			if(t.equals("/name"))
			{
				clientName = newName + ": ";
				printMsg(newName + " changed their name");
			}

			else
			{
				printMsg(clientName + s);
			}
			return s;
		}

		public void run()
		{
			this.handleConnection();
		}

		public void connect()
		{
			Thread thread = new Thread(this);
			thread.start();
		}

		/** Send a string */
		public void sendMsg(String s) throws IOException
		{
			writer.println(s);
		}

	}

	public static void main(String args[]){
		new ChatServer();
	}
}