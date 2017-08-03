import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Your implementation of HashMap.
 * 
 * @author Nick Liccini
 * @version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private LinkedList<MapEntry<K, V>>[] backingTable;
    private int size;

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Do not use magic numbers!
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        backingTable = ((LinkedList<MapEntry<K, V>>[])
                new LinkedList[initialCapacity]);
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        if (value == null || key == null) {
            throw new IllegalArgumentException("Input key and/or value is null"
                    + ", please use a valid input.");
        }
        // If the load factor will be exceeded from this add, resize the table
        if (((size + 1) / ((double) backingTable.length)) > MAX_LOAD_FACTOR) {
            resizeBackingTable((2 * backingTable.length) + 1);
        }
        int ind = Math.abs(key.hashCode()) % backingTable.length;
        // Case 1: There is an existing linked list at that index
        if (backingTable[ind] != null) {
            // If an entry with this key already exists, replace its value
            Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
            while (iter.hasNext()) {
                MapEntry<K, V> entry = iter.next();
                if (entry.getKey().equals(key)) {
                    V rep = entry.getValue();
                    // Use entry.setValue bc this is O(1)
                    entry.setValue(value);
                    return rep;
                }
            }
            // If not, then there is a collision, so add the entry to the front
            backingTable[ind].addFirst(new MapEntry<>(key, value));
            size++;
            return null;
            // Case 2: Create a new linked list since this is the first elt here
        } else {
            // If the index is empty, set the beginning of the linked list
            backingTable[ind] = new LinkedList<>();
            backingTable[ind].addFirst(new MapEntry<>(key, value));
            size++;
            return null;
        }
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Input key is null, please "
                    + "use a valid input.");
        }
        int ind = Math.abs(key.hashCode()) % backingTable.length;
        if (backingTable[ind] != null) {
            // The key is somewhere in the external chain
            Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
            while (iter.hasNext()) {
                MapEntry<K, V> entry = iter.next();
                if (entry.getKey().equals(key)) {
                    V rem = entry.getValue();
                    // Use iter.remove() bc this is an O(1) operation
                    iter.remove();
                    size--;
                    return rem;
                }
            }
        }
        // Key is not in the external chain or the table has no list at ind
        throw new NoSuchElementException("The input key does not have a "
                + "corresponding value in this map.");
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Input key is null, please "
                    + "use a valid input.");
        }
        int ind = Math.abs(key.hashCode()) % backingTable.length;
        if (backingTable[ind] != null) {
            // The key is somewhere in the external chain
            Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
            while (iter.hasNext()) {
                MapEntry<K, V> entry = iter.next();
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        // Key is not in the external chain or the table has no list at ind
        throw new NoSuchElementException("The input key does not have a "
                + "corresponding value in this map.");
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Input key is null, please "
                    + "use a valid input.");
        }
        int ind = Math.abs(key.hashCode()) % backingTable.length;
        if (backingTable[ind] != null) {
            // The key is somewhere in the external chain
            Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
            while (iter.hasNext()) {
                MapEntry<K, V> entry = iter.next();
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
        }
        // Key is not in the external chain or the table has no list at ind
        return false;
    }

    @Override
    public void clear() {
        backingTable = ((LinkedList<MapEntry<K, V>>[])
                new LinkedList[INITIAL_CAPACITY]);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        int ind = 0;
        while (ind < backingTable.length) {
            if (backingTable[ind] != null) {
                Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
                while (iter.hasNext()) {
                    set.add(iter.next().getKey());
                }
            }
            ind++;
        }
        return set;
    }

    @Override
    public List<V> values() {
        List<V> list = new LinkedList<>();
        int ind = 0;
        while (ind < backingTable.length) {
            if (backingTable[ind] != null) {
                Iterator<MapEntry<K, V>> iter = backingTable[ind].iterator();
                while (iter.hasNext()) {
                    ((LinkedList<V>) list).addLast(iter.next().getValue());
                }
            }
            ind++;
        }
        return list;
    }

    @Override
    public void resizeBackingTable(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The input length is not "
                    + "positive and therefore is invalid. Please use an input "
                    + "greater than 0.");
        }
        LinkedList<MapEntry<K, V>>[] newTable = ((LinkedList<MapEntry<K, V>>[])
                new LinkedList[length]);
        int i = 0;
        int ind;
        while (i < backingTable.length) {
            if (backingTable[i] != null) {
                Iterator<MapEntry<K, V>> iter = backingTable[i].iterator();
                while (iter.hasNext()) {
                    MapEntry<K, V> entry = iter.next();
                    ind = Math.abs(entry.getKey().hashCode()) % newTable.length;
                    if (newTable[ind] == null) {
                        newTable[ind] = new LinkedList<>();
                    }
                    newTable[ind].addFirst(entry);
                }
            }
            i++;
        }
        backingTable = newTable;
    }
    
    @Override
    public LinkedList<MapEntry<K, V>>[] getTable() {
        // DO NOT EDIT THIS METHOD!
        return backingTable;
    }
}
