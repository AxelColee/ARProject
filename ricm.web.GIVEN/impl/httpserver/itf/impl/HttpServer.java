package httpserver.itf.impl;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;


/**
 * Basic HTTP Server Implementation 
 * 
 * Only manages static requests
 * The url for a static ressource is of the form: "http//host:port/<path>/<ressource name>"
 * For example, try accessing the following urls from your brower:
 *    http://localhost:<port>/
 *    http://localhost:<port>/voile.jpg
 *    ...
 */
public class HttpServer {

	private int m_port;
	private File m_folder;  // default folder for accessing static resources (files)
	private ServerSocket m_ssoc;
	protected HashMap<String, String> cookies;

	public static HashMap<String, Application> applications = new HashMap<>();

	protected HttpServer(int port, String folderName) {
		m_port = port;
		if (!folderName.endsWith(File.separator)) 
			folderName = folderName + File.separator;
		m_folder = new File(folderName);
		try {
			m_ssoc=new ServerSocket(m_port);
			System.out.println("HttpServer started on port " + m_port);
		} catch (IOException e) {
			System.out.println("HttpServer Exception:" + e );
			System.exit(1);
		}
		
		cookies = new HashMap<>();
	}
	
	public File getFolder() {
		return m_folder;
	}
	
	public Application getApplication(String name) {
		Application app = applications.get(name);
		if(app == null) {
			app = new Application();
			applications.put(name, app);
		}
		return app;
	}
	
	

	public HttpRicmlet getInstance(String clsname)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    		
    		String split[] = clsname.split("\\.");
    		String appName = split[1];
    		Application app = this.getApplication(appName);

    		clsname = split[2];
    		return app.getInstance(clsname, appName, ClassLoader.getSystemClassLoader());    		
	}
	
	public void getCookiesFromHeader(BufferedReader br) {
		try {
			String line = br.readLine();
			while(!line.equals("") ) {
				if(line.contains("Cookie")) {
					//Set-Cookie: myFirstCookie=123;mySecondCookie=Hello
					String parseColone = line.split(":")[1];
					String parseSemiColon[] = parseColone.split(";");
					for(int i = 0;i < parseSemiColon.length; i++) {
						String parameters[] = parseSemiColon[i].trim().split("=");
						cookies.put(parameters[0], parameters[1]);
					}
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public HashMap<String, String> getCookies(){
		return this.cookies;
	}




	/*
	 * Reads a request on the given input stream and returns the corresponding HttpRequest object
	 */
	public HttpRequest getRequest(BufferedReader br) throws IOException {
		HttpRequest request = null;
		
		String startline = br.readLine();
		StringTokenizer parseline = new StringTokenizer(startline);
		String method = parseline.nextToken().toUpperCase(); 
		String ressname = parseline.nextToken();

		if (method.equals("GET")) {
			if(ressname.contains("ricmlets")) {
				this.getCookiesFromHeader(br);
				if(!cookies.containsKey("session-id")) {
					String appName = ressname.split("/")[2];
					Session s = this.getApplication(appName).getInstance(null);
					cookies.put("session-id",s.getId());
				}
				request = new HttpRicmletRequestImpl(this, method, ressname, br);
			}else {
				request = new HttpStaticRequest(this, method, ressname);
			}
		} else 
			request = new UnknownRequest(this, method, ressname);
		
		return request;
	}


	/*
	 * Returns an HttpResponse object associated to the given HttpRequest object
	 */
	public HttpResponse getResponse(HttpRequest req, PrintStream ps) {
		if(req instanceof HttpRicmletRequestImpl) {
			return new HttpRicmletResponseImpl(this, req, ps);
		}
		return new HttpResponseImpl(this, req, ps);
	}


	/*
	 * Server main loop
	 */
	protected void loop() {
		try {
			while (true) {
				Socket soc = m_ssoc.accept();
				(new HttpWorker(this, soc)).start();
			}
		} catch (IOException e) {
			System.out.println("HttpServer Exception, skipping request");
			e.printStackTrace();
		}
	}

	
	
	public static void main(String[] args) {
		int port = 0;
		if (args.length != 2) {
			System.out.println("Usage: java Server <port-number> <file folder>");
		} else {
			port = Integer.parseInt(args[0]);
			String foldername = args[1];
			HttpServer hs = new HttpServer(port, foldername);
			hs.loop();
		}
	}

}

