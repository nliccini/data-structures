import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Input collection is null, "
                    + "please use a valid argument next time.");
        }
        for (T item : data) {
            if (item == null) {
                throw new IllegalArgumentException("Collection contains null "
                        + "data, please use a valid argument next time.");
            }
            add(item);
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please "
                    + "use a valid argument next time.");
        }
        root = addNode(data, root);
    }

    /**
     * Private recursive helper method to add a node to the AVL tree,
     * recalculating heights and rebalancing the tree as necessary.
     *
     * @param data the data to add to the AVL tree
     * @param node the node currently being searched through
     * @return the updated node if the data was added successfully
     */
    private AVLNode<T> addNode(T data, AVLNode<T> node) {
        if (node == null) {
            size++;
            AVLNode<T> newNode = new AVLNode<>(data);
            updateHeightsAndBalances(newNode);
            return newNode;
        } else {
            int compare = data.compareTo(node.getData());
            if (compare > 0) {
                node.setRight(addNode(data, node.getRight()));
            } else if (compare < 0) {
                node.setLeft(addNode(data, node.getLeft()));
            }
            // Update node height and balance factor
            updateHeightsAndBalances(node);
            // Rotate as necessary
            return rotateTree(node);
        }
    }

    /**
     * Helper method to rotate the node and its subtree to the left.
     *
     * @param a the parent node to be rotated
     * @return the updated subtree root
     */
    private AVLNode<T> rotateLeft(AVLNode<T> a) {
        AVLNode<T> b = a.getRight();
        a.setRight(b.getLeft());
        b.setLeft(a);
        updateHeightsAndBalances(a);
        updateHeightsAndBalances(b);
        return b;
    }

    /**
     * Helper method to rotate the node and its subtree to the right.
     *
     * @param a the parent node to be rotated
     * @return the updated subtree root
     */
    private AVLNode<T> rotateRight(AVLNode<T> a) {
        AVLNode<T> b = a.getLeft();
        a.setLeft(b.getRight());
        b.setRight(a);
        updateHeightsAndBalances(a);
        updateHeightsAndBalances(b);
        return b;
    }

    /**
     * Helper method to determine if a node is unbalanced, and if so, to
     * properly rotate the tree as given by the four scenarios:
     * 1. Node is left-heavy with right-heavy child
     * 2. Node is left-heavy with balanced child
     * 3. Node is right-heavy with left-heavy child
     * 4. Node is right-heavy with balanced child
     *
     * @param node the node in question, and possibly the unbalanced node
     * @return the updated node and its appropriate subtree references
     */
    private AVLNode<T> rotateTree(AVLNode<T> node) {
        if (node.getBalanceFactor() == 2) {
            // Left heavy child
            if (node.getLeft().getBalanceFactor() == -1) {
                // double left-right rotation
                node.setLeft(rotateLeft(node.getLeft()));
                node = rotateRight(node);
            } else if (node.getLeft().getBalanceFactor() == 0
                    || node.getLeft().getBalanceFactor() == 1) {
                // single right rotation
                node = rotateRight(node);
            }
        } else if (node.getBalanceFactor() == -2) {
            // Right heavy child
            if (node.getRight().getBalanceFactor() == 1) {
                // double right-left rotation
                node.setRight(rotateRight(node.getRight()));
                node = rotateLeft(node);
            } else if (node.getRight().getBalanceFactor() == 0
                    || node.getRight().getBalanceFactor() == -1) {
                // single left rotation
                node = rotateLeft(node);
            }
        }
        return node;
    }

    /**
     * Helper method to update the height and balance factor of
     * the input node.
     *
     * Height is defined as {@code max(left.height, right.height) + 1}.
     * A leaf node has a height of 0.
     * A null node has a height of -1.
     *
     * Balance factor is defined as left.height - right.height.
     * A leaf node has a balance factor of 0 given by (-1 - (-1))
     *
     * @param node the node currently being updated
     * @return the height of the current node, or -1 if the node is null
     */
    private void updateHeightsAndBalances(AVLNode<T> node) {
        if (node != null) {
            if (isLeaf(node)) {
                node.setHeight(0);
                node.setBalanceFactor(0);
            } else {
                // Update height
                int height;
                if (node.getRight() != null && node.getLeft() == null) {
                    height = node.getRight().getHeight()  + 1;
                } else if (node.getLeft() != null && node.getRight() == null) {
                    height = node.getLeft().getHeight()  + 1;
                } else {
                    height = Math.max(node.getLeft().getHeight(),
                            node.getRight().getHeight())  + 1;
                }
                node.setHeight(height);
                // Update balance factor
                int bf;
                if (node.getRight() != null && node.getLeft() == null) {
                    bf = -1 - node.getRight().getHeight();
                } else if (node.getLeft() != null && node.getRight() == null) {
                    bf = node.getLeft().getHeight() - (-1);
                } else {
                    bf = node.getLeft().getHeight()
                            - node.getRight().getHeight();
                }
                node.setBalanceFactor(bf);
            }
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please "
                    + "use a valid argument next time.");
        }
        AVLNode<T> dummy = new AVLNode<>(null);
        root = removeNode(root, dummy, data);
        size--;
        return dummy.getData();
    }

    /**
     * Private recursive helper method to remove a node from the AVL tree,
     * recalculating heights and rebalancing the tree as necessary.
     *
     * @param data the data to remove from the AVL tree
     * @param node the node currently being searched through
     * @param dummy a dummy node used to store data for removal
     * @throws java.util.NoSuchElementException if the data is not in the tree
     * @return the updated node if the data was removed successfully
     */
    private AVLNode<T> removeNode(AVLNode<T> node, AVLNode<T> dummy, T data) {
        if (node == null) {
            throw new java.util.NoSuchElementException("The data " + data
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
                AVLNode<T> dummy2 = new AVLNode<>(null);
                node.setRight(removeSuccessor(node.getRight(), dummy2));
                node.setData(dummy2.getData());
            }
        }
        // Update node height and balance factor
        updateHeightsAndBalances(node);
        // Rotate as necessary
        return rotateTree(node);
    }

    /**
     * Helper method help remove the successor, or left most node
     * in a right branch of the tree
     *
     * @param node the node being searched through currently
     * @param dummy a dummy node used to store the data from the successor
     * @return the node holding the largest data, or the successor
     */
    private AVLNode<T> removeSuccessor(AVLNode<T> node, AVLNode<T> dummy) {
        if (node.getLeft() == null) {
            dummy.setData(node.getData());
            // If it has a right child, replace it with that, if the child
            // is null then it all works out fine too
            return node.getRight();
        } else {
            node.setLeft(removeSuccessor(node.getLeft(), dummy));
            updateHeightsAndBalances(node);
            return rotateTree(node);
        }
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please "
                    + "use a valid argument next time.");
        } else {
            return getNode(data, root);
        }
    }

    /**
     * Recursive helper method to find and get a node within the tree.
     *
     * @param data the data to be returned if found
     * @param node the node that is currently being searched through
     * @throws java.util.NoSuchElementException if the input data is not found
     * @return the data from the found node (not the input data)
     */
    private T getNode(T data, AVLNode<T> node) {
        if (node != null) {
            int compare = data.compareTo(node.getData());
            if (compare == 0) {
                // Return the data if the it was found
                return node.getData();
            } else if (compare < 0) {
                // If the data is less than the node, search the left side
                return getNode(data, node.getLeft());
            } else if (compare > 0) {
                // If the data is less than the node, search the right side
                return getNode(data, node.getRight());
            }
        }
        // Throw an exception if the node is not found
        throw new java.util.NoSuchElementException("The data " + data
                + " is not contained in this tree. Consider adding that data.");
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null, please "
                    + "use a valid argument next time.");
        }
        return search(data, root);
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
    private boolean search(T data, AVLNode<T> node) {
        if (node != null) {
            int compare = data.compareTo(node.getData());
            if (compare == 0) {
                return true;
            } else if (compare > 0) {
                return search(data, node.getRight());
            } else if (compare < 0) {
                return search(data, node.getLeft());
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<T> preorder() {
        if (isEmpty()) {
            return new LinkedList<>();
        } else {
            List<T> traversal = new LinkedList<>();
            return preorderTraversal(root, traversal);
        }
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list.
     *
     * Preorder Traversal is defined as marking Node, Node.left, Node.right in
     * that order.
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @return the list of all visited nodes
     */
    private List<T> preorderTraversal(AVLNode<T> node, List<T> traversal) {
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
        if (isEmpty()) {
            return new LinkedList<>();
        } else {
            List<T> traversal = new LinkedList<>();
            return postorderTraversal(root, traversal);
        }
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list.
     *
     * Postorder Traversal is defined as marking Node.left, Node.right, Node in
     * that order.
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @return the list of all visited nodes
     */
    private List<T> postorderTraversal(AVLNode<T> node, List<T> traversal) {
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
    public Set<T> threshold(T lower, T upper) {
        if (lower == null || upper == null) {
            throw new IllegalArgumentException("Input argument is null, please"
                    + " use valid bounds next time.");
        }
        Set<T> set = new java.util.HashSet<>();
        return thresholdSearch(lower, upper, root, set);
    }

    /**
     * Private recursive helper method to traverse through the AVL tree, adding
     * nodes' data to a set if they are within the threshold. The data are
     * added using an inorder traversal
     *
     * Applicable nodes are defined as having data such that
     * lower < node.data < upper
     *
     * @param lower The lower bound of the threshold
     * @param upper The upper bound of the threshold
     * @param node The node being traversed through currently
     * @param set The set that is continuously added to
     * @return a set of elements that are within the threshold
     */
    private Set<T> thresholdSearch(T lower, T upper, AVLNode<T> node,
                                   Set<T> set) {
        if (node != null) {
            if (node.getLeft() != null
                    && node.getLeft().getData().compareTo(lower) > 0) {
                thresholdSearch(lower, upper, node.getLeft(), set);
            }
            if (node.getData().compareTo(lower) > 0
                    && node.getData().compareTo(upper) < 0) {
                set.add(node.getData());
            }
            if (node.getRight() != null
                    && node.getRight().getData().compareTo(upper) < 0) {
                thresholdSearch(lower, upper, node.getRight(), set);
            }
        }
        return set;
    }

    @Override
    public List<T> levelorder() {
        List<T> traversal = new LinkedList<>();
        List<AVLNode<T>> queue = new LinkedList<>();
        return levelorderTraversal(root, traversal, queue);
    }

    /**
     * Recursive helper method to traverse through the tree, saving each
     * particular node to a list with the help of a queue.
     *
     * Levelorder Traversal is defined as marking all the nodes at a single
     * level before moving down to their children
     *
     * @param node the node being traversed through currently
     * @param traversal the list that the nodes are recorded in
     * @param queue the queue used to temporarily add nodes to the list
     * @return the list of all visited nodes
     */
    private List<T> levelorderTraversal(AVLNode<T> node, List<T> traversal,
                                        List<AVLNode<T>> queue) {
        if (node != null) {
            ((LinkedList<AVLNode<T>>) queue).addLast(node);
            while (!queue.isEmpty()) {
                AVLNode<T> temp = ((LinkedList<AVLNode<T>>) queue).removeFirst();
                ((LinkedList<T>) traversal).addLast(temp.getData());
                if (temp.getLeft() != null) {
                    ((LinkedList<AVLNode<T>>) queue).addLast(temp.getLeft());
                }
                if (temp.getRight() != null) {
                    ((LinkedList<AVLNode<T>>) queue).addLast(temp.getRight());
                }
            }
        }
        return traversal;
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
        }
        return root.getHeight();
    }

    @Override
    public AVLNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }

    /**
     * Determines whether the AVL Tree is empty, which is defined as the tree
     * having a size of 0.
     *
     * @return whether the AVL Tree is empty
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
    private boolean isLeaf(AVLNode<T> node) {
        return node.getLeft() == null && node.getRight() == null;
    }
}