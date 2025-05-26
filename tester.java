

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
