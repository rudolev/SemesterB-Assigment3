import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSkipList {
    final protected SkipListNode head;
    final protected SkipListNode tail;

    public AbstractSkipList() {
        head = new SkipListNode(Integer.MIN_VALUE);
        tail = new SkipListNode(Integer.MAX_VALUE);
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        tail.addLevel(null, head);
    }
	
    abstract void decreaseHeight();

    abstract SkipListNode find(int key);

    abstract int generateHeight();

    public SkipListNode search(int key) {
        SkipListNode curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public SkipListNode insert(int key) {
        int nodeHeight = generateHeight();

        while (nodeHeight > head.height()) {
            increaseHeight();
        }

        SkipListNode[] currentNodeHolder = new SkipListNode[head.height() + 1];
        int[] steps = new int[head.height() + 1];
        SkipListNode curr = head;
        int totalSteps = 0;

        for (int level = head.height(); level >= 0; level--) {
            while (curr.getNext(level) != tail && curr.getNext(level).key() < key) {
                totalSteps += curr.getWidth(level);
                curr = curr.getNext(level);
            }
            currentNodeHolder[level] = curr;
            steps[level] = totalSteps;
        }

        if (curr.getNext(0).key() == key) return null;

        SkipListNode newNode = new SkipListNode(key);
        for (int i = 0; i <= nodeHeight; i++) {
            SkipListNode prev = currentNodeHolder[i];
            SkipListNode next = prev.getNext(i);

            newNode.addLevel(next, prev);
            prev.setNext(i, newNode);
            next.setPrev(i, newNode);

            int skipped = steps[0] - steps[i];

            newNode.setWidth(i, prev.getWidth(i) - skipped);
            prev.setWidth(i, skipped + 1);
        }

        for (int i = nodeHeight + 1; i <= head.height(); i++) {
            currentNodeHolder[i].setWidth(i, currentNodeHolder[i].getWidth(i) + 1);
        }

        return newNode;
    }

    public boolean delete(SkipListNode skipListNode) {
        for (int level = 0; level <= skipListNode.height(); ++level) {
            SkipListNode prev = skipListNode.getPrev(level);
            SkipListNode next = skipListNode.getNext(level);
            prev.setNext(level, next);
            next.setPrev(level, prev);
        }
		
        while (head.height() >= 0 && head.getNext(head.height()) == tail) {
        	decreaseHeight();
        }

        return true;
    }

    public SkipListNode predecessor(SkipListNode skipListNode) {
        return skipListNode.getPrev(0);
    }

    public SkipListNode successor(SkipListNode skipListNode) {
        return skipListNode.getNext(0);
    }

    public SkipListNode minimum() {
        if (head.getNext(0) == tail) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return head.getNext(0);
    }

    public SkipListNode maximum() {
        if (tail.getPrev(0) == head) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return tail.getPrev(0);
    }

    private void levelToString(StringBuilder s, int level) {
        s.append("H    ");
        SkipListNode curr = head.getNext(0);

        while (curr != tail) {
            if (curr.height >= level) {
                s.append(curr.key());
                s.append("    ");
            }
            else {
            	s.append("    ");
            	for (int i = 0; i < curr.key().toString().length(); i = i + 1)
            		s.append(" ");
            }

            curr = curr.getNext(0);
        }

        s.append("T\n");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString(str, level);
        }

        return str.toString();
    }

    public static class SkipListNode extends Element<Integer, Object> {
        final private List<SkipListNode> next;
        final private List<SkipListNode> prev;
        private int height;
        private List<Integer> width;

        public SkipListNode(int key) {
        	super(key);
            next = new ArrayList<>();
            prev = new ArrayList<>();
            this.height = -1;
            width = new ArrayList<>();
            
        }

        public SkipListNode getPrev(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return prev.get(level);
        }

        public SkipListNode getNext(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return next.get(level);
        }

        public void setNext(int level, SkipListNode next) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.next.set(level, next);
        }

        public void setPrev(int level, SkipListNode prev) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.prev.set(level, prev);
        }

        public void addLevel(SkipListNode next, SkipListNode prev) {
            ++height;
            this.next.add(next);
            this.prev.add(prev);
            this.width.add(1);
        }
		
		public void removeLevel() {           
            this.next.remove(height);
            this.prev.remove(height);
            --height;
        }

        public int height() { return height; }

        public int getWidth(int level) { return width.get(level); }

        public void setWidth(int level, int w) {
            width.set(level, w);
        }
    }
}
