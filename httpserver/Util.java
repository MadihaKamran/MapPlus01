
package org.eclipse.jetty.embedded;

import java.util.HashMap;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

class Point
{
	// the geo-coordinates of a place
	double latitude;
	double longitude;

	public Point(double x, double y)
	{
		latitude = x;
		longitude = y;
	}

	public double distance(Point p)
	{
		return  Math.sqrt(Math.pow(p.latitude - latitude ,2) +
						  Math.pow(p.longitude -longitude ,2));
	}


	@Override
    public int hashCode() {
    	int result = 17;
        result =(int) (31 * result + latitude * 10000);
        result = (int) (31 * result + longitude * 10000);
        return result;
    }

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Point other = (Point) obj;
	    return (latitude==other.latitude)&&(longitude == other.longitude);
	}
}


class trafficData
{
	// we only extract the data we're interested in
	String contractId;
	double speed;
}


public class Util
{
	public static HashMap<Point, String>    geoToContractId; 
	public static QuadTree	monitoredPlaces ; 
	// the QuadTree data structure provided a quick way find the nearby places
    public static HashMap<String, Double>   contractIdToSpeed;


    public static void loadTrafficData(String file)
    {
    	// load the traffic data 
    	contractIdToSpeed = new HashMap<String, Double>();
    	System.out.println("Loading traffic data from \"" + file +"\"");
  

    	try {
	         File inputFile = new File(file);
	         DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder;

	         dBuilder = dbFactory.newDocumentBuilder();

	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();

	         XPath xPath =  XPathFactory.newInstance().newXPath();

	         String expression = "/vdsDataSet/vdsData";	        
	         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
	         for (int i = 0; i < nodeList.getLength(); i++)
	         {
	            Node currentNode = nodeList.item(i);
	            //System.out.println("\nCurrent Element :" 
	              // + currentNode.getNodeName());
	            if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
	            {

	               double speed = -1;
	               Element eElement = (Element) currentNode;
	               String contractId = eElement
	                  	.getElementsByTagName("contractId")
	                    .item(0)
	                    .getTextContent();
	               //System.out.println("contractId : " + contractId); 
	               String validString = eElement
	                     .getElementsByTagName("validThisPeriod")
	                     .item(0)
	                     .getTextContent();
	               boolean isValid = validString.equals("true") ? true : false;
	               if (!isValid)
	               {
	               		speed = 100;
	               		//System.out.println("Speed : Invalid, use default value: 100km/h");     
	            
	               }
	               else
	               {
	               	   speed = Double.parseDouble(
	               	   eElement
	                   .getElementsByTagName("spd")
	                   .item(0)
	                   .getTextContent());
		               if (speed > 0)
		               {
		               		//System.out.println("Speed : " + speed);                  
		               }
		               else
		               {
		               		speed = 100;
		               		//System.out.println("Speed : N/A, use default value: 100km/h");     
		               }
	               }
	               contractIdToSpeed.put(contractId, speed);
     
	          	 }
	         }
	         System.out.println("finishing loading " + nodeList.getLength() + " traffic data");
        } 

      catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (XPathExpressionException e) {
         e.printStackTrace();
      }

    }


    public static void loadGeoInfo(String file)
    {	// load the of coordinates -> contractId

    	System.out.println("Loading data from geo-coordinates to contractId from \"" + file +"\"");
    	geoToContractId = new HashMap<Point, String>();
    	monitoredPlaces = new QuadTree();

    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			int num = 0;
		    while (true)
		    {
		    	String line = br.readLine();
		    	if(line == null || line.isEmpty())
		    	{
		    		break;
		    	}

		    	else
		    	{
		    		num ++;
		    		String[] splitArray = line.split("\\s+");
		    		String contractId = splitArray[0];
		    		double lat = Double.parseDouble(splitArray[1]);
				    double lng = Double.parseDouble(splitArray[2]);
				    geoToContractId.put(new Point(lat,lng), contractId);
				    // System.out.println("add contractId: "+ contractId +
				    // 					"lat: " + lat + " lng: " + lng);
				    monitoredPlaces.insert(lat,lng);
		    	}
		       
		    }
		    System.out.println("finishing loading " + num + " coordinates->contractId data");
		   
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		finally {
			try{
				 br.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		   
		}

    }


   private static Comparator<Point> createComparator(Point p)
   {
        final Point finalP = new Point(p.latitude, p.longitude);
        return new Comparator<Point>()
        {
            @Override
            public int compare(Point p0, Point p1)
            {
                double ds0 = p0.distance(finalP);
                double ds1 = p1.distance(finalP);
                return Double.compare(ds0, ds1);
            }

        };
    }

    public static String verifyDirection(String dir, ArrayList<Point> nearby, Point origin)
    {	
    	// first sort all the palace by the distance to origin
    	Collections.sort(nearby, createComparator(origin));
    	for(Point p : nearby)
    	{
    		System.out.println(p.latitude + " " + p.longitude);
    		String contractId = geoToContractId.get(p);
    		if(contractId == null)
    			System.out.println(" contractId is null");

    		System.out.println(" contractId is " + contractId);
    		// if they have the same direction, return 
    		char direction = contractId.charAt(contractId.length()-2);
    		if(dir.indexOf(direction) >= 0)
    		{
    			return contractId;
    		}
    	}
    	return null;

    }

    public static double distanceInMeter(Point a, Point b)
    {
    	double lat = (a.latitude - b.latitude) * 111105.44; 
    	// One degree of latitude = 111105.44m in Toronto
        double lng = (a.longitude - b.longitude) * 80671.87;
        // One degree of longitude = 80671.867m in Toronto
        return Math.sqrt(lat*lat + lng * lng);

    }

    public static boolean tryUpdateDurations(ArrayList<Point> route
    						, ArrayList<Integer> duration
    						, int i)
    {

            Point start = route.get(i);
            Point end = route.get(i+1);
            String sDirection = "";
            String eDirection = "";
            sDirection += (end.latitude > start.latitude) ? "N" : "S";
          	sDirection += (end.longitude > start.longitude)? "E" : "W";
          	if(i+1 == route.size()-1)
          	{
          		// already the last point
          		eDirection = sDirection;
          	}
          	else
          	{
          		// else, we want to confirm the direction of the end point as 
          		// well cuz we don't want to misuse the traffic data in the 
          		// wrong direction 
          		Point last = route.get(i+2);
          		eDirection += (last.latitude > end.latitude) ? "N" : "S";
          		eDirection += (last.longitude > end.longitude)? "E" : "W";
          	}

        
            ArrayList<Point> nearStart = monitoredPlaces.findNearby(
                                         start.latitude,
                                         start.longitude,
                                         100);
            // within 100 meters
            ArrayList<Point> nearEnd = monitoredPlaces.findNearby(
                                         end.latitude,
                                         end.longitude,
                                         100);
            if(nearStart.size() == 0 || nearEnd.size() == 0)
            {
            	return false;
            }
            else
            {
            	String startId = verifyDirection(sDirection,nearStart, start);
            	String endId = verifyDirection(eDirection,nearEnd, end);
            	if(startId == null || endId == null)
            	{
            		return false;
            	}
            	else
            	{
            		// now we can safely replace the old duration with our own data
            		double distance= distanceInMeter(start, end); 
            		int spd = (int) (contractIdToSpeed.get(startId) * 0.5 
            						+ contractIdToSpeed.get(endId) * 0.5);

            		int timeInSec = (int) distance / (spd * 1000 / 3600);
            		duration.set(i, timeInSec);
            		return true;
            	}

            }
    }

}