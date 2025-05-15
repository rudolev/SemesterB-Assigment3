public class IndexableSkipList extends AbstractSkipList {
    final protected double p;	// p is the probability for "success" in the geometric process generating the height of each node.
    public IndexableSkipList(double probability) {
        super();
        this.p = probability;
    }
	
	@Override
    public void decreaseHeight() {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    @Override
    public SkipListNode find(int key) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    @Override
    public int generateHeight() {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public int rank(int key) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

}
