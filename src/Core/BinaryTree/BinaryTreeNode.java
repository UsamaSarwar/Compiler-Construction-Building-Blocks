package Core.BinaryTree;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class BinaryTreeNode {

	private Object value;
	private BinaryTreeNode parent, leftChild, rightChild;

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode(Object value) {
		setValue(value);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode() {
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode getParent() {
		return parent;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setParent(BinaryTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode getLeftChild() {
		return leftChild;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setLeftChild(BinaryTreeNode leftChild) {
		this.leftChild = leftChild;
		this.leftChild.setParent(this);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public BinaryTreeNode getRightChild() {
		return rightChild;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setRightChild(BinaryTreeNode rightChild) {
		this.rightChild = rightChild;
		this.rightChild.setParent(this);

	}
}