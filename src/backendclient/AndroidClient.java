package backendclient;

import workers.MapWorker;
import workers.ReduceWorker;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import master.Master;
import model.*;
// .abcdefghijklmnopqrstuvwxyz
public class AndroidClient {
	public static void main(String[] args){
		Master master = new Master();
		//master.askGoogleDirectionsAPI("33.812092","-117.918974","34.138117","-118.353378");
		//new ReducerClient(922, 52).start();
		MapWorker mapWorker = new MapWorker();
		try {
			System.out.println(mapWorker.calculateHash("33.81-117.91"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(mapWorker.getClass().getClassLoader().toString());
		
		
	}
}
