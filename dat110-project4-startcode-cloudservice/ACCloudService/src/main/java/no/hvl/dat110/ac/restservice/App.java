package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describied in the project description
		
		post("/accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			AccessMessage accessmessage = gson.fromJson(req.body(), AccessMessage.class);
			int id = accesslog.add(accessmessage.getMessage());
			AccessEntry accessentry = new AccessEntry(id, accessmessage.getMessage());
			return gson.toJson(accessentry, AccessEntry.class);
		});
		
		get("/accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			return accesslog.toJson();
		});
		
		get("/accessdevice/log:id", (req, res) -> {
			Gson gson = new Gson();
			Integer id = Integer.parseInt(req.params(":id"));
			AccessEntry accessentry = accesslog.get(id);
			return gson.toJson(accessentry, AccessEntry.class);
		});
		
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			accesscode = gson.fromJson(req.body(), AccessCode.class);
			return gson.toJson(accesscode, AccessCode.class);
		});
		
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode, AccessCode.class);
		});
		
		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			String emptylog = accesslog.toJson();
			res.body(emptylog);
			return emptylog;
		});
		
    }
}
