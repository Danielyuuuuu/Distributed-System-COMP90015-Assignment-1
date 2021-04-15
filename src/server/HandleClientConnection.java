package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * To handle client connection 
 */
public class HandleClientConnection implements Runnable {
	
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
