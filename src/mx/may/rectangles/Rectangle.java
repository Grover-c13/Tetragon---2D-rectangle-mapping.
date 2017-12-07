package mx.may.rectangles;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Courtney on 11/11/2017.
 */
public class Rectangle
{
	public final Point btmLeftCorner;
	public final Point topLeftCorner;
	public final Point topRightCorner;
	public final Point btmRightCorner;
	public final int width;
	public final int height;
	public final LineSegment bottom;
	public final LineSegment top;
	public final LineSegment left;
	public final LineSegment right;

	public Rectangle(int minx, int miny, int maxx, int maxy)
	{
		if (maxx < minx || maxy < miny)
		{
			throw new IllegalStateException("You cannot create a rectangle that has a lower max x/y value than min x/y value");
		}
		width = maxx - minx;
		height = maxy - miny;
		btmLeftCorner = new Point(minx, miny);
		topLeftCorner = new Point(minx, maxy);
		topRightCorner = new Point(maxx, maxy);
		btmRightCorner = new Point(maxx, miny);
		bottom = new LineSegment(miny, minx, maxx);
		top = new LineSegment(maxy, minx, maxx);
		right = new LineSegment(maxx, miny, maxy);
		left = new LineSegment(minx, miny, maxy);
	}

	public Rectangle(int width, int height, Point centre)
	{
		this(centre.x - width / 2, centre.y - height / 2, centre.x + width / 2, centre.y + height / 2);
	}


	public IntersectionType getIntersectionType(Point point)
	{
		return getIntersectionType(point.x, point.y);
	}

	public IntersectionType getIntersectionType(int x, int y)
	{
		IntersectionType type = IntersectionType.NONE;
		if (insideRectangle(x, y))
		{
			if (isEdge(x, y))
			{
				if (isCorner(x, y))
				{
					type = IntersectionType.CORNER;
				} else
				{
					type = IntersectionType.EDGE;
				}
			} else
			{
				type = IntersectionType.INSIDE;
			}
		}

		return type;
	}

	public boolean isCorner(int x, int y)
	{
		return btmLeftCorner.x == x && btmLeftCorner.y == y ||
				topLeftCorner.x == x && topLeftCorner.y == y ||
				topRightCorner.x == x && topRightCorner.y == y ||
				btmRightCorner.x == x && btmRightCorner.y == y;
	}

	public boolean isEdge(int x, int y)
	{
		return btmLeftCorner.x == x || btmLeftCorner.y == y ||
				topLeftCorner.x == x || topLeftCorner.y == y ||
				topRightCorner.x == x || topRightCorner.y == y ||
				btmRightCorner.x == x || btmRightCorner.y == y;
	}

	public boolean insideRectangle(int x, int y)
	{
		return btmLeftCorner.x <= x && x <= topRightCorner.x && btmLeftCorner.y <= y && y <= topRightCorner.y;
	}

	public boolean insideRectangle(Point point)
	{
		return insideRectangle(point.x, point.y);
	}


	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public boolean insideRectangle(Rectangle rectangle)
	{
		return insideRectangle(rectangle.btmLeftCorner) &&
				insideRectangle(rectangle.topLeftCorner) &&
				insideRectangle(rectangle.topRightCorner) &&
				insideRectangle(rectangle.btmRightCorner);
	}

	public boolean isDegraded()
	{
		return width == 0 || height == 0;
	}

	public Set<Point> getCornersInsideFrom(Rectangle rectangle)
	{
		HashSet<Point> corners = new HashSet<>();
		if (this.insideRectangle(rectangle.btmLeftCorner)) corners.add(rectangle.btmLeftCorner);
		if (this.insideRectangle(rectangle.topLeftCorner)) corners.add(rectangle.topLeftCorner);
		if (this.insideRectangle(rectangle.topRightCorner)) corners.add(rectangle.topRightCorner);
		if (this.insideRectangle(rectangle.btmRightCorner)) corners.add(rectangle.btmRightCorner);

		return corners;
	}

