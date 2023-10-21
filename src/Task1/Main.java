package Task1;

public class Main {
    public static void main(String[] args) {
        Insert insert = new Insert();

        Rectanglable rect1 = new Rectangle(0, 0, 2, 2);
        Rectanglable rect2 = new Rectangle(1, 1, 3, 3);
        Rectanglable rect3 = new Rectangle(4, 4, 6, 6);
        Rectanglable rect4 = new Rectangle(5, 5, 7, 7);
        Rectanglable rect5 = new Rectangle(8, 8, 10, 10);

        insert.insert(rect1);
        insert.insert(rect2);
        insert.insert(rect3);
        insert.insert(rect4);
        insert.insert(rect5);


        for (Rectanglable rectanglable : insert.root.rectanglables) {
            System.out.println(rectanglable.getRectangle());
        }
    }
}
