package nl.hu.v1wac.uitleenapp.model;

import java.util.List;

import nl.hu.v1wac.uitleenapp.persistence.CarPostgresDaolmpl;

public class DataService {
	
	public DataService() {}
	
	public List<Car> getCarsByUserId(int userId){
		return new CarPostgresDaolmpl().findCarsByOwner(userId);
	}
}
