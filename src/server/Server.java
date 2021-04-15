/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JTextField;

import client.ExceptionHandling;
//import server.MultiThreadServer.ClientHandler;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;


public class Server {

	private JFrame frame;
	private JTextField portInputField;
	
	private int port;
	
	private static JLabel serverStatus = null;
	
//	private static Hashtable<String, ArrayList<String>> dict = new Hashtable<>();
	
	private static int counter = 0;
	
	private SetPortToListen listeningClass;
	private JTextField dictionaryFilePath;
	
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
					System.out.println(e.getMessage());
					System.exit(1);
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws ExceptionHandling 
	 * @throws ParseException 
	 */
	public Server(String[] args) throws IOException, ExceptionHandling, ParseException {
//		if (args.length != 1) {
//			throw new ExceptionHandling("No server port or dictionary file specified", "format <port> <dictionary-file>");
//		}
//		port = Integer.parseInt(args[0]);
		initialize();
		
//		// Start a new thread to listen on a specific port
//		new Thread(new SetPortToListen(port)).start();
		
//		ReadDictFile("./dictfile.json");
	}
	
	
//	private Boolean ReadDictFile(String filePath) throws FileNotFoundException, IOException, ParseException {        
//		File f = new File(filePath);
//		if (f.exists()) {
//			Object fileObject = new JSONParser().parse(new FileReader(filePath));
//	        JSONObject fileJSONObject = (JSONObject) fileObject;
//	        
//	        for(Object word : fileJSONObject.keySet()) {
//	
//	        	ArrayList<String> translationArrayList = new ArrayList<>();
//	        	JSONArray array = (JSONArray) fileJSONObject.get(word);
//	
//	        	for(int i = 0; i < array.size(); i++) {
//	        		translationArrayList.add((String) array.get(i));
//	        	}
//	        	dict.put(word.toString().toLowerCase(), translationArrayList);
//	        }
//	        return true;
//		}        
//		return false;
//	}


	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
//		frame = new JFrame();
//		
//		frame.setBounds(100, 100, 450, 300);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(null);
//		
//		JLabel lblNewLabel = new JLabel("Server");
//		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
//		lblNewLabel.setBounds(194, 6, 61, 21);
//		frame.getContentPane().add(lblNewLabel);
//		
//		JLabel lblNewLabel_2 = new JLabel("Server Status:");
//		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
//		lblNewLabel_2.setBounds(32, 92, 118, 21);
//		frame.getContentPane().add(lblNewLabel_2);
//		
//		serverStatus = new JLabel("Inactive");
//		serverStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
//		serverStatus.setBounds(173, 92, 271, 21);
//		frame.getContentPane().add(serverStatus);		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel serverLabel = new JLabel("Server");
		serverLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		serverLabel.setBounds(194, 6, 61, 16);
		frame.getContentPane().add(serverLabel);
		
		portInputField = new JTextField();
		portInputField.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		portInputField.setBounds(153, 120, 136, 26);
		frame.getContentPane().add(portInputField);
		portInputField.setColumns(10);
		
		JLabel portToListenLabel = new JLabel("Port to listen:");
		portToListenLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		portToListenLabel.setBounds(27, 123, 113, 21);
		frame.getContentPane().add(portToListenLabel);
		
		JButton enterButton = new JButton("Enter");
		enterButton.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		enterButton.setBounds(172, 223, 117, 29);
		frame.getContentPane().add(enterButton);
		
		JLabel serverStatusLabel = new JLabel("Server Status:");
		serverStatusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		serverStatusLabel.setBounds(47, 125, 113, 16);
		frame.getContentPane().add(serverStatusLabel);
		serverStatusLabel.setVisible(false);

		
//		JLabel lblNewLabel_3 = new JLabel("Inactive");
//		lblNewLabel_3.setBounds(228, 54, 183, 16);
//		frame.getContentPane().add(lblNewLabel_3);	
		serverStatus = new JLabel("Inactive");
		serverStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		serverStatus.setBounds(172, 123, 271, 21);
		frame.getContentPane().add(serverStatus);	
		serverStatus.setVisible(false);
		
