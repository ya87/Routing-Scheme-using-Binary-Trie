//this class implements a binary trie
public class BinaryTrie<T> {
	BTNode<T> root; // root node of BT
	int numKeys; // number of keys in BT

	public BinaryTrie() {
		root = null;
		numKeys = 0;
	}

	/**
	 * create a new element node and insert it in BT
	 * 
	 * @param key
	 * @param data
	 */
	public void insert(String key, T data) {
		if (null == root) {
			root = new BTNode<T>(0);
			if ('0' == key.charAt(0)) {
				root.left = new BTNode<T>(key, data);
				root.left.parent = root;
			} else {
				root.right = new BTNode<T>(key, data);
				root.right.parent = root;
			}
		} else {
			BTNode<T> currNode = root;
			int i = 0;
			while (i < key.length()) {
				if (currNode.branchNode) {
					if ('0' == key.charAt(i)) {
						if (null == currNode.left) {
							currNode.left = new BTNode<T>(key, data);
							currNode.left.parent = currNode;
							break;
						} else {
							currNode = currNode.left;
						}
					} else {
						if (null == currNode.right) {
							currNode.right = new BTNode<T>(key, data);
							currNode.right.parent = currNode;
							break;
						} else {
							currNode = currNode.right;
						}
					}
				} else {
					if (!key.equals(currNode.key))
						splitNode(currNode, key, data, i);
					break;
				}
				i++;
			}
		}
		numKeys++;
	}

	/**
	 * converts (element) node to branch node and creates new element nodes as
	 * its children (splitting required during insert)
	 * 
	 * @param n
	 * @param key
	 * @param data
	 * @param i
	 */
	private void splitNode(BTNode<T> n, String key, T data, int i) {
		// create new element node from current node
		BTNode<T> splitNode = new BTNode<T>(n.key, n.data);
		// create new element node for the new key and data
		BTNode<T> newNode = new BTNode<T>(key, data);

		// make currNode as branch node
		n.branchNode = true;
		n.bitNum = i;
		n.key = null;
		n.data = null;

		// check where current node key and new key differ and add branch nodes
		// along the path
		while ((splitNode.key.charAt(i) == key.charAt(i)) && i < key.length()) {
			BTNode<T> newBranchNode = new BTNode<T>(i + 1);
			if ('0' == key.charAt(i)) {
				n.left = newBranchNode;
			} else {
				n.right = newBranchNode;
			}
			newBranchNode.parent = n;

			n = newBranchNode;
			i++;
		}

		if ('0' == key.charAt(i)) {
			n.left = newNode;
			n.right = splitNode;
		} else {
			n.left = splitNode;
			n.right = newNode;
		}
		newNode.parent = n;
		splitNode.parent = n;
	}

	/**
	 * removes branch nodes which have less than 2 children, this will reduce
	 * the size of BT which in turn will reduce search time
	 */
	public void compress() {
		if (numKeys > 1) {
			compress(root);
		}
	}

	private void compress(BTNode<T> n) {
		if (!n.branchNode)
			return;

		if (null == n.left || null == n.right) {
			BTNode<T> p = n.parent;
			BTNode<T> c = n.left != null ? n.left : n.right;
			if (null != p) {
				if (p.left == n) {
					p.left = c;
				} else {
					p.right = c;
				}
			} else {
				root = c;
			}
			c.parent = p;
			compress(c);
		} else {
			compress(n.left);
			compress(n.right);
		}
	}

	/**
	 * does a postorder traversal, removing subtries in which the next hop is
	 * the same for all destinations. Thus, multiple destinations having a
	 * prefix match and the same next hop will be grouped together in the BT
	 */
	public void potCompress() {
		if (numKeys > 1) {
			potCompress(root);
		}
	}

	private void potCompress(BTNode<T> n) {
		if (!n.branchNode)
			return;

		potCompress(n.right);
		potCompress(n.left);

		if (!n.right.branchNode && !n.left.branchNode) {
			// if (n.right.data.equals(n.left.data)) {
			NextHop nhRight = (NextHop) n.right.data;
			NextHop nhLeft = (NextHop) n.left.data;
			if (nhRight.id == nhLeft.id) {
				// make n as element node
				// set its key as prefix of its children keys
				// delete its children
				n.branchNode = false;
				n.data = n.left.data;
				if (n == n.parent.left) {
					n.key = (n.left.key).substring(0, n.parent.bitNum) + "0";
				} else {
					n.key = (n.left.key).substring(0, n.parent.bitNum) + "1";
				}
				n.left = null;
				n.right = null;
				numKeys--;
			}
		}
	}

	/**
	 * searches and returns node with key in BT
	 * 
	 * @param key
	 * @return
	 */
	public BTNode<T> search(String key) {
		BTNode<T> n = root;
		int i = n.bitNum;
		while (i < key.length()) {
			if (!n.branchNode)
				break;

			if ('0' == key.charAt(i)) {
				n = n.left;
			} else {
				n = n.right;
			}

			i = n.bitNum;
		}

		// System.out.println(n.key.substring(0, n.parent.bitNum+1)+" "+n.data);
		return n;
	}

	// printing as inorder traversal
	public void print() {
		if (null == root)
			System.out.println("Empty Tree !!");
		else {
			print(root);
			System.out.println();
		}
	}

	private void print(BTNode<T> node) {
		if (null == node)
			return;

		print(node.left);
		if (node.branchNode)
			System.out.print("B(" + node.bitNum + ") ");
		else
			System.out.print(node.key + "(" + node.data + ") ");
		print(node.right);
	}
}