import java.util.*;
import java.util.concurrent.*;


class FutureUtils {

    public static int howManyIsDone(List<Future> items) {
        // write your code here
        return items.stream()
                .map(item -> item.isDone()? 1:0)
                .reduce(0, Integer::sum);
    }

}