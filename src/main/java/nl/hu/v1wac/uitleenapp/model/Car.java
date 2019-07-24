package nl.hu.v1wac.uitleenapp.model;

import java.util.ArrayList;
import java.util.List;

public class Car {
	private int carId;
	private int owner;
	private String model;
	private double pricePerKm;
	private int mileage;
	private List<AvailabilityTimeframe> availability;
	private List<LendSession> lendSessions;
	
	
	public Car(int carId) {
		this.carId = carId;
	}
	
	public Car(int carId, int owner, String model, double pricePerKm, int mileage) {
		this.carId = carId;
		this.owner = owner;
		this.model = model;
		this.pricePerKm = pricePerKm;
		this.mileage = mileage;
		
		this.availability = new ArrayList<AvailabilityTimeframe>();
		this.lendSessions = new ArrayList<LendSession>();
	}
	
	public int getCarId() {return this.carId;}
	public int getOwner() {return owner;}
	public String getModel() {return model;}
	public double getPricePerKm() {return pricePerKm;}
	public int getMileage() {return mileage;}
	
	public int getReservationsPending() {
		int reservationsPending = 0;
		
		
		for (LendSession session : lendSessions)
			if (session.getAccepted() == false)
				reservationsPending++;
		
		return reservationsPending;
	}
	
	public int getPaidPending() {
		int paidPending = 0;
		
		for (LendSession session : lendSessions)
			if (session.getAccepted() == true && session.getPaid() == false)
				paidPending++;
		
		
		
		return paidPending;
	}
	
	public List<AvailabilityTimeframe> getAvailability() {return availability;}
	public List<LendSession> getLendSessions(){return lendSessions;}
	
	
	public void setOwner(int owner) {this.owner = owner;}
	public void setModel(String model) {this.model = model;}
	public void setPricePerKm(double pricePerKm) {this.pricePerKm = pricePerKm;}
	public void setMileage(int mileage) {this.mileage = mileage;}
	
	public void addAvailable(AvailabilityTimeframe timeFrame) {
		if (timeFrame != null)
			availability.add(timeFrame);
	}
	
	public void removeAvailable(AvailabilityTimeframe timeframe) {
		if (timeframe != null && availability.contains(timeframe))
			availability.remove(timeframe);
	}
	
	public void addLendSession(LendSession session) {
		if (session != null)
			lendSessions.add(session);
	}
	
	public void removeLendSession(LendSession session) {
		if (session != null && lendSessions.contains(session))
			lendSessions.remove(session);
	}
	
	public void addKilometers(int kilometers) {
		this.mileage += kilometers;
	}
	
	public String toString() {
		return "Car " + carId + " Owner: " + owner + " model: " + model + " price Per km: €" + pricePerKm + " mileage: " + mileage + "km";
	}
}
