package nl.hu.v1wac.uitleenapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.LendSession;
import nl.hu.v1wac.uitleenapp.model.User;

public class LendSessionPostgresDaolmpl extends PostgresBaseDao implements LendSessionDao {
	
	public List<LendSession> findByCar(int carId){
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  carId);
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			
			while (resultSet.next()) {
				if (resultSet.getString(2) == "y")
					accepted = true;
				else
					accepted = false;
				if (resultSet.getString(4) == "y")
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						resultSet.getInt(3),
						paid,
						resultSet.getTimestamp(5),
						resultSet.getTimestamp(6),
						carId,
						resultSet.getInt(7)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find lend sessions for car '" + carId + "':");
			e.printStackTrace();
		}
		
		return lendSessions;
	}

	@Override
	public List<LendSession> findLendSessionByLender(User user) {
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers, paid, start_, end_, car_id FROM lend_session WHERE user_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  user.getUserId());
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			
			while (resultSet.next()) {
				if (resultSet.getString(2) == "y")
					accepted = true;
				else
					accepted = false;
				if (resultSet.getString(4) == "y")
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						resultSet.getInt(3),
						paid,
						resultSet.getTimestamp(5),
						resultSet.getTimestamp(6),
						resultSet.getInt(7),
						user.getUserId()
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find lend sessions for user '" + user.getUserId() + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSessions;
	}

	@Override
	public List<LendSession> findLendSessionByCarAcceptPending(Car car) {
		
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id =1 and accepted = 'n' and paid = 'n'";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  car.getCarId());
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			
			while (resultSet.next()) {
				if (resultSet.getString(2) == "y")
					accepted = true;
				else
					accepted = false;
				if (resultSet.getString(4) == "y")
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						resultSet.getInt(3),
						paid,
						resultSet.getTimestamp(5),
						resultSet.getTimestamp(6),
						car.getCarId(),
						resultSet.getInt(7)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find accept pending lend sessions for car '" + car.getCarId() + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSessions;
	}

	@Override
	public List<LendSession> findLendSessionByCarPayPending(Car car) {
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id =1 and accepted = 'y' and paid = 'n'";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  car.getCarId());
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			
			while (resultSet.next()) {
				if (resultSet.getString(2) == "y")
					accepted = true;
				else
					accepted = false;
				if (resultSet.getString(4) == "y")
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						resultSet.getInt(3),
						paid,
						resultSet.getTimestamp(5),
						resultSet.getTimestamp(6),
						car.getCarId(),
						resultSet.getInt(7)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find paid pending lend sessions for car '" + car.getCarId() + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSessions;
	}

	@Override
	public boolean save(LendSession lendSession) {
		String query = "insert into lend_session (session_id, accepted, kilometers, paid, start_, end_, user_id, car_id) values(?, ?, ?, ?, ?, ?, ?, ?)";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		
		String accepted;
		String paid;
		
		if (lendSession.getAccepted())
			accepted = "y";
		else
			accepted = "n";
		
		if (lendSession.getPaid())
			paid = "y";
		else
			paid = "n";
		
		
		
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  lendSession.getSessionId());
			ps.setString(2,  accepted);
			ps.setInt(3,  lendSession.getKilometers());
			ps.setString(4,  paid);
			ps.setTimestamp(5,  lendSession.getStart());
			ps.setTimestamp(6,  lendSession.getEnd());
			ps.setInt(7,  lendSession.getUserId());
			ps.setInt(8,  lendSession.getCarId());
			
			rowsAffected = ps.executeUpdate();
			
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot save lendSession" + lendSession.getSessionId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean update(LendSession lendSession) {
		String query = "UPDATE lend_session SET accepted=?, kilometers=?, paid=?, start_=?, end_=?, user_id=?, car_id=? WHERE session_id=?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		
		String accepted;
		String paid;
		
		if (lendSession.getAccepted())
			accepted = "y";
		else
			accepted = "n";
		
		if (lendSession.getPaid())
			paid = "y";
		else
			paid = "n";
		
		
		
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,  accepted);
			ps.setInt(2,  lendSession.getKilometers());
			ps.setString(3,  paid);
			ps.setTimestamp(4,  lendSession.getStart());
			ps.setTimestamp(5,  lendSession.getEnd());
			ps.setInt(6,  lendSession.getUserId());
			ps.setInt(7,  lendSession.getCarId());
			ps.setInt(8,  lendSession.getSessionId());
			
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot update lendSession" + lendSession.getSessionId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}
	
	
	public boolean delete(int lendSessionId) {
		String query = "DELETE FROM lend_session WHERE session_id=?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  lendSessionId);
			
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot update lendSession" + lendSessionId);
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean delete(LendSession lendSession) {
		return delete(lendSession.getSessionId());
	}

}
