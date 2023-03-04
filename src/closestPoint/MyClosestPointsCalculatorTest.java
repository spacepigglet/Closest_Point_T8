package closestPoint;

import java.util.*;

class MyClosestPointsCalculatorTest {

    private static final Random RND = new Random();
    private static final int RANGE = 100;
    private static final int SIZE = 10;

    public static void main(String[] args) {
//        closestPoint.Point[] points = new closestPoint.Point[SIZE];
//        for (int i = 0 ; i < SIZE ; i++) {
//            points[i] = new closestPoint.Point(RND.nextInt(RANGE), RND.nextInt(RANGE));
//        }
//
//        System.out.println(Arrays.asList(points));

        Point[] points = new Point[] {new Point(5, 6),  new Point(6, 7), new Point(1,5), new Point(2, 4), new Point(10, 2),new Point(8, 6)};
        System.out.println(Arrays.asList(bruteForceSolution(points)));
        System.out.println(Arrays.asList(ClosestPointsCalculator.findClosestPairOfPoints(points)));

    }


    public static Point[] bruteForceSolution(Point[] points) {
        Point[] closest = { points[0], points[1] };
        double closestDistance = closest[0].distanceTo(closest[1]);

        for (Point p1 : points) {
            for (Point p2 : points) {
                if (p1 != p2) {
                    double distance = p1.distanceTo(p2);
                    if (distance < closestDistance) {
                        closest[0] = p1;
                        closest[1] = p2;
                        closestDistance = distance;
                    }
                }
            }
        }

        return closest;
    }

}