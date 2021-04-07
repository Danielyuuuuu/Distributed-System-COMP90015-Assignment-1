package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JTextField;

import client.ExceptionHandling;

import javax.swing.JButton;
import javax.swing.JTextArea;



public class Server {

	private JFrame frame;
	
	private int port;
	private ServerSocket listeningSocket = null;
	private Socket clientSocket = null;
	
	private Thread listeningThread = null;
	
	private JLabel serverStatus = null;

	/**
	 * Launch the application.
	 * @throws ExceptionHandling 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws ExceptionHandling 
	 */
	public Server(String[] args) throws IOException, ExceptionHandling {
		if (args.length != 1) {
			throw new ExceptionHandling("No server port or dictionary file specified", "format <port> <dictionary-file>");
		}
		port = Integer.parseInt(args[0]);
		initialize();
		
		// Start a new thread to listen on a specific port
		listeningThread = new Thread(() -> 
				{
					try {
						SetPortToListen();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		listeningThread.start();
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel.setBounds(194, 6, 61, 21);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Server Status:");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblNewLabel_2.setBounds(32, 92, 118, 21);
		frame.getContentPane().add(lblNewLabel_2);
		
		serverStatus = new JLabel("Inactive");
		serverStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		serverStatus.setBounds(173, 92, 271, 21);
		frame.getContentPane().add(serverStatus);
		
		
//		btnNewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				port = Integer.parseInt(textField.getText());
//				
//				// Start a new thread to listen on a specific port
//				listeningThread = new Thread(() -> {
//					try {
//						SetPortToListen();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				});
//				listeningThread.start();
//				lblNewLabel_1.setVisible(false);
//				btnNewButton.setVisible(false);
//				textField.setVisible(false);
//				btnNewButton_1.setVisible(true);
//				lblNewLabel_3.setText("Listening on port " + port);
//					
//			}
//		});
//		
//		btnNewButton_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				lblNewLabel_1.setVisible(true);
//				btnNewButton.setVisible(true);
//				textField.setVisible(true);
//				btnNewButton_1.setVisible(false);
//				listeningThread.interrupt();
//				listeningThread = null;
//				try {
//					listeningSocket.close();
//					listeningSocket = null;
//					if (clientSocket != null) {
//						clientSocket.close();
//						clientSocket = null;
//					}
//					lblNewLabel_3.setText("Inactive");
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
		
		
	}
	
	
	private void SetPortToListen() throws IOException {
		//Create a server socket listening on port
		listeningSocket = new ServerSocket(port);
		serverStatus.setText("Listening on port: " + port);
		
		//Listen for incoming connections for ever 
		while (listeningSocket != null && true) 
		{
			System.out.println("Server listening on port " + port +  " for a connection");
			//Accept an incoming client connection request 
			clientSocket = listeningSocket.accept(); //This method will block until a connection request is received
			System.out.println("Client conection accepted:");
			System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
			System.out.println("Local Port: " + clientSocket.getLocalPort());
			
			//Get the input/output streams for reading/writing data from/to the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

			
			//Read the message from the client and reply
			//Notice that no other connection can be accepted and processed until the last line of 
			//code of this loop is executed, incoming connections have to wait until the current
			//one is processed unless...we use threads!
			String clientMsg = null;
			try 
			{
				while((clientMsg = in.readLine()) != null) 
				{
					System.out.println("Message from client: " + clientMsg);
					out.write("Server Ack " + clientMsg + "\n");
					out.flush();
					System.out.println("Response sent");
				}
			}
			
			catch(SocketException e)
			{
				System.out.println("closed...");
			}
			
			clientSocket.close();
		}
	}

}
