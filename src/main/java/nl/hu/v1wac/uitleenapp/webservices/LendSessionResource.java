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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import nl.hu.v1wac.uitleenapp.model.Car;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.LendSession;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;
import nl.hu.v1wac.uitleenapp.model.User;

@Path("/lendSession")
public class LendSessionResource {
	
	private DataService dataservice = ServiceProvider.getDataService();
	
	@GET
	@RolesAllowed({"admin", "user"})
	@Path("{carId}")
	@Produces("application/json")
	public Response GetLendSessions(@Context SecurityContext sc, @PathParam("carId") String carIdString) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Paid pending cannot be loaded because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		int carId = Integer.parseInt(carIdString);
		
		User user = dataservice.getUserByUserName(userName);
		
		ArrayList <LendSession> sessions = (ArrayList<LendSession>) dataservice.getLendSessionByUserKilometersPending(user);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		
		for (LendSession ls : sessions) {
			Car car = dataservice.getCarById(ls.getCarId());
			
			JsonObjectBuilder sessionJob = Json.createObjectBuilder();
			sessionJob.add("sessionId", ls.getSessionId());
			sessionJob.add("model", car.getModel());
			sessionJob.add("start", ls.getStart().toLocaleString());
			sessionJob.add("end", ls.getEnd().toLocaleString());
			sessionJob.add("mileage", car.getMileage());
			jab.add(sessionJob);
		}
		
		job.add("sessions", jab);
		
		return Response.ok(job.build().toString()).build();
	}
	
	@POST
	@RolesAllowed({"admin", "user"})
	@Path ("/kilometers/{sessionId}")
	@Produces("application/json")
	public Response registerKilometers(@Context SecurityContext sc,
			@FormParam("mileage") String mileageString,
			@PathParam("sessionId") String sessionIdString){
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Accept pending cannot be loaded because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		int newMileage = Integer.parseInt(mileageString);
		
		int kilometers = 0;
		
		int sessionId = Integer.parseInt(sessionIdString);
		
		LendSession session = dataservice.getLendSessionById(sessionId);
		
		Car car = dataservice.getCarById(session.getCarId());
		
		if (car.getMileage() < newMileage) {
			car.setMileage(newMileage);
		}
			
		
		kilometers = newMileage - car.getMileage();
		if (kilometers < 0)
			kilometers = 0;
		
		session.setKilometers(kilometers);
		
		session.setKilometersSubmitted(true);
		
			
		boolean success = dataservice.updateLendSession(session);	
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot save session");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok(session).build();
	}
	
	@POST
	@RolesAllowed({"admin", "user"})
	@Path ("/{carId}")
	@Produces("application/json")
	public Response addNewSession(@Context SecurityContext sc,
			@FormParam("startDate") String startDateString,
			@FormParam("startTime") String startTimeString,
			@FormParam("endDate") String endDateString,
			@FormParam("endTime") String endTimeString,
			@PathParam("carId") String carIdString){
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "session cannot be saved because the user is unknown");
			
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
		
		int carId = Integer.parseInt(carIdString);
		
		int lendSessionId = dataservice.getNewLendSessionId();
		
		LendSession newSession = new LendSession(
				lendSessionId,
				false,
				false,
				0,
				false,
				start,
				end,
				carId,
				user.getUserId()
				);
		
			
		boolean success = dataservice.addLendSession(newSession);	
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot save session");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok(newSession).build();
	}
	
	@PUT
	@RolesAllowed({"admin", "user"})
	@Path ("accept/{sessionId}")
	@Produces("application/json")
	public Response acceptLendSession(@Context SecurityContext sc, @PathParam("sessionId") String sessionIdString){
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Accept pending cannot be loaded because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		User user = dataservice.getUserByUserName(userName);
		
		int sessionId = Integer.parseInt(sessionIdString);
		
		LendSession lendSession = dataservice.getLendSessionById(sessionId);
		
		lendSession.setAccepted(true);
		
		boolean success = dataservice.updateLendSession(lendSession);	
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot update session");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok(lendSession).build();
	}
	
	@PUT
	@RolesAllowed({"admin", "user"})
	@Path ("confirmPaid/{sessionId}")
	@Produces("application/json")
	public Response confirmPaidLendSession(@Context SecurityContext sc, @PathParam("sessionId") String sessionIdString){
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Accept pending cannot be loaded because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		User user = dataservice.getUserByUserName(userName);
		
		int sessionId = Integer.parseInt(sessionIdString);
		
		LendSession lendSession = dataservice.getLendSessionById(sessionId);
		
		lendSession.setPaid(true);
		
		boolean success = dataservice.updateLendSession(lendSession);	
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot update session");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok(lendSession).build();
	}
	
	@GET
	@RolesAllowed({"admin", "user"})
	@Path("/acceptPending/{carId}")
	@Produces("application/json")
	public Response GetAcceptPending(@Context SecurityContext sc, @PathParam("carId") String carIdString) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Paid pending cannot be loaded because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		int carId = Integer.parseInt(carIdString);
		
		ArrayList <LendSession> sessions = (ArrayList<LendSession>) dataservice.getLendSessionAcceptPending(carId);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (LendSession ls : sessions) {
			JsonObjectBuilder sessionJob = Json.createObjectBuilder();
			sessionJob.add("sessionId", ls.getSessionId());
			sessionJob.add("lender", dataservice.getUserById(ls.getUserId()).getName());
			sessionJob.add("start", ls.getStart().toLocaleString());
			sessionJob.add("end", ls.getEnd().toLocaleString());
			jab.add(sessionJob);
		}
		
		job.add("sessions", jab);
		
		return Response.ok(job.build().toString()).build();
	}
	
	@GET
	@RolesAllowed({"admin", "user"})
	@Path("/paidPending/{carId}")
	@Produces("application/json")
	public Response GetPaidPending(@Context SecurityContext sc, @PathParam("carId") String carIdString) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cars cannot be load because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		int carId = Integer.parseInt(carIdString);
		
		ArrayList <LendSession> sessions = (ArrayList<LendSession>)dataservice.getLendSessionPaidPending(carId);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (LendSession ls : sessions) {
			
			double price = dataservice.getLendSessionPrice(ls);
			
			String priceString = Double.toString(price);
			
			
			
			if (price == 0)
				priceString = "0.00";
			else if (!priceString.contains("."))
				priceString += ",00";
			else if (priceString.charAt(priceString.length()-2) == '.') {
				priceString.replace('.',  ',');
				priceString += "0";
			}
			JsonObjectBuilder sessionJob = Json.createObjectBuilder();
			sessionJob.add("sessionId", ls.getSessionId());
			sessionJob.add("lender", dataservice.getUserById(ls.getUserId()).getName());
			sessionJob.add("start", ls.getStart().toLocaleString());
			sessionJob.add("end", ls.getEnd().toLocaleString());
			sessionJob.add("kilometers",  ls.getKilometers());
			sessionJob.add("price", priceString);
			jab.add(sessionJob);
		}
		
		job.add("sessions", jab);
		
		return Response.ok(job.build().toString()).build();
	}
	
}
