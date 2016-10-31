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
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class Handler extends AbstractHandler
{
    public static double googleWeight = 0.5;
    public static double MTOWeight = 0.5;
     // The request comes in the query  string format from the user(mobile
     // application), so we need a parser function to extract the info we needed
     // from the query string
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

    // Since this is a HTTPREQUEST, we only need care about the body part
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
        String reqBody = getBody(request);
        if (reqBody != null)
        {  
            ArrayList<Point> route = new ArrayList<Point>();
            // the steps of that route 
            ArrayList<Integer> duration = new ArrayList<Integer>();
            // the duration of each step in that route
            parseRouteDurationInfo(reqBody, route, duration);   
            System.out.println(route.size() + " points parsed " + 
            					duration.size() + " durations parsed"); 
            
            // Step 2 - 5 combined
            double googleDuration = 0;
            for(double d : duration)
            {
                googleDuration += d;
            }

            int updated = 0;
            for(int i = 0; i <= duration.size()-1 ; i++)
            {
                // update the i-th duration
                boolean ret = Util.tryUpdateDurations(route, duration, i);
                if (ret)
                {
                    updated++;
                }
            }

            int MTODuration = 0;
            for (int d : duration)
            {
                MTODuration += d;
            }
       
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();  

            double weightedAvg = googleDuration * googleWeight 
                                 + MTODuration * MTOWeight;
          

            String message = "";
            JSONObject json, dur, incidents ;
            JSONArray duration_list,incidents_list ;
            json = new JSONObject();

            try
            {
               
                duration_list = new JSONArray();
                dur = new JSONObject();
                dur.put("google", googleDuration);
                dur.put("MTO", MTODuration);
                dur.put("weightedAvg", weightedAvg);
                duration_list.put(dur);
                json.put("duration", duration_list);

                incidents = new JSONObject();
                incidents_list = new JSONArray();
                for(int i = 0; i <= route.size() - 2 ; i++)
                {
                
                    Util.tryAddIncidents(route, incidents, i);
                }
                json.put("incidents", incidents_list);
            }
            catch (JSONException e) 
            {
                e.printStackTrace();
            }
            
            if(json != null)
            {
                 message = json.toString();
            }
           
            System.out.println(updated +"/" + duration.size() + " steps updated, new "
                        + "travelling time is " + weightedAvg + "sec");
            out.println(message);

        }
        else
        {
            System.out.println("input coordinates are null");
        }

        baseRequest.setHandled(true);
    }
}