import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    private HashingUtils utils;

    public MultiplicativeShiftingHash() {
        utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(int k) {
            this.k = k;
            a = utils.genUniqueIntegers(1)[0];
        }
        @Override
        public int hash(Long key) {
            return (int)(a * key) >>> (WORD_SIZE - k);
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
