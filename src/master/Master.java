package master;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
//import org.json.*;
import javafx.concurrent.Worker;
//import okhttp3.*;
import workers.MapWorker;
import model.Directions;
import model.Message;
import model.MyThread;
import java.util.Map;


public class Master implements MasterImp{
	
	private Directions ourDirections;
	private static Map<String, Object> cache;
	private MapWorker myWorker;

	public void initialize(){
		cache = new LinkedHashMap<>();
	}
	
	public void waitForNewQueriesThread(){
		MyThread mythread = new MyThread("Queries");
		new Thread(mythread).start();
		mythread.run();
	}
	
	public Directions searchCache(String dir){
		return (Directions) cache.get(dir);
	}
	
	public void distributeToMappers(){
		/**
		 * TODO: Fix myThread to open it again in all methods
		 */
		myWorker = new MapWorker();
		myWorker.initialize();
	}
	
	public void waitForMappers(){
		myWorker.notifyMaster();
	}
	
	public void ackToReducers(){
		
	}
	
	public void collecDataFromReducers(){
		
	}
	
	public Directions askGoogleDirectionsAPI(String startlat, String startlon, String endlat, String endlon){
		//TODO: Fix url: https://developers.google.com/maps/documentation/directions/start#get-a-key
		String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+startlat+","+startlon+"&destination="+endlat+","+endlon+"&key=AIzaSyB3ZUeeQPpFDS1SsD5KwIOiA9xyC8pBQM0";
		System.out.println(sendGet(url));
		//System.out.println(deserialize(sendGet(url)));
		return null;
		
	}
	
	public boolean updateCache(String dir, Directions newDir){
		if (!cache.containsKey(dir)){
			cache.put(dir, newDir);
			return true;
		}
		return false;
		
	}
	
	public boolean updateDatabase(String dir, Directions newDir){
		return false;
		
	}
	
	public void sendResultsToClient(){
		System.out.println(ourDirections.toString());
	}
	
	// HTTP GET request using OKHTTP
	private String sendGet(String url){
		try {
			return (new Master()).run(url);
		} catch (IOException e) {
			e.printStackTrace();
			return "Request to "+ url+" failed!";
		}
	}

	private	String run(String url) throws IOException {
		return null;
		/*OkHttpClient client = new OkHttpClient();
		  Request request = new Request.Builder()
		      .url(url)
		      .build();
		  try(Response response = client.newCall(request).execute()){
			  return response.body().string();
		  }	*/	  
	}
	//TODO: Fix the way we deserialize the json file we get from google
	private static String deserializeGooglejson(String str){
		return null;
		/*JSONObject object = new JSONObject(str);
		
		 * Reference to:
		 * http://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
		 
		
		return "";*/
	}
}
