package nl.hu.v1wac.uitleenapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	int userId;
	String email;
	String name;
	String passwordHash;
	String role;	
	List<LendSession> lendSessions;
	List<Car> cars;
	
	
	public User(int userId, String email){
		this.userId = userId;
		this.email = email;
		this.lendSessions = new ArrayList<LendSession>();
		this.cars = new ArrayList<Car>();
	}
	
	public User(int userId, String email, String name, String passwordHash, String role){
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.passwordHash = passwordHash;
		this.role = role;		
		this.lendSessions = new ArrayList<LendSession>();
		this.cars = new ArrayList<Car>();
	}
	
	public int getUserId() {return userId;}
	public String getEmail() {return email;}
	public String getName() {return name;}
	public String getPasswordHash() {return passwordHash;}
	public String getRole() {return role;}
	
	public List<LendSession> getLendSessions(){return lendSessions;}
	public List<Car> getCars() {return cars;}
	
	public void setEmail(String email) {this.email = email;}
	public void setName(String name) {this.name = name;}
	public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}
	public void setrole(String role) {this.role = role;}
	
	public void addCar(Car car) {
		if (car != null)
			cars.add(car);
	}
	
	public void removeCar(Car car) {
		if (car != null && cars.contains(car))
			cars.remove(car);
	}
	
	public void addLendSession(LendSession session) {
		if (session != null)
			lendSessions.add(session);
	}
	
	public void removeLendSession(LendSession session) {
		if (session != null && lendSessions.contains(session))
			lendSessions.remove(session);
	}
	
	
	
	public String toString() {
		String s = "User " + userId + " email: " + email + " name: " + name + " role " + role;
		
		s += "Cars:\r\n";
		
		for (Car c : cars)
			s += "    "+ c.toString() + "\r\n";
		
		s += "\r\n";
		s += "LendSessions:\r\n";
		for (LendSession ls : lendSessions)
			s+= "    " + ls.toString();
		
		return s;
	}
}
