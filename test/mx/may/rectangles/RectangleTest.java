package mx.may.rectangles;

import org.junit.jupiter.api.Executable;
import org.junit.jupiter.api.Test;
import org.w3c.dom.css.Rect;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Created by Courtney on 12/11/2017.
 */
public class RectangleTest
{
	Rectangle testRect1;
	Rectangle testRect2;
	@org.junit.jupiter.api.BeforeEach
	void setUp()
	{
		testRect1 = new Rectangle(0, 0, 10, 10);
		testRect2 = new Rectangle(-10, -10, 10, 10);
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown()
	{

	}


	@Test
	void getIntersectionType()
	{
		assertEquals(testRect1.getIntersectionType(0, 0), Rectangle.IntersectionType.CORNER);
		assertEquals(testRect1.getIntersectionType(0, 10), Rectangle.IntersectionType.CORNER);
		assertEquals(testRect1.getIntersectionType(10, 10), Rectangle.IntersectionType.CORNER);
		assertEquals(testRect1.getIntersectionType(10, 0), Rectangle.IntersectionType.CORNER);
		assertEquals(testRect1.getIntersectionType(5, 0), Rectangle.IntersectionType.EDGE);
		assertEquals(testRect1.getIntersectionType(0, 5), Rectangle.IntersectionType.EDGE);
		assertEquals(testRect1.getIntersectionType(10, 5), Rectangle.IntersectionType.EDGE);
		assertEquals(testRect1.getIntersectionType(5, 10), Rectangle.IntersectionType.EDGE);
		assertEquals(testRect1.getIntersectionType(5, 5), Rectangle.IntersectionType.INSIDE);
		assertEquals(testRect1.getIntersectionType(9, 9), Rectangle.IntersectionType.INSIDE);
		assertEquals(testRect1.getIntersectionType(50, 50), Rectangle.IntersectionType.NONE);
	}

	@Test
	void isCorner()
	{
		assertEquals(testRect1.isCorner(10, 10), true);
		assertEquals(testRect1.isCorner(0, 10), true);
		assertEquals(testRect1.isCorner(10, 0), true);
		assertEquals(testRect1.isCorner(0, 0), true);
		assertEquals(testRect1.isCorner(5, 0), false);
		assertEquals(testRect1.isCorner(5, 5), false);
	}

	@Test
	void isEdge()
	{
		assertEquals(testRect1.isEdge(10, 10), true);
		assertEquals(testRect1.isEdge(0, 10), true);
		assertEquals(testRect1.isEdge(10, 0), true);
		assertEquals(testRect1.isEdge(0, 0), true);
		assertEquals(testRect1.isEdge(5, 0), true);
		assertEquals(testRect1.isEdge(5, 5), false);
	}

	@Test
	void insideRectangle()
	{
		assertEquals(testRect1.insideRectangle(5, 5), true);
		assertEquals(testRect1.insideRectangle(10, 10), true);
		assertEquals(testRect1.insideRectangle(100, 10), false);
	}
	

	@Test
	void getWidth()
	{
		assertEquals(testRect1.getWidth(), 10);
		assertEquals(testRect2.getWidth(), 20);
	}

	@Test
	void getHeight()
	{
		assertEquals(testRect1.getHeight(), 10);
		assertEquals(testRect2.getHeight(), 20);
	}

	@Test
	void isDegraded()
	{
		Rectangle deg = new Rectangle(5, 5, 5, 5);
		assertEquals(deg.isDegraded(), true);
		assertEquals(testRect1.isDegraded(), false);
	}


	@Test
	void getCornersInsideFrom()
	{
		Set<Point> corners21 = testRect2.getCornersInsideFrom(testRect1);
		Set<Point> corners12 = testRect1.getCornersInsideFrom(testRect2);
		assertThat(corners12, containsInAnyOrder(new Point(10, 10)));
		assertThat(corners21, containsInAnyOrder(
				new Point(10, 0),
				new Point(10, 10),
				new Point(0, 0),
				new Point(0, 10)
		));
	}

	@Test
	void getIntersectionFromEdges()
	{
		Set<Point> intersections12 = testRect1.getIntersectionsFromEdges(testRect2);
		Set<Point> intersections21 = testRect2.getIntersectionsFromEdges(testRect1);
		assertThat(intersections12, containsInAnyOrder(
				new Point(10, 10),
				new Point(10, 0),
				new Point(0, 10)
		));
		assertThat(intersections21, containsInAnyOrder(
				new Point(10, 10),
				new Point(10, 0),
				new Point(0, 10)
		));

		Rectangle touchingRect = new Rectangle(10, 10, 20, 20);
		Set<Point> intersectionsTouching1 = touchingRect.getIntersectionsFromEdges(testRect1);

		assertThat(intersectionsTouching1, containsInAnyOrder(
				new Point(10, 10)
		));

		Rectangle notTouchingRect = new Rectangle(20, 20, 30, 30);
		assertEquals(notTouchingRect.getIntersectionsFromEdges(testRect1).isEmpty(), true);
	}

	@Test
	void getIntersection()
	{
		// Equal
		assertEquals(testRect1.getIntersection(testRect1), testRect1);

		// Inside
		Rectangle inside = new Rectangle(1, 1, 9, 9);
		Rectangle containsInside = testRect1;
		assertEquals(inside.getIntersection(containsInside), inside);
		assertEquals(containsInside.getIntersection(inside), inside);
		inside = testRect1;
		containsInside = new Rectangle(0, 0, 15, 10);
		assertEquals(inside.getIntersection(containsInside), inside);
		assertEquals(containsInside.getIntersection(inside), inside);

		// One corner
		Rectangle bottom = testRect1;
		Rectangle top =  new Rectangle(5,5, 15, 15);
		Rectangle intersection = new Rectangle(5, 5, 10, 10);
		assertEquals(bottom.getIntersection(top), intersection);
		assertEquals(top.getIntersection(bottom), intersection);

		// Two corners
		bottom = testRect1;
		top = new Rectangle(1, 9, 9, 18);
		intersection = new Rectangle(1, 9, 9, 10);
		assertEquals(bottom.getIntersection(top), intersection);
		assertEquals(top.getIntersection(bottom), intersection);

		// one corner shared
		bottom = testRect1;
		top = new Rectangle(10, 10, 20, 20);
		intersection = new Rectangle(10, 10, 10, 10);
		assertEquals(bottom.getIntersection(top), intersection);
		assertEquals(top.getIntersection(bottom), intersection);

		// one edge shared
		bottom = testRect1;
		top = new Rectangle(0, 10, 10, 20);
		intersection = new Rectangle(0, 10, 10, 10);
		assertEquals(bottom.getIntersection(top), intersection);
		assertEquals(top.getIntersection(bottom), intersection);

		// no corners inside
		Rectangle overlap1 = new Rectangle(0, 1, 10, 8);
		Rectangle overlap2 = new Rectangle(1, 0, 9, 9);
		intersection = new Rectangle(1, 1, 9, 8);
		assertEquals(overlap1.getIntersection(overlap2), intersection);
		assertEquals(overlap2.getIntersection(overlap1), intersection);

		// no intersection
		bottom = testRect1;
		top = new Rectangle(20, 20, 30, 30);
		assertEquals(bottom.getIntersection(top), null);
		assertEquals(top.getIntersection(bottom), null);
	}


	@Test
	void doesXOverlap()
	{

	}

	@Test
	void equals()
	{
		assertEquals(testRect1.equals(testRect1), true);
		assertEquals(testRect1.equals(new Rectangle(0, 0, 10, 10)), true);
		assertEquals(testRect1.equals(testRect2), false);

	}

}
