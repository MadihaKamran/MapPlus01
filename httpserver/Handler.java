package org.eclipse.jetty.embedded;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Handler extends AbstractHandler
{
   

     public static void parseRouteDurationInfo(String input,  
     					ArrayList<Point> route , ArrayList<Integer> duration)  
     {  
         String[] params = input.split("&");  
        
         HashMap<Integer,Double> lat = new HashMap<Integer,Double>(); 
         HashMap<Integer,Double> lng = new HashMap<Integer,Double>(); 
         HashMap<Integer,Integer> dur = new HashMap<Integer,Integer>(); 
         for (String param : params)  
         {  
             String name = param.split("=")[0];  
             double value = Double.parseDouble(param.split("=")[1]);
             int index = Integer.parseInt(name.substring(3));
            
             if(name.contains("lat"))
             {
               lat.put(index,value);
             }
             else if(name.contains("lng"))
             {
                lng.put(index,value);
             }
             else if(name.contains("dur"))
             {
             	// the duration of one step
             	dur.put(index,(int)value);
             }
         }  
         for(int i = 0; i< lat.size(); i++)
         {
            route.add(new Point(lat.get(i),lng.get(i)));
         }

         for(int i = 0; i< dur.size(); i++)
         {
            duration.add(dur.get(i));
         }

     }  


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
        /*
            For each user request, to generate appropriate response, we need go through
            the following steps: 
            1. parse the route(steps) planned by google
            2. For each steps, try find the nearest palaces for both starting point and
               ending point within a range (say 100m/200m)
            3. make sure the current location is on high way
            4. make sure we're calculating the right direction
            5. replace the estimated time with the one google provided
        */
        


        // Step 1 
        response.setContentType("text/html; charset=utf-8");
        String payloadRequest = getBody(request);
        ArrayList<Point> route = new ArrayList<Point>();
        // the steps of that route 
        ArrayList<Integer> duration = new ArrayList<Integer>();
        // the duration of each step in that route
        parseRouteDurationInfo(payloadRequest, route, duration);   
        System.out.println(route.size() + " points parsed " + 
        					duration.size() + " durations parsed"); 
        
        // Step 2 - 5 combined
        int updated = 0;
        for(int i = 0; i < route.size()-2 ; i++)
        {
            boolean ret = Util.tryUpdateDurations(route, duration, i);
            if (ret)
            {
                updated++;
            }
        }

        int sum = 0;
        for (int d : duration)
        {
            sum += d;
        }
       
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();  

        if (payloadRequest != null)
        {  
           
            out.println(updated +"/" + duration.size() + " steps updated, new "
                        + "travelling time is " + sum + "sec");
        }
        else
        {
            System.out.println("input coordinates are null");
        }

        baseRequest.setHandled(true);
    }
}