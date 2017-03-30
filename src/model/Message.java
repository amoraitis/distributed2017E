package model;
import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 6167773185136682168L;
	//private static final long /******************/;
	int id;
	Directions data;

	public Message(int id, Directions data) {
		super();
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Directions getData() {
		return data;
	}

	public void setData(Directions data) {
		this.data = data;
	}

	public String toString() {
		return id + " - " + data.toString();
	}

}
