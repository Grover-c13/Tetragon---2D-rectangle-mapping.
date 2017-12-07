package mx.may.rectangles;

/**
 * Created by Courtney on 1/12/2017.
 */
public class Point
{
	public final int x;
	public final int y;

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (obj instanceof Point)
		{
			Point other = (Point) obj;
			return other.x == x && other.y == y;
		}

		return false;
	}

	@Override
	public String toString()
	{
		return String.format("(%d, %d)", x, y);
	}

	@Override
	public int hashCode()
	{
		return 31*(31+x)+y;
	}
}
