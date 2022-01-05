package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet {
    public class Node {
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

        public double distance(Point targetPoint) {
            return Point.distance(point, targetPoint);
        }

        public double getX() {
            return this.point.getX();
        }

        public double getY() {
            return this.point.getY();
        }
    }

    private Node root = null;

    public KDTree(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("There should be elements in the passed in list");
        }
        for (Point p : points) {
            root = put(p, root, true);
        }
    }

    public Node getRoot() {
        return root;
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

    private Node nearest(Node n, Point goal, Node best, boolean isVertical) {
        if (n == null) {
            return best;
        }
        if (n.distance(goal) < best.distance(goal)) {
            best = n;
        }
        /* First check the good side then decide whether checking the bad side */
        if (isVertical && goal.getX() >= n.getX()) {
            best = nearest(n.right, goal, best, false);
            if (Math.pow(goal.getX() - n.getX(), 2) < Point.distance(goal, best.point)) {
                best = nearest(n.left, goal, best, false);
            }
        } else if (isVertical && goal.getX() < n.getX()) {
            best = nearest(n.left, goal, best, false);
            if (Math.pow(goal.getX() - n.getX(), 2) < Point.distance(goal, best.point)) {
                best = nearest(n.right, goal, best, false);
            }
        } else if (!isVertical && goal.getY() >= n.getY()) {
            best = nearest(n.right, goal, best, true);
            if (Math.pow(goal.getY() - n.getY(), 2) < Point.distance(goal, best.point)) {
                best = nearest(n.left, goal, best, true);
            }
        } else if (!isVertical && goal.getY() < n.getY()) {
            best = nearest(n.left, goal, best, true);
            if (Math.pow(goal.getY() - n.getY(), 2) < Point.distance(goal, best.point)) {
                best = nearest(n.right, goal, best, true);
            }
        }
        return best;
    }


    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        Node best = nearest(root, goal, root, true);
        return best.point;
    }


    public static void main(String[] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 5);
        Point p4 = new Point(3, 3);
        Point p5 = new Point(1, 5);
        Point p6 = new Point(0, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        Point nearestPoint = kd.nearest(0, 7);
        System.out.println(nearestPoint.getX());
        System.out.println(nearestPoint.getY());
    }
}
