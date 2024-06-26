package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.text.AbstractDocument.BranchElement;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

/*
 

    This class allows to build an object representing an HTTP static request*/

public class HttpStaticRequest extends HttpRequest {
    static final String DEFAULT_FILE = "index.html";
    static final String DEFAULT_PATH = "ricm.web.GIVEN/";

    public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
        super(hs, method, ressname);
    }

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

}