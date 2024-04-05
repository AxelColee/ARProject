package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

import examples.HelloRicmlet;
import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest{
	
	private String path;
	private String params;
	private HashMap<String, String> values;
	private static HashMap<String, HttpRicmlet> ricmlets = new HashMap<>();
	static final String DEFAULT_PATH = "ricm.web.GIVEN/";
	
	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
        super(hs, method, ressname, br);
        System.out.println("test");
        String temp[] = this.m_ressname.split("\\?");
        this.path = temp[0];
        if(temp.length == 2) {
        	this.params = temp[1];
        	String parse[] = this.params.split("&");
    		values = new HashMap<>();
            for(int i = 0; i < parse.length; i++) {
            	String temporary[] = parse[i].split("=");
            	values.put(temporary[0], temporary[1]);
            }
        }
        
        
    }

	@Override
	public void process(HttpResponse resp) throws Exception {
        try {
        	String clsname = this.path.replace("/", ".").substring(1).split("ricmlets.")[1];
        	HttpRicmlet ricmlet = ricmlets.get(clsname);
        	if(ricmlet == null) {
        		System.out.println("hola");
        		Class<?> c = Class.forName(clsname);
                
                ricmlet = (HttpRicmlet) c.getDeclaredConstructor().newInstance();
                ricmlets.put(clsname, ricmlet);
        	}
        	
            ricmlet.doGet(this, (HttpRicmletResponseImpl)resp);
        } catch(ClassNotFoundException e) {
        	e.printStackTrace();
        }
            

    }

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArg(String name) {
		if(values != null) {
			return values.get(name);
		}
		return null;
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
