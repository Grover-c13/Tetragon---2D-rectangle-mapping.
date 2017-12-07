package mx.may.rectangles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Courtney on 1/12/2017.
 */
public class PointTest
{
	@Test
	void equals()
	{
		Point p1 = new Point(10, 10);
		Point p2 = new Point(10, 10);
		Point p3 = new Point(0, 0);
		assertEquals(p1.equals(p2), true);
		assertEquals(p1.equals(p1), true);
		assertEquals(p2.equals(p1), true);
		assertEquals(p1.equals(p3), false);
		assertEquals(p3.equals(p1), false);
	}
}
