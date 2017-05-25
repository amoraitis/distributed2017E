package model;

import java.io.Serializable;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Directions implements Serializable{

	/**
   * 
   */
  private static final long serialVersionUID = -7981370642425531305L;
  private double startlon, startlat, endlon,endlat;
	private LinkedList<Direction> steps= new LinkedList<Direction>();
	private String dirs, duration,distance, startAddress, endAddress;


	public Directions(String dirs){
		this.dirs=dirs;
		deserializeGooglejson(dirs);
	}
	public Directions(double startlat, double startlon,double endlat, double endlon){
		this.endlat=endlat;
		this.endlon=endlon;
		this.startlat=startlat;
		this.startlon=startlon;
	}
	public Directions(){}
	
	public String toString() {
		return "SLat: "+this.getStartlat()+"\tSLon: "+this.getStartlon()
			+"\nELat: "+this.getEndlat()+"\tELon: "+this.getEndlon();	
	}
	
	public Directions get(){
		return this;
	}
	public LinkedList<Direction> getSteps(){
		return this.steps;
	}
	
	public String getStartAddress() {
		  return startAddress;
	}
	
	public void setStartAddress(String startAddress) {
		  this.startAddress = startAddress;
	}
	public String getEndAddress() {
		  return endAddress;
	}
	public void setEndAddress(String endAddress) {
		  this.endAddress = endAddress;
	}
	
	public String getDistance() {
		return distance;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public double getEndlon() {
		return endlon;
	}
	public void setEndlon(double endlon) {
		this.endlon = endlon;
	}
	public double getEndlat() {
		return endlat;
	}
	public void setEndlat(double endlat) {
		this.endlat = endlat;
	}
	public String getDirs() {
		return dirs;
	}
  
	public double getStartlon() {
		return startlon;
	}
	
	public double getStartlat() {
		return startlat;
	}
	
	public void setStartlon(double startlon){
		this.startlon=startlon;
	}
	public void setStartlat(double startlat) {
		this.startlat = startlat;
	}
	
	public String getForHashing() {
		return this.getStartlat()+""+this.getStartlon()+""+this.getEndlat()+""+this.getStartlat();
	}
	
	public boolean equals(Directions otherDirs){
		return (this.getStartlat()==otherDirs.getStartlat())&&(this.getStartlon()==otherDirs.getStartlon())
				&&(this.getEndlat()==otherDirs.getEndlat())&&(this.getEndlon()==otherDirs.getEndlon());
	}
	
	private void deserializeGooglejson(String directions){		
		JSONObject route= new JSONObject(new JSONObject(directions).getJSONArray("routes").get(0).toString());
		//System.out.println(route.toString());
		JSONArray legs = route.getJSONArray("legs");		
		JSONObject legsObj= new JSONObject(legs.get(0).toString());
		JSONArray stepsArray = legsObj.getJSONArray("steps");
		JSONObject endlocation = new JSONObject(legsObj.getJSONObject("end_location").toString());
		JSONObject startLocation = new JSONObject(legsObj.getJSONObject("start_location").toString());
		try{
			this.setStartlat(startlat = startLocation.getDouble("lat"));
			this.setStartlon(startlon= startLocation.getDouble("lng"));
			this.setEndlat(endlat = endlocation.getDouble("lat"));
			this.setEndlon(endlon = endlocation.getDouble("lng"));			
		}catch(Exception e){
			System.out.println(e.getMessage());
			//In case of fire: http://stackoverflow.com/questions/2591098/how-to-parse-json-in-java	
		}
		JSONObject durationObj = new JSONObject(legsObj.getJSONObject("duration").toString());		
		this.setDuration(durationObj.getString("text"));
		JSONObject distanceObj = new JSONObject(legsObj.getJSONObject("distance").toString());
		this.setDistance(distanceObj.getString("text"));
		this.setEndAddress(legsObj.getString("end_address").toString());
		this.setStartAddress(legsObj.getString("start_address").toString());
		JSONObject step;
		for(int i=0; i<stepsArray.length(); i++){
			step = new JSONObject(stepsArray.get(i).toString());
			steps.add(new Direction(step));
		}
	}
}
