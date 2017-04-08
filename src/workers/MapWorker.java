package workers;

import java.util.Map;

public class MapWorker implements Worker, MapWorkerImp{
	
	@Override
	public Map<String, Object> map(Object o1, Object o2){
		return null;
	}
	@Override
	public void notifyMaster(){
		
	}
	
	@Override
	public String calculateHash(String s){
		return null;
	}
	public void sendToReducers(Map<String, Object> mp){
		
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
	}
}