import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    private Random rand;
    private HashingUtils utils;

    public ModularHash() {
        rand = new Random();
        utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int k){
            Integer[] integers = utils.genUniqueIntegers(2);
            a = integers[0];
            b = integers[0];
            p = utils.genPrime(Integer.MAX_VALUE, Long.MAX_VALUE);
            m = 1 << k;
        }

        @Override
        public int hash(Integer key) {
            long t = HashingUtils.mod(a * (long)key + b, p);
            return (int)HashingUtils.mod(t, (long)m);
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
