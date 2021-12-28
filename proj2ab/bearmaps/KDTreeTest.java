package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    public List<Point> generatePoints(int number) {
        List<Point> points = new ArrayList<>();
        Random random1 = new Random(123);
        Random random2 = new Random(42);
        Random random3 = new Random(32);
        double negativeX = -1;
        double negativeY = -1;
        for (int i = 0; i < number; i++) {
            if (random2.nextDouble() > 0.5) {
                negativeX = 1;
            } else {
                negativeX = -1;
            }
            if (random3.nextDouble() > 0.5) {
                negativeY = 1;
            } else {
                negativeY = -1;
            }
            double x = random1.nextDouble() * 1000 * negativeX;
            double y = random1.nextDouble() * 1000 * negativeY;
            points.add(new Point(x, y));
        }
        return points;
    }

    @ Test
    public void testKdTreeNearestMethod() {
        List<Point> points = generatePoints(1000);
        List<Point> testPoints = generatePoints(70);
        NaivePointSet naivePointSet = new NaivePointSet(points);
        KDTree kdTree = new KDTree(points);
        for (Point p : testPoints) {
            Point expected = naivePointSet.nearest(p.getX(), p.getY());
            Point real = kdTree.nearest(p.getX(), p.getY());
            assertEquals((int)expected.getX(), (int)real.getX());
            assertEquals((int)expected.getY(), (int)real.getY());
        }
    }

    @Test
    /** KdTree is much slower than NaivePointSet without pruning */
    public void testEfficiency() {
        List<Point> points = generatePoints(100000);
        NaivePointSet naivePointSet = new NaivePointSet(points);
        KDTree kdTree = new KDTree(points);
        List<Point> testPoints = generatePoints(10000);
        long start = System.currentTimeMillis();
        for (Point p : testPoints) {
            naivePointSet.nearest(p.getX(), p.getY());
        }
        long end = System.currentTimeMillis();
        System.out.println("NaivePointSet: " + (end - start) / 1000.0 + " seconds");

        start = System.currentTimeMillis();
        for (Point p : testPoints) {
            kdTree.nearest(p.getX(), p.getY());
        }
        end = System.currentTimeMillis();
        System.out.println("KDTree: " + (end - start) / 1000.0 + " seconds");
    }
}
