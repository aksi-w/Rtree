package Task1;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<T extends Rectanglable> {
    protected Rectangle boundBox;
    public List<T> rectanglables;

    public Node(Rectangle boundBox) {
        this.boundBox = boundBox;
        this.rectanglables = new ArrayList<>();
    }
    public void addRectanglable(T rectanglable) {
        rectanglables.add(rectanglable);
        for (T lala : rectanglables) {
            Rectangle rect = lala.getRectangle();
            if (rect.intersects(boundBox)) {
                boundBox = boundBox.combine(boundBox, rect);
            }
        }
    }
    public Rectangle getBoundBox() {
        return boundBox;
    }

    abstract boolean isLeaf();
    abstract int size();
}
