import java.util.Scanner;

class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    @Override
    public void run() {
        // implement this method
        while (true) {
            String str = scanner.next();
            if (str.toUpperCase().equals(str)) {
                System.out.println("FINISHED");
                break;
            } else {
                System.out.println(str.toUpperCase());
            }
        }
    }
}