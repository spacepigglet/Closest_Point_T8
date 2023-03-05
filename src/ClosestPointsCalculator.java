//Amanda Sjöberg amsj1785, Jessica Borg jesbo0161
import java.util.*;

public class ClosestPointsCalculator {

	private static final int HALT = 5;

	public static Point[] findClosestPairOfPoints(Point[] points) {
		if (points.length < 2 ){
			return new Point[0]; //no pairs exist!
		}

		List<Point> pointsSortedByX = new ArrayList<>(Arrays.asList(points));
		sortByX(pointsSortedByX);
		List<Point> pointsSortedByY = new ArrayList<>(pointsSortedByX);
		sortByY(pointsSortedByY);

		return findClosest(pointsSortedByX, pointsSortedByY);
	}

	private static void sortByX(List<Point> points) {
		//O(n*log(n)) merge sort
		Collections.sort(points, (pointA, pointB) -> pointA.x() - pointB.x());
	}

	private static void sortByY(List<Point> points) {
		//O(n*log(n)) merge sort
		Collections.sort(points, (pointA, pointB) -> pointA.y() - pointB.y());
	}

	private static double distance(Point pointA, Point pointB) {
		return pointA.distanceTo(pointB);
	}

	private static Point[] findClosest(List<Point> pointsByX, List<Point> pointsByY) {
		if (pointsByX.size() <= HALT) {
			return bruteForceApproach(pointsByX);
		}
		int middle = pointsByX.size() / 2;
		List<Point> sortedXLeft = pointsByX.subList(0, middle);
		List<Point> sortedYLeft = new ArrayList<>();

		List<Point> sortedXRight = pointsByX.subList(middle, pointsByX.size());
		List<Point> sortedYRight = new ArrayList<>();

		//divide y sorted list into left/right by x-value
		for(Point p : pointsByY){
			if(p.x() < pointsByX.get(middle).x()){
				sortedYLeft.add(p);
			} else {
				sortedYRight.add(p);
			}
		}

		//find closest pair in right half and left half
		Point[] deltaLeftPair = findClosest(sortedXLeft, sortedYLeft);
		Point[] deltaRightPair = findClosest(sortedXRight, sortedYRight);

		double deltaLeft = distance(deltaLeftPair[0], deltaLeftPair[1]);
		double deltaRight = distance(deltaRightPair[0], deltaRightPair[1]);
		//shortest distance of the two pairs
		double delta = Math.min(deltaLeft, deltaRight);

		//important to send in ySorted as it needs to be y-sorted to find minDeltaPair
		List<Point> stripPoints = findPointsInStrip(pointsByY, delta, pointsByX.get(middle).x());

		//finds the closest pair overall, else null
		Point[] minDeltaPair = minDeltaInStrip(stripPoints, delta);

		if (minDeltaPair[0] == null) {
			if (delta == deltaLeft) {
				return deltaLeftPair;
			} else {
				return deltaRightPair;
			}
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

	private static Point[] bruteForceApproach(List<Point> sortedXPoints) {
		double minDistance = Double.MAX_VALUE;
		Point[] minPair = new Point[2];

		for (int i = 0; i < sortedXPoints.size() - 1; i++) { //-1 to skip last loop where nothing will be done
			for (int j = i + 1; j < sortedXPoints.size(); j++) {
				if (distance(sortedXPoints.get(i), sortedXPoints.get(j)) < minDistance) {
					minDistance = distance(sortedXPoints.get(i), sortedXPoints.get(j));

					minPair[0] = sortedXPoints.get(i);
					minPair[1] = sortedXPoints.get(j);
				}
			}
		}
		return minPair;
	}


	private static List<Point> findPointsInStrip(List<Point> sortedYPoints, double delta, double middleX) {

		List<Point> stripPoints = new ArrayList<>();
		for (Point p : sortedYPoints) {
			//if the point is closer to the vertical middle than delta, then add
			if (Math.abs(p.x() - middleX) < delta) {
				stripPoints.add(p);
			}
		}
		return stripPoints; //not returning clone... possible issue?
	}



}
