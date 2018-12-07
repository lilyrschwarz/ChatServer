package chatroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Lily Schwarz
 */
public class ChatClient extends ChatWindow
{

	// Inner class used for networking
	private Communicator comm;


	// GUI Objects
	private JTextField serverText;
	private JTextField nameText;
	private JButton connectButton;
	private JTextField messageText;
	private JButton sendButton;

	/**
	 * Sets up display of Client window
	 */
	public ChatClient()
	{
		super();
		this.setTitle("Chat Client");
		printMsg("Chat Client Started.");

		// GUI elements at top of window
		// Need a Panel to store several buttons/text fields
		serverText = new JTextField("localhost");
		serverText.setColumns(15);
		nameText = new JTextField("Name");
		nameText.setColumns(10);
		connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
		topPanel.add(serverText);
		topPanel.add(nameText);
		topPanel.add(connectButton);
		contentPane.add(topPanel, BorderLayout.NORTH);

		// GUI elements and panel at bottom of window
		messageText = new JTextField("");
		messageText.setColumns(40);
		sendButton = new JButton("Send");
		JPanel botPanel = new JPanel();
		botPanel.add(messageText);
		botPanel.add(sendButton);
		contentPane.add(botPanel, BorderLayout.SOUTH);

		// Resize window to fit all GUI components
		this.pack();

		// Setup the communicator so it will handle the connect button
		Communicator comm = new Communicator();
		connectButton.addActionListener(comm);
		sendButton.addActionListener(comm);

	}

	/** This inner class handles communication with the server. */
	/**
	 * Client Communicates with Server
	 */
	class Communicator implements ActionListener, Runnable
	{
		private Socket socket;
		private PrintWriter writer;
		private BufferedReader reader;
		private int port = 2113;

		/** Connect/ Send Button commands **/
		@Override
		public void actionPerformed(ActionEvent actionEvent) {

			if (actionEvent.getActionCommand().compareTo("Connect") == 0) {
				connect();

			}
			else if (actionEvent.getActionCommand().compareTo("Send") == 0)
			{
				sendMsg(messageText.getText());

			}



		}

		public void run()
		{

			try {
				while (true) {
					String s = readMsg();
				}
			}catch (IOException e)
			{
				printMsg("\nERROR Server:" + e.getLocalizedMessage() + "\n");
			}
		}


		/** Connect to the remote server and setup input/output streams. */
		public void connect(){
			try {
				socket = new Socket(serverText.getText(), port);
				InetAddress serverIP = socket.getInetAddress();
				printMsg("Connection made to " + serverIP);
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Thread thread = new Thread(this);
				thread.start();

				sendMsg("Hello server");


			}
			catch(IOException e)
			{
				printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
			}
		}

		/** Receive and display a message */
		public String readMsg() throws IOException
		{

				String s = reader.readLine();
				printMsg(s);

				return s;

		}
		

		/** Send a string */
		public void sendMsg(String s){
			writer.println(s);
		}
	}


	public static void main(String args[])
	{
		new ChatClient();
	}

}