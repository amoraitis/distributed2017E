package model;

import java.io.*;
import java.net.*;

public class ReducerClient extends Thread {
	    int a, b;
	    public ReducerClient(int a, int b) {
	        this.a = a;
	        this.b = b;
	    }
	 
	    public void run() {
	        Socket requestSocket = null;
	        ObjectOutputStream out = null;
	        ObjectInputStream in = null;
	        try {
	            requestSocket = new Socket(InetAddress.getByName("172.16.3.4"), 4322);
	             
	             
	            out = new ObjectOutputStream(requestSocket.getOutputStream());
	            in = new ObjectInputStream(requestSocket.getInputStream());
	             
	            out.writeInt(a);
	            out.flush();
	             
	            out.writeInt(b);
	            out.flush();
	             
	            System.out.println("Server>" + in.read());
	 
	     
	        } catch (UnknownHostException unknownHost) {
	            System.err.println("You are trying to connect to an unknown host!");
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        } finally {
	            try {
	                in.close(); out.close();
	                requestSocket.close();
	            } catch (IOException ioException) {
	                ioException.printStackTrace();
	            }
	        }
	    }
}
