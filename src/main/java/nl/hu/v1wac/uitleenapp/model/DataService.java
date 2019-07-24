package nl.hu.v1wac.uitleenapp.model;

import java.util.List;

import nl.hu.v1wac.uitleenapp.persistence.AvailabilityTimeframePostgresDaolmpl;
import nl.hu.v1wac.uitleenapp.persistence.CarPostgresDaolmpl;
import nl.hu.v1wac.uitleenapp.persistence.LendSessionPostgresDaolmpl;
import nl.hu.v1wac.uitleenapp.persistence.UserPostgresDaolmpl;

public class DataService {
	
	public DataService() {}
	
	//User
	
	public User getUserByUserName(String username) {
		return new UserPostgresDaolmpl().findByUsername(username);
	}
	
	public int getNewUserId() {
		return new UserPostgresDaolmpl().getNewUserId();
	}
	
	
	
	//Car
	
	public List<Car> getCarsByUserId(int userId){
		return new CarPostgresDaolmpl().findCarsByOwner(userId);
	}
	
	public List<Car> getCarsByUser(User user){
		return new CarPostgresDaolmpl().findCarsByOwner(user);
	}
	
	public boolean removeCar(int id) {
		return new CarPostgresDaolmpl().delete(id);
	}
	
	public boolean addCar(Car car) {
		return new CarPostgresDaolmpl().save(car);
	}
	
	public int getNewCarId() {
		return new CarPostgresDaolmpl().getNewCarId();
	}
	
	
	//Timeframe
	
	public int getNewTimeframeId() {
		return new AvailabilityTimeframePostgresDaolmpl().getNewTimeframeId();
	}
	
	public boolean addTimeframe(AvailabilityTimeframe timeframe) {
		return new AvailabilityTimeframePostgresDaolmpl().save(timeframe);
	}
	
	//lend session
	
	public int getNewLendSessionId() {
		return new LendSessionPostgresDaolmpl().getNewSessionId();
	}
	
}
