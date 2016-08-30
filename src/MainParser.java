package GoEuroTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;

public class MainParser {
	
	//Creating two classes, that are needed and used to deserealize Objects from JSON array  
	
	class City {
		private String name;
		private int _id;
		private String type;
		private GeoPosition geo_position;
		
	}
	class GeoPosition {
		private double latitude;
		private double longitude;
	}

	public static void main(String[] args) {
		
		//Checking, how many args are typed. Creating String
		
		int numberOfArgs = args.length;
		String userInput = "";
		for (int i=0; i<numberOfArgs;i++) {
			userInput+=args[i];
		}
		
		//Creating URL and Connection with data source  
		
		URL importURL=null;
		try {
			importURL = new URL ("http://api.goeuro.com/api/v2/position/suggest/en/"+userInput);
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
		}
		URLConnection GoEuroConn = null;
        try {
        	GoEuroConn = importURL.openConnection();
        }
        catch (IOException e) {
        System.out.println("IO Exception while opening URL Connection");
        }
        HttpURLConnection httpGoEuroConn = (HttpURLConnection)GoEuroConn;
        
        // Creating BufferedReader to read byte data from InputStream
        
        BufferedReader br=null;
        try {
			br = new BufferedReader (new InputStreamReader(httpGoEuroConn.getInputStream()));
		} catch (IOException e) {
			System.out.println("Something has gone wrong with inputStream from URL");
			e.printStackTrace();
		}
        //we always getting just one line of text with JSON array of objects, so
        
        String currentLine=null;
		try {
			currentLine = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
             if (br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
			} 
		
		
		// Printing data to console to check
		
				System.out.println(currentLine);
				
				// Creating a list of Objects Type City, received from JSON array.
				// We are using GSON package, and storing objects in the List of objects
				
				Type collectionType = new TypeToken<List<City>>(){}.getType();
				List<City> ourList = (List<City>) new Gson().fromJson (currentLine, collectionType);
				
				// With the help of CSVWriter from opencsv package we are writing data from Objects fields to a file
				
				CSVWriter writer=null;
				try {
					writer = new CSVWriter(new FileWriter("result.csv"));
					System.out.println(System.getProperty("user.dir"));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				
				
			    for (City c : ourList) {
			    	String [] CSVData = {(Integer.toString(c._id)), c.name, c.type, (Double.toString(c.geo_position.latitude)), 
			    	(Double.toString(c.geo_position.longitude))};
			    	writer.writeNext(CSVData);
			    			    }
			    
			    try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
        
        
	}

	

