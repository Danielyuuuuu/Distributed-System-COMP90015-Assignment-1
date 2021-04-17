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
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JTextField;

import client.ExceptionHandling;

import javax.swing.JButton;
import org.json.simple.parser.ParseException;


public class Server {

	private JFrame frame;
	
	private SetPortToListen listeningClass;
	
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
		initialize();
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
		
		JLabel serverLabel = new JLabel("Server");
		serverLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		serverLabel.setBounds(194, 6, 61, 16);
		frame.getContentPane().add(serverLabel);
		
		JTextField portInputField = new JTextField();
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

		JLabel serverStatus = new JLabel("Inactive");
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
		
		JTextField dictionaryFilePath = new JTextField();
		dictionaryFilePath.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		dictionaryFilePath.setColumns(10);
		dictionaryFilePath.setBounds(153, 175, 274, 26);
		frame.getContentPane().add(dictionaryFilePath);
		
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean dictFileFound = false;
				Boolean portNotANumber = false;
				int port = 0;
				try {
					port = Integer.parseInt(portInputField.getText());
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
					serverStatus.setText("Listening on port: " + port);
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
}
