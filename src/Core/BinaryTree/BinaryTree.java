package Core.BinaryTree;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class BinaryTree {

	private BinaryTreeNode root;

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTree(BinaryTreeNode root) {
		setRoot(root);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTree() {

	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode getRoot() {
		return root;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setRoot(BinaryTreeNode root) {
		this.root = root;
		this.root.setParent(null);
	}

}
