package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	
	private int port;
//	private ServerSocket listeningSocket = null;
	private Socket clientSocket = null;
	
	private Thread listeningThread = null;
	
	private static JLabel serverStatus = null;
	
	private Hashtable<String, ArrayList<String>> dict = new Hashtable<>();
	
	private static int counter = 0;
	
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
	 * @throws ParseException 
	 */
	public Server(String[] args) throws IOException, ExceptionHandling, ParseException {
		if (args.length != 1) {
			throw new ExceptionHandling("No server port or dictionary file specified", "format <port> <dictionary-file>");
		}
		port = Integer.parseInt(args[0]);
		initialize();
		
//		// Start a new thread to listen on a specific port
//		listeningThread = new Thread(() -> 
//				{
//					try {
//						SetPortToListen();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				});
//		listeningThread.start();
		
		// Start a new thread to listen on a specific port
		new Thread(new SetPortToListen(port)).start();
		
		ReadDictFile("./dict-file.json");
	}
	
	
	private void ReadDictFile(String filePath) throws FileNotFoundException, IOException, ParseException {        
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader(filePath));
          
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
        
        for(Object word : jo.keySet()) {
//        	System.out.println(word + ": " + jo.get(word));
//        	Object[] translations = (Object [])jo.get(word);
        	ArrayList<String> translationArrayList = new ArrayList<>();
        	JSONArray array = (JSONArray) jo.get(word);
//        	array.add(jo.get(word));
//        	System.out.println(array.size());
        	for(int i = 0; i < array.size(); i++) {
        		translationArrayList.add((String) array.get(i));
        	}
        	dict.put(word.toString(), translationArrayList);
        }
        
//        System.out.println("Print dict");
//        for (String word : dict.keySet()) {
//        	System.out.println(word + ": " + dict.get(word));
//        }
	}
	
	
	private String AddNewWord(String word, ArrayList<String> meanings) {
		word = word.strip();
		if (dict.containsKey(word)) {
			return "The word already exist";
		}
		else if (word.isEmpty()) {
			return "Did not specify the word";
		}
		else if (meanings.size() == 0) {
			return "Did not specify the meaning of the word";
		}
		dict.put(word, meanings);
		return "Success";
	}
	
	
	private String RemoveExistingWord(String word) {
		word = word.strip();
		if (dict.containsKey(word)) {
			dict.remove(word);
			return "Success";
		}
		return "Word does not exist";
	}
	
	private String UpdateWord(String word ,ArrayList<String> meanings) {
		word = word.strip();
		if (!dict.containsKey(word)) {
			return "Word not found";
		}
		else if (meanings.size() == 0) {
			return "Did not specify the meaning of the word";
		}
		dict.replace(word, meanings);
		return "Success";
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
	
	
//	private void SetPortToListen() throws IOException {
//		//Create a server socket listening on port
//		listeningSocket = new ServerSocket(port);
//		serverStatus.setText("Listening on port: " + port);
//		System.out.println("Server listening on port " + port +  " for a connection");
//		
//        // running infinite loop for getting
//        // client request
//        while (true) {
//
//            // socket object to receive incoming client
//            // requests
//            Socket client = listeningSocket.accept();
//            
//            counter++;
//            // Displaying that new client is connected
//            
//            // to server
//            System.out.println("New client connected: " + counter);
//
//
//            // This thread will handle the client
//            // separately
//            new Thread(new HandleClientConnection(client, counter)).start();
//        }
//		
//		
////		//Listen for incoming connections for ever 
////		while (listeningSocket != null && true) 
////		{
////			System.out.println("Server listening on port " + port +  " for a connection");
////			//Accept an incoming client connection request 
////			clientSocket = listeningSocket.accept(); //This method will block until a connection request is received
////			System.out.println("Client conection accepted:");
////			System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
////			System.out.println("Local Port: " + clientSocket.getLocalPort());
////			
////			//Get the input/output streams for reading/writing data from/to the socket
////			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
////			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
////
////			
////			//Read the message from the client and reply
////			//Notice that no other connection can be accepted and processed until the last line of 
////			//code of this loop is executed, incoming connections have to wait until the current
////			//one is processed unless...we use threads!
////			String clientMsg = null;
////			try 
////			{
////				while((clientMsg = in.readLine()) != null) 
////				{
////					System.out.println("Message from client: " + clientMsg);
////					out.write("Server Ack " + clientMsg + "\n");
////					out.flush();
////					System.out.println("Response sent");
////				}
////			}
////			
////			catch(SocketException e)
////			{
////				System.out.println("closed...");
////			}
////			
////			clientSocket.close();
////		}
//	}
	
	
	private static class SetPortToListen implements Runnable {
		//Create a server socket listening on port
		private final ServerSocket listeningSocket;
		private final int port;
		
		public SetPortToListen(int port) throws IOException {
			this.port = port;
			listeningSocket = new ServerSocket(port);
		}
		
		public void run() {
			serverStatus.setText("Listening on port: " + port);
			System.out.println("Server listening on port " + port +  " for a connection");
			
	        // running infinite loop for getting
	        // client request
	        while (true) {
	
	            // socket object to receive incoming client
	            // requests
	            Socket client;
				try {
					client = listeningSocket.accept();
					
		            counter++;
		            // Displaying that new client is connected
		            
		            // to server
		            System.out.println("New client connected: " + counter);
		
		
		            // This thread will handle the client
		            // separately
		            new Thread(new HandleClientConnection(client, counter)).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            

	        }
		}
		
	}
	
	
	
	// To handle client connection 
    private static class HandleClientConnection implements Runnable {
    	
        private final Socket clientSocket;
        private final int clientID;
  
        // Constructor
        public HandleClientConnection(Socket socket, int clientID)
        {
            this.clientSocket = socket;
            this.clientID = clientID;
        }
  
        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                    
                  // get the outputstream of client
                out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
  
                  // get the inputstream of client
                in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream()));
  
                String line;
                while ((line = in.readLine()) != null) {
  
                    // writing the received message from
                    // client
                    System.out.println(" Sent from the client " + clientID + ": " + line);
					out.write("Server Ack " + line + "\n");
					out.flush();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
