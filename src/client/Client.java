package client;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import org.json.simple.JSONArray;
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
import java.util.ArrayList;
import java.util.Scanner;


public class Client {

	protected JFrame frame;
	
	
	private static String hostname;
	private int port;
	
	Socket socket = null;
	// Get the input/output streams for reading/writing data from/to the socket
	BufferedReader in;
	BufferedWriter out;

	
//	private JTextArea textArea = null;
	private JTextField wordInputField;
	private JTextArea addMeaningTextArea;
	private JTextArea textDisplayArea;
	
	private Boolean isAddingOrUpdatingWord = false;
	private String wordToAddOrUpdate = "";
	private ArrayList<String> wordMeaningsList = new ArrayList<>();
	
	
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
		
		// Getting the input and output streams
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
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "query");
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
	
	@SuppressWarnings("unchecked")
	private void addWord(String word, ArrayList<String> meanings) throws IOException, ParseException {
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "add");
		sendJson.put("word", word);
		
		JSONArray meaningsJSONArray = new JSONArray();
		for (String meaning : meanings) {
			meaningsJSONArray.add(meaning);
		}
		sendJson.put("meanings", meaningsJSONArray);
		
		out.write(sendJson.toString() + "\n");
		out.flush();
		System.out.println("Message sent: " + sendJson.toJSONString());
		
		isAddingOrUpdatingWord = false;
		wordToAddOrUpdate = "";
		wordMeaningsList = new ArrayList<>();
		
		textDisplayArea.setText("");
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		textDisplayArea.append(jsonReceived.get("respond") + "\n");
		System.out.println(jsonReceived.get("respond"));
	}
	
	@SuppressWarnings("unchecked")
	private void updateWord(String word, ArrayList<String> meanings) throws IOException, ParseException {
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "update");
		sendJson.put("word", word);
		
		JSONArray meaningsJSONArray = new JSONArray();
		for (String meaning : meanings) {
			meaningsJSONArray.add(meaning);
		}
		sendJson.put("meanings", meaningsJSONArray);
		
		out.write(sendJson.toString() + "\n");
		out.flush();
		System.out.println("Message sent: " + sendJson.toJSONString());
		
		isAddingOrUpdatingWord = false;
		wordToAddOrUpdate = "";
		wordMeaningsList = new ArrayList<>();
		
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
		wordInputField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		wordInputField.setBounds(16, 54, 314, 36);
		frame.getContentPane().add(wordInputField);
		wordInputField.setColumns(10);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(submitButton);
		
		textDisplayArea = new JTextArea();
		textDisplayArea.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
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
		
		JButton addAddMeaningButton = new JButton("Add Meaning");
		addAddMeaningButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(addAddMeaningButton);
		addAddMeaningButton.setEnabled(false);
		addAddMeaningButton.setVisible(false);
		
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
		
		JButton updateAddMeaningButton = new JButton("Add Meaning");
		updateAddMeaningButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(updateAddMeaningButton);
		updateAddMeaningButton.setEnabled(false);
		updateAddMeaningButton.setVisible(false);
		
		JButton updateSubmitButton = new JButton("Submit");
		updateSubmitButton.setBounds(359, 101, 117, 33);
		frame.getContentPane().add(updateSubmitButton);
		updateSubmitButton.setEnabled(false);
		updateSubmitButton.setVisible(false);
		
		JButton updateCancelButton = new JButton("Cancel");
		updateCancelButton.setBounds(359, 142, 117, 33);
		frame.getContentPane().add(updateCancelButton);
		updateCancelButton.setEnabled(false);
		updateCancelButton.setVisible(false);
		
		addMeaningTextArea = new JTextArea();
		addMeaningTextArea.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
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
		
		addAddMeaningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchWord = wordInputField.getText().strip().toLowerCase();
				String fetchMeaning = addMeaningTextArea.getText().strip();
				if (isAddingOrUpdatingWord) {
					if (fetchMeaning.equals("")) {
						textDisplayArea.setText("The meaning is missing");
					}
					else {
						wordMeaningsList.add(fetchMeaning);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Adding word \"" + wordToAddOrUpdate + "\"" + ":\n");
						int count = 1;
						for (String meaning : wordMeaningsList) {
							textDisplayArea.append(count + ". " + meaning + "\n");
							count++;
						}
					}
				}
				else {
					if (fetchWord.equals("") || fetchMeaning.equals("")) {
						textDisplayArea.setText("The word or the meaning is missing");
					}
					else {
						isAddingOrUpdatingWord = true;
						wordToAddOrUpdate = fetchWord;
						wordMeaningsList.add(fetchMeaning);
						addSubmitButton.setEnabled(true);
						addSubmitButton.setVisible(true);
						addCancelButton.setEnabled(true);
						addCancelButton.setVisible(true);
						wordInputField.setEnabled(false);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Adding word \"" + wordToAddOrUpdate + "\"" + ":\n");
						int count = 1;
						for (String meaning : wordMeaningsList) {
							textDisplayArea.append(count + ". " + meaning + "\n");
							count++;
						}
						
					}
				}
				
			}
		});
		
		updateAddMeaningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchWord = wordInputField.getText().strip().toLowerCase();
				String fetchMeaning = addMeaningTextArea.getText().strip();
				if (isAddingOrUpdatingWord) {
					if (fetchMeaning.equals("")) {
						textDisplayArea.setText("The meaning is missing");
					}
					else {
						wordMeaningsList.add(fetchMeaning);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Updating word \"" + wordToAddOrUpdate + "\"" + ":\n");
						int count = 1;
						for (String meaning : wordMeaningsList) {
							textDisplayArea.append(count + ". " + meaning + "\n");
							count++;
						}
					}
				}
				else {
					if (fetchWord.equals("") || fetchMeaning.equals("")) {
						textDisplayArea.setText("The word or the meaning is missing");
					}
					else {
						isAddingOrUpdatingWord = true;
						wordToAddOrUpdate = fetchWord;
						wordMeaningsList.add(fetchMeaning);
						updateSubmitButton.setEnabled(true);
						updateSubmitButton.setVisible(true);
						updateCancelButton.setEnabled(true);
						updateCancelButton.setVisible(true);
						wordInputField.setEnabled(false);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Updating word \"" + wordToAddOrUpdate + "\"" + ":\n");
						int count = 1;
						for (String meaning : wordMeaningsList) {
							textDisplayArea.append(count + ". " + meaning + "\n");
							count++;
						}
						
					}
				}
				
			}
		});
		
		addSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					addWord(wordToAddOrUpdate, wordMeaningsList);
					wordInputField.setEnabled(true);
					wordInputField.setText("");
					addMeaningTextArea.setText("");
					addSubmitButton.setEnabled(false);
					addSubmitButton.setVisible(false);
					addCancelButton.setEnabled(false);
					addCancelButton.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		updateSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					updateWord(wordToAddOrUpdate, wordMeaningsList);
					wordInputField.setEnabled(true);
					wordInputField.setText("");
					addMeaningTextArea.setText("");
					updateSubmitButton.setEnabled(false);
					updateSubmitButton.setVisible(false);
					updateCancelButton.setEnabled(false);
					updateCancelButton.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		addCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wordInputField.setEnabled(true);
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				textDisplayArea.setText("Adding word \"" + wordToAddOrUpdate + "\" has been cancelled \n");
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
				
				addSubmitButton.setEnabled(false);
				addSubmitButton.setVisible(false);
				addCancelButton.setEnabled(false);
				addCancelButton.setVisible(false);
			}
		});
		
		
		updateCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wordInputField.setEnabled(true);
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				textDisplayArea.setText("Updating word \"" + wordToAddOrUpdate + "\" has been cancelled \n");
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
				
				updateSubmitButton.setEnabled(false);
				updateSubmitButton.setVisible(false);
				updateCancelButton.setEnabled(false);
				updateCancelButton.setVisible(false);
			}
		});
		
		queryWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				submitButton.setEnabled(true);
				submitButton.setVisible(true);
				deleteButton.setEnabled(false);
				deleteButton.setVisible(false);
				addMeaningLabel.setEnabled(false);
				addMeaningLabel.setVisible(false);
				addMeaningTextArea.setEnabled(false);
				addMeaningTextArea.setVisible(false);
				addSubmitButton.setEnabled(false);
				addSubmitButton.setVisible(false);
				addCancelButton.setEnabled(false);
				addCancelButton.setVisible(false);
				addAddMeaningButton.setEnabled(false);
				addAddMeaningButton.setVisible(false);
				updateSubmitButton.setEnabled(false);
				updateSubmitButton.setVisible(false);
				updateCancelButton.setEnabled(false);
				updateCancelButton.setVisible(false);
				updateAddMeaningButton.setEnabled(false);
				updateAddMeaningButton.setVisible(false);
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
				wordInputField.setEnabled(true);
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
			}
		});
		
		removeWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				submitButton.setEnabled(false);
				submitButton.setVisible(false);
				deleteButton.setEnabled(true);
				deleteButton.setVisible(true);
				addMeaningLabel.setEnabled(false);
				addMeaningLabel.setVisible(false);
				addMeaningTextArea.setEnabled(false);
				addMeaningTextArea.setVisible(false);
				addSubmitButton.setEnabled(false);
				addSubmitButton.setVisible(false);
				addCancelButton.setEnabled(false);
				addCancelButton.setVisible(false);
				addAddMeaningButton.setEnabled(false);
				addAddMeaningButton.setVisible(false);
				updateSubmitButton.setEnabled(false);
				updateSubmitButton.setVisible(false);
				updateCancelButton.setEnabled(false);
				updateCancelButton.setVisible(false);
				updateAddMeaningButton.setEnabled(false);
				updateAddMeaningButton.setVisible(false);
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
				wordInputField.setEnabled(true);
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
			}
		});
		
		addWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				submitButton.setEnabled(false);
				submitButton.setVisible(false);
				deleteButton.setEnabled(false);
				deleteButton.setVisible(false);
				addMeaningLabel.setEnabled(true);
				addMeaningLabel.setVisible(true);
				addMeaningTextArea.setEnabled(true);
				addMeaningTextArea.setVisible(true);
				addSubmitButton.setEnabled(false);
				addSubmitButton.setVisible(false);
				addCancelButton.setEnabled(false);
				addCancelButton.setVisible(false);
				addAddMeaningButton.setEnabled(true);
				addAddMeaningButton.setVisible(true);
				updateSubmitButton.setEnabled(false);
				updateSubmitButton.setVisible(false);
				updateCancelButton.setEnabled(false);
				updateCancelButton.setVisible(false);
				updateAddMeaningButton.setEnabled(false);
				updateAddMeaningButton.setVisible(false);
				titleLabel.setText("Add a new word");
				queryWord.setEnabled(true);
				queryWord.setVisible(true);
				queryWord.setFocusPainted(false);
				addWord.setEnabled(false);
