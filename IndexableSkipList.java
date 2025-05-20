public class IndexableSkipList extends AbstractSkipList {
    final protected double p;	// p is the probability for "success" in the geometric process generating the height of each node.
    public IndexableSkipList(double probability) {
        super();
        this.p = probability;
    }
	
	@Override
    public void decreaseHeight() {
        if (head.height() >= 0 && head.getNext(head.height()) == tail) {
            head.removeLevel();
            tail.removeLevel();
        }
    }

    @Override
    public SkipListNode find(int key) {
        SkipListNode currentNode = head;
        int level = head.height();

        while (level >= 0) {
            while (currentNode.getNext(level) != tail && currentNode.getNext(level).key() <= key) {
                currentNode = currentNode.getNext(level);
            }
            level--;
        }

        return currentNode;
    }

    @Override
    public int generateHeight() {
        int height = 0;
        while (Math.random() < p && height <= head.height()) {
            height++;
        }
        return height;
    }

    public int rank(int key) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

}
