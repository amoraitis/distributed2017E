package workers;

import java.util.Map;

import model.Message;

import java.io.*;
import java.net.*;


public class ReduceWorker extends Worker implements ReduceWorkerImp{
	
	@Override
	public void waitForMasterAck(){
		this.openServer();
		//or: new ReduceWorker()
	}

	@Override
	public Map<String, Object> reduce(String dir, Object ob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendResults(Map<String, Object> mp) {
		// TODO Auto-generated method stub
		
	}
	public void openServer() {
		ServerSocket providerSocket = null;
		Socket connection = null;
		try {
			providerSocket = new ServerSocket (4321);
			

			while (true) {
				
				connection = providerSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
				
				System.out.println(in.readUTF());
				System.out.println((Message) in.readObject());
				
				in.close();
				out.close();
				connection.close();
			}
				
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

}
