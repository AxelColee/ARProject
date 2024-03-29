package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest{
	
	static final String DEFAULT_PATH = "ricm.web.GIVEN/";
	
	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
        super(hs, method, ressname, br);
        
    }

	@Override
	public void process(HttpResponse resp) throws Exception {
        try {
        	String clsname = DEFAULT_PATH + this.m_ressname.replace("/", ".").substring(1);
        	System.out.println(clsname);
            Class<?> c = Class.forName(clsname);
            HttpRicmlet ricmlet = (HttpRicmlet) c.getDeclaredConstructor().newInstance();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
