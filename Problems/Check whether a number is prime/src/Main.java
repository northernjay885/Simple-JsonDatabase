import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize); // assign an object to it

        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            // create and submit tasks
            PrintIfPrimeTask task = new PrintIfPrimeTask(number);
            executor.submit(task);
        }
        executor.shutdown();
    }
}

class PrintIfPrimeTask implements Runnable {
    private final int number;

    public PrintIfPrimeTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        // write code of task here
        boolean isPrime = number > 1 && IntStream
                .rangeClosed(2, number - 1)
                .noneMatch(divisor -> number % divisor == 0);
        if (isPrime) {
            System.out.println(number);
        }
    }
}