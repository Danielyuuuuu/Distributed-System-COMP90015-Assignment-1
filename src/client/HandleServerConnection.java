/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/*
 * To handle server connection
 */
public class HandleServerConnection {
	private Socket socket = null;
	
	// Get the input/output streams for reading/writing data from/to the socket
	private BufferedReader in;
	private BufferedWriter out;
	private Boolean isConnected = false;
	
	private Boolean isAddingOrUpdatingWord = false;
	private String wordToAddOrUpdate = "";
	private ArrayList<String> wordMeaningsList = new ArrayList<>();
	
	
	public HandleServerConnection(String hostname, String portString) throws NumberFormatException, UnknownHostException, IOException, ParseException {
		isConnected = createTCPConnection(hostname, portString);
	}
	
	/*
	 * To create a TCP connection with the server
	 */
	private Boolean createTCPConnection(String hostname, String portString) throws UnknownHostException, IOException, NumberFormatException, ParseException{	
		int port = Integer.parseInt(portString);
		socket = new Socket(hostname, port);
		
		// Getting the input and output streams
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		
		if (jsonReceived.get("connection").equals("connected")) {
			return true;
		}
		return false;
	}
	
	public Boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * Make a query word request
	 * @param word
	 * @throws IOException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String queryWordMeaning(String word) throws IOException, ParseException {		
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "query");
		sendJson.put("word", word);
		out.write(sendJson.toString() + "\n");
		out.flush();
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		return jsonReceived.get("respond").toString();
		
	}
	
	/*
	 * Make a delete word request
	 */
	@SuppressWarnings("unchecked")
	public String deleteWord(String word) throws IOException, ParseException {
		JSONObject sendJson = new JSONObject();
		sendJson.put("operation", "delete");
		sendJson.put("word", word);
		out.write(sendJson.toString() + "\n");
		out.flush();
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		
		return jsonReceived.get("respond").toString();
	}
	
	/*
	 * Make a update word request
	 */
	@SuppressWarnings("unchecked")
	public String addWord(String word, ArrayList<String> meanings) throws IOException, ParseException {
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
		
		isAddingOrUpdatingWord = false;
		wordToAddOrUpdate = "";
		wordMeaningsList = new ArrayList<>();
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		
		return jsonReceived.get("respond").toString();
	}
	
	/*
	 * Make a update word request
	 */
	@SuppressWarnings("unchecked")
	public String updateWord(String word, ArrayList<String> meanings) throws IOException, ParseException {
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
		
		isAddingOrUpdatingWord = false;
		wordToAddOrUpdate = "";
		wordMeaningsList = new ArrayList<>();
		
		String received = in.readLine();
		JSONParser parser = new JSONParser();
		JSONObject jsonReceived = (JSONObject) parser.parse(received);
		
		return jsonReceived.get("respond").toString();
	}
	
	public Boolean isAddingOrUpdatingWord() {
		return isAddingOrUpdatingWord;
	}
	
	public ArrayList<String> getWordMeaningsList() {
		return wordMeaningsList;
	}
	
	public String getWordToAddOrUpdate() {
		return wordToAddOrUpdate;
	}
	
	public void setIsAddingOrUpdatingWord(Boolean isAddingOrUpdatingWord) {
		this.isAddingOrUpdatingWord = isAddingOrUpdatingWord;
	}
	
	public void setWordToAddOrUpdate(String wordToAddOrUpdate) {
		this.wordToAddOrUpdate = wordToAddOrUpdate;
	}
	
	public void setWordMeaningsList(ArrayList<String> wordMeaningsList) {
		this.wordMeaningsList = wordMeaningsList;
	}
}
