package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {
    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        double distance = Double.MAX_VALUE;
        Point nearestPoint = null;
        Point currentPoint = new Point(x, y);
        for (Point p : points) {
            double currentDistance = Point.distance(currentPoint, p);
            if (distance > currentDistance) {
                distance = currentDistance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
//        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
//        Point p2 = new Point(3.3, 4.4);
//        Point p3 = new Point(-2.9, 4.2);
//
//        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
//        Point ret = nn.nearest(3.0, 4.0); // returns p2
//        System.out.println(ret.getX()); // evaluates to 3.3
//        System.out.println(ret.getY()); // evaluates to 4.4

        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(4, 4);
        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6));
        Point nearest = nn.nearest(0, 7);
        System.out.println(nearest.getX()); // evaluates to 3.3
        System.out.println(nearest.getY()); // evaluates to 4.4
    }
}
