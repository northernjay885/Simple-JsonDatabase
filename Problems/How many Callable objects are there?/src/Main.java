import java.util.concurrent.*;


class FutureUtils {
    static int counter = 0;

    public static int determineCallableDepth(Callable callable) throws Exception {
        // write your code here
        Object tmp = null;
        try {
            tmp = callable.call();
        } catch (Exception e) { }
        return tmp instanceof Callable ? 1 + determineCallableDepth((Callable) tmp) : 1;
    }

}