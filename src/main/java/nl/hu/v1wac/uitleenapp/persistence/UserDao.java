package nl.hu.v1wac.uitleenapp.persistence;

import java.util.List;

import nl.hu.v1wac.uitleenapp.model.User;

public interface UserDao {
	public List<User> findAll();
	public User findByUsername(String username);
	public boolean save(User user);
	public boolean update(User user);
	public boolean delete(User user);
	
}
