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
		
		String query = "SELECT session_id, accepted, kilometers_submitted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  carId);
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			boolean kilometersSubmitted;
			
			
			
			while (resultSet.next()) {
				if (resultSet.getInt(2) == 1)
					accepted = true;
				else
					accepted = false;
				if (resultSet.getInt(3) == 1)
					kilometersSubmitted = true;
				else
					kilometersSubmitted = false;
				if (resultSet.getInt(5) == 1)
					paid = true;
				else
					paid = false;
				
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						kilometersSubmitted,
						resultSet.getInt(4),
						paid,
						resultSet.getTimestamp(6),
						resultSet.getTimestamp(7),
						carId,
						resultSet.getInt(8)
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
	
	public LendSession findById(int sessionId) {
		String query = "SELECT accepted, kilometers_submitted, kilometers, paid, start_, end_, car_id, user_id FROM lend_session WHERE session_id = ?";
		LendSession lendSession = null;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  sessionId);
			ResultSet resultSet = ps.executeQuery();
			
			boolean accepted;
			boolean paid;
			boolean kilometersSubmitted;
			
			
			
			while (resultSet.next()) {
				if (resultSet.getInt(1) == 1)
					accepted = true;
				else
					accepted = false;
				if (resultSet.getInt(2) == 1)
					kilometersSubmitted = true;
				else
					kilometersSubmitted = false;
				if (resultSet.getInt(4) == 1)
					paid = true;
				else
					paid = false;
				
				
				lendSession = new LendSession(
						sessionId,
						accepted,
						kilometersSubmitted,
						resultSet.getInt(3),
						paid,
						resultSet.getTimestamp(5),
						resultSet.getTimestamp(6),
						resultSet.getInt(7),
						resultSet.getInt(8)
						);
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find lend sessions for user '" + sessionId + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSession;
	}

	@Override
	public List<LendSession> findLendSessionByLender(User user) {
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers_submitted,  kilometers, paid, start_, end_, car_id FROM lend_session WHERE user_id = ?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  user.getUserId());
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			boolean kilometersSubmitted;
			
			while (resultSet.next()) {
				if (resultSet.getInt(2) == 1)
					accepted = true;
				else
					accepted = false;
				
				if (resultSet.getInt(3) == 1)
					kilometersSubmitted = true;
				else
					kilometersSubmitted = false;
				if (resultSet.getInt(5) == 1)
					paid = true;
				else
					paid = false;
				
				
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						kilometersSubmitted,
						resultSet.getInt(4),
						paid,
						resultSet.getTimestamp(6),
						resultSet.getTimestamp(7),
						resultSet.getInt(8),
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
	public List<LendSession> findLendSessionByCarAcceptPending(int carId) {
		
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers_submitted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id =? and accepted = 0 and kilometers_submitted = 0 and paid = 0";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  carId);
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean paid;
			boolean kilometersSubmitted;
			while (resultSet.next()) {
				
				if (resultSet.getInt(2) == 1)
					accepted = true;
				else
					accepted = false;
				
				if (resultSet.getInt(3) == 1)
					kilometersSubmitted = true;
				else
					kilometersSubmitted = false;
				
				if (resultSet.getInt(5) == 1)
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						kilometersSubmitted,
						resultSet.getInt(4),
						paid,
						resultSet.getTimestamp(6),
						resultSet.getTimestamp(7),
						carId,
						resultSet.getInt(8)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find accept pending lend sessions for car '" + carId + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSessions;
	}
	
	@Override
	public List<LendSession> findLendSessionByLenderKilometersPending(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LendSession> findLendSessionByCarPaidPending(int carId) {
		ArrayList<LendSession> lendSessions = null;
		
		String query = "SELECT session_id, accepted, kilometers_submitted, kilometers, paid, start_, end_, user_id FROM lend_session WHERE car_id =? and accepted = 1 and kilometers_submitted = 1 and paid = 0";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  carId);
			ResultSet resultSet = ps.executeQuery();
			
			
			
			lendSessions = new ArrayList<LendSession>();
			
			boolean accepted;
			boolean kilometersSubmitted;
			boolean paid;
			
			while (resultSet.next()) {
				
				if (resultSet.getInt(2) == 1)
					accepted = true;
				else
					accepted = false;
				if (resultSet.getInt(3) == 1)
					kilometersSubmitted = true;
				else
					kilometersSubmitted = false;
				if (resultSet.getInt(5) == 1)
					paid = true;
				else
					paid = false;
				lendSessions.add(new LendSession(
						resultSet.getInt(1),
						accepted,
						kilometersSubmitted,
						resultSet.getInt(4),
						paid,
						resultSet.getTimestamp(6),
						resultSet.getTimestamp(7),
						carId,
						resultSet.getInt(8)
						));
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find paid pending lend sessions for car '" + carId + "':");
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return lendSessions;
	}

	@Override
	public boolean save(LendSession lendSession) {
		String query = "insert into lend_session (session_id, accepted, kilometers_submitted, kilometers, paid, start_, end_, user_id, car_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		
		int accepted = 0;
		int kilometersSubmitted = 0;
		int paid = 0;
		
		if (lendSession.getAccepted())
			accepted = 1;
		
		if (lendSession.getKilometersSubmitted())
			kilometersSubmitted = 1;
		
		if (lendSession.getPaid())
			paid = 1;
		
		
		
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  lendSession.getSessionId());
			ps.setInt(2,  accepted);
			ps.setInt(3,  kilometersSubmitted);
			ps.setInt(4,  lendSession.getKilometers());
			ps.setInt(5,  paid);
			ps.setTimestamp(6,  lendSession.getStart());
			ps.setTimestamp(7,  lendSession.getEnd());
			ps.setInt(8,  lendSession.getUserId());
			ps.setInt(9,  lendSession.getCarId());
			
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
		String query = "UPDATE lend_session SET accepted=?, kilometers_submitted=?, kilometers=?, paid=?, start_=?, end_=?, user_id=?, car_id=? WHERE session_id=?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		
		int accepted = 0;
		int kilometersSubmitted = 0;
		int paid = 0;
		
		if (lendSession.getAccepted())
			accepted = 1;
		
		if (lendSession.getKilometersSubmitted())
			kilometersSubmitted = 1;
		
		if (lendSession.getPaid())
			paid = 1;
		
		
		
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1,  accepted);
			ps.setInt(2,  kilometersSubmitted);
			ps.setInt(3,  lendSession.getKilometers());
			ps.setInt(4,  paid);
			ps.setTimestamp(5,  lendSession.getStart());
			ps.setTimestamp(6,  lendSession.getEnd());
			ps.setInt(7,  lendSession.getUserId());
			ps.setInt(8,  lendSession.getCarId());
			ps.setInt(9,  lendSession.getSessionId());
			
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

	public int getNewSessionId() {
		int id = 1;
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		
		String query = "SELECT session_id FROM lend_session";
		
		
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
			System.out.println("Error cannot generate new session id:");
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
	
	public double getLendSesionPrice(LendSession lendSession) {
		double pricePerKm = new CarPostgresDaolmpl().findById(lendSession.getCarId()).getPricePerKm();
		return lendSession.getKilometers() * pricePerKm;
	}
}
