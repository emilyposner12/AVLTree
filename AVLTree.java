package avl;

import java.util.LinkedList;

public class AVLTree<T extends Comparable<T>> {

	private TreeNode<T> root;
	public int size;
	
	public AVLTree() {
	    this.root = null;
	    this.size = 0;
	}
	
	
	public boolean exists(T value) {
	    return existsHelper(value, this.root);
	}
	
	private boolean existsHelper(T value, TreeNode<T> root) {
		if (root == null) { // not found
			return false;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found
	    		return true;
	    	} else if (comparison < 0) { // still looking - go left
	    		return existsHelper(value, root.left);
	    	} else { // still looking - go right
	    		return existsHelper(value, root.right);
	    	}
	    }
	}
	
	
	public T min() {
	    return minValueInSubtree(this.root);
	}
	

	private T minValueInSubtree(TreeNode<T> root) {
	    while (root.left != null)
	    	root = root.left;
	    
	    return root.value;
	}

	public T max() {
	    return maxValueInSubtree(this.root);
	}


	private T maxValueInSubtree(TreeNode<T> root) {
	    while (root.right != null)
	    	root = root.right;
	    
	    return root.value;
	}
	
	
	public int insert(T value) 
	{
	    this.root = insertHelper(value, this.root);
	    return size;
	}
	

	// Recursive procedure that inserts a value into the subtree rooted at "root".
  // If value is already present in the tree, nothing is inserted.
	// Returns the root node of subtree after insertion
	private TreeNode<T> insertHelper(T value,
					 TreeNode<T> root) {
		if (root == null) {
			// add new element as leaf of tree
			TreeNode<T> newNode = new TreeNode<T>(value); 
			size++;
			return newNode;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) {
	    		// duplicate element -- return existing node
	    		return root;
	    	} else if (comparison < 0) {
	    		// still looking -- go left
	    		root.setLeft(insertHelper(value, root.left));
	    	} else {
	    		// still looking -- go right
	    		root.setRight(insertHelper(value, root.right));
	    	}
	    	updateHeight(root);
	    	return rebalance(root);
	    }
	}

	// Remove a value from the set if it is present
	public void remove(T value) {
	    this.root = removeHelper(value, this.root);
	}
	

	// Recursive procedure to remove a value from the subtree rooted at "root", if it exists.
	// Returns root node of subtree after insertion
	private TreeNode<T> removeHelper(T value,
					 TreeNode<T> root) {
	    
	    if (root == null) {
	    	return null;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found element to remove
	    		if (root.left == null || root.right == null) {
	    			// base case -- root has at most one subtree,
	    			// so return whichever one is not null (or null
	    			// if both are)
	    			size--;
	    			return (root.left == null ? root.right : root.left);
	    		} else {
	    			// node with two subtrees -- replace key
	    			// with successor and recursively remove
	    			// the successor.
	    			T minValue = minValueInSubtree(root.right);
	    			root.value = minValue;
			
	    			root.setRight(removeHelper(minValue, root.right));
	    		}
	    	} 
	    	else if (comparison < 0) {
	    		// still looking for element to remove -- go left
	    		root.setLeft(removeHelper(value, root.left));
	    	} else {
	    		// still looking for element to remove -- go right
	    		root.setRight(removeHelper(value, root.right));
	    	}
	    	updateHeight(root);
	    	return rebalance(root);
	    }
	}

	
	private void updateHeight(TreeNode<T> root) {
	    int left = 0;
	    int right = 0;
	    if(root.left==null) { left = -1; }
	    else { left = root.left.height; }
	    
	    if(root.right == null) { right = -1; }
	    else { right = root.right.height; }
	    
	    int m = Math.max(right,left);
	    root.height = m + 1;
	}
	
	// Return the balance factor of a subtree rooted at "root"
	// (right subtree height - left subtree height)
	private int getBalance(TreeNode<T> root) {
	    int left = 0;
	    int right = 0;
	  
	    if(root.left==null) { left = -1; }
	    else { left = root.left.height; }
	    
	    if(root.right == null) { right = -1; }
	    else { right = root.right.height;}
	    
	    return right - left;
	}

	// Rebalance an AVL subtree, rooted at "root", that has possibly been unbalanced by a single node's insertion or deletion.
	// Returns the root of the subtree after rebalancing
	private TreeNode<T> rebalance(TreeNode<T> root) {
	    if(getBalance(root) == 2) {
	    	if(getBalance(root.right) == -1) {
	    		root.right = rightRotate(root.right);
	    	}
	    	root=leftRotate(root);
	    }
	    else if(getBalance(root) == -2) {
	    	if(getBalance(root.left) ==1) {
	    		root.left=leftRotate(root.left); 
	    		}
	    	root = rightRotate(root);
	    	}
	  
		 return root;
	}
	
	// Performs a right rotation on a tree rooted at "root"
	// The tree's root is assumed to have a left child.
 	// Returns the new root after rotation.
	private TreeNode<T> rightRotate(TreeNode<T> root) {
	   TreeNode<T> rootNew = root.left;
	   root.left = rootNew.right;
	   rootNew.right = root;
	   updateHeight(root);
	   updateHeight(rootNew);
	   
	   return rootNew;
	}

	// Performs a left rotation on a tree rooted at "root"
	// The tree's root is assumed to have a right child.
	// Returns the new root after rotation.
	private TreeNode<T> leftRotate(TreeNode<T> root) {
		TreeNode<T> rootNew = root.right;
		root.right = rootNew.left;
		rootNew.left = root;
		updateHeight(root);
		updateHeight(rootNew);
		   
		return rootNew;
	}
	
	// Return the root node of the tree (for validation only!)
	public TreeNode<T> getRoot() {
	    return this.root;
	}
	
		
	// Returns the contents of the tree as an ordered list
	public LinkedList<T> enumerate() {
	    return enumerateHelper(this.root);
	}
	
	// Enumerates the contents of the tree rooted at "root" in order as a linked list
	private LinkedList<T> enumerateHelper(TreeNode<T> root) {
	    if (root == null) 
		{
		    return new LinkedList<T>();
		}
	    else
		{
		    LinkedList<T> list = enumerateHelper(root.left);
		    list.addLast(root.value);
		    list.addAll(enumerateHelper(root.right));
		    
		    return list;
		}
	}
}
