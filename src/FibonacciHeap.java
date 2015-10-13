import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//Min Fibonacci Heap
public class FibonacciHeap<T> {

	// minimum node of the fibonacci heap
	// (pointer to the root of a tree containing minimum key)
	private Node<T> min;
	private int numNodes;

	public FibonacciHeap() {
		min = null;
		numNodes = 0;
	}

	public boolean isEmpty() {
		return (null == min);
	}

	/**
	 * Inserts key x in fibonacci heap
	 * 
	 * @param key: key to be inserted
	 */
	public void insert(int key, T data) {
		// create a node as a circular linked list
		Node<T> x = new Node<T>(key, data);

		if (null == min) {
			min = x;
		} else {
			min.join(x);
			if (key < min.key) {
				min = x;
			}
		}

		numNodes += 1;
	}

	/**
	 * Inserts node x in fibonacci heap
	 * 
	 * @param x: node to be inserted
	 */
	public void insert(Node<T> x) {

		if (null == min) {
			min = x;
		} else {
			min.join(x);
			if (x.key < min.key) {
				min = x;
			}
		}

		numNodes += 1;
	}

	// public void remove(){}

	/**
	 * removes and returns the node with minimum value of key from FH
	 * 
	 * @return
	 */
	public Node<T> removeMin() {
		Node<T> z = min;

		if (null != z) {
			if (null != z.child) {
				Node<T> zChild = min.child;
				do {
					zChild.parent = null;
					zChild = zChild.left;
				} while (z.child != zChild);

				min.join(zChild);
			}
			// removing z from the root list
			if (z.left == z) {
				min = null;
			} else {
				z.left.right = z.right;
				z.right.left = z.left;
				min = z.left;

				pairwiseCombine();
			}
			numNodes -= 1;
		}

		return z;
	}

	/**
	 * if newKey is less than current key of node then update key of node to
	 * newKey, cuts node if newKey is less than key of parent node this node and
	 * add it to root list of FH and update the min pointer if required
	 * 
	 * @param x
	 * @param newKey
	 */
	public void decreaseKey(Node<T> x, int newKey) {
		if (newKey > x.key) {
			System.out.println("Error: new key is greater than current key");
			System.exit(-1);
		}

		x.key = newKey;
		Node<T> y = x.parent;

		if (null != y && x.key < y.key) {
			cut(x, y);
			cascadingCut(y);
		}

		if (x.key < min.key) {
			min = x;
		}
	}

	/**
	 * removes subtree rooted at x from the child list of y and add it to the
	 * root list of FH
	 * 
	 * @param x
	 * @param y: parent of x
	 */
	private void cut(Node<T> x, Node<T> y) {
		if (x.left == x) {
			y.child = null;
		} else {
			x.left.right = x.right;
			x.right.left = x.left;
			if (x == y.child) {
				y.child = x.left;
			}
		}
		y.degree--;
		x.parent = null;
		x.childCut = false;

		// add x to the root list of FH
		x.left = x;
		x.right = x;
		min.join(x);
	}

	/**
	 * if childCut is true for node y then do a cut at node y and cascading cut
	 * at its parent node otherwise set childCut of y to true
	 * 
	 * @param y
	 */
	private void cascadingCut(Node<T> y) {
		Node<T> z = y.parent;
		if (null != z) {
			if (!y.childCut) {
				y.childCut = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}

	/**
	 * combine trees of same degree in the root list into one after a removeMin
	 * operation and update min pointer if required
	 */
	private void pairwiseCombine() {
		// maxDegree <= floor(log n) with base (1+sqrt(5))/2)
		int maxDegree = (int) Math.floor(Math.log(numNodes)
				/ Math.log((1 + Math.sqrt(5)) / 2));

		@SuppressWarnings("unchecked")
		Node<T>[] lookup = (Node<T>[]) Array.newInstance((Node.class),
				maxDegree + 1);

		List<Node<T>> toVisit = new ArrayList<Node<T>>();
		Node<T> z = min;
		do {
			toVisit.add(z);
			z = z.left;
		} while (z != min);

		for (Node<T> x : toVisit) {
			int d = x.degree;
			while (null != lookup[d]) {
				Node<T> y = lookup[d];
				if (x.key > y.key) {
					// swap(x, y);
					Node<T> temp = x;
					x = y;
					y = temp;
				}
				link(x, y);
				lookup[d] = null;
				d = d + 1;
				x.degree = d;
			}
			lookup[d] = x;
		}

		min = null;
		for (int i = 0; i <= maxDegree; i++) {
			if (null != lookup[i]) {
				Node<T> x = lookup[i];
				x.left = x;
				x.right = x;
				// add lookup[i] to root list of FH
				if (null == min) {
					min = x;
				} else {
					min.join(x);
					// update min pointer if required
					if (min.key > x.key) {
						min = x;
					}
				}
			}
		}
	}

	/**
	 * link y to x, remove y from the node list and make y a child of x (key[x]
	 * < key[y]), increment degree of x, set childCut of y to false
	 * 
	 * @param x
	 * @param y
	 */
	private void link(Node<T> x, Node<T> y) {
		// remove y from root list of FH
		y.left.right = y.right;
		y.right.left = y.left;

		// making y as a single node circular doubly linked list
		y.left = y;
		y.right = y;
		// add y as a child of x
		if (null == x.child) {
			// if x has no child then x's child pointer will point to y
			x.child = y;
		} else {
			// y will join the circular doubly linked list of x's children
			y.join(x.child);
		}
		y.parent = x;
		y.childCut = false;
	}

	public void print() {
		print(min);
	}

	private void print(Node<T> z) {
		if (null == z)
			return;

		Node<T> x = z;
		do {
			System.out.print(x.key + "<->");
			x = x.left;
		} while (x != z);
		System.out.println();

		x = z;
		do {
			if (null != x.child)
				print(x.child);
			x = x.left;
		} while (x != z);
	}
}
