/**
 * Node Structure
 *	Degree, Child, Data
 *	Left and Right Sibling	
 *		Used for circular doubly linked list of siblings.
 *	Parent
 *		Pointer to parent node.
 *	ChildCut
 *		True if node has lost a child since it became a child of its current parent.
 *		Set to false by remove min, which is the only operation that makes one node a child of another.
 *		Undefined for a root node.
 */
public class Node<T> {
	Node<T> parent, child, left, right;
	int key, degree;
	boolean childCut;
	T data;
	
	//initialize a node as a circular linked list
	public Node(int key, T data){
		this.key = key;
		this.degree = 0;
		this.parent = null;
		this.child = null;
		this.left = this;
		this.right = this;
		this.childCut = false;
		this.data = data;
	}

	/**
	 * joins node x with this node (represented as circular doubly linked list) into one circular doubly linked list
	 * @param x
	 */
	public void join(Node<T> x){
		Node<T> xLeft = x.left;
		Node<T> thisLeft = this.left;
		
		x.left = thisLeft;
		this.left = xLeft;
		
		xLeft.right = this;
		thisLeft.right = x;
	}
	
	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getChild() {
		return child;
	}

	public void setChild(Node<T> child) {
		this.child = child;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public boolean isChildCut() {
		return childCut;
	}

	public void setChildCut(boolean childCut) {
		this.childCut = childCut;
	}
}