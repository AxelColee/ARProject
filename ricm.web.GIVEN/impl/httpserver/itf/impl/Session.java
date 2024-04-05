package httpserver.itf.impl;

import java.time.Clock;
import java.util.Calendar;
import java.util.HashMap;

import httpserver.itf.HttpSession;

public class Session implements HttpSession{
	
	private static HashMap<String, Session> sessions = new HashMap<>();
	
	private String id;
	private HashMap<String, Object> content;
	
	private Session() {
		id = Long.toString(Clock.systemDefaultZone().millis());
		content = new HashMap<>();
	}
	
	private Session(String id) {
		this.id = id;
		content = new HashMap<>();
	}
	
	public static Session getInstance(String id) {
		Session result = sessions.get(id);
		if(id == null || result == null) {
			 result = new Session(id);
			sessions.put(result.getId(), result);
		}
		return result;
	}
	
	

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return content.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		// TODO Auto-generated method stub
		content.put(key, value);
	}

}
