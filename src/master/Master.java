package master;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Map;

import model.*;

import org.json.*;

import okhttp3.*;


public class Master implements MasterImp{
	
	private Directions ourDirections;
	private static LinkedList<Directions> cache;
	private Map<Integer, Directions> mappedDirections;

	public void initialize(){
		cache = new LinkedList<Directions>();
	}
	
	public void waitForNewQueriesThread(){
		
	}
	
	public Directions searchCache(Directions dir){
		Directions idir=null;
		for(int i=0; i<cache.size(); i++){
			idir=cache.get(i);
			if(dir.equals(idir))
				return idir;
		}
		return idir;
	}
	
	public void distributeToMappers(){
		/**
		 * TODO: Fix myThread to open it again in all methods
		 */

	}
	
	public void waitForMappers(){
	}
	
	public void ackToReducers(){
		startClient(mappedDirections);
	}
	
	public void collecDataFromReducers(){
		
	}
	
	public Directions askGoogleDirectionsAPI(String startlat, String startlon, String endlat, String endlon){
		//TODO: Fix url: https://developers.google.com/maps/documentation/directions/start#get-a-key
		String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+startlat+","+startlon+"&destination="+endlat+","+endlon+"&key=AIzaSyB3ZUeeQPpFDS1SsD5KwIOiA9xyC8pBQM0";
		//System.out.println(sendGet(url));
		//System.out.println(deserialize(sendGet(url)));
		return new Directions(sendGet(url));
	}
	
	public boolean updateCache(Directions newDir){
		boolean isThere=true;
		Directions idir;
		for(int i=0; i<cache.size(); i++){
			idir=cache.get(i);
			if(newDir.equals(idir)){
				isThere=false;
				i=cache.size()+2000;
			}
		}
		
		if(isThere)cache.add(newDir);
		
		return isThere;
		
	}
	
	
	public boolean updateDatabase(String dir, Directions newDir){
		return false;
	}
	
	public void sendResultsToClient(){

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
		
		OkHttpClient client = new OkHttpClient();
		  Request request = new Request.Builder()
		      .url(url)
		      .build();
		  try(Response response = client.newCall(request).execute()){
			  return response.body().string();
		  }  
	}
	
	
	public void startClient(Map<Integer, Directions> reducedDirections) {
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Directions message;
        try {
             
            requestSocket = new Socket("172.16.2.46", 4321);
             
             
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());
             
            try{
                out.writeObject(reducedDirections);
                out.flush();
                message = (Directions) in.readObject();
                System.out.println("Server>" + message.getDirs());
                 
                                  
            }catch (ClassNotFoundException classNot) {
                System.err.println("data received in unknown format");
            }
 
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
