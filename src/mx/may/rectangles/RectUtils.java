package mx.may.rectangles;

import java.util.Collection;

/**
 * Created by Courtney on 29/11/2017.
 */
public class RectUtils
{

	public static Rectangle createRectangleFromPoints(Collection<Point> points)
	{
		if (points.size() == 0)
		{
			throw new IllegalArgumentException("Given an empty set of points, there needs to be at least one point to form a rectangle");
		}

		int maxx = 0, maxy = 0, minx = 0, miny = 0;
		boolean first = true;
		for (Point point : points)
		{
			if (first)
			{
				first = false;
				maxx = point.x;
				minx = point.x;
				maxy = point.y;
				miny = point.y;
			}
			else
			{
				maxx = Math.max(maxx, point.x);
				minx = Math.min(minx, point.x);
				maxy = Math.max(maxy, point.y);
				miny = Math.min(miny, point.y);
			}
		}

		return new Rectangle(minx, miny, maxx, maxy);
	}
}
