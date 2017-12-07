package mx.may.rectangles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Courtney on 12/11/2017.
 */
public class LineSegmentTest
{
	LineSegment horizontalLine1;
	LineSegment horizontalLine2;
	LineSegment verticalLine1;
	LineSegment verticalLine2;

	@BeforeEach
	void setUp()
	{
		horizontalLine1 = new LineSegment(5, 0, 10);
		horizontalLine2 = new LineSegment(15, 0, 10);
		verticalLine1 = new LineSegment(8, 0, 10);
		verticalLine2 = new LineSegment(15, 0, 10);
	}

	@AfterEach
	void tearDown()
	{

	}

	@Test
	void testIntersectsWithHorizontal()
	{
		assertEquals(horizontalLine1.intersectsWithHorizontal(verticalLine1), new Point(5, 8));
		assertEquals(horizontalLine1.intersectsWithHorizontal(verticalLine2), null);
	}

	@Test
	void testIntersectsWithVertical()
	{
		assertEquals(verticalLine1.intersectsWithVertical(horizontalLine1), new Point(5, 8));
		assertEquals(verticalLine1.intersectsWithVertical(horizontalLine2), null);
	}

}
