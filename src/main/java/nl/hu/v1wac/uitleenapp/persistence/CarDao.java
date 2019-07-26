package nl.hu.v1wac.uitleenapp.persistence;

import java.sql.Timestamp;
import java.util.List;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.User;

public interface CarDao {
	public List<Car> findCarsByOwner(int userId);
	public List<Car> findCarsByTimeframe(AvailabilityTimeframe timeframe, User user);
	public List<Car> findCarsByTimeframe(Timestamp start, Timestamp end, User user);
	public boolean save(Car car);
	public boolean update(Car car);
	public boolean delete(Car car);
}