		JLabel errorMessage = new JLabel("error text");
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		errorMessage.setBounds(27, 60, 400, 26);
		frame.getContentPane().add(errorMessage);
		errorMessage.setVisible(false);
		errorMessage.setHorizontalAlignment(JLabel.CENTER);
		errorMessage.setVerticalAlignment(JLabel.CENTER);
		
		JLabel dictionaryFileLabel = new JLabel("Dictionary file:");
		dictionaryFileLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		dictionaryFileLabel.setBounds(27, 175, 126, 26);
		frame.getContentPane().add(dictionaryFileLabel);
		
		dictionaryFilePath = new JTextField();
		dictionaryFilePath.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		dictionaryFilePath.setColumns(10);
		dictionaryFilePath.setBounds(153, 175, 274, 26);
		frame.getContentPane().add(dictionaryFilePath);
		
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dictFilePath;
				Boolean dictFileFound = false;
				Boolean portNotANumber = false;
				
				try {
					port = Integer.parseInt(portInputField.getText());
//					Thread listeningThread = new Thread(new SetPortToListen(port));
//					listeningThread.start();
					dictFileFound = DictionaryFile.setUpDictionary(dictionaryFilePath.getText().strip());
					if (dictFileFound && !portNotANumber) {
						listeningClass = new SetPortToListen(port);
					}
				} catch (ExceptionHandling e1) {
					serverStatus.setText(e1.getMessage());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					System.out.println("NumberFormatException");
					portNotANumber = true;
					errorMessage.setText("Error: The port you entered is not correct");
					errorMessage.setVisible(true);
					portInputField.setText("");
					dictionaryFilePath.setText("");
				}
				
				if (dictFileFound && !portNotANumber && listeningClass.isServerActive()) {
					errorMessage.setVisible(false);
					portToListenLabel.setVisible(false);
					portToListenLabel.setEnabled(false);
					enterButton.setVisible(false);
					enterButton.setEnabled(false);
					portInputField.setVisible(false);
					portInputField.setEnabled(false);
					serverStatusLabel.setVisible(true);
					serverStatus.setVisible(true);
					dictionaryFilePath.setEnabled(false);
					dictionaryFilePath.setVisible(false);
					dictionaryFileLabel.setEnabled(false);
					dictionaryFileLabel.setVisible(false);
				}
				else if (!portNotANumber && !dictFileFound) {
					errorMessage.setText("Error: Dictionary file not found");
					errorMessage.setVisible(true);
					portInputField.setText("");
					dictionaryFilePath.setText("");
					
				}
				else if (!portNotANumber && listeningClass != null && !listeningClass.isServerActive()){
					errorMessage.setText("Error: The port " + port + " is already in use");
					errorMessage.setVisible(true);
					portInputField.setText("");
					dictionaryFilePath.setText("");
				}


				
					
			}
		});	
		
	}

	
	
	/**
	 * Set up a Server port that constantly listen for connections
 	 */
	private static class SetPortToListen {
		//Create a server socket listening on port
		private ServerSocket listeningSocket;
		private int port;
		
		public SetPortToListen(int port) throws ExceptionHandling {

			this.port = port;
			try {
				this.listeningSocket = new ServerSocket(port);
				if (listeningSocket != null && listeningSocket.isBound()) {
					System.out.println("in if");
					new Thread(() -> run()).start();
				}else {
					System.out.println("in else");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//				String exception = "The port " + port + " has been occupied";
//				throw new ExceptionHandling(exception, "Try again using another port");
				System.out.println(e.getMessage());
				
			}
		}
		
		
		public Boolean isServerActive() {
			if (listeningSocket != null && listeningSocket.isBound()) {
				return true;
			}
			return false;
		}
		
		public void run() {
			serverStatus.setText("Listening on port: " + port);
			System.out.println("Server listening on port " + port +  " for a connection");
			
	        // Constantly listening on a specific port
	        while (true) {
	
	        	// Client socket
	            Socket client;
	            
				try {
					// Connect a new client
					client = listeningSocket.accept();
					
		            counter++;
		            System.out.println("New client connected: " + counter);
		            
		            // Create a new thread for handling the new client connection
		            new Thread(new HandleClientConnection(client, counter)).start();
		            
				} catch (IOException e) {
					System.out.println(e.getMessage());
					System.exit(1);
					
				}
	        }			
		}		
	}
}
