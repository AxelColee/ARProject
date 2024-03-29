package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;
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
            String resname = super.getContentType(this.m_ressname);
            resp.setReplyOk();
            resp.setContentType(resname);
            FileInputStream fis = new FileInputStream(this.m_ressname.substring(1));
            byte b[] = fis.readAllBytes();
            resp.setContentLength(b.length);
            PrintStream pt = resp.beginBody();
            pt.write(b);
        }catch(FileNotFoundException e){
            resp.setReplyError(404, "File Not Found");
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