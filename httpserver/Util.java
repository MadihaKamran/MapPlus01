
package org.eclipse.jetty.embedded;

import java.util.HashMap;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.BufferedReader;

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



public class Util
{
	public static HashMap<Point, String>    geoToContractId 
				= new HashMap<Point, String>();
    public static HashMap<String, Double>   ContractIdToSpeed;



    public static void loadGeoInfo(String file)
    {	
    	System.out.println("Loading data from geo-coordinates to contractId ...");
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		    while (true)
		    {
		    	String line = br.readLine();
		    	if(line == null || line.isEmpty())
		    	{
		    		break;
		    	}

		    	else
		    	{
		    		String[] splitArray = line.split("\\s+");
		    		String contractId = splitArray[0];
		    		double lat = Double.parseDouble(splitArray[1]);
				    double lng = Double.parseDouble(splitArray[2]);
				    geoToContractId.put(new Point(lat,lng), contractId);
		    	}
		       
		    }
		   
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