package nl.hu.v1wac.uitleenapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.Car;

public class AvailabilityTimeframePostgresDaolmpl extends PostgresBaseDao implements AvailabilityTimeframeDao{

	@Override
	public List<AvailabilityTimeframe> findAvailableByCar(Car car) {
		ArrayList<AvailabilityTimeframe> timeframes = null;
		
		String query = "SELECT timeframe_id, start_, end_ FROM availability_timeframe WHERE car_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  car.getCarId());
			ResultSet resultSet = ps.executeQuery();
			
			timeframes = new ArrayList<AvailabilityTimeframe>();
			while (resultSet.next()) {
				timeframes.add(new AvailabilityTimeframe(
						resultSet.getInt(1),
						resultSet.getTimestamp(2),
						resultSet.getTimestamp(3),
						car.getCarId()
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find available for car '" + car.getCarId() + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return timeframes;
	}

	@Override
	public boolean save(AvailabilityTimeframe timeframe) {
		String query = "INSERT INTO availability_timeframe (timeframe_id, start_, end_, car_id) VALUES (?, ?, ?, ?)";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot save timeframe" + timeframe.getTimeframeId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean update(AvailabilityTimeframe timeframe) {
		String query = "UPDATE availability_timeframe SET start_=?, end_=?, car_id=? WHERE timeframe_id=?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1,  timeframe.getStart());
			ps.setTimestamp(2,  timeframe.getEnd());
			ps.setInt(3,  timeframe.getCarId());
			ps.setInt(4, timeframe.getTimeframeId());
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot update timeframe" + timeframe.getTimeframeId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}
	
	@Override
	public boolean delete(AvailabilityTimeframe timeframe) {
		return delete(timeframe.getTimeframeId());
	}
	
	public boolean delete(int timeframeId) {
		String query = "DELETE FROM availability_timeframe WHERE timeframe_id=?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  timeframeId);
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot update timeframe" + timeframeId);
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}
}
