/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

package client;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import org.json.simple.parser.ParseException;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The client dictionary program 
 */
public class Client {

	protected JFrame frame;
	
	private HandleServerConnection handleServerConnection;
	
	/**
	 * Launch the application.
	 * @throws ExceptionHandling 
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.exit(1);
				}
			}
		});
		
	}


	private Client() {
		initializeConnectionGUI();
	}
	
	
	/*
	 * Initialize the GUI that asks for server IP address and server port
	 */
	private void initializeConnectionGUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel headerLabel = new JLabel("Connect to dictionary server");
		headerLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		headerLabel.setBounds(114, 6, 280, 25);
		frame.getContentPane().add(headerLabel);
		
		JTextField severPortTextField = new JTextField();
		severPortTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		severPortTextField.setBounds(257, 213, 170, 26);
		frame.getContentPane().add(severPortTextField);
		severPortTextField.setColumns(10);
		
		JLabel serverPortLabel = new JLabel("Dictionary Server Port:");
		serverPortLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		serverPortLabel.setBounds(54, 214, 191, 25);
		frame.getContentPane().add(serverPortLabel);
		
		JButton connectButton = new JButton("Connect");
		connectButton.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		connectButton.setBounds(182, 262, 117, 29);
		frame.getContentPane().add(connectButton);
		
		JLabel errorMessage = new JLabel("Error message");
		errorMessage.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		errorMessage.setBounds(21, 90, 458, 25);
		frame.getContentPane().add(errorMessage);
		errorMessage.setHorizontalAlignment(JLabel.CENTER);
		errorMessage.setVerticalAlignment(JLabel.CENTER);
		
		JTextField hostNameTextField = new JTextField();
		hostNameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		hostNameTextField.setColumns(10);
		hostNameTextField.setBounds(257, 162, 170, 26);
		frame.getContentPane().add(hostNameTextField);
		
		JLabel hostNameLabel = new JLabel("Dictionary Host Name:");
		hostNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		hostNameLabel.setBounds(54, 163, 191, 25);
		frame.getContentPane().add(hostNameLabel);
		errorMessage.setVisible(false);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean hasError = false;
				Boolean isConnectedToServer = false;
				try {
					handleServerConnection = new HandleServerConnection(hostNameTextField.getText().strip(), severPortTextField.getText().strip());
					isConnectedToServer = handleServerConnection.isConnected();
				} catch (IOException e1) {
					hasError = true;
					errorMessage.setText("The hostname or port number is not correct");
					errorMessage.setVisible(true);
					hostNameTextField.setText("");
					severPortTextField.setText("");
					
				} catch (NumberFormatException e1) {
					hasError = true;
					errorMessage.setText("The port number is not correct");
					errorMessage.setVisible(true);
					hostNameTextField.setText("");
					severPortTextField.setText("");
				} catch (ParseException e1) {
					hasError = true;
					errorMessage.setText("Having trouble on the server end");
					errorMessage.setVisible(true);
					hostNameTextField.setText("");
					severPortTextField.setText("");
				}

