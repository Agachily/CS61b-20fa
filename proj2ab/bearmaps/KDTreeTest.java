package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    public List<Point> generatePoints(int number) {
        List<Point> points = new ArrayList<>();
        Random random = new Random(42);
        for (int i = 0; i < number; i++) {
            double x = random.nextDouble() * 1000;
            double y = random.nextDouble() * 1000;
            points.add(new Point(x, y));
        }
        return points;
    }

    @ Test
    public void testKdTreeNearestMethod() {
        List<Point> points = generatePoints(1000);
        List<Point> testPoints = generatePoints(200);
        NaivePointSet naivePointSet = new NaivePointSet(points);
        KdTree kdTree = new KdTree(points);
        for (Point p : points) {
            Point expected = naivePointSet.nearest(p.getX(), p.getY());
            Point real = kdTree.nearest(p.getX(), p.getY());
            assertEquals(expected, real);
        }
    }

}
