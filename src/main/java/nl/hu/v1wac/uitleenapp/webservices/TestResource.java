package nl.hu.v1wac.uitleenapp.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nl.hu.v1wac.uitleenapp.model.*;
import nl.hu.v1wac.uitleenapp.persistence.*;

@Path ("test/")
public class TestResource {
	
	private DataService dataservice = ServiceProvider.getDataService();
	
	@GET
	@Produces("application/json")
	public String GetCarsForUser() {
		StringBuilder sb = new StringBuilder();
		
		UserPostgresDaolmpl udao = new UserPostgresDaolmpl();
		CarPostgresDaolmpl cdao = new CarPostgresDaolmpl();
		AvailabilityTimeframePostgresDaolmpl atdao = new AvailabilityTimeframePostgresDaolmpl();
		LendSessionPostgresDaolmpl lsdao = new LendSessionPostgresDaolmpl();
		
		
		for (User u : udao.findAll())
			sb.append(u.toString() + "\r\n");
		
		sb.append("\r\n");
			
		
		
		
		return sb.toString();
	}
}
