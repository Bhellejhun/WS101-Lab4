import java.util.Scanner;

public class Array {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int[] nums = new int[5];
        int sum = 0;

        System.out.println("Enter 5 numbers:");
        for (int i = 0; i < 5; i++) {
            nums[i] = sc.nextInt();
            sum += nums[i];
        }

        double average = sum / 5.0;

        System.out.println("Sum = " + sum);
        System.out.println("Average = " + average);
    }
}