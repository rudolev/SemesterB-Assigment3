import java.util.*;

public class tester {
    public static void main(String[] args) {
        {
            IndexableSkipList skipList = new IndexableSkipList(0.5);

            System.out.println("=== TEST: Inserting Elements ===");
            skipList.insert(10);
            skipList.insert(20);
            skipList.insert(30);
            skipList.insert(25);
            skipList.insert(5);
            System.out.println("Inserted 5, 10, 20, 25, 30");
            System.out.println(skipList);

            System.out.println("=== TEST: Search Function ===");
            assertTest(skipList.search(10) != null && skipList.search(10).key() == 10, "Search for 10");
            assertTest(skipList.search(25) != null && skipList.search(25).key() == 25, "Search for 25");
            assertTest(skipList.search(99) == null, "Search for 99 (should not exist)");

            System.out.println("=== TEST: Minimum/Maximum ===");
            assertTest(skipList.minimum().key() == 5, "Minimum key should be 5");
            assertTest(skipList.maximum().key() == 30, "Maximum key should be 30");

            System.out.println("=== TEST: Delete Node ===");
            AbstractSkipList.SkipListNode nodeToDelete = skipList.search(20);
            boolean deleteResult = false;
            if (nodeToDelete != null) {
                deleteResult = skipList.delete(nodeToDelete);
            }
            assertTest(deleteResult && skipList.search(20) == null, "Delete key 20");

            System.out.println("=== Skip List After Deletion ===");
            System.out.println(skipList);

            System.out.println("=== TEST: Decrease Height (no effect if level not empty) ===");
            int heightBefore = skipList.head.height();
            skipList.decreaseHeight();
            int heightAfter = skipList.head.height();
            assertTest(heightAfter <= heightBefore, "Decrease height (should not increase)");
        }

        {
            IndexableSkipList skipList = new IndexableSkipList(0.5);

            int[] values = {10, 5, 30, 20, 25, 15}; // inserted in shuffled order
            for (int val : values) {
                skipList.insert(val);
            }

            System.out.println("=== Skip List before ranking check");
            System.out.println(skipList);

            System.out.println("=== TEST: rank(v) ===");
            assertTest(skipList.rank(5) == 0, "rank(5) == 0");
            assertTest(skipList.rank(10) == 1, "rank(10) == 1");
            assertTest(skipList.rank(12) == 2, "rank(12) == 2");
            assertTest(skipList.rank(30) == 5, "rank(30) == 5");
            assertTest(skipList.rank(40) == 6, "rank(40) == 6");

            System.out.println("=== TEST: select(i) ===");
            System.out.println(skipList.select(0));
            assertTest(skipList.select(0) == 5, "select(0) == 5");
            assertTest(skipList.select(1) == 10, "select(1) == 10");
            assertTest(skipList.select(2) == 15, "select(2) == 15");
            assertTest(skipList.select(5) == 30, "select(5) == 30");
        }


        {
            System.out.println("=== TESTING ChainedHashTable IMPLEMENTATION ===");

            ModularHash modularHash = new ModularHash();
            HashTable<Integer, String> table = new ChainedHashTable<>(modularHash);

            System.out.println("Inserting key-value pairs...");
            table.insert(1, "one");
            table.insert(2, "two");
            table.insert(3, "three");

            assertTest("one".equals(table.search(1)), "Search key 1 == one");
            assertTest("two".equals(table.search(2)), "Search key 2 == two");
            assertTest("three".equals(table.search(3)), "Search key 3 == three");
            assertTest(table.search(99) == null, "Search key 99 == null");

            System.out.println("Deleting key 2...");
            assertTest(table.delete(2), "Delete key 2 succeeds");
            assertTest(table.search(2) == null, "Search key 2 == null after delete");

            System.out.println("Re-inserting key 2...");
            table.insert(2, "TWO");
            assertTest("TWO".equals(table.search(2)), "Search key 2 == TWO (after reinsertion)");

            System.out.println("Testing capacity and hash function...");
            assertTest(table.capacity() > 0, "Capacity > 0");
            assertTest(table.getHashFunc() != null, "Hash function is not null");
        }

        {
            System.out.println("=== MASSIVE TESTING: ChainedHashTable INSERT/SEARCH/DELETE ===");

            ModularHash modularHash = new ModularHash();
            HashTable<Integer, String> table = new ChainedHashTable<>(modularHash);

            int numEntries = 1000;
            Random rand = new Random(42); // fixed seed for repeatability

            List<Integer> keys = new ArrayList<>();
            Map<Integer, String> expected = new HashMap<>();

            // Insert unique keys
            while (keys.size() < numEntries) {
                int key = rand.nextInt(10_000);
                if (!expected.containsKey(key)) {
                    String value = "val" + key;
                    keys.add(key);
                    expected.put(key, value);
                    table.insert(key, value);
                }
            }

            System.out.println("Inserted " + keys.size() + " unique key-value pairs.");

            // Search and validate
            for (int key : keys) {
                String expectedVal = expected.get(key);
                String actualVal = table.search(key);
                assertTest(expectedVal.equals(actualVal), "Search key " + key + " == " + expectedVal);
            }

            // Test missing keys
            for (int i = 0; i < 100; i++) {
                int fakeKey = 100_000 + i;
                assertTest(table.search(fakeKey) == null, "Search non-existent key " + fakeKey + " == null");
            }

            // Delete half the keys and validate
            for (int i = 0; i < keys.size(); i += 2) {
                int key = keys.get(i);
                assertTest(table.delete(key), "Delete key " + key + " success");
                assertTest(table.search(key) == null, "Search deleted key " + key + " == null");
            }

            // Check the remaining keys are still intact
            for (int i = 1; i < keys.size(); i += 2) {
                int key = keys.get(i);
                String val = expected.get(key);
                assertTest(val.equals(table.search(key)), "Key " + key + " still exists after deletion");
            }

            System.out.println("Mass test complete.");
        }


        {
            System.out.println("=== TESTING ProbingHashTable IMPLEMENTATION ===");

            ModularHash modularHash = new ModularHash();
            HashTable<Integer, String> table = new ProbingHashTable<>(modularHash);

            System.out.println("Inserting key-value pairs...");
            table.insert(1, "one");
            table.insert(2, "two");
            table.insert(3, "three");

            assertTest("one".equals(table.search(1)), "Search key 1 == one");
            assertTest("two".equals(table.search(2)), "Search key 2 == two");
            assertTest("three".equals(table.search(3)), "Search key 3 == three");
            assertTest(table.search(99) == null, "Search key 99 == null");

            System.out.println("Deleting key 2...");
            assertTest(table.delete(2), "Delete key 2 succeeds");
            assertTest(table.search(2) == null, "Search key 2 == null after delete");

            System.out.println("Re-inserting key 2...");
            table.insert(2, "TWO");
            assertTest("TWO".equals(table.search(2)), "Search key 2 == TWO (after reinsertion)");

            System.out.println("Testing capacity and hash function...");
            assertTest(table.capacity() > 0, "Capacity > 0");
            assertTest(table.getHashFunc() != null, "Hash function is not null");
        }

        {
            System.out.println("=== MASSIVE TESTING: ProbingHashTable HashTable INSERT/SEARCH/DELETE ===");

            ModularHash modularHash = new ModularHash();
            HashTable<Integer, String> table = new ProbingHashTable<>(modularHash);

            int numEntries = 1000;
            Random rand = new Random(42); // fixed seed for repeatability

            List<Integer> keys = new ArrayList<>();
            Map<Integer, String> expected = new HashMap<>();

            // Insert unique keys
            while (keys.size() < numEntries) {
                int key = rand.nextInt(10_000);
                if (!expected.containsKey(key)) {
                    String value = "val" + key;
                    keys.add(key);
                    expected.put(key, value);
                    table.insert(key, value);
                }
            }

            System.out.println("Inserted " + keys.size() + " unique key-value pairs.");

            // Search and validate
            for (int key : keys) {
                String expectedVal = expected.get(key);
                String actualVal = table.search(key);
                assertTest(expectedVal.equals(actualVal), "Search key " + key + " == " + expectedVal);
            }

            // Test missing keys
            for (int i = 0; i < 100; i++) {
                int fakeKey = 100_000 + i;
                assertTest(table.search(fakeKey) == null, "Search non-existent key " + fakeKey + " == null");
            }

            // Delete half the keys and validate
            for (int i = 0; i < keys.size(); i += 2) {
                int key = keys.get(i);
                assertTest(table.delete(key), "Delete key " + key + " success");
                assertTest(table.search(key) == null, "Search deleted key " + key + " == null");
            }

            // Check the remaining keys are still intact
            for (int i = 1; i < keys.size(); i += 2) {
                int key = keys.get(i);
                String val = expected.get(key);
                assertTest(val.equals(table.search(key)), "Key " + key + " still exists after deletion");
            }

            System.out.println("Mass test complete.");
        }

        {
            System.out.println("=== TESTING ProbingHashTable ===");

            HashFactory<Integer> factory = new ModularHash();
            ProbingHashTable<Integer, String> table = new ProbingHashTable<>(factory, 4, 0.75);

            // Insert some elements
            System.out.println("Inserting keys 1 to 5...");
            for (int i = 1; i <= 5; i++) {
                table.insert(i, "val" + i);
            }

            // Search test
            for (int i = 1; i <= 5; i++) {
                assertTest(("val" + i).equals(table.search(i)), "Search key " + i);
            }

            // Update key
            System.out.println("Updating key 3...");
            table.insert(3, "updated");
            assertTest("updated".equals(table.search(3)), "Updated key 3");

            // Delete some keys
            System.out.println("Deleting keys 2 and 4...");
            assertTest(table.delete(2), "Delete key 2");
            assertTest(table.delete(4), "Delete key 4");

            assertTest(table.search(2) == null, "Search deleted key 2");
            assertTest(table.search(4) == null, "Search deleted key 4");

            // Insert more keys to test tombstone reuse
            System.out.println("Inserting key 6 (reuses tombstone?)...");
            table.insert(6, "val6");
            assertTest("val6".equals(table.search(6)), "Search key 6");

            // Insert a lot to trigger rehash
            System.out.println("Inserting many keys to trigger rehash...");
            int base = 100;
            for (int i = base; i < base + 50; i++) {
                table.insert(i, "val" + i);
            }

            assertTest(table.capacity() > 16, "Capacity increased after rehash");

            // Check a few random rehashed values
            assertTest("val105".equals(table.search(105)), "Search rehashed key 105");
            assertTest("val109".equals(table.search(109)), "Search rehashed key 109");

            // Search for non-existing
            assertTest(table.search(9999) == null, "Search missing key 9999");

            System.out.println("All probing hash table tests done.");
        }

        System.out.println("=== TESTING COMPLETE ===");

    }

    private static void assertTest(boolean condition, String description) {
        if (condition) {
            System.out.println("[PASS] " + description);
        } else {
            System.out.println("[FAIL] " + description);
        }
    }


}
