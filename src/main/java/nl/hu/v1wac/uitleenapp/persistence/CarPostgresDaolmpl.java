package nl.hu.v1wac.uitleenapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.LendSession;

public class CarPostgresDaolmpl extends PostgresBaseDao implements CarDao {

	@Override
	public List<Car> findCarsByOwner(int userId) {
		ArrayList<Car> cars = null;
		
		String query = "SELECT car_id, model, price_per_km, mileage FROM car WHERE user_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  userId);
			ResultSet resultSet = ps.executeQuery();
			
			cars = new ArrayList<Car>();
			
			while (resultSet.next()) {
				Car newCar = new Car(
						resultSet.getInt(1),
						userId,
						resultSet.getString(2),
						resultSet.getDouble(3),
						resultSet.getInt(4)
					);
				
				for (LendSession ls : new LendSessionPostgresDaolmpl().findByCar(resultSet.getInt(1)))
					newCar.addLendSession(ls);
				
				for (AvailabilityTimeframe timeframe : new AvailabilityTimeframePostgresDaolmpl().findAvailableByCar(newCar))
					newCar.addAvailable(timeframe);
				
				
				cars.add(newCar);
				
			}
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find car for user '" + userId + "':");
			e.printStackTrace();
		}
		return cars;
	}
	
	private Car findById(int id) {
		Car car = null;
		
		String query = "SELECT user_id, model, price_per_km, mileage FROM car WHERE car_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				car = new Car(
						id,
						resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getDouble(3),
						resultSet.getInt(4)
					);
				
				for (LendSession ls : new LendSessionPostgresDaolmpl().findByCar(id))
					car.addLendSession(ls);
				
				for (AvailabilityTimeframe timeframe : new AvailabilityTimeframePostgresDaolmpl().findAvailableByCar(car))
					car.addAvailable(timeframe);
				
				
				connection.close();
			}
		}
		catch (Exception e){
			System.out.println("Error cannot find car " + id);
			e.printStackTrace();
		}
		return car;
	}

	@Override
	public List<Car> findCarsByTimeframe(AvailabilityTimeframe timeframe) {
		return findCarsByTimeframe(timeframe.getStart(), timeframe.getEnd());
	}
	
	@Override
	public List<Car> findCarsByTimeframe(Timestamp start, Timestamp end) {
		ArrayList<Car> cars = null;
		
		String query = "SELECT DISTINCT cr.car_id, cr.user_id, cr.model, cr.price_per_km, cr.mileage, ls.car_id" + 
				"FROM availability_timeframe atf" + 
				"JOIN car cr" + 
					"ON atf.car_id = cr.car_id" + 
				"FULL OUTER JOIN lend_session ls" + 
					"ON atf.car_id = ls.car_id" + 
				"WHERE" + 
					"? >= atf.start_" + 
					"AND ? <= atf.end_" + 
					"AND ((ls.start_ NOT BETWEEN ? AND ?" + 
						"AND ls.end_ NOT BETWEEN ? AND ?)" + 
					"OR ls.car_id is null)";
		
		
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1, start);
			ps.setTimestamp(2, end);
			ps.setTimestamp(3, start);
			ps.setTimestamp(4, end);
			ps.setTimestamp(5, start);
			ps.setTimestamp(6, end);
			ResultSet resultSet = ps.executeQuery();
			
			cars = new ArrayList<Car>();
			
			while (resultSet.next()) {
				Car newCar = new Car(
						resultSet.getInt(1),
						resultSet.getInt(2),
						resultSet.getString(3),
						resultSet.getDouble(4),
						resultSet.getInt(5)
					);
				for (LendSession ls : new LendSessionPostgresDaolmpl().findByCar(resultSet.getInt(1)))
					newCar.addLendSession(ls);
				
				for (AvailabilityTimeframe timeframe : new AvailabilityTimeframePostgresDaolmpl().findAvailableByCar(newCar))
					newCar.addAvailable(timeframe);
				
				
				cars.add(newCar);
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find cars between timeframe start: " + start + "end: " + end);
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return cars;
	}

	@Override
	public boolean save(Car car) {
		String query = "INSERT INTO car (car_id, model, price_per_km, mileage, user_id) VALUES (?, ?, ?, ?, ?)";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  car.getCarId());
			ps.setString(2,  car.getModel());
			ps.setDouble(3,  car.getPricePerKm());
			ps.setInt(4, car.getMileage());
			ps.setInt(5,  car.getOwner());
			rowsAffected = ps.executeUpdate();
			
			for (LendSession session : car.getLendSessions())
				new LendSessionPostgresDaolmpl().save(session);
			
			for (AvailabilityTimeframe timeframe : car.getAvailability())
				new AvailabilityTimeframePostgresDaolmpl().save(timeframe);
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot save car" + car.getCarId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean update(Car car) {
		String query = "UPDATE car SET model=?, price_per_km=?, mileage=?, user_id=? WHERE car_id = ?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,  car.getModel());
			ps.setDouble(2,  car.getPricePerKm());
			ps.setInt(3, car.getMileage());
			ps.setInt(4,  car.getOwner());
			ps.setInt(5,  car.getCarId());
			rowsAffected = ps.executeUpdate();
			
			ArrayList<AvailabilityTimeframe> oldTimeframes = (ArrayList<AvailabilityTimeframe>) new AvailabilityTimeframePostgresDaolmpl().findAvailableByCar(car);
			ArrayList<Integer> oldTimeframeIds = new ArrayList<Integer>();
			ArrayList<Integer> newTimeframeIds = new ArrayList<Integer>();
			
			
			
			for (AvailabilityTimeframe timeframe : oldTimeframes) {
				oldTimeframeIds.add(timeframe.getTimeframeId());
			}
			
			for(AvailabilityTimeframe timeframe : car.getAvailability())
				newTimeframeIds.add(timeframe.getTimeframeId());
			
			
 
			for (int id : oldTimeframeIds) {
				if (!newTimeframeIds.contains(id))
					new AvailabilityTimeframePostgresDaolmpl().delete(id);	//delete timeframes from database that are not in the car object
			}
			
			for (AvailabilityTimeframe timeframe : car.getAvailability()) {
				if (!oldTimeframeIds.contains(timeframe.getTimeframeId()))
					new AvailabilityTimeframePostgresDaolmpl().save(timeframe); // save new timeframes to database
				else
					new AvailabilityTimeframePostgresDaolmpl().update(timeframe); // update existing timeframes in database
			}
			
			
			ArrayList<LendSession> oldSessions = (ArrayList<LendSession>) new LendSessionPostgresDaolmpl().findByCar(car.getCarId());
			ArrayList<Integer> oldSessionIds = new ArrayList<Integer>();
			ArrayList<Integer> newSessionIds = new ArrayList<Integer>();
			
			for (LendSession session : oldSessions)
				oldSessionIds.add(session.getSessionId());
			
			for (LendSession session : car.getLendSessions())
				newSessionIds.add(session.getSessionId());
			
			for (int id : oldSessionIds)
				if (!newSessionIds.contains(id))
					new LendSessionPostgresDaolmpl().delete(id); //delete lendsessions from database that are not in the car object
			
			for (LendSession session : car.getLendSessions()) {
				if (!oldSessionIds.contains(session.getSessionId()))
					new LendSessionPostgresDaolmpl().save(session); // save new lendsessions to database
				else
					new LendSessionPostgresDaolmpl().update(session); // update existing lendsessions in database
			}
			
			connection.close();
			
		}
		catch (Exception e){
			System.out.println("Error cannot update car" + car.getCarId());
			e.printStackTrace();
		}
		
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean delete(Car car) {
		String query = "DELETE FROM car WHERE car_id = ?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			for (AvailabilityTimeframe timeframe : car.getAvailability())
				new AvailabilityTimeframePostgresDaolmpl().delete(timeframe);
			
			for (LendSession session : car.getLendSessions())
				new LendSessionPostgresDaolmpl().delete(session);
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  car.getCarId());
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot delete car" + car.getCarId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	public boolean delete(int id) {
		Car car = this.findById(id);
		
		return delete(car);
	}
}
