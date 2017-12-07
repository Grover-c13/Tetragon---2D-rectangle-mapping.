package mx.may.interval;

import mx.may.rectangles.Rectangle;

/**
 * Created by Courtney on 7/12/2017.
 */
public class YIntervalTree extends IntervalTree
{

	private static class YNode extends Node
	{
		private Rectangle rect;
		YNode(Rectangle rect, int lower, int upper)
		{
			super(lower, upper);
			this.rect = rect;
		}
	}

	@Override
	public Node createNode(Rectangle rect)
	{
		return new YNode(rect, rect.btmLeftCorner.y, rect.topRightCorner.y);
	}

	@Override
	public boolean onIntervalEqualConflict(Node node, Rectangle rect, int lower, int upper)
	{
		// if we get a equal conflict here, that means we have an overlapping rectangle. We dont want any overlapping
		// rectangles so return false. I dont want to use exceptions here, while it would make it more structured,
		// you would have to do an overlap query before insertion. so the run time would be 2log n, where with this way,
		// it would only be log n

		return false;
	}

	@Override
	public boolean onIntervalStartConflict(Node node, Rectangle rect, int lower, int upper)
	{
		// if we get a start conflict here, that means we have an overlapping rectangle. We dont want any overlapping
		// rectangles so return false. I dont want to use exceptions here, while it would make it more structured,
		// you would have to do an overlap query before insertion. so the run time would be 2log n, where with this way,
		// it would only be log n
		return false;
	}

	@Override
	public Rectangle getOverlap(Node node, Rectangle rect)
	{
		Rectangle out = null;
		if (node.containsInterval(rect.btmLeftCorner.y, rect.topRightCorner.y))
		{
			YNode yNode = (YNode) node;
			out = yNode.rect;
		}

		return out;
	}

	@Override
	public int getLowPoint(Rectangle rect)
	{
		return rect.btmLeftCorner.y;
	}

}
