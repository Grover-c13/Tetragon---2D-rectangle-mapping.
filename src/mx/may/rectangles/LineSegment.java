package mx.may.rectangles;

/**
 * Created by Courtney on 28/11/2017.
 */
public class LineSegment
{
	public final int offset;
	public final int min;
	public final int max;

	public LineSegment(int offset, int min, int max)
	{
		this.min = min;
		this.max = max;
		this.offset = offset;
	}

	public Point intersectsWithHorizontal(LineSegment horizontal)
	{
		Point point = null;
		// check that the offset of this line (the vertical line) is less than the horizontal lines max and min
		if (this.offset <= horizontal.max && this.offset >= horizontal.min)
		{
			// check that the offset of the horizontal line is less than this line (the vertical line) max and min
			if (horizontal.offset <= this.max && horizontal.offset >= this.min)
			{
				// since the points are between the limits of the line segment, return a point
				// that is the horizontal as the y value, and the x value of this vertical line
				point = new Point(this.offset, horizontal.offset);
			}
		}
		return point;
	}

	public Point intersectsWithVertical(LineSegment vertical)
	{
		Point point = null;
		if (this.offset <= vertical.max && this.offset >= vertical.min)
		{
			if (vertical.offset <= this.max && vertical.offset >= this.min)
			{
				point = new Point(vertical.offset, this.offset);
			}
		}
		return point;
	}

	public boolean unboundedEqual(LineSegment line)
	{
		return line.offset == offset;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else
		{
			if (obj instanceof LineSegment)
			{
				LineSegment other = (LineSegment) obj;
				return other.offset == offset && other.max == max && other.min == min;
			}
		}
		return false;
	}
}
