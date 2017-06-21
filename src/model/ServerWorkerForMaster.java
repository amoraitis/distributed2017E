package model;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerWorkerForMaster extends Thread{
	ObjectInputStream in;
	ObjectOutputStream out;
	Directions askedDirections;
	Object readAppend;
	boolean hasAPI= false;
	Map<Integer, Directions> mappedDirections;
	
	public ServerWorkerForMaster(Socket connection) {
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
	        	askedDirections =((Directions)in.readObject());      
	        	System.out.println(askedDirections.toString());
	        	
	        }catch(ClassNotFoundException classnot){              
	            System.err.println("Data received in unknown format!");
	        } catch (IOException e) {
	        	
	 		   System.err.println("Cannot read object!\n");
	 		   e.printStackTrace();
			}            
	    
	}
	
	
	
	public void writeOutAndClose(Map<Integer, Directions> mappedDirections) {
		
		try {
			this.setMappedDirections(mappedDirections);
			out.writeObject(this.getMappedDirs());
			out.flush();
			readAppend = (Directions) in.readObject();
			if(((Directions)readAppend).equals(new Directions(0, 0, 0, 0))){
				hasAPI=false;
			}else{
				hasAPI=true;
			}
	    } catch (IOException ioException) {
	    	ioException.printStackTrace();
	    }catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	private void close() {
		try{
			in.close();
			out.close();
			this.interrupt();
		}catch (IOException e) {
			e.getMessage();
		}
	}
	
	public Object getReadAppend() {
		return readAppend;
	}

	public boolean isHasAPI() {
		return hasAPI;
	}
}
