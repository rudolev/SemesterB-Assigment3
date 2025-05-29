import java.awt.geom.NoninvertibleTransformException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private List<Element<K,V>>[] table;

    // My fields
    private int numOfItems = 0;
    private int currentK;

    private double calculateLoadFactor() {
        return (double) numOfItems / capacity();
    }

    private boolean needsRehashing() {
        return maxLoadFactor < calculateLoadFactor();
    }

    private void rehashTable() {
        int newCapacity = capacity * 2;
        List<Element<K,V>>[] newTable = new List[newCapacity];
        HashFunctor<K> newHashFunc = hashFactory.pickHash(currentK + 1);

        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList();
        }

        for (int i = 0; i < capacity; i++) {
            for (Element<K, V> elm : table[i]) {
                int hashedKey = newHashFunc.hash(elm.key());
                newTable[hashedKey].add(elm);
            }
        }

        table = newTable;
        capacity = newCapacity;
        hashFunc = newHashFunc;
        currentK += 1;
    }

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        currentK = k;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new List[this.capacity];

        for (int i = 0; i < this.capacity; i++) {
            table[i] = new LinkedList();
        }
    }

    public V search(K key) {
        int hashedKey = hashFunc.hash(key);
        for (Element<K, V> currentElement : table[hashedKey]) {
            if (currentElement.key().equals(key))
                return currentElement.satelliteData();
        }
        return null;
    }

    public void insert(K key, V value) {
        int hashedKey = hashFunc.hash(key);
        table[hashedKey].add(new Element<K, V>(key, value));
        numOfItems += 1;

        if (needsRehashing()) {
            rehashTable();
        }
    }

    public boolean delete(K key) {
        int hashedKey = hashFunc.hash(key);
        for (Element<K, V> currentElement : table[hashedKey]) {
            if (currentElement.key().equals(key)) {
                table[hashedKey].remove(currentElement);
                return true;
            }
        }
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