//				addWord.setVisible(false);
				addWord.setFocusPainted(false);
				removeWord.setEnabled(true);
				removeWord.setVisible(true);
				removeWord.setFocusPainted(false);
				updateWord.setEnabled(true);
				updateWord.setVisible(true);
				updateWord.setFocusPainted(false);
				wordInputField.setEnabled(true);
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
			}
		});
		
		updateWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textDisplayArea.setText("");
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				submitButton.setEnabled(false);
				submitButton.setVisible(false);
				deleteButton.setEnabled(false);
				deleteButton.setVisible(false);
				addMeaningLabel.setEnabled(true);
				addMeaningLabel.setVisible(true);
				addMeaningTextArea.setEnabled(true);
				addMeaningTextArea.setVisible(true);
				addSubmitButton.setEnabled(false);
				addSubmitButton.setVisible(false);
				addCancelButton.setEnabled(false);
				addCancelButton.setVisible(false);
				addAddMeaningButton.setEnabled(false);
				addAddMeaningButton.setVisible(false);
				updateSubmitButton.setEnabled(false);
				updateSubmitButton.setVisible(false);
				updateCancelButton.setEnabled(false);
				updateCancelButton.setVisible(false);
				updateAddMeaningButton.setEnabled(true);
				updateAddMeaningButton.setVisible(true);
				titleLabel.setText("Update an existing word");
				queryWord.setEnabled(true);
				queryWord.setVisible(true);
				queryWord.setFocusPainted(false);
				addWord.setEnabled(true);
				addWord.setVisible(true);
				addWord.setFocusPainted(false);
				removeWord.setEnabled(true);
				removeWord.setVisible(true);
				removeWord.setFocusPainted(false);
				updateWord.setEnabled(false);
//				updateWord.setVisible(false);
				updateWord.setFocusPainted(false);
				wordInputField.setEnabled(true);
				
				isAddingOrUpdatingWord = false;
				wordToAddOrUpdate = "";
				wordMeaningsList = new ArrayList<>();
			}
		});
	}
}
