package nl.hu.v1wac.uitleenapp.webservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;
import nl.hu.v1wac.uitleenapp.model.User;

@Path ("/cars")
public class CarResource {
	
	private DataService dataservice = ServiceProvider.getDataService();
	
	@GET
	@Produces("application/json")
	public String GetCarsForUser() {
		
		if (AuthenticationResource.currentUsername == null || AuthenticationResource.currentUsername == "")
			return "{}";
		
		System.out.println();
		System.out.println(AuthenticationResource.currentUsername);
		
		User user = dataservice.getUserByUserName(AuthenticationResource.currentUsername);
		
		ArrayList<Car> cars = (ArrayList<Car>) dataservice.getCarsByUser(user);
		
		
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		job.add("userId", user.getUserId());
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Car car : cars) {
			JsonObjectBuilder carJob = Json.createObjectBuilder();
			carJob.add("carId", car.getCarId());
			carJob.add("owner", car.getOwner());
			carJob.add("model", car.getModel());
			carJob.add("pricePerKm", car.getPricePerKm());
			carJob.add("mileage", car.getMileage());
			carJob.add("reservationsPending", car.getReservationsPending());
			carJob.add("paidPending", car.getPaidPending());
			jab.add(carJob);
		}
		
		job.add("cars", jab);
		
		return job.build().toString();
	}
	
	
	@DELETE
	@Path ("/{id}")
	@Produces("application/json")
	public Response deleteCar(@PathParam("id") String id) {
		
		int carId = Integer.parseInt(id);
		
		boolean success = dataservice.removeCar(carId);
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Car cannot be deleted because it does not exist");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok().build();
	}
	
	@RolesAllowed({"admin", "user"})
	@POST
	@Produces("application/json")
	public Response addCar(
			@FormParam("model") String model,
			@FormParam("mileage") int mileage,
			@FormParam("price") double price
			) {
		
		int id = dataservice.getNewCarId();
		
		User user = dataservice.getUserByUserName(AuthenticationResource.currentUsername);
		
		Car newCar = new Car(id, user.getUserId(),  model, price, mileage);		
		boolean success = dataservice.addCar(newCar);
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot add car");
			
			return Response.status(409).entity(messages).build();
		}
		
		
	    return Response.ok(newCar).build();
	}
}
