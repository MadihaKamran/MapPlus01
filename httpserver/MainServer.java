
package server;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import java.util.Scanner;
import java.text.DecimalFormat;


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

	public static void loadAllData( Scanner scanner)
	{

        // MTO contractID -> geo-coordinates
        System.out.println("Enter the file name for geo-coordinates to contractId: ");
		String geo = scanner.nextLine();
		System.out.println("Loading data from geo-coordinates to contractId from \"" 
        				  + geo +"\"");
        Util.loadGeoInfo(geo);	// coordinates -> contractId
      

       	
       	// MTO traffic data
       	System.out.println("Enter the file name of the MTO traffic data");
        String traffic = scanner.nextLine();
        System.out.println("Loading traffic data from \"" + traffic +"\"");
        Util.loadTrafficData(traffic);
        

        // set up the maximum allowed distance error
       	System.out.println("Enter max allowed distance when estimate duration");
       	Util.velocityThreshold = Double.parseDouble(scanner.nextLine());
       	System.out.println("We can use the MTO speed data within at most "
       						+ Util.velocityThreshold + " meters tolerance");
       	
       	System.out.println("Enter max allowed distance when looking for incidents");
       	Util.incidentsRange = Double.parseDouble(scanner.nextLine());
        System.out.println("We will report the incidents within "
       						+ Util.incidentsRange + " meters range");


        // traffic data information coming from cellphone based probes.
        while (scanner.hasNextLine()) {
			
			String file = scanner.nextLine();
			System.out.println("Loading addtional traffic data from \"" + file +"\"");
			Util.loadIncidentData(file);	
		}
	}

	public static void setWeight(Scanner scanner)
	{
		
		double google = Double.parseDouble(scanner.nextLine());
		double mto = Double.parseDouble(scanner.nextLine());
		Handler.googleWeight = google/(google + mto) *1.0;
		Handler.MTOWeight = 1 - Handler.googleWeight;
		System.out.println("The data from google weighs " 
						  + (new DecimalFormat("##.##").format(Handler.googleWeight * 100))
						  + "%, while the data from MTO weighs " 
						  +(new DecimalFormat("##.##").format(Handler.MTOWeight * 100))
						  + "%" );
	}

    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new Handler());
        Scanner scanner = new Scanner(System.in);

        setWeight(scanner);

        // load all the data required to start the server
        loadAllData(scanner);

        server.start();
        server.join();
    }
}