package nl.hu.v1wac.uitleenapp.webservices;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;
import nl.hu.v1wac.uitleenapp.model.User;

@Path ("/cars")
public class CarsResource {
	
	private DataService dataservice = ServiceProvider.getDataService();
	

	@POST
	@RolesAllowed({"admin", "user"})
	@Path("/availableCars")
	@Produces("application/json")
	public Response GetCarsForTimeframe(@Context SecurityContext sc,
			@FormParam("startDate") String startDateString,
			@FormParam("startTime") String startTimeString,
			@FormParam("endDate") String endDateString,
			@FormParam("endTime") String endTimeString) {
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cars cannot be load because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		User user = dataservice.getUserByUserName(userName);
		
		Timestamp start = null;
		Timestamp end = null;
		
		String seperator = ", ";
		
		SimpleDateFormat sdf = new SimpleDateFormat	("yyyy-MM-dd" + seperator + "HH:mm");

		String startString = startDateString + seperator + startTimeString;
		String endString = endDateString + seperator + endTimeString;
		
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = sdf.parse(startString);
			endDate = sdf.parse(endString);
			
			start = new Timestamp(startDate.getTime());
			end = new Timestamp(endDate.getTime());
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<Car> cars = (ArrayList<Car>) dataservice.getCarsByTimeframe(start, end);
		
		
		
		
		job.add("userId", user.getUserId());
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		
		
		
		for (Car car : cars) {
			String pricePerKm = Double.toString(car.getPricePerKm());
			
			
			
			if (car.getPricePerKm() == 0)
				pricePerKm = "0.00";
			else if (!pricePerKm.contains("."))
				pricePerKm += ",00";
			else if (pricePerKm.charAt(pricePerKm.length()-2) == '.') {
				pricePerKm.replace('.',  ',');
				pricePerKm += "0";
			}
				
			
			
			JsonObjectBuilder carJob = Json.createObjectBuilder();
			carJob.add("carId", car.getCarId());
			carJob.add("owner", car.getOwner());
			carJob.add("model", car.getModel());
			carJob.add("pricePerKm", "€ " + pricePerKm);
			carJob.add("mileage", car.getMileage());
			jab.add(carJob);
		}
		
		job.add("cars", jab);
		
		return Response.ok(job.build().toString()).build();
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

	@GET
	@RolesAllowed({"admin", "user"})
	@Produces("application/json")
	public Response GetCarsForUser(@Context SecurityContext sc) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cars cannot be load because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		User user = dataservice.getUserByUserName(userName);
		
		ArrayList<Car> cars = (ArrayList<Car>) dataservice.getCarsByUser(user);
		
		
		
		
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
		
		return Response.ok(job.build().toString()).build();
	}
	
	
	
	
	@RolesAllowed({"admin", "user"})
	@POST
	@Produces("application/json")
	public Response addCar(
			@Context SecurityContext sc,
			@FormParam("model") String model,
			@FormParam("mileage") int mileage,
			@FormParam("price") double price
			) {
		
		int id = dataservice.getNewCarId();
		
		String userName = sc.getUserPrincipal().getName();
		
		User user = dataservice.getUserByUserName(userName);
		
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
