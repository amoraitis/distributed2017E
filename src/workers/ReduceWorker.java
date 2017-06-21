package workers;

import java.util.Map;
import java.util.NoSuchElementException;

import model.Directions;
import model.ServerReducerForMaster;
import java.io.*;
import java.net.*;



public class ReduceWorker extends Thread implements Worker, ReduceWorkerImp{
	private Directions askedDirections, reducedDirections;
	private static Map<Integer, Directions> mappedDirections=null;
	private ServerReducerForMaster serverReducerForMaster;
    private Socket connection = null;
	private ServerSocket providerSocket = null;
	public ReduceWorker(Map<Integer, Directions> map, Directions askedDirections){
		mappedDirections=map;
		this.askedDirections=askedDirections;
	}
	public ReduceWorker() {
		// TODO Auto-generated constructor stub
	}
	public void waitForMasterAck(){
		//or: new ReduceWorker()
	}


	public Directions getReducedDirections() {
		return reducedDirections;
	}
	
	private void setReducedDirections(Directions reducedDirections) {
		this.reducedDirections= reducedDirections;
	}
	
	public Directions reduce(Map<Integer, Directions> mp) {
		/*Directions directions =*/
		Directions counted = null;
		try {
			counted = (Directions) mp.entrySet().stream().parallel().filter(p->p.getValue().equals(this.askedDirections)).
					map(p->p.getValue()).reduce((sum, p)->sum).get();
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
		
		return counted;
	}
	
	public void sendResults(Directions dirs) {
		((ServerReducerForMaster)serverReducerForMaster).writeOutAndClose(dirs);
		try {
			connection.close();
			providerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverReducerForMaster=null; providerSocket=null; connection=null;
	}


	public void initialize() {
		while(true){
			if(connection!=null){
				serverReducerForMaster.read();
			}else{
				openServerForMaster();
			}
    		this.askedDirections=((ServerReducerForMaster)serverReducerForMaster).getAskedDirections();
            this.mappedDirections=((ServerReducerForMaster)serverReducerForMaster).getMappedDirs();
			reducedDirections=reduce(this.mappedDirections);
			sendResults(reducedDirections);
		}
	}


	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
	}
	
	private void openServerForMaster() {
         
            try {
            	if (providerSocket==null) {
	                providerSocket = new ServerSocket (4005);
	                connection = providerSocket.accept();
	                serverReducerForMaster = new ServerReducerForMaster(connection);
            	}
                serverReducerForMaster.run();
            } catch (UnknownHostException unknownHost) {
                System.err.println("You are trying to connect to an unknown host!");
            }catch (IOException ioException) {
                ioException.printStackTrace();
            }    
	}
}
