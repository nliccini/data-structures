/**
 * Your implementation of a linked queue.
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class LinkedQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot dequeue "
                    + "when queue is empty.");
        } else {
            // Most General Case: Remove from the front
            LinkedNode<T> rem = head;
            head = head.getNext();
            size--;
            // Special Case: The last element was dequeued
            if (isEmpty()) {
                tail = null;
            }
            return rem.getData();
        }
    }

    @Override
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null.");
        } else if (isEmpty()) {
            // Case 1: The queue is empty
            head = new LinkedNode<T>(data);
            tail = head;
        } else {
            // Most General Case: Add to the Back
            tail.setNext(new LinkedNode<T>(data));
            tail = tail.getNext();
        }
        size++;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the head of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the head node
     */
    public LinkedNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the tail node
     */
    public LinkedNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}