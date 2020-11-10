import java.util.*;
import java.util.stream.*;

public class Main {

    private static List<LongStream> invertedStreams(List<LongStream> streams) {
        // write your code here
        for (int i = 0; i < streams.size(); ++i) {
            if (streams.get(i).isParallel()) {
                streams.set(i, streams.get(i).sequential());
            } else {
                streams.set(i, streams.get(i).parallel());
            }
        }
        return streams;
    }

    /* Do not modify the code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Boolean> parallelFlags = Arrays
                .stream(scanner.nextLine().split("\\s+"))
                .map(Boolean::parseBoolean)
                .collect(Collectors.toList());

        // :)
        List<LongStream> streams = Stream
                .iterate(0,
                        i -> i < parallelFlags.size(),
                        i -> i + 1)
                .map(i -> {
                    var stream = LongStream.of();
                    if (parallelFlags.get(i)) {
                        stream = stream.parallel();
                    }
                    return stream;
                }).collect(Collectors.toList());

        List<String> invertedParallelFlagsAsStrings =
                invertedStreams(streams).stream()
                        .map(LongStream::isParallel)
                        .map(Object::toString)
                        .collect(Collectors.toList());

        System.out.println(String.join(" ", invertedParallelFlagsAsStrings));
    }
}