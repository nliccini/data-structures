/**
 * Your implementation of a linked stack.
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class LinkedStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private int size;

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot pop "
                    + "when stack is empty.");
        } else {
            // Most General Case: Remove from the front
            LinkedNode<T> rem = head;
            head = head.getNext();
            size--;
            return rem.getData();
        }
    }

    @Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null.");
        } else {
            // Most General Case: Add to the front
            head = new LinkedNode<T>(data, head);
            size++;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the head of this stack.
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
}