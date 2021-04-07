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
import javax.swing.JButton;
import javax.swing.JTextArea;



public class Server {

	private JFrame frame;
	private JTextField textField;
	
	private int port;
	private ServerSocket listeningSocket = null;
	private Socket clientSocket = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel.setBounds(194, 6, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(154, 109, 130, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Port to listen");
		lblNewLabel_1.setBounds(52, 114, 90, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.setBounds(309, 109, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("Server Status:");
		lblNewLabel_2.setBounds(126, 54, 90, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Inactive");
		lblNewLabel_3.setBounds(228, 54, 183, 16);
		frame.getContentPane().add(lblNewLabel_3);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				port = Integer.parseInt(textField.getText());
				
				// Start a new thread for a connection
				Thread t = new Thread(() -> {
					try {
						SetPortToListen();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				t.start();
				lblNewLabel_1.setEnabled(false);
				btnNewButton.setEnabled(false);
				textField.setEnabled(false);
				lblNewLabel_3.setText("Listening on port " + port);

			}
		});
	}
	
	
	private void SetPortToListen() throws IOException {
		//Create a server socket listening on port
		listeningSocket = new ServerSocket(port);
		
		//Listen for incoming connections for ever 
		while (true) 
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
