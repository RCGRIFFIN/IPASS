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

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;
import nl.hu.v1wac.uitleenapp.model.User;


@Path ("availability")
public class AvailabilityResource {
	
	private DataService dataservice = ServiceProvider.getDataService();

	
	
	
	@GET
	@RolesAllowed({"admin", "user"})
	@Path("/{id}")
	@Produces("application/json")
	public Response GetAvailableForCar(@Context SecurityContext sc, @PathParam("id") String carIdString) {
		
		String userName = sc.getUserPrincipal().getName();
		
		if (userName == null || userName == "Unknown") {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cars cannot be load because the user is unknown");
			
			return Response.status(409).entity(messages).build();
		}
		
		int carId = Integer.parseInt(carIdString);
		
		
		User user = dataservice.getUserByUserName(userName);
		
		ArrayList<AvailabilityTimeframe> timeframes = (ArrayList<AvailabilityTimeframe>) dataservice.getTimeframesByCar(carId);
		
		
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		job.add("timeframes", user.getUserId());
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (AvailabilityTimeframe timeframe : timeframes) {
			JsonObjectBuilder timeframeJob = Json.createObjectBuilder();
			timeframeJob.add("timeframeId", timeframe.getTimeframeId());
			timeframeJob.add("start", timeframe.getStart().toLocalDateTime().toString());
			timeframeJob.add("end", timeframe.getEnd().toLocalDateTime().toString());
			jab.add(timeframeJob);
		}
		
		job.add("timeframes", jab);
		
		return Response.ok(job.build().toString()).build();
	}
	
	@RolesAllowed({"admin", "user"})
	@POST
	@Path("/{id}")
	@Produces("application/json")
	public Response addTimeframe(
			@Context SecurityContext sc,
			@FormParam("startDate") String startDateString,
			@FormParam("startTime") String startTimeString,
			@FormParam("endDate") String endDateString,
			@FormParam("endTime") String endTimeString,
			@PathParam("id") String carIdString) {
		
		int carId = Integer.parseInt(carIdString);
		int id = dataservice.getNewTimeframeId();
		
		String userName = sc.getUserPrincipal().getName();
		
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
		
		AvailabilityTimeframe newTimeframe = new AvailabilityTimeframe(id, start, end, carId, user.getUserId());
		
		
		boolean success = dataservice.addTimeframe(newTimeframe);
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Cannot add car");
			
			return Response.status(409).entity(messages).build();
		}
		
		
	    return Response.ok(newTimeframe).build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces("application/json")
	public Response deleteCar(@PathParam("id") String id) {
		
		int timeframeId = Integer.parseInt(id);
		
		boolean success = dataservice.removeTimeframe(timeframeId);
		
		
		if (!success) {
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "Timeframe cannot be deleted because it does not exist");
			
			return Response.status(409).entity(messages).build();
		}
		
		return Response.ok().build();
	}
}
