import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


class FutureUtils {

    public static int executeCallableObjects(List<Future<Callable<Integer>>> items) {
        // write your code here
        int sum = 0;
        for (int i = items.size() - 1; i >= 0; --i) {
            try {
                int item = items.get(i).get().call();
                sum += item;
            } catch (Exception e) {
                System.out.println("Weird things happened!");
            }
        }
        return sum;
    }

}