import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> list = new ArrayList<>();
        while (scanner.hasNext()) {
            list.add(scanner.nextInt());
        }
        int[] arr = list.stream().mapToInt(i -> i).toArray();
        System.out.print(bubbleSort(arr));
    }

    public static int bubbleSort(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr.length - i - 1; ++j) {
                if (arr[j] < arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    count++;
                }
            }
        }
        return count;
    }
}