package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JButton;
import javax.swing.JTextArea;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	protected JFrame frame;
	private JTextField textField;
	
	private static String hostname;
	private int port;
	
	Socket socket = null;
	// Get the input/output streams for reading/writing data from/to the socket
	BufferedReader in;
	BufferedWriter out;

	
	private JTextArea textArea = null;
	
	/**
	 * Launch the application.
	 * @throws ExceptionHandling 
	 */
	public static void main(String[] args) throws ExceptionHandling {
		System.out.print("Hello from client\n");
		
		if (args.length != 2) {
			throw new ExceptionHandling("No server address or port number specified", "format <server-address> <server-port>");
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client(args);
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
	 * @throws UnknownHostException 
	 */
	public Client(String[] args) throws UnknownHostException, IOException {
		initialize();
		hostname = args[0];
		port = Integer.parseInt(args[1]);
		createTCPConnection();
	}
	
	
	/**
	 * Create TCP connection
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void createTCPConnection() throws UnknownHostException, IOException {		
		socket = new Socket(hostname, port);
		// Get the input/output streams for reading/writing data from/to the socket
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	}
	
	
	/**
	 * Send word to the dictionary server
	 * @param word
	 * @throws IOException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private void sendWordToServer(String word) throws IOException, ParseException {		
		// Send the input string to the server by writing to the socket output stream
//		out.write("search-word-meaning\n");
//		out.write(word + "\n");
//		out.flush();
//		System.out.println("Message sent: " + word);
		
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "query");
		sendJson.put("word", word);
		out.write(sendJson.toString() + "\n");
		out.flush();
		System.out.println("Message sent: " + word);
		
		
		textArea.setText("");
		
//		// This method blocks until there  is something to read from the
//		String received;
//		while ((received = in.readLine()) != null) {
//			if((received.strip()).equals("end")) {
//				System.out.println("Before break");
//				break;
//			}
//			textArea.append(received + "\n");
//			System.out.println(received);
//		}
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		textArea.append(jsonReceived.get("respond") + "\n");
		System.out.println(jsonReceived.get("respond"));
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dictionary");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel.setBounds(186, 6, 78, 19);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textField.setBounds(27, 56, 254, 36);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setBounds(309, 59, 117, 33);
		frame.getContentPane().add(btnNewButton);
		
		textArea = new JTextArea();
		textArea.setBounds(27, 132, 399, 118);
		frame.getContentPane().add(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchText = textField.getText();
				textField.setText("");
				try {
					sendWordToServer(fetchText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
