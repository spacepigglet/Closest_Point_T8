import java.util.*;

public class ClosestPointsCalculator {

	private static final int HALT = 5;

	public static Point[] findClosestPairOfPoints(Point[] points) {


		List<Point> xSortedPoints = new ArrayList<>(Arrays.asList(points));
		sortByX(xSortedPoints);
		List<Point> ySortedPoints = new ArrayList<>(xSortedPoints);
		sortByY(ySortedPoints);


		//divide into left and right sublists
		//List<Point> xSortedLeft = xSortedPoints.subList(0, xSortedPoints.size()/2);
		//List<Point> xListRight = xSortedPoints.subList(xSortedPoints.size()/2, xSortedPoints.size());


		return findClosest(xSortedPoints, ySortedPoints);
	}

	private static void sortByX(List<Point> points) {
		Collections.sort(points, new Comparator<>() { //O(n*log(n)) modified merge sort
			@Override
			public int compare(Point p1, Point p2) {
				return p1.x() - p2.x();
			}
		});
	}

	private static void sortByY(List<Point> points) {
		Collections.sort(points, new Comparator<>() { //O(n*log(n)) modified merge sort
			@Override
			public int compare(Point p1, Point p2) {
				return p1.y() - p2.y();
			}
		});
	}

	private static double distance(Point p1, Point p2) {
		return p1.distanceTo(p2);
	}

	private static Point[] findClosest(List<Point> xSortedPoints, List<Point> yPoints) {
		if (xSortedPoints.size() < HALT) {

			return bruteForceApproach(xSortedPoints);
		}
		int middle = xSortedPoints.size() / 2;
		List<Point> xSortedLeft = xSortedPoints.subList(0, middle);
		List<Point> yPointsLeft = yPoints.subList(0, middle);

		List<Point> xSortedRight = xSortedPoints.subList(middle, xSortedPoints.size());
		List<Point> yPointsRight = yPoints.subList(middle, yPoints.size());

		Point[] deltaLeftPair = findClosest(xSortedLeft, yPointsLeft);
		Point[] deltaRightPair = findClosest(xSortedRight, yPointsRight);
		double deltaLeft = distance(deltaLeftPair[0], deltaLeftPair[1]);
		double deltaRight = distance(deltaRightPair[0], deltaRightPair[1]);
		double delta = Math.min(deltaLeft, deltaRight);

		Point[] deltaPair;
		if (delta == deltaLeft) {
			deltaPair = deltaLeftPair;
		} else {
			deltaPair = deltaRightPair;
		}

		List<Point> stripPoints = findPointsInStrip(yPoints, delta, xSortedPoints.get(middle).x());

		Point[] minDeltaPair = minDeltaInStrip(stripPoints, delta);
		if (minDeltaPair[0] == null) {
			return deltaPair;
		}

		return minDeltaPair;

	}


	private static Point[] minDeltaInStrip(List<Point> stripPoints, double delta) {
		double minDelta = delta;
		Point[] minPair = new Point[2];
		for (int i = 0; i < stripPoints.size() - 1; i++) { //-1 to skip last loop where nothing will be done
			for (int j = i + 1; j < stripPoints.size(); j++) {
				if(stripPoints.get(i).y()-stripPoints.get(j).y() > minDelta){
					break;
				} else if (distance(stripPoints.get(i), stripPoints.get(j)) < minDelta) {
					minDelta = distance(stripPoints.get(i), stripPoints.get(j));
					minPair[0] = stripPoints.get(i);
					minPair[1] = stripPoints.get(j);
				}
			}

		}
		return minPair;
	}

	private static Point[] bruteForceApproach(List<Point> xSortedPoints) {
		double minDistance = Double.MAX_VALUE;
		Point[] minPair = new Point[2];

		for (int i = 0; i < xSortedPoints.size() - 1; i++) { //-1 to skip last loop where nothing will be done
			for (int j = i + 1; j < xSortedPoints.size(); j++) {
				if (distance(xSortedPoints.get(i), xSortedPoints.get(j)) < minDistance) {
					minDistance = distance(xSortedPoints.get(i), xSortedPoints.get(j));

					minPair[0] = xSortedPoints.get(i);
					minPair[1] = xSortedPoints.get(j);
				}
			}
		}
		return minPair;
	}

	//call with x list????
	private static List<Point> findPointsInStrip(List<Point> yPoints, double delta, double middleX) {

		List<Point> stripPoints = new ArrayList<>();
		for (int i = 0; i < yPoints.size(); i++) {
			//if the point is closer to the vertical middle than delta, then add
			if (Math.abs(yPoints.get(i).x() - middleX) < delta) {
				stripPoints.add(yPoints.get(i));
			}
		}
		return stripPoints; //not returning clone... possible issue?
	}



}
