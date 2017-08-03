import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.LinkedList;


/**
 * Your implementation of a binary search tree.
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST.
     * YOU DO NOT NEED TO IMPLEMENT THIS CONSTRUCTOR!
     */
    public BST() {
    }

    /**
     * Initializes the BST with the data in the Collection. The data in the BST
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The collection contained"
                    + " null data and couldn't be added.");
        }
        for (T item : data) {
            if (item == null) {
                throw new IllegalArgumentException("The collection contained"
                        + " null data and couldn't be added.");
            }
            add(item);
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please"
                    + " use a non-null input.");
        }
        // Most General Case:
        root = addNode(data, root);
    }

    /**
     * Recursive helper method to add a new node to the tree using pointer
     * reinforcement.
     * A successful add indicates that a location was found and there are
     * no duplicate nodes in the tree.
     *
     * @param data the data to be added
     * @param node the node that is being searched through currently
     * @return whether the data was added successfully or not
     */
    private BSTNode<T> addNode(T data, BSTNode<T> node) {
        if (node == null) {
            size++;
            return new BSTNode<>(data);
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(addNode(data, node.getRight()));
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addNode(data, node.getLeft()));
        }
        return node;
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Input data is null,"
                    + " please use a non-null input.");
        }
        BSTNode<T> dummy = new BSTNode<>(null);
        root = removeNode(root, dummy, data);
        size--;
        return dummy.getData();
    }

    /**
     * Recursive helper method to help remove a certain node from the tree
     *
     * @param  node the node being searched through currently
     * @param  dummy a dummy node to store data
     * @param  data the data being searched for and removed
     * @throws java.util.NoSuchElementException if the node is not found
     * @return the removed node
     */
    private BSTNode<T> removeNode(BSTNode<T> node, BSTNode<T> dummy, T data) {
        if (node == null) {
            throw new java.util.NoSuchElementException("The data to be removed"
                    + " does not exist in this tree. Please use a valid input "
                    + "next time.");
        }
        int compare = data.compareTo(node.getData());
        if (compare < 0) {
            node.setLeft(removeNode(node.getLeft(), dummy, data));
        } else if (compare > 0) {
            node.setRight(removeNode(node.getRight(), dummy, data));
        } else {
            dummy.setData(node.getData());
            if (isLeaf(node)) {
                return null;
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else if (node.getLeft() == null) {
                return node.getRight();
            } else {
                BSTNode<T> dummy2 = new BSTNode<>(null);
                node.setLeft(removePredecessor(node.getLeft(), dummy2));
                node.setData(dummy2.getData());
            }
        }
        return node;
    }

    /**
     * Helper method help remove the predecessor, or right most node
     * in a branch of the tree
     *
     * @param node the node being searched through currently
     * @param dummy a dummy node used to store the data from the predecessor
     * @return the node holding the largest data, or the predecessor
     */
    private BSTNode<T> removePredecessor(BSTNode<T> node, BSTNode<T> dummy) {
        if (node.getRight() == null) {
            dummy.setData(node.getData());
            return node.getLeft();
        } else {
            node.setRight(removePredecessor(node.getRight(), dummy));
            return node;
        }
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please"
                    + " use a non-null input.");
        } else {
            return getNode(data, root);
        }
    }

    /**
     * Recursive helper method to find and get a node within the tree.
     *
     * @param data the data to be returned if found
     * @param node the node that is currently being searched through
     * @throws NoSuchElementException if the input data is not found
     * @return the data from the found node (not the input data)
     */
    private T getNode(T data, BSTNode<T> node) {
        if (node != null) {
            if (data.compareTo(node.getData()) == 0) {
                // Return the data if the it was found
                return node.getData();
            } else if (data.compareTo(node.getData()) < 0) {
                // If the data is less than the node, search the left side
                return getNode(data, node.getLeft());
            } else if (data.compareTo(node.getData()) > 0) {
                // If the data is less than the node, search the right side
                return getNode(data, node.getRight());
            }
        }
        // Throw an exception if the node is not found
        throw new NoSuchElementException("The input is not contained in this"
                + " tree. Consider adding that data.");
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please"
                    + " use a non-null input.");
        } else {
            return search(data, root);
        }
    }

    /**
     * Search through the tree looking for some particular data within a
     * node.
     * A successful search indicates that a node containing the same data
     * as the one in question was found within the tree
     *
     * @param data the data being searched for
     * @param node the node being searched through
     * @return whether the node was found or not
     */
    private boolean search(T data, BSTNode<T> node) {
        if (node != null) {
            if (data.compareTo(node.getData()) == 0) {
                // Return true if the data was found
                return true;
            } else if (data.compareTo(node.getData()) < 0) {
                // If the data is less than the node, search its left side
                return search(data, node.getLeft());
            } else if (data.compareTo(node.getData()) > 0) {
                // If the data is less than the node, search its right side
                return search(data, node.getRight());
            }
        }
        // Return false if the data is never found
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<T> preorder() {
        // node, left, right; base: is leaf
        LinkedList<T> traversal = new LinkedList<>();
        return preorderTraversal(root, traversal);
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list.
     * Preorder Traversal is defined as marking Node, Node.left, Node.right in
     * that order.
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @return the list of all visited nodes
     */
    private List<T> preorderTraversal(BSTNode<T> node, List<T> traversal) {
        if (node != null && !isLeaf(node)) {
            traversal.add(node.getData());
            preorderTraversal(node.getLeft(), traversal);
            preorderTraversal(node.getRight(), traversal);
        } else if (node != null && isLeaf(node)) {
            traversal.add(node.getData());
        }
        return traversal;
    }

    @Override
    public List<T> postorder() {
        // left, right, node; base: if leaf
        LinkedList<T> traversal = new LinkedList<>();
        return postorderTraversal(root, traversal);
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list.
     * Postorder Traversal is defined as marking Node.left, Node.right, Node in
     * that order.
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @return the list of all visited nodes
     */
    private List<T> postorderTraversal(BSTNode<T> node, List<T> traversal) {
        if (node != null && !isLeaf(node)) {
            postorderTraversal(node.getLeft(), traversal);
            postorderTraversal(node.getRight(), traversal);
            traversal.add(node.getData());
        } else if (node != null && isLeaf(node)) {
            traversal.add(node.getData());
        }
        return traversal;
    }

    @Override
    public List<T> inorder() {
        // left, node, right; base: is leaf
        LinkedList<T> traversal = new LinkedList<>();
        return inorderTraversal(root, traversal);
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list.
     * Inorder Traversal is defined as marking Node.left, Node, Node.right in
     * that order.
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @return the list of all visited nodes
     */
    private List<T> inorderTraversal(BSTNode<T> node, List<T> traversal) {
        if (node != null && !isLeaf(node)) {
            inorderTraversal(node.getLeft(), traversal);
            traversal.add(node.getData());
            inorderTraversal(node.getRight(), traversal);
        } else if (node != null && isLeaf(node)) {
            traversal.add(node.getData());
        }
        return traversal;
    }

    @Override
    public List<T> findPathBetween(T data1, T data2) {
        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("Input data is null, please"
                    + " use a non-null input.");
        }
        List<T> path = new LinkedList<>();
        if (data1.equals(data2)) {
            path.add(data1);
        } else {
            BSTNode<T> dca = findCommonAncestor(data1, data2, root);
            path = findFirstLeg(data1, dca, path);
            path = findSecondLeg(data2, dca, path);
        }
        return path;
    }

    /**
     * Recursive helper method to find the first half of the path between
     * two nodes. This path is from data1 to the Deepest Common Ancestor (DCA),
     * adding all the nodes to data1 to the front of the list.
     *
     * @param data the data to which the path is made
     * @param node the node being searched through currently
     * @param path the continuously growing path to the data
     * @throws NoSuchElementException if the data is not found in the tree
     * @return the list of all nodes between the DCA and the data, excluding
     * the DCA, as it will be entered in during the second half
     */
    private List<T> findFirstLeg(T data, BSTNode<T> node, List<T> path) {
        // Traverse to data1 adding nodes along the way to the front
        if (node != null) {
            if (data.compareTo(node.getData()) == 0) {
                ((LinkedList<T>) path).addFirst(node.getData());
                // Since inevitably the DCA was added, remove it since
                // it will also be added in the second leg - O(1)
                ((LinkedList<T>) path).removeLast();
                // This is the end of the search
                return path;
            } else if (data.compareTo(node.getData()) > 0) {
                ((LinkedList<T>) path).addFirst(node.getData());
                return findFirstLeg(data, node.getRight(), path);
            } else {
                ((LinkedList<T>) path).addFirst(node.getData());
                return findFirstLeg(data, node.getLeft(), path);
            }
        }
        throw new NoSuchElementException("One or more of the given inputs"
                + " is not contained in this tree.");
    }

    /**
     * Recursive helper method to find the second half of the path between
     * two nodes. This path is from data2 to the Deepest Common Ancestor (DCA),
     * adding all the nodes to data2 to the back of the list.
     *
     * @param data the data to which the path is made
     * @param node the node being searched through currently
     * @param path the continuously growing path to the data
     * @throws NoSuchElementException if the data is not found in the tree
     * @return the list of all nodes between the DCA and the data, including
     * the DCA
     */
    private List<T> findSecondLeg(T data, BSTNode<T> node, List<T> path) {
        // Traverse to data2 adding nodes along the way to the back
        if (node != null) {
            if (data.compareTo(node.getData()) == 0) {
                ((LinkedList<T>) path).addLast(node.getData());
                // This is the end of the search
                return path;
            } else if (data.compareTo(node.getData()) > 0) {
                ((LinkedList<T>) path).addLast(node.getData());
                return findSecondLeg(data, node.getRight(), path);
            } else {
                ((LinkedList<T>) path).addLast(node.getData());
                return findSecondLeg(data, node.getLeft(), path);
            }
        }
        throw new NoSuchElementException("One or more of the given inputs"
                + " is not contained in this tree.");
    }

    /**
     * Recursive helper method to determine the Deepest Common Ancestor (DCA)
     * of data1 and data2.
     * The DCA is defined as the lowest node in the tree that possesses both
     * data1 and data2 as children in its subtree.
     *
     * @param data1 the first data input (can be less than data2)
     * @param data2 the second data input (can be less than data1)
     * @param node the node being searched through currently
     * @return the node for the Deepest Common Ancestor
     */
    private BSTNode<T> findCommonAncestor(T data1, T data2, BSTNode<T> node) {
        // Traverse to DCA once
        if (data1.compareTo(data2) < 0) {
            // d1 < DCA < d2
            if (node.getData().compareTo(data1) >= 0
                    && node.getData().compareTo(data2) <= 0) {
                // DCA found
                return node;
            } else if (node.getData().compareTo(data1) < 0) {
                return findCommonAncestor(data1, data2, node.getRight());
            } else {
                return findCommonAncestor(data1, data2, node.getLeft());
            }
        } else {
            // d2 < DCA < d1
            if (node.getData().compareTo(data2) >= 0
                    && node.getData().compareTo(data1) <= 0) {
                // DCA found
                return node;
            } else if (node.getData().compareTo(data2) < 0) {
                return findCommonAncestor(data1, data2, node.getRight());
            } else {
                return findCommonAncestor(data1, data2, node.getLeft());
            }
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int height() {
        if (isEmpty()) {
            return -1;
        } else {
            return nodeHeight(root);
        }
    }

    /**
     * Recursive helper method for determining the height of any node.
     * A node's height is defined as {@code max(left.height, right.height) + 1}.
     * A leaf node has a height of 0. Calculated in O(n).
     *
     * @param node the node in question
     * @return the height of that node
     */
    private int nodeHeight(BSTNode<T> node) {
        if (node == null || isLeaf(node)) {
            return 0;
        } else {
            return Math.max(nodeHeight(node.getLeft()),
                    nodeHeight(node.getRight())) + 1;
        }
    }

    /**
     * Determines whether the BST is empty, which is defined as the tree
     * having a size of 0.
     *
     * @return whether the BST is empty
     */
    private boolean isEmpty() {
        return size == 0;
    }

    /**
     * Determines whether then input node is an internal or external node.
     * An external node, or leaf, is defined as having no children; that is,
     * the input node has neither a left nor a right child.
     *
     * @param node the node in question
     * @return whether the input node is a leaf or not
     */
    private boolean isLeaf(BSTNode<T> node) {
        return node.getLeft() == null && node.getRight() == null;
    }

    @Override
    public BSTNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}
