/**
 * Your implementation of an array-backed stack.
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class ArrayStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayStack.
     */
    public ArrayStack() {
        backingArray = ((T[]) new Object[INITIAL_CAPACITY]);
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Pop from the stack.
     *
     * Do not shrink the backing array.
     *
     * @see StackInterface#pop()
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot pop "
                    + "when stack is empty.");
        } else {
            // Grab (size - 1) elt
            T rem = backingArray[size - 1];
            // Make that slot null and decrement size
            backingArray[size - 1] = null;
            size--;
            // Return the elt
            return rem;
        }
    }

    /**
     * Push the given data onto the stack.
     *
     * If sufficient space is not available in the backing array, you should
     * regrow it to (double the current length) + 1; in essence, 2n + 1, where n
     * is the current capacity.
     *
     * @see StackInterface#push(T)
     */
    @Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data is null.");
        } else if (size + 1 > backingArray.length) {
            // Case 1: Array is full; Resize and recopy the array
            T[] newArray = ((T[]) new Object[(2 * backingArray.length) + 1]);
            for (int i = 0; i < size; i++) {
                newArray[i] = backingArray[i];
            }
            backingArray = newArray;
        }
        // Most General Case: (or after resizing)
        backingArray[size] = data;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the backing array of this stack.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the backing array
     */
    public Object[] getBackingArray() {
        // DO NOT MODIFY!
        return backingArray;
    }
}