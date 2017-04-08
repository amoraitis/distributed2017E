package model;


public class Directions {
	private String result, startlon, startlat;
	//private ArrayList[]() routes;
	public Directions(){
		
	}
	public Directions(String result, String startlon, String startlat){
		this.result=result;
		this.startlat=startlat;
		this.startlon=startlon;
	}
	public String getResult() {
		return result;
	}
	
	public String getStartlon() {
		return startlon;
	}
	
	public String getStartlat() {
		return startlat;
	}
	
}
