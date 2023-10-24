package Task1;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Rectanglable> implements Rectanglable {
    protected Rectangle boundBox;

    private List<T> rectanglables;

    private List<Node<T>> child;

    public List<T> value() {
        return rectanglables;
    }

    public List<Node<T>> getChild() {
        return child;
    }


    public Node() {
        this.boundBox = null;
        this.rectanglables = new ArrayList<>();
    }

    public void addRectanglable(T rectanglable) {

        /*
        rectanglables.add(rectanglable);
        Rectangle rect = rectanglable.getRectangle();
        if (rect.intersects(boundBox)) {
            boundBox = boundBox.combine(boundBox, rect);
        }*/
        if (boundBox == null) {
            boundBox = rectanglable.getRectangle();
        }
    }

    public void addChild(T child){

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

    @Override
    public Rectangle getRectangle() {
        return getBoundBox();
    }
}
