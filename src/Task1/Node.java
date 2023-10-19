package Task1;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Rectanglable> {
    protected Rectangle boundBox;
    public List<T> rectanglables;

    public Node() {
        this.boundBox = null;
        this.rectanglables = new ArrayList<>();
    }

    public void addRectanglable(T rectanglable) {
        if (boundBox == null) {
            boundBox = rectanglable.getRectangle();
            return;
        }
        rectanglables.add(rectanglable);
        Rectangle rect = rectanglable.getRectangle();
        if (rect.intersects(boundBox)) {
            boundBox = boundBox.combine(boundBox, rect);
        }
    }

    public Rectangle getBoundBox() {
        return boundBox;
    }

    public boolean isLeaf() {
        return rectanglables.isEmpty();
    }

    public void clear() {
        rectanglables.clear();
        boundBox = null;
    }

    int size() {
        return rectanglables.size();
    }


}
