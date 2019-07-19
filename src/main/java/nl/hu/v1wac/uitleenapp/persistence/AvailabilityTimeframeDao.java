package nl.hu.v1wac.uitleenapp.persistence;

import java.util.List;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.Car;

public interface AvailabilityTimeframeDao {
	public List<AvailabilityTimeframe> findAvailableByCar(Car car);
	public boolean save(AvailabilityTimeframe timeframe);
	public boolean update(AvailabilityTimeframe timeframe);
	public boolean delete(AvailabilityTimeframe timeframe);
}
