/**
 * 
 */
package model;

import org.json.JSONObject;
import java.io.Serializable;

/**
 * @author tasos
 *
 */
public class Direction implements Serializable{

/**
   * 
   */
  private static final long serialVersionUID = -4871766606666947778L;
private String distance, duration,polyline_points;
  public String getPolyline_points() {
	return polyline_points;
  }

	public void setPolyline_points(String polyline_points) {
		this.polyline_points = polyline_points;
	}
private double startLat,endLat,startLon,endLon;
	
	public Direction(JSONObject step){
		deserializeStep(step);
	}
	
	private void deserializeStep(JSONObject step) {
		this.setDistance(step.getJSONObject("distance").getString("text").toString());
		this.setDuration(step.getJSONObject("duration").getString("text").toString());
		this.setStartLat(step.getJSONObject("start_location").getDouble("lat"));
		this.setStartLon(step.getJSONObject("start_location").getDouble("lng"));
		this.setEndLat(step.getJSONObject("end_location").getDouble("lat"));
		this.setEndLon(step.getJSONObject("end_location").getDouble("lng"));
		this.setPolyline_points(step.getJSONObject("polyline").getString("points").toString());

	}	
	
	/**
	* @return the distance
	*/
	public String getDistance() {
	  return distance;
	}
	/**
	* @param distance the distance to set
	*/
	public void setDistance(String distance) {
		this.distance = distance;
	}
	/**
	* @return the duration
	*/
	public String getDuration() {
		return duration;
	}
	/**
	* @param duration the duration to set
	*/
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * @return the startLat
	*/
	public double getStartLat() {
		return startLat;
	}
	/**
	* @param startLat the startLat to set
	*/
	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}
	/**
	* @return the endLat
	*/
	public double getEndLat() {
		return endLat;
	}
	/**
	* @param endLat the endLat to set
	*/
	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}
	/**
	* @return the startLon
	*/
	public double getStartLon() {
		return startLon;
	}
	/**
	* @param startLon the startLon to set
	*/
	public void setStartLon(double startLon) {
		this.startLon = startLon;
	}
	/**
	* @return the endLon
	*/
	public double getEndLon() {
		return endLon;
	}
	/**
	* @param endLon the endLon to set
	*/
	public void setEndLon(double endLon) {
		this.endLon = endLon;
	}
	
}
