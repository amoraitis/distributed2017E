package master;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import model.*;
import okhttp3.*;
import workers.*;

public class Master extends Thread implements MasterImp{
	private boolean goneTOApi=false;
	private ObjectOutputStream workerOut=null;
	private ServerSocket providerSocket = null;
    private Socket connection, requestSocketForWorker = null;
	private Directions askedDirections,ourDirections;
	private static LinkedList<Directions> cache;
	private static Map<Integer, Directions> mappedDirections=null;
	private static Thread serverMasterforClient;
	
	public Master(){
		cache = new LinkedList<Directions>();
	}

	public void initialize(){
		while(true){
			waitForNewQueriesThread();
			askedDirections = ((ServerMasterforClient) serverMasterforClient).getAskedDirections();
			ourDirections = searchCache(askedDirections);
			if(ourDirections==null){
				/*MapWorker mapWorker = new MapWorker();
				mappedDirections = mapWorker.map();
				ReduceWorker reduceWorker=new ReduceWorker(mappedDirections, askedDirections);
				ourDirections= reduceWorker.reduce(mappedDirections);*/
				startClientForMapper();
				startClientforReducer(mappedDirections);
			}
			
			if(ourDirections==null){				
				ourDirections=askGoogleDirectionsAPI(askedDirections.getStartlat(),askedDirections.getStartlon(),
					askedDirections.getEndlat(),askedDirections.getEndlon());
				goneTOApi=true;
			}
			updateCache(ourDirections);
			distributeToMappers();
			System.out.println(ourDirections.toString());
			sendResultsToClient();
			askedDirections = null; ourDirections = null;
		}		
	}
	
	public void waitForNewQueriesThread(){
		if(connection!=null){
			((ServerMasterforClient) serverMasterforClient).read();
		}else{
			openServerForClient();
		}
	}
	
	public Directions searchCache(Directions dir){
		Directions idir=null;
		for(int i=0; i<cache.size(); i++){
			idir=cache.get(i);
			if(dir.equals(idir))
				return idir;
		}
		return null;
	}
	
	public void distributeToMappers(){
		if(goneTOApi){
			sendFromAPItoWorker(ourDirections);
			return;
		}
		sendFromAPItoWorker(null);
	}
	
	public void waitForMappers(){
		//startClientForMapper(ourDirections);//prosoxi sto object: ourDirections
	}
	
	public void ackToReducers(){
		startClientforReducer(mappedDirections);
	}
	
	public void collecDataFromReducers(){
		
	}
	
	public Directions askGoogleDirectionsAPI(double startlat, double startlon, double endlat, double endlon){
		String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+startlat+","+startlon+"&destination="+endlat+","+endlon+"&key=AIzaSyB3ZUeeQPpFDS1SsD5KwIOiA9xyC8pBQM0";
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
		((ServerMasterforClient)serverMasterforClient).write(ourDirections);
		try {
	      connection.close();
	      providerSocket.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		serverMasterforClient=null;	providerSocket=null; connection=null;
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
			  //System.out.println(response.body().string());
			  return response.body().string();
		  }  
	}
	
	private void startClientForMapper() {
		ObjectInputStream inputStream = null;
		int port = 4232;
		String worker1="172.16.1.57",worker2="172.16.2.14",worker3="172.16.2.13",workerForMap="";
		if(calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker3+port))<0 
				&& calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker2+port))>0){
			workerForMap=worker3;
		}else if(calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker2+port))<0 
				&& calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker1+port))>0){
			workerForMap=worker2;
		}else if(calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker1+port))<0 
				|| calculateHash(askedDirections.getForHashing()).compareTo(calculateHash(worker3+port))>0){
			workerForMap=worker1;
		}
		
        try {              
            requestSocketForWorker = new Socket(worker1, port);
            workerOut = new ObjectOutputStream(requestSocketForWorker.getOutputStream());
            inputStream = new ObjectInputStream(requestSocketForWorker.getInputStream());
            workerOut.writeObject(askedDirections);
            workerOut.flush();
            this.mappedDirections= ((Map<Integer, Directions>) inputStream.readObject());
            
        } catch (Exception e) {
        	e.printStackTrace();
        	System.err.println(e.getMessage());
		}
	}
	
	private void sendFromAPItoWorker(Directions ourDirections) {
		try {
		  workerOut.writeObject(ourDirections);
	      workerOut.flush();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }finally {
            try {
                requestSocketForWorker.close();                
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
	}
	
	private void startClientforReducer(Map<Integer, Directions> reducedDirections) {
        Socket requestSocket = null;
        ObjectOutputStream out=null;
        ObjectInputStream inputStream = null;
        try {
              
            requestSocket = new Socket("172.16.1.56", 4005);
            out= new ObjectOutputStream(requestSocket.getOutputStream());
            inputStream = new ObjectInputStream(requestSocket.getInputStream());
            out.writeObject(mappedDirections);
            out.flush();
            out.writeObject(askedDirections);
            out.flush();
            Object ourObject= inputStream.readObject();
            if(ourObject.toString().equals("null")){
            	this.ourDirections=null;
            }else{
                this.ourDirections = ((Directions)ourObject);
            }
            //ActionsForReducer actionsForReducer = new ActionsForReducer(requestSocket, mappedDirections);
            //actionsForReducer.start();
            
        } catch (Exception e) {
        	e.printStackTrace();
        	System.err.println(e.getMessage());
		} finally {
            try {
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }	
    
	private void openServerForClient() {
		
        
            try {
            	if(providerSocket==null){
		            providerSocket = new ServerSocket (4321);
					connection = providerSocket.accept();
					serverMasterforClient = new ServerMasterforClient(connection, askedDirections);
				}
				serverMasterforClient.run();
				//serverMasterforClient.setReducedDirections(new Directions(22,45,745,45));
				//serverMasterforClient.writeOutAndClose();
			} catch (Exception e) {
				e.printStackTrace();
			}              
    }

	private String calculateHash(String latlngopport){
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        md.update(latlngopport.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println("Hex format : " + hexString.toString());
		return hexString.toString();
	}
}
