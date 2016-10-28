
package org.eclipse.jetty.embedded;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import java.util.Scanner;

/*
	The purpose of this server is to get a better estimate of the travelling 
	time given the route.

	The input are a series of geo-coordinates describing the route, the output
	should be the travelling time estimated.

	At the start-up time, the server need to load the following data:
		1. the map from geo-coordinates -> contractID
			since all the XML traffic data are using contractID instead of 
			coordniates, we need this map
		2. all the traffic data
			this data should be used to compute the estimated travelling time

	For each user request, to generate appropriate response, we need go through
	the following steps: 
		1. parse the route(steps) planned by google
		2. For each steps, find the nearest monitered places and
		   replace the duration of that estimate with ATC data
		3. make sure the current location is on high way
		4. make sure we're calculating the right direction
		5. return the estimated time

*/


public class MainServer
{

	public static void loadAllData()
	{

        // MTO contractID -> geo-coordinates
        System.out.println("Enter the file name for geo-coordinates to contractId: ");
		Scanner scanner = new Scanner(System.in);
		String geo = scanner.nextLine();
		System.out.println("Loading data from geo-coordinates to contractId from \"" 
        				  + geo +"\"");
        Util.loadGeoInfo(geo);	// coordinates -> contractId
      

       	
       	// MTO traffic data
        String traffic = scanner.nextLine();
        Util.loadTrafficData(traffic);
        System.out.println("Loading traffic data from \"" + traffic +"\"");


        // traffic data information coming from cellphone based probes.
        while (scanner.hasNextLine()) {
			
			String file = scanner.nextLine();
			System.out.println("Loading addtional traffic data from \"" + file +"\"");
			Util.loadIncidentData(file);	
		}
	}

    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Handler());

        // load all the data required to start the server
        loadAllData();

        server.start();
        server.join();
    }
}