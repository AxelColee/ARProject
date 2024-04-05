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
	
	public static Session getInstance(String id) {
		if(id == null) {
			Session s = new Session();
			sessions.put(s.getId(), s);
			return s;
		}
		return sessions.get(id);
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
