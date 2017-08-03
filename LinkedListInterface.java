/**
 * This interface describes the public methods needed for SinglyLinkedList,
 * which should be singly-linked and should have both a head and a tail pointer.
 *
 * The expected Big-O for each method has been given to you.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public interface LinkedListInterface<T extends Comparable<? super T>> {

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data The data for the new element.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    void addToFront(T data);

    /**
     * Adds the element to the index specified.
     *
     * Adding to indices 0 and {@code size} should be O(1), all other cases are
     * O(n).
     *
     * @param index The requested index for the new element.
     * @param data The data for the new element.
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index > size.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    void addAtIndex(T data, int index);

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data The data for the new element.
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    void addToBack(T data);

    /**
     * Removes the node and returns the data at the front of the list. If the
     * list is empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return The object formerly located at the front.
     */
    T removeFromFront();

    /**
     * Removes the node and returns the data from the index specified.
     *
     * Removing from index 0 should be O(1), all other cases are O(n).
     *
     * @param index The requested index to be removed.
     * @return The object formerly located at index.
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index >= size.
     */
    T removeAtIndex(int index);

    /**
     * Removes the node and returns the data at the back of the list. If the
     * list is empty, return {@code null}.
     *
     * Must be O(n) for all cases.
     *
     * @return The object formerly located at the back.
     */
    T removeFromBack();

    /**
     * Returns the element at the specified index.
     *
     * Getting from indices 0 and {@code size - 1} should be O(1), all other
     * cases
     * are O(n).
     *
     * @param index The index of the requested element.
     * @return The object stored at index.
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or
     * index >= size.
     */
    T get(int index);

    /**
     * Returns what is considered the largest element in the LinkedList. If
     * the list is empty, return {@code null}. Should use the Comparable
     * interface's {@code compareTo(Object o)} method. See the Java API for
     * more details on how to it is used.
     *
     * Must be O(n) for all cases
     *
     * @return The largest element in the list
     */
    T findLargestElement();

    /**
     * Returns the number of elements in the list.
     *
     * Must be O(1) for all cases.
     *
     * @return The size of the list.
     */
    int size();

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return An array of length {@code size} holding all of the objects in
     * this list in the same order.
     */
    Object[] toArray();

    /**
     * Clears the list of all data.
     *
     * Must be O(1) for all cases.
     */
    void clear();

    /**
     * Returns a boolean value indicating if the list is empty.
     *
     * Must be O(1) for all cases.
     *
     * @return true if empty; false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the head node of the linked list.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return Node at the head of the linked list.
     */
    SLLNode<T> getHead();

    /**
     * Returns the tail node of the linked list.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return Node at the tail of the linked list.
     */
    SLLNode<T> getTail();

    /**
     * Updates the linked list by removing all nodes (if they are Integers) that
     * are even
     *
     * This was an exam question
     *
     * @return whether or not this method updated the list
     */
    boolean removeEvens();
}