	public Set<Point> getIntersectionsFromEdges(Rectangle other)
	{
		HashSet<Point> intersections = new HashSet<>();
		// only interested in intersections	from horizontal edges in this rectangle to vertical edges in the other,
		// and vice versa
		Point topleft = top.intersectsWithVertical(other.left);
		Point topright = top.intersectsWithVertical(other.right);
		Point bottomleft = bottom.intersectsWithVertical(other.left);
		Point bottomright = bottom.intersectsWithVertical(other.right);
		Point lefttop = left.intersectsWithHorizontal(other.top);
		Point leftbottom = left.intersectsWithHorizontal(other.bottom);
		Point righttop = right.intersectsWithHorizontal(other.top);
		Point rightbottom = right.intersectsWithHorizontal(other.bottom);

		if (topleft != null) intersections.add(topleft);
		if (topright != null) intersections.add(topright);
		if (bottomleft != null) intersections.add(bottomleft);
		if (bottomright != null) intersections.add(bottomright);
		if (lefttop != null) intersections.add(lefttop);
		if (leftbottom != null) intersections.add(leftbottom);
		if (righttop != null) intersections.add(righttop);
		if (rightbottom != null) intersections.add(rightbottom);

		return intersections;
	}

	public boolean hasIntersection(Rectangle other)
	{
		return this.insideRectangle(other) || !this.getIntersectionsFromEdges(other).isEmpty();
	}

	public Rectangle getIntersection(Rectangle other)
	{
		Rectangle intersection;
		if (other.equals(this))
		{
			// rectangles are equal, so either of them are the intersection of both
			intersection = this;
		} else
		{
			boolean otherInsideThis = this.insideRectangle(other);
			boolean thisInsideOther = other.insideRectangle(this);
			if (otherInsideThis || thisInsideOther)
			{
				// the other rectangle is a smaller rectangle inside the rectangle,
				// has no intersections on edges, or vice versa
				intersection = (otherInsideThis) ? other : this;
			} else
			{
				// part of the rectangle is partially overlapping
				// 3 cases:
				// 1: one corner of each rectangle is inside the other rectangle
				// 2: 2 corners are inside the other rectangle, or vice versa.
				// 3: 0 corners are inside and 2 edges intersect with 2 edges from the other rectangle
				Set<Point> cornersThisOther = this.getCornersInsideFrom(other);
				Set<Point> cornersOtherThis = other.getCornersInsideFrom(this);
				if (cornersThisOther.size() == 1) // no need to check both for 1
				{
					// Case 1
					// make the corners from the components of the two corners inside eachother (one from each rectangle)
					Point corner1 = cornersOtherThis.iterator().next();
					Point corner2 = cornersThisOther.iterator().next();
					int minx = Math.min(corner1.x, corner2.x);
					int maxx = Math.max(corner1.x, corner2.x);
					int miny = Math.min(corner1.y, corner2.y);
					int maxy = Math.max(corner1.y, corner2.y);
					intersection = new Rectangle(minx, miny, maxx, maxy);
				} else if (cornersThisOther.size() == 2 || cornersOtherThis.size() == 2)
				{
					// case 2
					// get the corners that are inside the rectangle, these corners are 2 corners of the intersection
					Set<Point> corners = (cornersThisOther.size() == 2) ? cornersThisOther : cornersOtherThis;
					corners.addAll(this.getIntersectionsFromEdges(other));
					intersection = RectUtils.createRectangleFromPoints(corners);
				} else
				{
					// there cant be 3 corners inside a rectangle from another rectangle, so else means it == 0 implicitly
					Set<Point> intersections = this.getIntersectionsFromEdges(other);
					intersection = (!intersections.isEmpty()) ? RectUtils.createRectangleFromPoints(intersections) : null;
				}
			}
		}

		return intersection;
	}

	public Rectangle[] decompose(Rectangle sub)
	{
		return (width < height) ? decomposeHorizontal(sub) : decomposeVertical(sub);
	}

