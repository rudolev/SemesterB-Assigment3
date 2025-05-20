public class SkipListUtils {

    public static double calculateExpectedHeight(double p) {
        int height = 0;
        while (Math.random() < p) {
            height++;
        }
        return height;
    }

}
