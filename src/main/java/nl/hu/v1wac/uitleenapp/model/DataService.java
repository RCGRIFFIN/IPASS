package nl.hu.v1wac.uitleenapp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
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
	
	public User getUserById(int userId) {
		return new UserPostgresDaolmpl().findByUserId(userId);
	}
	
	
	
	//Car
	
	public List<Car> getCarsByUserId(int userId){
		return new CarPostgresDaolmpl().findCarsByOwner(userId);
	}
	
	public List<Car> getCarsByUser(User user){
		return new CarPostgresDaolmpl().findCarsByOwner(user);
	}
	
	public Car getCarById(int carId) {
		return new CarPostgresDaolmpl().findById(carId);
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
	
	public List<Car> getCarsByTimeframe(Timestamp start, Timestamp end) {
		return new CarPostgresDaolmpl().findCarsByTimeframe(start,  end);
	}
	
	//Timeframe
	
	public int getNewTimeframeId() {
		return new AvailabilityTimeframePostgresDaolmpl().getNewTimeframeId();
	}
	
	public List<AvailabilityTimeframe> getTimeframesByCar(int carId){
		return new AvailabilityTimeframePostgresDaolmpl().findAvailableByCar(carId);
	}
	
	public boolean addTimeframe(AvailabilityTimeframe timeframe) {
		return new AvailabilityTimeframePostgresDaolmpl().save(timeframe);
	}
	
	public boolean removeTimeframe(int id) {
		return new AvailabilityTimeframePostgresDaolmpl().delete(id);
	}
	
	//lend session
	
	public int getNewLendSessionId() {
		return new LendSessionPostgresDaolmpl().getNewSessionId();
	}
	
	public LendSession getLendSessionById(int sessionId) {
		return new LendSessionPostgresDaolmpl().findById(sessionId);
	}
	
	public List<LendSession> getLendSessionAcceptPending(int carId){
		return new LendSessionPostgresDaolmpl().findLendSessionByCarAcceptPending(carId);
	}
	
	public List<LendSession> getLendSessionByUser(User user){
		return new LendSessionPostgresDaolmpl().findLendSessionByLender(user);
	}
	
	public List<LendSession> getLendSessionByUserKilometersPending(User user){
		return new LendSessionPostgresDaolmpl().findLendSessionByLenderKilometersPending(user);
	} 
	
	public List<LendSession> getLendSessionPaidPending(int carId){
		return new LendSessionPostgresDaolmpl().findLendSessionByCarPaidPending(carId);
	}
	
	public double getLendSessionPrice(LendSession lendSession) {
		return new LendSessionPostgresDaolmpl().getLendSesionPrice(lendSession);
	}
	
	public boolean addLendSession(LendSession lendSession) {
		return new LendSessionPostgresDaolmpl().save(lendSession);
	}
	
	public boolean updateLendSession(LendSession lendSession) {
		return new LendSessionPostgresDaolmpl().update(lendSession);
	}
}
