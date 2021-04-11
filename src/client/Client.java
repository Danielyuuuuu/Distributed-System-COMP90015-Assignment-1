package client;

import java.awt.Color;
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
	private JTextField wordInputField;
	
	private static String hostname;
	private int port;
	
	Socket socket = null;
	// Get the input/output streams for reading/writing data from/to the socket
	BufferedReader in;
	BufferedWriter out;

	
	private JTextArea textArea = null;
	private JTextArea textDisplayArea;
	
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
	private void queryWordMeaning(String word) throws IOException, ParseException {		
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
		
		
		textDisplayArea.setText("");
		
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
		textDisplayArea.append(jsonReceived.get("respond") + "\n");
		System.out.println(jsonReceived.get("respond"));
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void deleteWord(String word) throws IOException, ParseException {
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "delete");
		sendJson.put("word", word);
		out.write(sendJson.toString() + "\n");
		out.flush();
		System.out.println("Message sent: " + word);
		
		
		textDisplayArea.setText("");
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		textDisplayArea.append(jsonReceived.get("respond") + "\n");
		System.out.println(jsonReceived.get("respond"));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel titleLabel = new JLabel("Query a word meaning");
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		titleLabel.setBounds(27, 6, 449, 19);
		frame.getContentPane().add(titleLabel);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVerticalAlignment(JLabel.CENTER);
		
		wordInputField = new JTextField();
		wordInputField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		wordInputField.setBounds(16, 54, 314, 36);
		frame.getContentPane().add(wordInputField);
		wordInputField.setColumns(10);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(submitButton);
		
		textDisplayArea = new JTextArea();
		textDisplayArea.setBounds(16, 207, 460, 118);
		frame.getContentPane().add(textDisplayArea);
		textDisplayArea.setLineWrap(true);
		textDisplayArea.setWrapStyleWord(true);
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(deleteButton);
		deleteButton.setEnabled(false);
		deleteButton.setVisible(false);
		
		JButton queryWord = new JButton("Query Word");
		queryWord.setForeground(Color.BLACK);
		queryWord.setBounds(6, 337, 117, 29);
		frame.getContentPane().add(queryWord);
		queryWord.setEnabled(false);
		
		JButton addWord = new JButton("Add Word");
		addWord.setBounds(124, 337, 117, 29);
		frame.getContentPane().add(addWord);
		
		JButton removeWord = new JButton("Remove Word");
		removeWord.setBounds(253, 337, 117, 29);
		frame.getContentPane().add(removeWord);
		
		JButton updateWord = new JButton("Update Word");
		updateWord.setBounds(377, 337, 117, 29);
		frame.getContentPane().add(updateWord);
		
		JButton btnAddMeaning = new JButton("Add Meaning");
		btnAddMeaning.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(btnAddMeaning);
		
		JButton addSubmitButton = new JButton("Submit");
		addSubmitButton.setBounds(359, 101, 117, 33);
		frame.getContentPane().add(addSubmitButton);
		addSubmitButton.setEnabled(false);
		addSubmitButton.setVisible(false);
		
		JButton addCancelButton = new JButton("Cancel");
		addCancelButton.setBounds(359, 142, 117, 33);
		frame.getContentPane().add(addCancelButton);
		addCancelButton.setEnabled(false);
		addCancelButton.setVisible(false);
		
		JTextArea addMeaningTextArea = new JTextArea();
		addMeaningTextArea.setBounds(16, 119, 314, 56);
		frame.getContentPane().add(addMeaningTextArea);
		addMeaningTextArea.setLineWrap(true);
		addMeaningTextArea.setWrapStyleWord(true);
		addMeaningTextArea.setEnabled(false);
		addMeaningTextArea.setVisible(false);
		
		JLabel resultLabel = new JLabel("Result:");
		resultLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		resultLabel.setBounds(16, 187, 61, 16);
		frame.getContentPane().add(resultLabel);
		
		JLabel wordLabel = new JLabel("Word:");
		wordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		wordLabel.setBounds(16, 37, 61, 16);
		frame.getContentPane().add(wordLabel);
		
		JLabel addMeaningLabel = new JLabel("Add Meaning:");
		addMeaningLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		addMeaningLabel.setBounds(16, 97, 100, 19);
		frame.getContentPane().add(addMeaningLabel);
		addMeaningLabel.setEnabled(false);
		addMeaningLabel.setVisible(false);
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchText = wordInputField.getText();
				wordInputField.setText("");
				try {
					queryWordMeaning(fetchText);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchText = wordInputField.getText();
				wordInputField.setText("");
				try {
					deleteWord(fetchText);
				} catch (IOException | ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		queryWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				submitButton.setEnabled(true);
				submitButton.setVisible(true);
				deleteButton.setEnabled(false);
				deleteButton.setVisible(false);
				titleLabel.setText("Query a word meaning");
				queryWord.setEnabled(false);
//				queryWord.setVisible(false);
				queryWord.setFocusPainted(false);
				addWord.setEnabled(true);
				addWord.setVisible(true);
				addWord.setFocusPainted(false);
				removeWord.setEnabled(true);
				removeWord.setVisible(true);
				removeWord.setFocusPainted(false);
				updateWord.setEnabled(true);
				updateWord.setVisible(true);
				updateWord.setFocusPainted(false);
			}
		});
		
		removeWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				submitButton.setEnabled(false);
				submitButton.setVisible(false);
				deleteButton.setEnabled(true);
				deleteButton.setVisible(true);
				titleLabel.setText("Delete an existing word");
				queryWord.setEnabled(true);
				queryWord.setVisible(true);
				queryWord.setFocusPainted(false);
				addWord.setEnabled(true);
				addWord.setVisible(true);
				addWord.setFocusPainted(false);
				removeWord.setEnabled(false);
//				removeWord.setVisible(false);
				removeWord.setFocusPainted(false);
				updateWord.setEnabled(true);
				updateWord.setVisible(true);
				updateWord.setFocusPainted(false);
			}
		});
	}
}
