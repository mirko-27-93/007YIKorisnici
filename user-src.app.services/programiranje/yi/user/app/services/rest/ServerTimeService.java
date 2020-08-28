package programiranje.yi.user.app.services.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;

/**
 * Пропагација времена и датума до прецизности секунде, са сервера. 
 * @author mirko
 * @version 1.0
 */

@Path("/timestamp")
public class ServerTimeService{
	
	@POST
	@Path("/standard")
	@Produces(MediaType.APPLICATION_JSON)
	public String getStandardTimeStamp() {
		JsonObject object = new JsonObject();
		object.addProperty("timestamp", getTimeStamp());
		return object.toString();
	}
	
	@POST
	@Path("/yi")
	@Produces(MediaType.APPLICATION_JSON)
	public String getYITimeStamp() {
		JsonObject object = new JsonObject();
		object.addProperty("timestamp", getTimeStamp());
		return object.toString();
	}
	
	public String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
		return sdf.format(new Date());
	}
}
