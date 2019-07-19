package nl.hu.v1wac.uitleenapp.persistence;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PostgresBaseDao {
	protected final Connection getConnection() {
		Connection result = null;
		
		try {
			InitialContext ic = new InitialContext();
			DataSource dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
			result = dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	protected final void closeConnection() {
		return;
	}
}