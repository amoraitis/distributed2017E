package workers;

import java.util.Map;

import model.Directions;
import model.Message;

import java.io.*;
import java.net.*;


public class ReduceWorker implements Worker, ReduceWorkerImp{
	private static Map<String,Object> reducedDirections;
	@Override
	public void waitForMasterAck(){
		//or: new ReduceWorker()
	}

	@Override
	public Map<String, Object> reduce(String dir, Object ob) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendResults(Map<String, Object> mp) {
		
		
	}

	@Override
	public void initialize() {		
		sendResults(reduce("", new Object()));
		
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
	}
	
}
