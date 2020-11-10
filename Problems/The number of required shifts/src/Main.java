import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        List<Integer> list = new ArrayList<>();
        int size = scanner.nextInt();
        while (scanner.hasNext()) {
            list.add(scanner.nextInt());
        }
        int[] arr = list.stream().mapToInt(i -> i).toArray();
        System.out.print(insertSort(arr));
    }

    public static int insertSort(int[] arr) {
        int res = 0;
        for (int i = 1; i < arr.length; ++i) {
            int elem = arr[i];
            int j = i - 1;
            int count = 0;
            while (j >= 0 && elem > arr[j]) {
                arr[j + 1] = arr[j];
                j--;
                count++;
            }
            if (count > 0) {
                res++;
                count = 0;
            }
            arr[j + 1] = elem;
        }
        return res;
    }
}