package nl.hu.v1wac.uitleenapp.webservices;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1wac.uitleenapp.model.AvailabilityTimeframe;
import nl.hu.v1wac.uitleenapp.model.DataService;
import nl.hu.v1wac.uitleenapp.model.ServiceProvider;
import nl.hu.v1wac.uitleenapp.model.User;


@Path ("/availability")
public class AvailabilityResource {
	
	private DataService dataservice = ServiceProvider.getDataService();

	@RolesAllowed({"admin", "user"})
	@POST
	@Produces("application/json")
	public Response addTimeframe(
			@FormParam("startDate") String startDateString,
			@FormParam("startTime") String startTimeString,
			@FormParam("endDate") String endDateString,
			@FormParam("endTime") String endTimeString
			) {
		
		int carId = 1;
		int id = dataservice.getNewTimeframeId();
		
		User user = dataservice.getUserByUserName(AuthenticationResource.currentUsername);
		
		Timestamp start = null;
		Timestamp end = null;
		
		String seperator = ", ";
		
		SimpleDateFormat sdf = new SimpleDateFormat	("yyyy-dd-MM" + seperator + "HH:mm");

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
	
}
