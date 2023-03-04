package closestPoint;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

public class ClosestPointsCalculatorTest {

	private static final int[] NUMBER_OF_POINTS_TO_TEST = { 5, 10, 20, 50, 100, 1000, 10000 };
	private static final int NUMBER_OF_TEST_RUNS = 1;

	private static final long DEFAULT_TIMEOUT_MILLIS = 1000;
	private static final int MAX_POINTS_IN_ERROR_MSG = 50;

	private static final Random RND = new Random();

	private Point[] bruteForceSolution(Point[] points) {
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

	private Point[] randomPoints(int number) {
		Set<Point> points = new HashSet<>();
		while (points.size() < number) {
			points.add(new Point(RND.nextInt(number), RND.nextInt(number)));
		}

		return points.toArray(new Point[number]);
	}

	/**
	 * Tests the closestPoint.ClosestPointsCalculator.findClosestPairOfPoints with random points
	 * against a brute force oracle. Times out if the algorithm takes too long to
	 * possibly be in the right big-O range, but the default timeout is pretty
	 * generous, so it won't catch every problem with a working implementation.
	 * <p>
	 * Since the tests are random there are no guarantees that a single run will
	 * catch a problem. The submission box in ilearn thus runs the test multiple
	 * times in order to increase the likelihood of catching errors. You can do the
	 * same by increasing the constant NUMBER_OF_TEST_RUNS.
	 * 
	 * @param numberOfPoints number of random points to test with
	 */
	@ParameterizedTest(name = "[{0}.{1}] {2} points")
	@MethodSource
	void testRandomPoints(int run, int test, int numberOfPoints) {
		Point[] points = randomPoints(numberOfPoints);
		Point[] expected = bruteForceSolution(points);

		Point[] actual = assertTimeoutPreemptively(Duration.ofMillis(DEFAULT_TIMEOUT_MILLIS), () -> {
			return ClosestPointsCalculator.findClosestPairOfPoints(points);
		});

		assertEquals(expected[0].distanceTo(expected[1]), actual[0].distanceTo(actual[1]), 0.001,
				errorMsg(points, actual, expected));
	}

	private static List<Arguments> testRandomPoints() {
		List<Arguments> testArguments = new ArrayList<>();

		for (int run = 1; run <= NUMBER_OF_TEST_RUNS; run++) {
			for (int test = 0; test < NUMBER_OF_POINTS_TO_TEST.length; test++) {
				testArguments.add(Arguments.of(run, test + 1, NUMBER_OF_POINTS_TO_TEST[test]));
			}
		}

		return testArguments;
	}

	private String errorMsg(Point[] points, Point[] actual, Point[] expected) {
		if (points.length > MAX_POINTS_IN_ERROR_MSG)
			return "Too many points (%d) to display".formatted(points.length);

		return """

				Points identified as closest: %s, distance=%f
				Example of points that are closer: %s, distance=%f
				All points: %s
				""".formatted(Arrays.toString(actual), actual[0].distanceTo(actual[1]), Arrays.toString(expected),
				expected[0].distanceTo(expected[1]), Arrays.toString(points));
	}

}
