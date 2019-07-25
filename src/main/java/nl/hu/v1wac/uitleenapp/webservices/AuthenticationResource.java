package nl.hu.v1wac.uitleenapp.webservices;

import java.security.Key;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.v1wac.uitleenapp.model.User;
import nl.hu.v1wac.uitleenapp.persistence.UserPostgresDaolmpl;

@Path("/authentication")
public class AuthenticationResource {
  final static public Key key = MacProvider.generateKey();

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response authenticateUser(@FormParam("username") String username, 
                                   @FormParam("password") String password) {
    try {
      // Authenticate the user against the database
      UserPostgresDaolmpl dao = new UserPostgresDaolmpl();
      
      User user = dao.findByCredentials(username, password);
      
      
      if (user == null)
    	  throw new IllegalArgumentException("Invalid credentials!");
      
      
      String role = user.getRole();
      
      if (role == null || role == "") { throw new IllegalArgumentException("No user found!");  } 
      
      String token = createToken(username, role);

      SimpleEntry<String, String> JWT = new SimpleEntry<String, String>("JWT", token);
      return Response.ok(JWT).build();


    } catch (JwtException | IllegalArgumentException e) 
        { return Response.status(Response.Status.UNAUTHORIZED).build(); }
  }
  
  
  @GET
  @RolesAllowed({"admin", "user"})
  @Produces("application/json")
  public Response isAuthenticated(@Context SecurityContext sc) {
	  boolean authenticated = false;
	  
	  String userName = sc.getUserPrincipal().getName();
	  
	  if (userName != null && userName != "Unknown")
		  authenticated = true;
	  
	  
	  SimpleEntry<String, Boolean> auth = new SimpleEntry<String, Boolean>("authenticated", authenticated);
      return Response.ok(auth).build();
  }
  
  private String createToken(String username, String role) {
	  
	  Calendar expiration = Calendar.getInstance();
	  expiration.add(Calendar.MINUTE, 30);
	  
	  return Jwts.builder()
				.setIssuer("www.myapp.com")
				.setSubject(username)
				.setExpiration(expiration.getTime())
				.claim("role", role)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
  }
}
