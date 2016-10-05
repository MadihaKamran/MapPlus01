
package org.eclipse.jetty.embedded;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;

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
		1. find the nearest monitered places
		2. make sure the current location is on high way
		3. make sure we're calculating the right direction
		4. return the estimated time

*/


public class MainServer
{

    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Handler());

        Util.loadGeoInfo("VDS_devices.txt");	// coordinates -> contractId

        Util.loadTrafficData("trafficData.xml");
        server.start();
        server.join();
    }
}