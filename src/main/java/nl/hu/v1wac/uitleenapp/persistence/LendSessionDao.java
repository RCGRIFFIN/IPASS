package nl.hu.v1wac.uitleenapp.persistence;

import java.util.List;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.LendSession;
import nl.hu.v1wac.uitleenapp.model.User;

public interface LendSessionDao {
	public List<LendSession> findLendSessionByLender(User user);
	public List<LendSession> findLendSessionByCarAcceptPending(int carId);
	public List<LendSession> findLendSessionByLenderKilometersPending(User user);
	public List<LendSession> findLendSessionByCarPaidPending(int carId);
	public boolean save(LendSession lendSession);
	public boolean update(LendSession lendSession);
	public boolean delete(LendSession lendSession);
}
