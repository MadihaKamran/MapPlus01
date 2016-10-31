package server;
import java.util.ArrayList;


  //  class: QuadTree.
  //  @purpose: this class is a helper class providing the fast processing time
  //  for finding the place nearby(in a certain range)

class QuadTree  {
    public static final double LatToMeters = 111105.44;
     // One degree of latitude = 111105.44m in Toronto
    public static final double LngToMeters = 80671.87;
     // One degree of longitude = 80671.867m in Toronto
    private Node root;

    // helper node data type
    private class Node {
        double x,y;      // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees   
        Node(double x, double y) {
            this.x = x;
            this.y =y;
        }
    }


  
    //  Insert (x, y) into appropriate quadrant
   
    public void insert(double x, double y) {
        root = insert(root, x, y);
    }

    private Node insert(Node current, double x, double y) {
        if (current == null) return new Node(x, y);
        else if ( less(x, current.x) &&  less(y, current.y)) current.SW = insert(current.SW, x, y);
        else if ( less(x, current.x) && !less(y, current.y)) current.NW = insert(current.NW, x, y);
        else if (!less(x, current.x) &&  less(y, current.y)) current.SE = insert(current.SE, x, y);
        else if (!less(x, current.x) && !less(y, current.y)) current.NE = insert(current.NE, x, y);
        return current;
    }

  /***********************************************************************
    *  Range search. Given the original point, we want to find all the places within
       range meters
    ***************************************************************************/

    public ArrayList<Point> findNearby(double lat, double lng, double range) {

        double[] rect = new double[4];
        rect[0] = lat - range/LatToMeters; 
       
        rect[1] = lng - range/LngToMeters; 
        
        rect[2] = lat + range/LatToMeters;
        rect[3] = lng + range/LngToMeters;
        ArrayList<Point> points = new ArrayList<Point>();
        query2D(root, rect, points);
        //System.out.println(points.size() + " nearby location detected");
        return points;
    }

    private void query2D(Node current, double[] rect, ArrayList<Point> points) {
        if (current == null) return;
        double xmin = rect[0];
        double ymin = rect[1];
        double xmax = rect[2];
        double ymax = rect[3];

        if ( (current.x <= xmax) && (current.x >= xmin) && (current.y <= ymax) && (current.y >= ymin))
            points.add(new Point(current.x, current.y));
        if ( less(xmin, current.x) &&  less(ymin, current.y)) query2D(current.SW, rect, points);
        if ( less(xmin, current.x) && !less(ymax, current.y)) query2D(current.NW, rect, points);
        if (!less(xmax, current.x) &&  less(ymin, current.y)) query2D(current.SE, rect, points);
        if (!less(xmax, current.x) && !less(ymax, current.y)) query2D(current.NE, rect, points);
    }

    private boolean less(double k1, double k2) { return k1 < k2; }
    private boolean eq  (double k1, double k2) { return k1 == k2; }

}
