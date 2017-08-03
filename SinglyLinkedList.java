import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * Your implementation of a SinglyLinkedList
 *
 * @author Nick Liccini
 * @version 1.0
 */
public class SinglyLinkedList<T extends Comparable<? super T>> implements
        LinkedListInterface<T> {
    // Do not add new instance variables.
    private SLLNode<T> head;
    private SLLNode<T> tail;
    private int size;

    @Override
    public void addToFront(T data) {
        // Check for exceptions in inputs
        if (data == null) {
            throw new IllegalArgumentException("Input data is null");
        } else {
            SLLNode<T> node = new SLLNode<T>(data);
            // Case 1: The list is empty
            if (isEmpty()) {
                head = node;
                tail = head;
                size++;
                return;
            }
            // Most General Case:
            node.setNext(head);
            head = node;
            size++;
        }
    }

    @Override
    public void addAtIndex(T data, int index) {
        // Check for exceptions in inputs
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is not within the "
                + "size of the list: " + 0 + " to " + size);
        } else if (data == null) {
            throw new IllegalArgumentException("Input data is null");
        } else {
            SLLNode<T> node = new SLLNode<T>(data);
            if (index == 0) {
                // Case 1: index == 0 (addToFront)
                addToFront(data);
            } else if (index == size) {
                // Case 2: index == size (addToBack)
                addToBack(data);
            } else {
                // Most General Case: Add in middle
                SLLNode<T> prev = head;
                for (int i = 1; i < index; i++) {
                    prev = prev.getNext();
                }
                node.setNext(prev.getNext());
                prev.setNext(node);
                size++;
            }
        }
    }

    @Override
    public void addToBack(T data) {
        // Check for exceptions in inputs
        if (data == null) {
            throw new IllegalArgumentException("Input data is null");
        } else {
            SLLNode<T> node = new SLLNode<>(data);
            // Case 1: The list is empty
            if (isEmpty()) {
                addToFront(data);
            } else {
                // Most General Case:
                tail.setNext(node);
                tail = node;
                size++;
            }
        }
    }

    @Override
    public T removeFromFront() {
        // Case 1: The list is empty
        if (isEmpty()) {
            return null;
        } else {
            // Most General Case:
            SLLNode<T> rem = head;
            head = head.getNext();
            size--;
            // Special Case: The last element was removed
            if (isEmpty()) {
                tail = null;
            }
            return rem.getData();
        }
    }

    @Override
    public T removeAtIndex(int index) {
        // Check for exceptions in inputs
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is not within the "
                    + "size of the list: " + 0 + " to " + size);
        } else {
            if (index == 0) {
                // Case 1: index == 0 (removeFromFront)
                return removeFromFront();
            } else if (index == size - 1) {
                // Case 2: index is last entry (removeFromBack)
                return removeFromBack();
            } else {
                // Most General Case: Remove from middle
                SLLNode<T> prev = head;
                for (int i = 1; i < index; i++) {
                    prev = prev.getNext();
                }
                SLLNode<T> temp = prev.getNext();
                prev.setNext(prev.getNext().getNext());
                size--;
                return temp.getData();
            }
        }
    }

    @Override
    public T removeFromBack() {
        // Case 1: The list is empty
        if (isEmpty()) {
            return null;
        } else if (size == 1) {
            // Case 2: There is only one thing in the list
            T remData = head.getData();
            head = null;
            tail = null;
            size--;
            return remData;
        } else {
            // Most General Case:
            SLLNode<T> prev = head;
            while (prev.getNext() != tail) {
                prev = prev.getNext();
            }
            SLLNode<T> rem = prev.getNext();
            prev.setNext(null);
            tail = prev;
            size--;
            return rem.getData();
        }
    }

    @Override
    public T get(int index) {
        // Check for exceptions in inputs
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is not within the "
                    + "size of the list: " + 0 + " to " + size);
        } else if (index == 0) {
            // Case 1: Get head
            return head.getData();
        } else if (index == size - 1) {
            // Case 2: Get tail
            return tail.getData();
        } else {
            // Most General Case: Get from the middle
            SLLNode<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current.getData();
        }
    }

    @Override
    public T findLargestElement() {
        // Case 1: The list is empty
        if (isEmpty()) {
            return null;
        } else {
            // Most General Case:
            T largest = head.getData();
            SLLNode<T> prev = head;
            // Walk through list comparing the next elt to the previous largest
            for (int i = 1; i < size; i++) {
                if (prev.getNext().getData().compareTo(largest) > 0) {
                    largest = prev.getNext().getData();
                }
                prev = prev.getNext();
            }
            return largest;
        }
    }

    @Override
    public Object[] toArray() {
        // Walk through list and put all data into an array of Comparable's
        Comparable[] arr = new Comparable[size];
        SLLNode<T> current = head;
        for (int i = 0; i < size; i++) {
            arr[i] = current.getData();
            current = current.getNext();
        }
        return arr;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public SLLNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    @Override
    public SLLNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }

    @Override
    public boolean removeEvens() {
        if (isEmpty()) {
            throw new NoSuchElementException("This list is empty!");
        }
        int arrSize = 0;
        int initialSize = size;
        ArrayList<SLLNode<T>> arr = new ArrayList<>();
        SLLNode<T> curr = head;
        while (curr != null) {
            if (curr.getData() instanceof Integer) {
                if ((Integer) curr.getData() % 2 != 0) {
                    arr.add(curr);
                    arrSize++;
                    size--;
                }
                curr = curr.getNext();
            }
        }
        arr.get(arrSize - 1).setNext(null);
        for (int i = arrSize - 2; i <= 0; i--) {
            arr.get(i).setNext(arr.get(i + 1));
        }
        head = arr.get(0);
        return initialSize != size;
    }
}
