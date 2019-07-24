package nl.hu.v1wac.uitleenapp.persistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.LendSession;
import nl.hu.v1wac.uitleenapp.model.User;
import nl.hu.v1wac.uitleenapp.webservices.AuthenticationResource;

public class UserPostgresDaolmpl extends PostgresBaseDao implements UserDao{
	
	@Override
	public List<User> findAll() {
		ArrayList<User> users = null;
		
		String query = "SELECT user_id, email, name_, passwordhash, role_ FROM user_";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet resultSet = ps.executeQuery();
			
			users = new ArrayList<User>();
			
			while (resultSet.next()) {
				User newUser = new User(
						resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5)
						);
				
				
				
				for (LendSession session : new  LendSessionPostgresDaolmpl().findLendSessionByLender(newUser))
					newUser.addLendSession(session);
				
				for (Car car : new CarPostgresDaolmpl().findCarsByOwner(newUser.getUserId()))
					newUser.addCar(car);
				
				users.add(newUser);
			}
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find users");
			e.printStackTrace();
		}
		
		return users;
	}

	@Override
	public User findByUsername(String username) {
		User user = null;
		
		String query = "SELECT user_id, name_, passwordhash, role_ FROM user_ where email=?";
		
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,  username);
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				user = new User(
						resultSet.getInt(1),
						username,
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4)
						);
				
				for (LendSession session : new  LendSessionPostgresDaolmpl().findLendSessionByLender(user))
					user.addLendSession(session);
				
				for (Car car : new CarPostgresDaolmpl().findCarsByOwner(user.getUserId()))
					user.addCar(car);
			}
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find user: " + username);
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User findByCredentials(String username, String password) {
		User user = null;
		
		
		String passwordHash = getMD5Hash(password); 
		
		String query = "SELECT user_id, name_, role_ FROM user_ where email=? AND passwordhash=?";
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,  username);
			ps.setString(2,  passwordHash);
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				user = new User(
						resultSet.getInt(1),
						username,
						resultSet.getString(2),
						passwordHash,
						resultSet.getString(3)
						);
				
				for (LendSession session : new  LendSessionPostgresDaolmpl().findLendSessionByLender(user))
					user.addLendSession(session);
				
				for (Car car : new CarPostgresDaolmpl().findCarsByOwner(user.getUserId()))
					user.addCar(car);
			}
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot find user: " + username);
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public boolean save(User user) {
		String query = "INSERT INTO user_ (user_id, email, name_, passwordhash, role_) VALUES (?, ?, ?, ?, ?)";
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			rowsAffected = ps.executeUpdate();
			
			
			for (LendSession session : user.getLendSessions())
				if (new LendSessionPostgresDaolmpl().save(session))
					rowsAffected++;
			
			for (Car car : user.getCars())
				if (new CarPostgresDaolmpl().save(car))
					rowsAffected++;
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot save user " + user.getUserId());
			e.printStackTrace();
		}
		
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean update(User user) {
		String query = "UPDATE user SET email=?, name_=?, passwordhash=?, role=? WHERE user_id=?";
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1,  user.getEmail());
			ps.setString(2,  user.getName());
			ps.setString(3,  user.getPasswordHash());
			ps.setString(4,  user.getRole());
			ps.setInt(5,  user.getUserId());
			
			rowsAffected = ps.executeUpdate();
			
			ArrayList<Car> oldCars = (ArrayList<Car>) new CarPostgresDaolmpl().findCarsByOwner(user.getUserId());
			ArrayList<Integer> oldCarIds = new ArrayList<Integer>();
			ArrayList<Integer> newCarIds = new ArrayList<Integer>();
			
			
			
			for (Car car : oldCars) {
				oldCarIds.add(car.getCarId());
			}
			
			for(Car car : user.getCars())
				newCarIds.add(car.getCarId());
			
			
 
			for (int id : oldCarIds) {
				if (!newCarIds.contains(id))
					new CarPostgresDaolmpl().delete(id);	//delete cars from database that are not in the car object
			}
			
			for (Car car : user.getCars()) {
				if (!oldCarIds.contains(car.getCarId()))
					new CarPostgresDaolmpl().save(car); // save new cars to database
				else
					new CarPostgresDaolmpl().update(car); // update existing cars in database
			}
			
			
			ArrayList<LendSession> oldSessions = (ArrayList<LendSession>) new LendSessionPostgresDaolmpl().findLendSessionByLender(user);
			ArrayList<Integer> oldSessionIds = new ArrayList<Integer>();
			ArrayList<Integer> newSessionIds = new ArrayList<Integer>();
			
			for (LendSession session : oldSessions)
				oldSessionIds.add(session.getSessionId());
			
			for (LendSession session : user.getLendSessions())
				newSessionIds.add(session.getSessionId());
			
			for (int id : oldSessionIds)
				if (!newSessionIds.contains(id))
					new LendSessionPostgresDaolmpl().delete(id); //delete lendsessions from database that are not in the car object
			
			for (LendSession session : user.getLendSessions()) {
				if (!oldSessionIds.contains(session.getSessionId()))
					new LendSessionPostgresDaolmpl().save(session); // save new lendsessions to database
				else
					new LendSessionPostgresDaolmpl().update(session); // update existing lendsessions in database
			}
			
			connection.close();
		}
		catch (Exception e) {
			System.out.println("Error cannot update user" + user.getUserId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		return false;
	}

	@Override
	public boolean delete(User user) {
		String query = "DELETE FROM user_ WHERE user_id = ?";
		
		
		int rowsAffected = 0;
		
		Connection connection = getConnection();
		
		try {
			
			for (Car car : user.getCars())
				new CarPostgresDaolmpl().delete(car);
			
			for (LendSession session : user.getLendSessions())
				new LendSessionPostgresDaolmpl().delete(session);
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, user.getUserId());
			
			
			rowsAffected = ps.executeUpdate();
			
			connection.close();
		}
		catch (Exception e){
			System.out.println("Error cannot delete user" + user.getUserId());
			e.printStackTrace();
		}
		
		if (rowsAffected > 0)
			return true;
		
		return false;
	}
	
	public int getNewUserId() {
		int id = 1;
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		
		String query = "SELECT user_id FROM user_";
		
		
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
			System.out.println("Error cannot generate new user id:");
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

	
	public static String getMD5Hash(String password){
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	    byte[] passBytes = password.getBytes();
	    byte[] passHash = md.digest(passBytes);
	    
	    String md5String = DatatypeConverter.printHexBinary(passHash).toLowerCase();
	    
	    return md5String;
	}
}
