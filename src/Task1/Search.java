package Task1;

import java.util.ArrayList;
import java.util.List;

public class Search {

    public static List<Rectangle> search(Node<Rectangle> node, Rectangle searchRectangle) {
        List<Rectangle> result = new ArrayList<>();
        searchHelper(node, searchRectangle, result);
        return result;
    }

    private static <T extends Rectanglable> void searchHelper(Node<T> node, Rectangle searchRectangle, List<T> result) {
        if (!node.isLeaf()) {
            for (Node<T> rectangle : node.getChild()) {
                if (searchRectangle.intersects(rectangle.getRectangle())) {
                    searchHelper(rectangle, searchRectangle, result);
                }
            }
        }
        else {
            for (T rectangle : node.value()) {
                if (searchRectangle.intersects(rectangle.getRectangle())) {
                    result.add(rectangle);
                }
            }
        }
    }
}
