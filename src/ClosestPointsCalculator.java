import java.util.*;

public class ClosestPointsCalculator {

	private static final int HALT = 5;

	public static Point[] findClosestPairOfPoints(Point[] points) {
		if (points.length < 2 ){
			return new Point[0]; //no pairs exist!
		}

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

	private static Point[] findClosest(List<Point> xSortedPoints, List<Point> ySortedPoints) {
		if (xSortedPoints.size() <= HALT) {
			return bruteForceApproach(xSortedPoints);
		}
		int middle = xSortedPoints.size() / 2;
		List<Point> xSortedLeft = xSortedPoints.subList(0, middle);
		List<Point> ySortedLeft = new ArrayList<>();

		List<Point> xSortedRight = xSortedPoints.subList(middle, xSortedPoints.size());
		List<Point> ySortedRight = new ArrayList<>();

		//divide y sorted lists into left/right by x-value
		for(Point p : ySortedPoints){
			if(p.x() < xSortedPoints.get(middle).x()){
				ySortedLeft.add(p);
			} else {
				ySortedRight.add(p);
			}
		}

		Point[] deltaLeftPair = findClosest(xSortedLeft, ySortedLeft);
		Point[] deltaRightPair = findClosest(xSortedRight, ySortedRight);

		double deltaLeft = distance(deltaLeftPair[0], deltaLeftPair[1]);
		double deltaRight = distance(deltaRightPair[0], deltaRightPair[1]);
		double delta = Math.min(deltaLeft, deltaRight);

		//important to send in ySorted as it needs to be y-sorted to find minDeltaPair
		List<Point> stripPoints = findPointsInStrip(ySortedPoints, delta, xSortedPoints.get(middle).x());

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


	private static List<Point> findPointsInStrip(List<Point> ySortedPoints, double delta, double middleX) {

		List<Point> stripPoints = new ArrayList<>();
		for (int i = 0; i < ySortedPoints.size(); i++) {
			//if the point is closer to the vertical middle than delta, then add
			if (Math.abs(ySortedPoints.get(i).x() - middleX) < delta) {
				stripPoints.add(ySortedPoints.get(i));
			}
		}
		return stripPoints; //not returning clone... possible issue?
	}



}
