package httpserver.itf.impl;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import httpserver.itf.HttpSession;

public class Session implements HttpSession{
	
	public static final int TIME_START = 1000;
	
	private String id;
	private HashMap<String, Object> content;
	private int timeLeft;
	
	public Session() {
		id = Long.toString(Clock.systemDefaultZone().millis());
		content = new HashMap<>();
		timeLeft = TIME_START;
	}
	
	public Session(String id) {
		this.id = id;
		content = new HashMap<>();
		timeLeft = TIME_START;
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
	
	public boolean update() {
		this.timeLeft -= 1;
		return timeLeft == 0;
	}

}
