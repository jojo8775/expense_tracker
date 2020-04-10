package expenseapp.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("temp")
public class Temp {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String tempGet() {
		return "this is my first rest page after a long time";
	}
}
