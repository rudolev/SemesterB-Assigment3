import java.util.Random;

public class HashingExperimentUtils {
    final private static int k = 16;

    private static double testInsert(HashTable<Integer, String> hashTable, double maxLoadFactor) {
        int numEntries = (int) ((1 << (k / 2)) * maxLoadFactor) - 1;
        Random rand = new Random(42); // fixed seed for repeatability

        // Insert unique keys
        long beforeTime = System.nanoTime();
        for (int i = 0; i < numEntries; i++) {
            int key = rand.nextInt(10000);
            hashTable.insert(key, "bla");
        }
        long totalTime = System.nanoTime() - beforeTime;

        return (double) totalTime / numEntries;
    }

    private static double testSearch(HashTable<Integer, String> hashTable, double maxLoadFactor) {
        int numEntries = (int) ((1 << (k / 2)) * maxLoadFactor) - 1;

        // Insert unique keys
        for (int i = 0; i < numEntries; i++) {
            hashTable.insert(i, "bla");
        }

        long beforeTime = System.nanoTime();
        // 50 percent successful searches
        for (int i = 0; i < numEntries / 2; i++) {
            hashTable.search(i);
        }

        // 50 percent failed searches
        for (int i = 0; i < numEntries / 2; i++) {
            hashTable.search(i + numEntries);
        }

        long totalTime = System.nanoTime() - beforeTime;

        return (double) totalTime / numEntries;
    }

    public static double[] measureInsertionsProbing() {
        double[] loadFactors = {0.5, 3.0 / 4, 7.0 / 8, 15.0 / 16};
        double[] results = new double[loadFactors.length];
        ModularHash hashFactory = new ModularHash();

        for (int i = 0; i < loadFactors.length; i++) {
            ProbingHashTable<Integer, String> hashTable = new ProbingHashTable<>(hashFactory, k, loadFactors[i]);
            results[i] = testInsert(hashTable, loadFactors[i]);
        }

        return results;
    }

    public static double[] measureSearchesProbing() {

        double[] loadFactors = {0.5, 3.0 / 4, 7.0 / 8, 15.0 / 16};
        double[] results = new double[loadFactors.length];

        ModularHash hashFactory = new ModularHash();

        for (int i = 0; i < loadFactors.length; i++) {
            ProbingHashTable<Integer, String> hashTable = new ProbingHashTable<>(hashFactory, k, loadFactors[i]);
            results[i] = testSearch(hashTable, loadFactors[i]);
        }

        return results;
    }

    public static double[] measureInsertionsChaining() {
        double[] loadFactors = {0.5, 3.0 / 4, 1.0, 3.0 / 2, 2.0};
        double[] results = new double[loadFactors.length];

        ModularHash hashFactory = new ModularHash();

        for (int i = 0; i < loadFactors.length; i++) {
            ChainedHashTable<Integer, String> hashTable = new ChainedHashTable<>(hashFactory, k, loadFactors[i]);
            results[i] = testInsert(hashTable, loadFactors[i]);
        }

        return results;
    }

    public static double[] measureSearchesChaining() {
        double[] loadFactors = {0.5, 3.0 / 4, 1.0, 3.0 / 2, 2.0};
        double[] results = new double[loadFactors.length];

        ModularHash hashFactory = new ModularHash();

        for (int i = 0; i < loadFactors.length; i++) {
            ChainedHashTable<Integer, String> hashTable = new ChainedHashTable<>(hashFactory, k, loadFactors[i]);
            results[i] = testSearch(hashTable, loadFactors[i]);
        }

        return results;
    }
}
