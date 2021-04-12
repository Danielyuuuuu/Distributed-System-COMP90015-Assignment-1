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
	
	private static JLabel serverStatus = null;
	
	private static Hashtable<String, ArrayList<String>> dict = new Hashtable<>();
	
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

        	ArrayList<String> translationArrayList = new ArrayList<>();
        	JSONArray array = (JSONArray) jo.get(word);

        	for(int i = 0; i < array.size(); i++) {
        		translationArrayList.add((String) array.get(i));
        	}
        	dict.put(word.toString().toLowerCase(), translationArrayList);
        }
        

	}
	
	
	private static String AddNewWord(String word, JSONArray meanings) {

//		String word = (String) jsonQuery.get("word");
//		JSONArray meanings = (JSONArray) jsonQuery.get("meanings");
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
		ArrayList<String> meaningsArrayList = new ArrayList<>();
		for (Object meaning : meanings) {
			meaningsArrayList.add(meaning.toString());
		}
		dict.put(word, meaningsArrayList);
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
	
	private String UpdateWord(String word, ArrayList<String> meanings) {
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
		
	}

	
	
	/**
	 * Set up a Server port that constantly listen for connections
 	 */
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
//            PrintWriter out = null;
//            BufferedReader in = null;
            try {
                    
//                  // get the outputstream of client
//                out = new PrintWriter(
//                    clientSocket.getOutputStream(), true);
//  
//                  // get the inputstream of client
//                in = new BufferedReader(
//                    new InputStreamReader(
//                        clientSocket.getInputStream()));
  
                //Get the input/output streams for reading/writing data from/to the socket
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                
                String clientQuery;
                while ((clientQuery = in.readLine()) != null) {
//                	if ((line.strip()).equals("search-word-meaning")) {
//                		line = in.readLine();
//                        // writing the received message from
//                        // client
//                        System.out.println(" Sent from the client " + clientID + ": " + line);
////    					out.write("Server Ack " + dict.get(line) + "\n");
////    					out.flush();
//                        
//                        GetWordMeaning(line);
//                	}
                	
            		JSONParser parser = new JSONParser();
            		JSONObject clientQueryJson = (JSONObject) parser.parse(clientQuery);
            		if (clientQueryJson.get("operation").equals("query")) {
            			GetWordMeaning(clientQueryJson.get("word").toString());
            		}
            		else if(clientQueryJson.get("operation").equals("delete")) {
            			deleteWord(clientQueryJson.get("word").toString());
            		}
            		else if (clientQueryJson.get("operation").equals("add")) {
//            			JSONArray meaningsJSONArray = (JSONArray) clientQueryJson.get("meanings");
//            			clientQueryJson.get("word").toString(), meaningsJSONArray
            			AddNewWord(clientQueryJson.get("word").toString(), (JSONArray) clientQueryJson.get("meanings"));
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
        
        @SuppressWarnings("unchecked")
		private void GetWordMeaning(String word) throws IOException {
        	word = word.strip().toLowerCase();
        	if (dict.containsKey(word)) {
        		String response = "";
        		response = response + "Meaning of \"" + word + "\" is: \n";
//        		out.write("Meaning of " + word + " is: \n");
        		int count = 1;
        		for (String meaning : dict.get(word)) {
        			response = response + count + ". " + meaning + ".\n";
//        			out.write(count + ". " + meaning + ".\n");
        			count++;
        		}
        		System.out.println("Before write end");
//        		response = response + "end" + "\n";
//        		out.write(response);
//        		out.flush();
        		
        		JSONObject responseJson = new JSONObject();
        		responseJson.put("respond", response);
        		out.write(responseJson.toString() + "\n");
        		out.flush();
        	}
        	else {
//        		out.write("Word \"" + word + "\" does not exist" + "\n" + "end\n");
//        		out.flush();
        		
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
        	if (dict.containsKey(word)) {
        		dict.remove(word);
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
        
    }

}
