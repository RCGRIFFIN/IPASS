package nl.hu.v1wac.uitleenapp.model;

import java.sql.Date;
import java.sql.Timestamp;

public class LendSession {
	private int sessionId;
	private boolean accepted;
	private boolean kilometersSubmitted;
	private int kilometers;
	private boolean paid;
	private Timestamp start;
	private Timestamp end;
	private int carId;
	private int userId;
	
	public LendSession(int sessionId) {
		this.sessionId = sessionId;
	}
	
	public LendSession(int sessionId, boolean accepted, boolean kilometersSubmitted, int kilometers, boolean paid, Timestamp start, Timestamp end, int carId, int userId) {
		this.sessionId = sessionId;
		this.accepted = accepted;
		this.kilometersSubmitted = kilometersSubmitted;
		this.kilometers = kilometers;
		this.paid = paid;
		this.start = start;
		this.end = end;
		this.carId = carId;
		this.userId = userId;
	}
	
	
	
	public int getSessionId() {return sessionId;}
	public boolean getAccepted() {return accepted;}
	public int getKilometers() {return kilometers;}
	public boolean getKilometersSubmitted() {return kilometersSubmitted;}
	public boolean getPaid() {return paid;}
	public Timestamp getStart() {return start;}
	public Timestamp getEnd() {return end;}
	public int getCarId() {return carId;}
	public int getUserId() {return userId;}
	
	
	public void setAccepted(boolean accepted) {this.accepted = accepted;}
	public void setKilometers(int kilometers) {
		if (kilometers < 0)
			kilometers = 0;
		
		this.kilometers = kilometers;
	}
	public void setKilometersSubmitte(boolean kilometersSubmitted) {this.kilometersSubmitted = kilometersSubmitted;}
	public void setPaid(boolean paid) {this.paid = paid;}
	public void setStart(Timestamp start) {this.start = start;}
	public void setEnd(Timestamp end) {this.end = end;}
	public void setCarId(int carId) {this.carId = carId;}
	public void setUserId(int userId) {this.userId = userId;}
	
	
	
	public String toString() {
		return "Session " + sessionId + " Accepted: " + accepted +  " distance: " + kilometers + "km kilometersSubmitted: " + kilometersSubmitted + " Paid: " + paid + " start: " + start.toString() + "end: " + end.toString() + " car: " + carId + " user: " + userId; 
	}
}
