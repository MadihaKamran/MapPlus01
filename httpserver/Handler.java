package org.eclipse.jetty.embedded;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Handler extends AbstractHandler
{
   

    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        StringBuffer buffer = new StringBuffer();
        String[] coordinates = request.getParameterValues("geo");   

        if (coordinates != null)
        {  
            for (int i = 0; i < coordinates.length; i++)
            {

                buffer.append(coordinates[i]+" ");

            }
            out.println(buffer.toString());
        }

        baseRequest.setHandled(true);
    }
}