	public Rectangle[] decomposeHorizontal(Rectangle sub)
	{
		if (!this.insideRectangle(sub))
		{
			throw new IllegalArgumentException("Tried to decompose a rectangle witch is not a subrectangle of this one.");
		}

		// 1, 2, 3, 4 corners of the rectangle from btmLeft to btm right clockwise (so btmLeft = 1, topLeft = 2,
		// topright = 3, btmright = 4), likewise 5-8 for the subrectangle in the same order, the subrectangles from
		// decompoising horizontally:
		// A = (1x, 6y), 3
		// B = (1x, 5y), 6
		// C = 8, (4x, 7y)
		// D = 1, (4x, 8y)
		Rectangle[] rectangles = new Rectangle[4];
		rectangles[0] = new Rectangle(this.btmLeftCorner.x, sub.topLeftCorner.y, topRightCorner.x, topRightCorner.y);
		rectangles[1] = new Rectangle(this.btmLeftCorner.x, sub.btmLeftCorner.y, sub.topRightCorner.x, sub.topRightCorner.y);
		rectangles[2] = new Rectangle(sub.btmRightCorner.x, sub.btmRightCorner.y, this.btmRightCorner.x, sub.topRightCorner.y);
		rectangles[3] = new Rectangle(this.btmLeftCorner.x, this.btmRightCorner.y, this.btmRightCorner.x, sub.btmRightCorner.y);
		return rectangles;
	}

	public Rectangle[] decomposeVertical(Rectangle sub)
	{
		if (!this.insideRectangle(sub))
		{
			throw new IllegalArgumentException("Tried to decompose a rectangle witch is not a subrectangle of this one.");
		}


		// 1, 2, 3, 4 corners of the rectangle from btmLeft to btm right clockwise (so btmLeft = 1, topLeft = 2,
		// topright = 3, btmright = 4), likewise 5-8 for the subrectangle in the same order, the subrectangles from
		// decompoising horizontally:
		// A = 1, (6x, 2y)
		// B = 6, (7x, 2y)
		// C = (5x, 1y), 8
		// D = (8x, 1y), 3
		Rectangle[] rectangles = new Rectangle[4];
		rectangles[0] = new Rectangle(this.btmLeftCorner.x, this.btmLeftCorner.y, sub.topLeftCorner.x, this.topLeftCorner.y);
		rectangles[1] = new Rectangle(sub.topRightCorner.x, sub.topRightCorner.y, sub.topRightCorner.x, this.topLeftCorner.y);
		rectangles[2] = new Rectangle(sub.btmLeftCorner.x, this.btmLeftCorner.y, sub.btmRightCorner.x, sub.btmRightCorner.y);
		rectangles[3] = new Rectangle(sub.btmRightCorner.x, this.btmLeftCorner.y, this.topRightCorner.x, this.topRightCorner.y);

		return rectangles;
	}

	public boolean isMergable(Rectangle other)
	{
		return width == other.width || height == other.height;
	}

	private boolean doesIntervalOverlap(int a, int b, int c, int d)
	{
		// given two intervals: a----b c----d, they overlap in two cases:
		// a--c--b--d, a <= c < b <= d ( c should not be equal to b, otherwise touching rectangles are overlapping) and
		// c--a--d--b, c <= a < d <= b
		System.out.println(a + " " + b + " " + c + " " + d);
		return (a <= c && c < b && b <= d) || (c <= a && a < d && d <= b);
	}

	public boolean doesXOverlap(Rectangle other)
	{
		return doesIntervalOverlap(this.btmLeftCorner.x, this.topRightCorner.x, other.btmLeftCorner.x, other.topRightCorner.x);

	}

	public boolean doesYOverlap(Rectangle other)
	{
		return doesIntervalOverlap(this.btmLeftCorner.y, this.topRightCorner.y, other.btmLeftCorner.y, other.topRightCorner.y);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (obj instanceof Rectangle)
		{
			Rectangle other = (Rectangle) obj;
			return 	btmLeftCorner.equals(other.btmLeftCorner) &&
					btmRightCorner.equals(other.btmRightCorner) &&
					topRightCorner.equals(other.topRightCorner) &&
					topLeftCorner.equals(other.topLeftCorner);
		}

		return false;
	}

	@Override
	public String toString()
	{
		return String.format("[%s, %s, %s, %s]", btmLeftCorner, topLeftCorner, topRightCorner, btmRightCorner);
	}

	public enum IntersectionType
	{
		EDGE,
		CORNER,
		INSIDE,
		NONE
	}
}
