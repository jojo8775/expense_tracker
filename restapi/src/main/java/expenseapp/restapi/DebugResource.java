package expenseapp.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import expensetracker.webapp.Program;

@Path("/debug")
public class DebugResource {
	
	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response trigger() {
		var result = new Program().execute();
		return Response.ok(new Gson().toJson(result)).build();
	}
}
