package workers;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.util.*;
import client.AppClient;
import model.*;

public class MapWorker extends Thread implements Worker, MapWorkerImp{

	private static ServerWorkerForMaster serverWorkerForMaster;
	private static Map<Integer, Directions> mappedDirections;
    private static Directions askedDirections;
    private Socket connection = null;    
    private ServerSocket providerSocket = null;
	public Map<Integer, Directions> map(){
		final File currentFilePath = new File(AppClient.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		
		String DBFOLDER = currentFilePath.getParentFile().getParentFile()+ File.separator+"dbsnfiles"+File.separator+"directs.txt";
		//System.out.println(DBFOLDER);
		Map<Integer, Directions> map=null;
		try{
			map = new HashMap<Integer, Directions>();
			BufferedReader br = new BufferedReader(new FileReader(DBFOLDER));
			String json ="";
	        String line="";
	        int i=0;
	        while (line != null) {
	            line = br.readLine();
	            json = json + line;
	            if(json.contains('"'+"status"+'"')){
	            	line = br.readLine();
	            	json+= line;
		            map.put(i,new Directions(json));
		            i++;
		            json="";
	            }
	        }	        
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		this.mappedDirections = map;
		return map;
	}

	public void notifyMaster(){
		sendToReducers();
	}
	
	
	
	
	public int calculateHash(String str){
		String plaintext = str;
	    int hash = str.hashCode();
	    MessageDigest m;
	    try {
	        m = MessageDigest.getInstance("MD5");
	        m.reset();
	        m.update(plaintext.getBytes());
	        byte[] digest = m.digest();
	        BigInteger bigInt = new BigInteger(1,digest);
	        String hashtext = bigInt.toString(10);
	        // Now we need to zero pad it if you actually want the full 32 chars.
	        while(hashtext.length() < 32 ){
	          hashtext = "0"+hashtext;
	        }
	        int temp = 0;
	        for(int i =0; i<hashtext.length();i++){
	            char c = hashtext.charAt(i);
	            temp+=(int)c;
	        }
	        return hash+temp;
	    } catch (NoSuchAlgorithmException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return hash;
    }
	
	public void sendToReducers(){
		map();
		closeServerForClient();
	}
	
	/**
	 *  
	 * @param slat
	 * @param slon
	 * @param elat
	 * @param elon
	 * @return the distance between coordinates.
	 */
	private double distance(Double slat,Double slon, Double elat,Double elon){
		return Math.pow(Math.abs(elat - slat), 2)+Math.pow(Math.abs(elon-slon), 2);
	}

	
	public void initialize() {
		//sendToReducers(map());
		while(true){
			waitForTasksThread();
			sendToReducers();
			if(serverWorkerForMaster.isHasAPI()){
				appendLocation(((Directions)serverWorkerForMaster.getReadAppend()).getDirs());
			}
			serverWorkerForMaster=null;	
		}
	}

	
	public void waitForTasksThread() {
		if(connection!=null){
			((ServerWorkerForMaster)serverWorkerForMaster).read();
		}else{
			openServerForMaster();
		}
		
	}
	
	private void openServerForMaster() {      
        
         
            try {
            	if (providerSocket==null) {
	                providerSocket = new ServerSocket (4232);
	                connection = providerSocket.accept();
	                serverWorkerForMaster = new ServerWorkerForMaster(connection);
            	}
	            serverWorkerForMaster.run();
                //serverMasterforClient.setReducedDirections(new Directions(22,45,745,45));
            } catch (UnknownHostException unknownHost) {
                System.err.println("You are trying to connect to an unknown host!");
            }catch (IOException ioException) {
                ioException.printStackTrace();
            }    
    }
     
    private synchronized void closeServerForClient() {
    	((ServerWorkerForMaster)serverWorkerForMaster).writeOutAndClose(mappedDirections); 
        try {
  	      connection.close();
  	      providerSocket.close();
  	    } catch (IOException e) {
  	    	e.printStackTrace();
  	    }
  		providerSocket=null; connection=null;
        
    }
    
    private static void appendLocation(String response) {		
		final File currentFilePath = new File(AppClient.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		
		String DBFOLDER = "file:///"+ currentFilePath.getParentFile().getParentFile()+ File.separator+"dbsnfiles"+File.separator+"directs.txt";
		URL myUri = null;
		try {
			myUri = new URL(DBFOLDER);
			System.out.println(myUri.toString());
			Files.write(Paths.get(new URI(myUri.getProtocol(), myUri.getHost(), myUri.getPath(), myUri.getQuery(), null)), response.getBytes(), StandardOpenOption.APPEND);
      
	    } catch (Exception e) {
	      //System.out.println(e.getMessage());
	      //if(myUri!=null)System.out.println(myUri.toString());
	    }
		
	}
}