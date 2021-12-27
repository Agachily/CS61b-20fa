package bearmaps;

import java.util.List;

public class KdTree {
    private class Node {
        /* Children of this Node. */
        private Point point;
        private Node left;
        private Node right;
        private boolean isVertical;

        /* Initialize the node */
        private Node(Point point, boolean isVertical) {
            left = null;
            right = null;
            this.point = point;
            this.isVertical = isVertical;
        }

        public double getX() {
            return this.point.getX();
        }

        public double getY() {
            return this.point.getY();
        }
    }

    private Node root = null;

    public KdTree(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("There should be elements in the passed in list");
        }
        for (Point p : points) {
            root = put(p, root, true);
        }
    }

    /** The p represent the point to be inserted*/
    public Node put(Point p, Node currentRoot, boolean isVertical) {
        if (currentRoot == null) {
            return new Node(p, isVertical);
        } else if (currentRoot.isVertical && p.getX() >= currentRoot.getX()) {
            currentRoot.right = put(p, currentRoot.right, false);
        } else if (currentRoot.isVertical && p.getX() < currentRoot.getX()) {
            currentRoot.left = put(p, currentRoot.left, false);
        } else if (!currentRoot.isVertical && p.getY() >= currentRoot.getY()) {
            currentRoot.right = put(p, currentRoot.right, true);
        } else if (!currentRoot.isVertical && p.getY() < currentRoot.getY()) {
            currentRoot.left = put(p, currentRoot.left, true);
        }
        return currentRoot;
    }

    public Point nearest(double x, double y) {
        Point point = new Point(x, y);
        return point;
    }

    public static void main(String[] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(4, 4);

        KdTree kd = new KdTree(List.of(p1, p2, p3, p4, p5, p6));
    }
}
