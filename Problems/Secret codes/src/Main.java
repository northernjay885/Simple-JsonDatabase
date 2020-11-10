import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    private static List<String> extractCodes(List<String> codes) {
        // write your code here
        return codes.stream().dropWhile(n -> n.compareTo("#0000") != 0)
                .skip(1).takeWhile(n -> n.compareTo("#FFFF") != 0)
                .collect(Collectors.toList());
    }

    /* Please do not modify the code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> codes = Arrays.stream(scanner.nextLine()
                .split("\\s+"))
                .collect(Collectors.toList());

        System.out.println(String.join(" ", extractCodes(codes)));
    }
}