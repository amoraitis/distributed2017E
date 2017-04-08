package workers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	public String calculateHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.reset();
        byte[] buffer = str.getBytes("UTF-8");
        messageDigest.update(buffer);
        byte[] digest = messageDigest.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
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