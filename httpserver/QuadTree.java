package org.eclipse.jetty.embedded;
import java.util.ArrayList;

/******************************************************************************
 *  class: QuadTree.
 *	@purpose: this class is a helper class providing the fast processing time
 * 			  for finding the place nearby(in a certain range)
 ******************************************************************************/

class QuadTree  {
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


  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***************************************************************************/
    public void insert(double x, double y) {
        root = insert(root, x, y);
    }

    private Node insert(Node h, double x, double y) {
        if (h == null) return new Node(x, y);
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y);
        return h;
    }

  /***********************************************************************
    *  Range search.
    ***************************************************************************/

    public ArrayList<Point> query(double[] rect) {
        ArrayList<Point> points = new ArrayList<Point>();
        query2D(root, rect, points);
        return points;
    }

    private void query2D(Node h, double[] rect, ArrayList<Point> points) {
        if (h == null) return;
        double xmin = rect[0];
        double ymin = rect[1];
        double xmax = rect[2];
        double ymax = rect[3];

        if ( (h.x < xmax) && (h.x > xmin) && (h.y < ymax) && (h.y > ymin))
            points.add(new Point(h.x, h.y));
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, points);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, points);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, points);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, points);
    }

    private boolean less(double k1, double k2) { return k1 < k2; }
    private boolean eq  (double k1, double k2) { return k1 == k2; }

}
