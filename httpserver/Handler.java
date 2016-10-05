package org.eclipse.jetty.embedded;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Handler extends AbstractHandler
{
   

    public static String body = "geo%5B21%5D=-79.48293&geo%5B393%5D=-79.53894&geo%5B238%5D=43.6338&geo%5B934%5D=43.68809&geo%5B342%5D=43.62225&geo%5B623%5D=-79.58551&geo%5B870%5D=43.68534&geo%5B634%5D=43.6716&geo%5B808%5D=43.6861&geo%5B498%5D=43.62755&geo%5B440%5D=43.61544&geo%5B322%5D=43.62499&geo%5B454%5D=43.61632&geo%5B441%5D=-79.55023&geo%5B323%5D=-79.49253&geo%5B842%5D=43.68545&geo%5B636%5D=43.67163&geo%5B79%5D=-79.47522&geo%5B807%5D=-79.60058&geo%5B665%5D=-79.59031&geo%5B444%5D=43.61559&geo%5B94%5D=43.64157&geo%5B713%5D=-79.59288&geo%5B328%5D=43.62347&geo%5B496%5D=43.6267&geo%5B575%5D=-79.57479&geo%5B343%5D=-79.50446&geo%5B660%5D=43.67246&geo%5B987%5D=-79.62091";

    public static String getBody(HttpServletRequest request) throws IOException 
    {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        String payloadRequest = getBody(request);
        System.out.println("new user request \n" + payloadRequest);
        System.out.println("Get new user request");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();  

        if (payloadRequest != null)
        {  
            // System.out.println("The route consists " + coordinates.length + "segments");
            // for (int i = 0; i < coordinates.length; i++)
            // {
            //     System.out.println(coordinates[i]+" ");
            // }
            // ArrayList<Point> points = Util.monitoredPlaces.findNearby(
            //                           Double.parseDouble(coordinates[0])
            //                         , Double.parseDouble(coordinates[1])
            //                         , Double.parseDouble(coordinates[2]));
            // for (int i = 0; i < points.size(); i++)
            // {
            //     buffer.append(points.get(i).latitude+" " + points.get(i).longitude + "<br>");
            // }
            //buffer.append("5 mins");

            //out.println(buffer.toString());
            out.println("10 mins");
        }
        else
        {
            System.out.println("input coordinates are null");
        }

        baseRequest.setHandled(true);
    }
}