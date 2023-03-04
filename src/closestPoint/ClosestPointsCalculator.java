package closestPoint;

import java.util.*;

public class ClosestPointsCalculator {

	private static final int HALT = 5;

	public static Point[] findClosestPairOfPoints(Point[] points) {


		List<Point> xSortedPoints = new ArrayList<>(Arrays.asList(points));
		List<Point> yPoints = new ArrayList<>(xSortedPoints);
		sortByX(xSortedPoints);



		//divide into left and right sublists
		//List<closestPoint.Point> xSortedLeft = xSortedPoints.subList(0, xSortedPoints.size()/2);
		//List<closestPoint.Point> xListRight = xSortedPoints.subList(xSortedPoints.size()/2, xSortedPoints.size());


		return findClosest(xSortedPoints, yPoints);
	}

	private static void sortByX(List<Point> points) {
		Collections.sort(points, new Comparator<>() {
			@Override
			public int compare(Point p1, Point p2) {
				return p1.x() - p2.x();
			}
		});
	}

	private static double distance(Point p1, Point p2) {
		return p1.distanceTo(p2);
	}

	private static Point[] findClosest(List<Point> xSortedPoints, List<Point> yPoints) {
		if (xSortedPoints.size() < HALT) {
			return minDeltaPair(xSortedPoints);
		}

		//middle index
		int middle = xSortedPoints.size() / 2;

		//Points on left side
		List<Point> xSortedLeft = xSortedPoints.subList(0, middle);
		List<Point> yPointsLeft = new ArrayList<>();
		//List<Point> yPointsLeft = yPoints.subList(0, middle);

		//Points on right side
		List<Point> xSortedRight = xSortedPoints.subList(middle, xSortedPoints.size());
		List<Point> yPointsRight = new ArrayList<>();
		//List<Point> yPointsRight = yPoints.subList(middle, yPoints.size());

		for(Point p : yPoints){
			if(p.x() < xSortedPoints.get(middle).x()){
				yPointsLeft.add(p);
			} else {
				yPointsRight.add(p);
			}
		}

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

		Point[] minStripPair = minDeltaPair(stripPoints);
		double deltaStrip = distance(minStripPair[0], minStripPair[1]);

		double deltaMin = Math.min(delta, deltaStrip);

		if (deltaMin == delta) {
			return deltaPair;
		} else {
			return minStripPair;
		}

//		if (minStripPair[0] == null) {
//			return deltaPair;
//		}
//
//		return minStripPair;

	}

//	private static closestPoint.Point[] minDeltaInStrip(List<closestPoint.Point> stripPoints, double delta) {
//		double minDelta = delta;
//		closestPoint.Point[] minPair = new closestPoint.Point[2];
//		for (int i = 0; i < stripPoints.size() - 1; i++) { //-1 to skip last loop where nothing will be done
//			for (int j = i + 1; j < stripPoints.size(); j++) {
//				if (distance(stripPoints.get(i), stripPoints.get(j)) < minDelta) {
//					minDelta = distance(stripPoints.get(i), stripPoints.get(j));
//					minPair[0] = stripPoints.get(i);
//					minPair[1] = stripPoints.get(j);
//				}
//			}
//
//		}
//		return minPair;
//	}

	private static Point[] minDeltaPair(List<Point> points) {
		double minDelta = Double.MAX_VALUE;
		Point[] minPair = new Point[2];

		for (int i = 0; i < points.size() - 1; i++) { //-1 to skip last loop where nothing will be done
			for (int j = i + 1; j < points.size(); j++) {
				if (distance(points.get(i), points.get(j)) < minDelta) {
					minDelta = distance(points.get(i), points.get(j));

					minPair[0] = points.get(i);
					minPair[1] = points.get(j);
				}
			}
		}
		return minPair;
	}

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
