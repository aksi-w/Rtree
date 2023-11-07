package ru.vsu.cs.oop.popova_p_n.task1;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        int M = 5;
        RTree<Rectanglable> rtree = new RTree<>(M);

        Rectanglable rect1 = new Rectangle(0, 0, 2, 2);
        Rectanglable rect2 = new Rectangle(1, 1, 3, 3);
        Rectanglable rect3 = new Rectangle(4, 4, 6, 6);
        Rectanglable rect4 = new Rectangle(5, 5, 7, 7);
        Rectanglable rect5 = new Rectangle(8, 8, 10, 10);

        rtree.insert(rect1);
        rtree.insert(rect2);
        rtree.insert(rect3);
        rtree.insert(rect4);
        rtree.insert(rect5);

        rtree.print();

        Rectangle searchRectangle = new Rectangle(2, 2, 7, 7);

        List<Rectanglable> searchResult = rtree.search(searchRectangle);

        if (!searchResult.isEmpty()) {
            System.out.println("Результаты поиска:");
            for (Rectanglable result : searchResult) {
                System.out.println(result.getRectangle());
            }
        } else {
            System.out.println("Ничего не найдено.");
        }
    }
}
