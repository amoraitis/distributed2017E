package model;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerReducerForMaster extends Thread{
	ObjectInputStream in;
	ObjectOutputStream out;
	Directions reducedDirectons,askedDirections;
	Map<Integer, Directions> mappedDirections;
	public ServerReducerForMaster(Socket connection) {
		
		   try {
		        out = new ObjectOutputStream(connection.getOutputStream());
		        in = new ObjectInputStream(connection.getInputStream());
		
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	public Map<Integer, Directions> getMappedDirs(){
	  	return this.mappedDirections;
	}
	
	public Directions getAskedDirections() {
		return this.askedDirections;
	}
	
	public void setMappedDirections(Map<Integer, Directions> mappedDirections){
		this.mappedDirections=mappedDirections;
	}
	
	@Override
	public void run() {
		read();
	}
	 
	public synchronized void read() {	
	    
	         
	        try{
	        	//System.out.println(in.readObject().getClass().getName());
	        	mappedDirections =((Map<Integer, Directions>)in.readObject());
	        	askedDirections = (Directions)in.readObject();
	        	System.out.println(askedDirections.toString());
	        	
	        }catch(ClassNotFoundException classnot){              
	            System.err.println("Data received in unknown format!");
	        } catch (IOException e) {
	        	
	 		   System.err.println("Cannot read object!\n");
	 		   e.printStackTrace();
			}            
	    
	}	
	
	public void writeOutAndClose(Directions reducedDirections) {
		
		try {
			if (reducedDirections == null ){
				out.writeObject("null");
				out.flush();
			}else{
				out.writeObject(reducedDirections);
				out.flush();
			}
	        in.close();
	        out.close();
	    } catch (IOException ioException) {
	    	ioException.printStackTrace();
	    }
	}
}
