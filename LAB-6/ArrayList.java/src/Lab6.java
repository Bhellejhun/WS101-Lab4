import java.util.*;
import java.util.stream.*;

public class Lab6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 50000));
        products.add(new Product("Mouse", 300));
        products.add(new Product("Keyboard", 800));
        products.add(new Product("Phone", 15000));
        products.add(new Product("Headset", 1200));

        System.out.print("Enter minimum price to filter: ");
        double minPrice = sc.nextDouble();

        long count = products.stream()
                .filter(p -> p.price > minPrice)
                .count();

        System.out.println("Number of products more expensive than " + minPrice + ": " + count);
    }
}