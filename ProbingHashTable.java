public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private Element<K,V>[] table;

    // My fields
    private int size;
    final private Element<K, V> deleteMark;
    private int currentK;

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.currentK = k;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new Element[capacity];

        deleteMark = new Element<>(null, null);
    }

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    private double calculateLoadFactor() {
        return (double) size / capacity();
    }

    private boolean needsRehashing() {
        return maxLoadFactor < calculateLoadFactor();
    }

    private void markIndexAsDeleted(int index) {
        table[index] = deleteMark;
    }

    private boolean isIndexDeleted(int index) {
        return table[index].equals(deleteMark);
    }

    @Override
    public V search(K key) {
        int index = probe(key);
        if (index != -1 && table[index] != null && !isIndexDeleted(index)) {
            return table[index].satelliteData();
        }
        return null;
    }

    @Override
    public void insert(K key, V value) {
        int hash = hashFunc.hash(key);
        boolean finishedInsert = false;

        for (int i = 0; i < capacity && !finishedInsert; i++) {
            int index = (hash + i) % capacity;

            if (table[index] == null || isIndexDeleted(index)) {
                table[index] = new Element<>(key, value);
                size++;
                finishedInsert = true;
            }

            else if (table[index].key().equals(key)) {
                table[index].setSatData(value); // update existing key
                finishedInsert = true;
            }
        }

        if (needsRehashing())
            rehashTable();

    }

    @Override
    public boolean delete(K key) {
        int index = probe(key);
        if (index != -1 && table[index] != null && !isIndexDeleted(index)) {
            markIndexAsDeleted(index);
            size--;
            return true;
        }
        return false;
    }

    private int probe(K key) {
        int hash = hashFunc.hash(key);
        for (int i = 0; i < capacity; i++) {
            int index = (hash + i) % capacity;
            Element<K, V> elem = table[index];
            if (elem == null)
                return -1;
            if (!isIndexDeleted(index) && elem.key().equals(key))
                return index;
        }
        return -1;
    }

    private void rehashTable() {
        int newCapacity = capacity * 2;
        HashFunctor<K> newHashFunc = hashFactory.pickHash(currentK + 1);

        Element<K, V>[] oldTable = table;
        table = new Element[newCapacity];
        capacity = newCapacity;
        hashFunc = newHashFunc;
        size = 0;
        currentK = currentK + 1;

        for (Element<K, V> elem : oldTable) {
            if (elem != null && !elem.equals(deleteMark)) {
                insert(elem.key(), elem.satelliteData());
            }
        }
    }


    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
