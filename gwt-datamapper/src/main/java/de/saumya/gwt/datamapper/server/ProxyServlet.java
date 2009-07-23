package de.saumya.gwt.datamapper.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        URL url = new URL("http://localhost:3000" + req.getRequestURI());
        HttpURLConnection con = ((HttpURLConnection) url.openConnection());
        con.setRequestMethod(req.getMethod());
        con.setDoInput(true);
        con.addRequestProperty("Content-type", req.getContentType());
        log(req.getMethod());
        InputStream in = null;
        OutputStream out = null;
        if (req.getContentLength() > 0) {
            con.setDoOutput(true);
            con.addRequestProperty("Content-length", ""
                    + req.getContentLength());
            try {
                in = req.getInputStream();
                out = con.getOutputStream();
                int i = in.read();
                while (i != -1) {
                    out.write(i);
                    i = in.read();
                }
            }
            finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        try {
            in = con.getInputStream();
            out = resp.getOutputStream();
            int i = in.read();
            while (i != -1) {
                out.write(i);
                i = in.read();
            }
        }
        finally {
            if (in != null) in.close();
        }
    }
}
