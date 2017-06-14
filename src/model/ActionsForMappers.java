package model;
/**
 * UNUSED
 */
import java.io.*;
import java.net.*;
import java.util.Map;

public class ActionsForMappers extends Thread{
	
	ObjectInputStream in;
    ObjectOutputStream out;
    Directions askedDirections;
    Map<Integer, Directions> mappedDirections;
    
    public ActionsForMappers(Socket connection, Directions askedDirections) {
        this.askedDirections= askedDirections;
    	try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Map<Integer, Directions> getMappedDirs(){
    	return this.mappedDirections;
    }
     
    @SuppressWarnings("unchecked")
    public void run() {
 
        try {
             
            try{
            	out.writeObject(this.askedDirections);
                System.out.println("asdafasfafasfasf");
                out.flush();
            	this.mappedDirections =((Map<Integer, Directions>)in.readObject());
            	System.out.println(mappedDirections.get(0).toString());
            }catch(ClassNotFoundException classnot){            
                 
                System.err.println("Data received in unknown format!");
            }
 
             
             
            } catch (IOException e) {
            	System.out.println(e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
  
}
