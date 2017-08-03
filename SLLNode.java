/**
 * Node class used for implementing your SinglyLinkedList.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class SLLNode<T extends Comparable<? super T>> {
    private T data;
    private SLLNode<T> next;

    /**
     * Creates a new SLLNode with the given T object and node reference.
     *
     * @param data The data stored in the new node.
     * @param next The next node in the list.
     */
    public SLLNode(T data, SLLNode<T> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Creates a new SLLNode with only the given T object.
     *
     * @param data The data stored in the new node.
     */
    public SLLNode(T data) {
        this(data, null);
    }

    /**
     * Gets the data stored in the node.
     *
     * @return The data in this node.
     */
    public T getData() {
        return data;
    }

    /**
     * Gets the next node.
     *
     * @return The next node.
     */
    public SLLNode<T> getNext() {
        return next;
    }

    /**
     * Set the next node.
     *
     * @param next The new next node.
     */
    public void setNext(SLLNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        // FOR TESTING PURPOSES ONLY! Failure to follow this rules will
        // result in a penalty.
        return "Node containing: " + data;
    }
}
