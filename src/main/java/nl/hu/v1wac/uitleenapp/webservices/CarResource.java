package nl.hu.v1wac.uitleenapp.webservices;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;

@Path ("car/{id}")
public class CarResource {
	
	private DataService dataservice = ServiceProvider.getDataService();
	
	@GET
	@Produces("application/json")
	public String GetCarsForUser(@PathParam("id") int id) {
		
		
		ArrayList<Car> cars = (ArrayList<Car>) dataservice.getCarsByUserId(id);
		
		
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		job.add("userId", id);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Car car : cars) {
			JsonObjectBuilder carJob = Json.createObjectBuilder();
			carJob.add("carId", car.getCarId());
			carJob.add("owner", car.getOwner());
			carJob.add("model", car.getModel());
			carJob.add("pricePerKm", car.getPricePerKm());
			carJob.add("mileage", car.getMileage());
			carJob.add("reservationsPending", 0);
			carJob.add("paidPending", 0);
			jab.add(carJob);
		}
		
		job.add("cars", jab);
		
		return job.build().toString();
	}
}
