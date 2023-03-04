public record Point(int x, int y) {

	public final double distanceTo(Point point) {
		int dx = x - point.x;
		int dy = y - point.y;
		double d = Math.sqrt(dx * dx + dy * dy);
		return d;
	}

	public String toString() {
		return "(x=%d, y=%d)".formatted(x,y);
	}
	
}
