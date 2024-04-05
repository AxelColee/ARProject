package httpserver.itf.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import httpserver.itf.HttpRicmlet;

public class Application {
	
	public Application() {}
	
	public HttpRicmlet getInstance(String className, String appName, ClassLoader parent) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		File appJar = new File(appName+".jar");
		URL[] classpath = new URL[] {appJar.toURI().toURL()};
		URLClassLoader appCL = new URLClassLoader(classpath, parent);
		
		Class<?> appClass = appCL.loadClass(className);
		
		return (HttpRicmlet) appClass.newInstance();
		
	}

}
