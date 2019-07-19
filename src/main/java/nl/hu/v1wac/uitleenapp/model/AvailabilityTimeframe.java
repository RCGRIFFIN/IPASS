package nl.hu.v1wac.uitleenapp.model;

import java.sql.Timestamp;



public class AvailabilityTimeframe {
	private int timeframeId;
	private Timestamp start;
	private Timestamp end;
	private int carId;
	
	public AvailabilityTimeframe(int timeframeId) {
		this.timeframeId = timeframeId;
	}
	
	public AvailabilityTimeframe(int timeframeId, Timestamp start, Timestamp end, int carId) {
		this.timeframeId = timeframeId;
		this.start = start;
		this.end = end;
		this.carId = carId;
	}
	
	public int getTimeframeId() {return timeframeId;}
	public Timestamp getStart() {return start;}
	public Timestamp getEnd() {return end;}
	public int getCarId() {return carId;}
	
	
	public void setStart(Timestamp start) {this.start = start;}
	public void setEnd(Timestamp end) {this.end = end;}
	public void setCarId(int carId) {this.carId = carId;}
	
	
	public String toString() {
		return "Timeframe " + timeframeId + " Start: " + start.toString() + " end: " + end.toString() + " car: " + carId;
	}
}
