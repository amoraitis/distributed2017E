package backendclient;

import workers.ReduceWorker;
import master.Master;
import model.*;
// .abcdefghijklmnopqrstuvwxyz
public class AndroidClient {
	public static void main(String[] args){
		Master master = new Master();
		//master.askGoogleDirectionsAPI("33.812092","-117.918974","34.138117","-118.353378");
		new ReducerClient(922, 52).start();
		
		
	}
}
