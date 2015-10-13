/**
 * Node 
 * 	Branch Node 
 * 		left child 
 * 		right child 
 * 	Element Node 
 * 		data
 */
public class BTNode<T> {
	boolean branchNode;
	int bitNum; // which bit number to compare at branch node (starts at 0)
	BTNode<T> parent, left, right;
	String key;
	T data;

	/*
	 * BTNode() { this.branchNode = true; }
	 */

	/**
	 * creates a branch node with index of key compared at that node as bitNum
	 * 
	 * @param bitNum
	 */
	BTNode(int bitNum) {
		this.branchNode = true;
		this.bitNum = bitNum;
	}

	/**
	 * creates an element node
	 * 
	 * @param key
	 * @param data
	 */
	BTNode(String key, T data) {
		this.branchNode = false;
		this.key = key;
		this.data = data;
	}

	@Override
	public String toString() {
		if (!branchNode)
			return key + ":" + data;
		else
			return "B";
	}
}