package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/*
 * To read and store the dictionary file in a HashTable
 */
public class DictionaryFile {
	
	private static DictionaryFile dictionaryFile = null;
	private static Hashtable<String, ArrayList<String>> dict = new Hashtable<>();
	
	private DictionaryFile(){

	}
	
	public static boolean setUpDictionary(String filePath) throws FileNotFoundException, IOException, ParseException {
		if (dictionaryFile == null) {
			dictionaryFile = new DictionaryFile();
		}
		File f = new File(filePath);
		if (f.exists()) {
			Object fileObject = new JSONParser().parse(new FileReader(filePath));
	        JSONObject fileJSONObject = (JSONObject) fileObject;
	        
	        for(Object word : fileJSONObject.keySet()) {
	
	        	ArrayList<String> translationArrayList = new ArrayList<>();
	        	JSONArray array = (JSONArray) fileJSONObject.get(word);
	
	        	for(int i = 0; i < array.size(); i++) {
	        		translationArrayList.add((String) array.get(i));
	        	}
	        	dict.put(word.toString().toLowerCase(), translationArrayList);
	        }
	        return true;
		}        
		return false;
	}
	
	public static Hashtable<String, ArrayList<String>> getDictionary(){
		return dict;
	}
}
