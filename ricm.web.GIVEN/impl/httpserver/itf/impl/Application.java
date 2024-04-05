package httpserver.itf.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

import httpserver.itf.HttpRicmlet;

public class Application {
	
	protected HashMap<String, HttpRicmlet> ricmlets;
	private HashMap<String, Session> sessions;
	
	public Application() {
		ricmlets = new HashMap<>();
		sessions = new HashMap<>();
	}
	
	public HttpRicmlet getInstance(String className, String appName, ClassLoader parent) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		HttpRicmlet result = ricmlets.get(className);
		if(result == null) {
			File appJar = new File(appName+".jar");
			URL[] classpath = new URL[] {appJar.toURI().toURL()};
			URLClassLoader appCL = new URLClassLoader(classpath, parent);
			
			Class<?> appClass = appCL.loadClass(appName + "."  + className);
			result = (HttpRicmlet) appClass.newInstance();
		}
		
		return result;
		
	}
	
	public Session getInstance(String id) {
		Session result = sessions.get(id);
		if(result == null) {
			if(id == null) {
				result = new Session();
			}
			else {
				result = new Session(id);
			}
			sessions.put(result.getId(), result);
		}
		
		ArrayList<String> toRemove = new ArrayList<>();
		
		sessions.forEach((key, value) -> {
			if(key != id) {
				if(value.update()) toRemove.add(key);
			}
		});
		
		for(String s : toRemove) {
			sessions.remove(s);
		}
		
		return result;
	}

}
