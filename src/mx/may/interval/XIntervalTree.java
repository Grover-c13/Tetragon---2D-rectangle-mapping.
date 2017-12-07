package mx.may.interval;

import mx.may.rectangles.Rectangle;

/**
 * Created by Courtney on 7/12/2017.
 */
public class XIntervalTree extends IntervalTree
{

	private static class XNode extends Node
	{
		private YIntervalTree yTree;
		XNode(int lower, int upper)
		{
			super(lower, upper);
			yTree = new YIntervalTree();
		}
	}

	@Override
	public Node createNode(Rectangle rec)
	{
		XNode node = new XNode(rec.btmLeftCorner.x, rec.btmRightCorner.x);
		node.yTree.insert(rec, rec.btmLeftCorner.y, rec.topRightCorner.y);
		return node;
	}

	@Override
	public boolean onIntervalEqualConflict(Node node, Rectangle rect, int lower, int upper)
	{
		// add it to the y tree
		XNode xnode = (XNode) node;
		xnode.yTree.insert(rect, rect.btmLeftCorner.y, rect.topRightCorner.y);

		return true;
	}

	@Override
	public boolean onIntervalStartConflict(Node node, Rectangle rect, int lower, int upper)
	{
		// we cant have two intervals with the same start, so split it in two.

		XNode xnode = (XNode) node;
		if (upper > node.upper)
		{
			// add the interval
			// we want to add this rectangle to the new intervals y tree
			xnode.yTree.insert(rect, rect.btmLeftCorner.y, rect.topRightCorner.y);
			this.insert(node.right, rect, node.upper, upper);
		}
		else
		{
			// change the upper of this node to the lower
			int old_upper = node.upper;
			node.upper = upper;
			// this y tree should only contain the first node, which it should.
			//xnode.yTree.insert(rect, rect.btmLeftCorner.y, rect.topRightCorner.y);
			this.insert(node.right, rect, upper, old_upper);
		}

		return true;
	}

	@Override
	public Rectangle getOverlap(Node node, Rectangle rect)
	{
		XNode xnode = (XNode) node;
		Rectangle out = null;
		if (node.containsInterval(rect.btmLeftCorner.x, rect.topRightCorner.x))
		{
			out = xnode.yTree.getAnyOverlapping(rect);
		}
		return out;
	}

	@Override
	public int getLowPoint(Rectangle rect)
	{
		return rect.btmLeftCorner.x;
	}


	public void addRectangle(Rectangle rect)
	{
		this.insert(rect, rect.btmLeftCorner.x, rect.topRightCorner.x);
	}
}
