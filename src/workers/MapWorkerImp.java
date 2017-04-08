package workers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface MapWorkerImp extends Worker{
	
	public Map<String, Object> map(Object o1, Object o2);
	public void notifyMaster();
	public String calculateHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	public void sendToReducers(Map<String, Object> mp);
}
