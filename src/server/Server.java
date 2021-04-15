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
	
	
	
	/**
	 * To handle client connection 
	 */
    private static class HandleClientConnection implements Runnable {
    	
        private final Socket clientSocket;
        private final int clientID;
        private BufferedWriter out = null;
        private BufferedReader in = null;
        
        // Constructor
        public HandleClientConnection(Socket socket, int clientID)
        {
            this.clientSocket = socket;
            this.clientID = clientID;
        }
  
        public void run()
        {
            try {

                //Get the input/output streams for reading/writing data from/to the socket
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
				
				JSONObject responseJson = new JSONObject();
        		String respondText = "connected";
        		responseJson.put("connection", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
                
                String clientQuery;
                while ((clientQuery = in.readLine()) != null) {
                	
            		JSONParser parser = new JSONParser();
            		JSONObject clientQueryJson = (JSONObject) parser.parse(clientQuery);

            		
            		switch(clientQueryJson.get("operation").toString()) {
            			case "query":
            				System.out.println("In query");
            				GetWordMeaning(clientQueryJson.get("word").toString());
            				break;
            			case "delete":
            				System.out.println("In delete word");
            				deleteWord(clientQueryJson.get("word").toString());
            				break;
            			case "add":
            				System.out.println("In add new word");
            				AddNewWord(clientQueryJson.get("word").toString(), (JSONArray) clientQueryJson.get("meanings"));
            				break;
            			case "update":
            				System.out.println("In update word");
            				UpdateWord(clientQueryJson.get("word").toString(), (JSONArray) clientQueryJson.get("meanings"));
            				break;
            		}
            		
                	
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            finally {
            	// Close the input stream and the output stream
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
    				System.out.println(e.getMessage());
    				System.exit(1);
                }
            }
        }
        
        @SuppressWarnings("unchecked")
		private void GetWordMeaning(String word) throws IOException {
        	word = word.strip().toLowerCase();
        	if (DictionaryFile.getDictionary().containsKey(word)) {
        		String response = "";
        		response = response + "Meaning of \"" + word + "\" is: \n";
        		int count = 1;
        		for (String meaning : DictionaryFile.getDictionary().get(word)) {
        			response = response + count + ". " + meaning + ".\n";
        			count++;
        		}

        		
        		JSONObject responseJson = new JSONObject();
        		responseJson.put("respond", response);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
        	}
        	else {   		
        		JSONObject responseJson = new JSONObject();
        		String respondText = "Word \"" + word + "\" does not exist" + "\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
        	}
        	
        }
        
        
        @SuppressWarnings("unchecked")
		private void deleteWord(String word) throws IOException {
        	word = word.strip().toLowerCase();
        	if (DictionaryFile.getDictionary().containsKey(word)) {
        		DictionaryFile.getDictionary().remove(word);
        		JSONObject responseJson = new JSONObject();
        		String respondText = "Word \"" + word + "\" deleted successfully" + "\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
        	}
        	else {
        		JSONObject responseJson = new JSONObject();
        		String respondText = "Word \"" + word + "\" does not exist" + "\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
        	}
        }
        
        
    	@SuppressWarnings("unchecked")
		private void AddNewWord(String word, JSONArray meanings) throws IOException {
    		
    		word = word.strip();
    		if (word.isEmpty()) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Did not specify the word to add\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else if (DictionaryFile.getDictionary().containsKey(word)) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Word \"" + word + "\" already exist in the dictionary" + "\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else if (meanings.size() == 0) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Did not specify the meaning of the word to add\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else {
    			ArrayList<String> meaningsArrayList = new ArrayList<>();
	    		for (Object meaning : meanings) {
	    			meaningsArrayList.add(meaning.toString());
	    		}
	    		DictionaryFile.getDictionary().put(word, meaningsArrayList);
	    		JSONObject responseJson = new JSONObject();
	    		String respondText = "Word \"" + word + "\" has been added successfully" + "\n";
	    		responseJson.put("respond", respondText);
	    		out.write(responseJson.toString() + "\n");
	    		out.flush();
    		}
    		
    	}
    	
    	@SuppressWarnings("unchecked")
		private void UpdateWord(String word, JSONArray meanings) throws IOException {

    		word = word.strip();
    		if (word.isEmpty()) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Did not specify the word to update\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else if (!DictionaryFile.getDictionary().containsKey(word)) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Word \"" + word + "\" does not exist in the dictionary" + "\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else if (meanings.size() == 0) {
    			JSONObject responseJson = new JSONObject();
        		String respondText = "Did not specify the meaning of the word to update\n";
        		responseJson.put("respond", respondText);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
    		}
    		else {
    			ArrayList<String> meaningsArrayList = new ArrayList<>();
	    		for (Object meaning : meanings) {
	    			meaningsArrayList.add(meaning.toString());
	    		}
	    		DictionaryFile.getDictionary().put(word, meaningsArrayList);
	    		JSONObject responseJson = new JSONObject();
	    		String respondText = "Word \"" + word + "\" has been updated successfully" + "\n";
	    		responseJson.put("respond", respondText);
	    		out.write(responseJson.toString() + "\n");
	    		out.flush();
    		}
    		
    	}
        
    }
}
