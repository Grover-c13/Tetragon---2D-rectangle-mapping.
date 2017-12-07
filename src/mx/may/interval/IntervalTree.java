package mx.may.interval;

import mx.may.rectangles.Rectangle;

/**
 * Created by Courtney on 4/12/2017.
 */
public abstract class IntervalTree
{
	private Node root;

	public abstract Node createNode(Rectangle rect);
	public abstract boolean onIntervalEqualConflict(Node node, Rectangle rect, int lower, int upper);
	public abstract boolean onIntervalStartConflict(Node node, Rectangle rect, int lower, int upper);
	public abstract Rectangle getOverlap(Node node, Rectangle rect);
	public abstract int getLowPoint(Rectangle rect);

	public void insert(Rectangle rect, int lower, int upper)
	{
		root = insert(root, rect, lower, upper);
	}

	protected Node insert(Node node, Rectangle rect, int lower, int upper)
	{
		if (node == null)
		{
			return this.createNode(rect);
		}
		else
		{
			// handle duplicate lower values by spliting intervals
			if (lower == node.lower)
			{
				if (upper == node.upper)
				{
					// this is this interval
					if (!this.onIntervalEqualConflict(node, rect, lower, upper))
					{
						// the tree wants us to stop adding this rectangle due to an equal rectangle existing
						return null;
					}
				}
				else
				{
					if (!this.onIntervalStartConflict(node, rect, lower, upper))
					{
						// the tree wants us to stop adding this rectangle due to an equal start point existing
						return null;
					}
				}


			}
			else if (lower < node.lower)
			{
				node.left = insert(node.left, rect, lower, upper);
			} else
			{
				node.right = insert(node.right, rect, lower, upper);
			}


			node.height = Math.max(height(node.left), height(node.right)) + 1;
			node.max = findMax(node);

			// balance the tree
			return rebalance(node);
		}
	}


	private Node rebalance(Node node)
	{
		int heightDiff = heightDiff(node);
		if (heightDiff < -1)
		{
			if (heightDiff(node.right) > 0)
			{
				node.right = rightRotate(node.right);
				return leftRotate(node);
			}
			else
			{
				return leftRotate(node);
			}
		}
		else if (heightDiff > 1)
		{
			if (heightDiff(node.left) < 0)
			{
				node.left = leftRotate(node.left);
				return rightRotate(node);
			}
			else
			{
				return rightRotate(node);
			}
		}

		return node;
	}

	private Node leftRotate(Node n)
	{
		Node r = n.right;
		n.right = r.left;
		r.left = n;
		n.height = Math.max(height(n.left), height(n.right)) + 1;
		r.height = Math.max(height(r.left), height(r.right)) + 1;
		n.max = findMax(n);
		r.max = findMax(r);
		return r;
	}

	private Node rightRotate(Node n)
	{
		Node r = n.left;
		n.left = r.right;
		r.right = n;
		n.height = Math.max(height(n.left), height(n.right)) + 1;
		r.height = Math.max(height(r.left), height(r.right)) + 1;
		n.max = findMax(n);
		r.max = findMax(r);
		return r;
	}

	private int heightDiff(Node node)
	{
		return (node != null) ? height(node.left) - height(node.right) : 0;
	}

	private int height(Node node)
	{
		return (node != null) ? node.height : 0;
	}

	private int findMax(Node node)
	{
		int max = node.upper;

		if (node.left != null && node.right != null)
		{
			max = Math.max(node.left.max, Math.max(node.right.max, max));
		}

		if (node.left == null && node.right != null)
		{
			max = Math.max(node.right.max, max);
		}

		if (node.left != null && node.right == null)
		{
			max = Math.max(node.left.max, max);
		}

		return max;
	}


	public Rectangle getAnyOverlapping(Rectangle rect)
	{
		Node current = root;
		Rectangle overlaps = null;
		while (current != null && overlaps == null)
		{
			overlaps = this.getOverlap(current, rect);
			if (overlaps == null)
			{
				if (current.left == null)
				{
					current = current.right;
				}
				else if (current.left.max < this.getLowPoint(rect))
				{
					current = current.right;
				}
				else
				{
					current = current.left;
				}
			}
		}

		return overlaps;
	}


	public static class Node
	{
		protected Node left;
		protected Node right;
		protected int lower;
		protected int upper;
		protected int max;
		protected int height;

		public Node(int lower, int upper)
		{
			this.lower = lower;
			this.upper = upper;
		}

		public boolean containsInterval(int otherLower, int otherUpper)
		{
			// given two intervals:
			// a---b, c---d (where a and c are lower values of the two intervals, and b and d are the max)
			// they overlap in the following cases:
			// 1) a---c-b--d: a <= c < b <= d
			// 2) c---a-d--b: c <= a < d <= b
			// 3) a-c----d-b: a <= c < d <= b
			// 4) c-a----b-d: c <= a < b <= d
			// we dont want the following case since we are dealing with rectangles (otherwise it will get touching recs)
			// a---b/c---d or c---d/a---b

			return 	(lower <= otherLower && otherLower < upper && upper <= otherUpper) ||
					(otherLower <= lower && lower < otherUpper && otherUpper <= upper) ||
					(lower <= otherLower && otherLower < otherUpper && otherUpper <= upper) ||
					(otherLower <= lower && lower < upper && upper <= otherUpper);
		}

		public boolean containsValue(int value)
		{
			return lower <= value && value < upper;
		}


	}



}