				if (!hasError && isConnectedToServer) {
					frame.getContentPane().removeAll();
					frame.repaint();
					initializeDictGUI();
				}
				else if (!hasError && !isConnectedToServer) {
					errorMessage.setText("You have connected to the wrong server");
					errorMessage.setVisible(true);
					hostNameTextField.setText("");
					severPortTextField.setText("");
				}

				
			}
		});
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeDictGUI() {
		
		JLabel titleLabel = new JLabel("Query a word meaning");
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		titleLabel.setBounds(27, 6, 449, 19);
		frame.getContentPane().add(titleLabel);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVerticalAlignment(JLabel.CENTER);
		
		JTextField wordInputField = new JTextField();
		wordInputField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		wordInputField.setBounds(16, 54, 314, 36);
		frame.getContentPane().add(wordInputField);
		wordInputField.setColumns(10);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setBounds(359, 57, 117, 33);
		frame.getContentPane().add(submitButton);
		
		JTextArea textDisplayArea = new JTextArea();
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
		
		JTextArea addMeaningTextArea = new JTextArea();
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
					textDisplayArea.setText("");
					textDisplayArea.append(handleServerConnection.queryWordMeaning(fetchText));
				} catch (IOException | ParseException e1) {
					System.out.println(e1.getMessage());
					System.exit(1);
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchText = wordInputField.getText();
				wordInputField.setText("");
				try {
					textDisplayArea.setText("");
					textDisplayArea.append(handleServerConnection.deleteWord(fetchText));
				} catch (IOException | ParseException e1) {
					System.out.println(e1.getMessage());
					System.exit(1);
				}
			}
		});
		
		addAddMeaningButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fetchWord = wordInputField.getText().strip().toLowerCase();
				String fetchMeaning = addMeaningTextArea.getText().strip();
				if (handleServerConnection.isAddingOrUpdatingWord()) {
					if (fetchMeaning.equals("")) {
						textDisplayArea.setText("The meaning is missing");
					}
					else {
						handleServerConnection.getWordMeaningsList().add(fetchMeaning);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Adding word \"" + handleServerConnection.getWordToAddOrUpdate() + "\"" + ":\n");
						int count = 1;
						for (String meaning : handleServerConnection.getWordMeaningsList()) {
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
						handleServerConnection.setIsAddingOrUpdatingWord(true);
						handleServerConnection.setWordToAddOrUpdate(fetchWord);
						handleServerConnection.getWordMeaningsList().add(fetchMeaning);
						addSubmitButton.setEnabled(true);
						addSubmitButton.setVisible(true);
						addCancelButton.setEnabled(true);
						addCancelButton.setVisible(true);
						wordInputField.setEnabled(false);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Adding word \"" + handleServerConnection.getWordToAddOrUpdate() + "\"" + ":\n");
						int count = 1;
						for (String meaning : handleServerConnection.getWordMeaningsList()) {
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
				if (handleServerConnection.isAddingOrUpdatingWord()) {
					if (fetchMeaning.equals("")) {
						textDisplayArea.setText("The meaning is missing");
					}
					else {
						handleServerConnection.getWordMeaningsList().add(fetchMeaning);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Updating word \"" + handleServerConnection.getWordToAddOrUpdate() + "\"" + ":\n");
						int count = 1;
						for (String meaning : handleServerConnection.getWordMeaningsList()) {
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
						handleServerConnection.setIsAddingOrUpdatingWord(true);
						handleServerConnection.setWordToAddOrUpdate(fetchWord);
						handleServerConnection.getWordMeaningsList().add(fetchMeaning);
						updateSubmitButton.setEnabled(true);
						updateSubmitButton.setVisible(true);
						updateCancelButton.setEnabled(true);
						updateCancelButton.setVisible(true);
						wordInputField.setEnabled(false);
						
						addMeaningTextArea.setText("");
						textDisplayArea.setText("Updating word \"" + handleServerConnection.getWordToAddOrUpdate() + "\"" + ":\n");
						int count = 1;
						for (String meaning : handleServerConnection.getWordMeaningsList()) {
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
					textDisplayArea.setText("");
					textDisplayArea.append(handleServerConnection.addWord(handleServerConnection.getWordToAddOrUpdate(), handleServerConnection.getWordMeaningsList()));
					wordInputField.setEnabled(true);
					wordInputField.setText("");
					addMeaningTextArea.setText("");
					addSubmitButton.setEnabled(false);
					addSubmitButton.setVisible(false);
					addCancelButton.setEnabled(false);
					addCancelButton.setVisible(false);
				} catch (IOException | ParseException e1) {
					System.out.println(e1.getMessage());
					System.exit(1);
				}
			}
		});
		
		updateSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					textDisplayArea.setText("");
					textDisplayArea.append(handleServerConnection.updateWord(handleServerConnection.getWordToAddOrUpdate(), handleServerConnection.getWordMeaningsList()));
					wordInputField.setEnabled(true);
					wordInputField.setText("");
					addMeaningTextArea.setText("");
					updateSubmitButton.setEnabled(false);
					updateSubmitButton.setVisible(false);
					updateCancelButton.setEnabled(false);
					updateCancelButton.setVisible(false);
				} catch (IOException | ParseException e1) {
					System.out.println(e1.getMessage());
					System.exit(1);
				}
			}
		});
		
		
		addCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wordInputField.setEnabled(true);
				wordInputField.setText("");
				addMeaningTextArea.setText("");
				textDisplayArea.setText("Adding word \"" + handleServerConnection.getWordToAddOrUpdate() + "\" has been cancelled \n");
				
				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
				
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
				textDisplayArea.setText("Updating word \"" + handleServerConnection.getWordToAddOrUpdate() + "\" has been cancelled \n");

				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
				
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
				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
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
				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
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
				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
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
				handleServerConnection.setIsAddingOrUpdatingWord(false);
				handleServerConnection.setWordToAddOrUpdate("");
				handleServerConnection.setWordMeaningsList(new ArrayList<>());
			}
		});
	}
}
