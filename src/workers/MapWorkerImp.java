package workers;

import java.util.Map;

public interface MapWorkerImp extends Worker{
	
	public Map<String, Object> map(Object o1, Object o2);
	public void notifyMaster();
	public String calculateHash(String s);
	public void sendToReducers(Map<String, Object> mp);
}
