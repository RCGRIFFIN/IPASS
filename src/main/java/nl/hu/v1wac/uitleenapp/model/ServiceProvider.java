package nl.hu.v1wac.uitleenapp.model;

public class ServiceProvider {
	
	private static DataService dataservice = new DataService();
	
	public static DataService getDataService() {
		return dataservice;
	}
}
