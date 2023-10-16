package Task1;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Rectangle rectangle1 = new Rectangle(1, 1, 4, 4);
        Rectangle rectangle2 = new Rectangle(2, 2, 6, 6);

        double difference = rectangle1.calculateDifference(rectangle2);
        System.out.println("Разница площадей: " + difference);

    }
}
