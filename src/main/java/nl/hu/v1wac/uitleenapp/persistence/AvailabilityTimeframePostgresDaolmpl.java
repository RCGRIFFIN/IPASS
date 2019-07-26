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
		return findAvailableByCar(car.getCarId());
	}
	
	public List<AvailabilityTimeframe> findAvailableByCar(int carId) {
		ArrayList<AvailabilityTimeframe> timeframes = null;
		
		String query = "SELECT timeframe_id, start_, end_, user_id FROM availability_timeframe WHERE car_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  carId);
			ResultSet resultSet = ps.executeQuery();
			
			timeframes = new ArrayList<AvailabilityTimeframe>();
			while (resultSet.next()) {
				timeframes.add(new AvailabilityTimeframe(
						resultSet.getInt(1),
						resultSet.getTimestamp(2),
						resultSet.getTimestamp(3),
						carId,
						resultSet.getInt(4)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find available for car '" + carId + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return timeframes;
	}

	@Override
	public boolean save(AvailabilityTimeframe timeframe) {
		String query = "INSERT INTO availability_timeframe (timeframe_id, start_, end_, car_id, user_id) VALUES (?, ?, ?, ?, ?)";
		
		ArrayList<AvailabilityTimeframe> timeframes = (ArrayList<AvailabilityTimeframe>) findAvailableByCar(timeframe.getCarId());
		
		
		for (AvailabilityTimeframe tf : timeframes) {
			if (tf.isInTimeframe(timeframe.getStart()) && tf.isInTimeframe(timeframe.getEnd())) {
				return true;
			}
			else if (tf.isInTimeframe(timeframe.getStart()) && !tf.isInTimeframe(timeframe.getEnd())) {
				tf.setEnd(timeframe.getEnd());
				return update(tf);
			}
			else if (!tf.isInTimeframe(timeframe.getStart()) && tf.isInTimeframe(timeframe.getEnd())) {
				tf.setStart(timeframe.getStart());
				return update(tf);
			}        
			else if (tf.getEnd().getTime() <= timeframe.getEnd().getTime() && tf.getStart().getTime() >= timeframe.getStart().getTime()) {
				System.out.println(tf.getEnd().getTime() <= timeframe.getEnd().getTime());
				System.out.println(tf.getStart().getTime() >= timeframe.getStart().getTime());
				tf.setStart(timeframe.getStart());
				tf.setEnd(timeframe.getEnd());
				return update(tf);
			}
				
		}
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  timeframe.getTimeframeId());
			ps.setTimestamp(2,  timeframe.getStart());
			ps.setTimestamp(3,  timeframe.getEnd());
			ps.setInt(4, timeframe.getCarId());
			ps.setInt(5,  timeframe.getUserId());
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
		String query = "UPDATE availability_timeframe SET start_=?, end_=?, car_id=?, user_id=? WHERE timeframe_id=?";
		
		
		int rowsAffected = 0;
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1,  timeframe.getStart());
			ps.setTimestamp(2,  timeframe.getEnd());
			ps.setInt(3,  timeframe.getCarId());
			ps.setInt(4,  timeframe.getUserId());
			ps.setInt(5, timeframe.getTimeframeId());
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
	
	public int getNewTimeframeId() {
		int id = 1;
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		
		String query = "SELECT timeframe_id FROM availability_timeframe";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				ids.add(resultSet.getInt(1));
			}
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot generate new timeframe id:");
			e.printStackTrace();
		}
		
		for (int i : ids) {
			if (ids.contains(id))
				id++;
			else
				return id;
		}
		return id;
	}
	
}
