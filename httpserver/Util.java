
package org.eclipse.jetty.embedded;

import java.util.HashMap;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;

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

   

}