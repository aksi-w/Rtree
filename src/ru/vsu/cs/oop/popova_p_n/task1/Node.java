package ru.vsu.cs.oop.popova_p_n.task1;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Rectanglable> implements Rectanglable {

    protected Rectangle boundBox;

    private List<T> rectanglables;

    private List<Node<T>> child;

    public List<T> value() {
        return rectanglables;
    }
    public void setBoundBox(Rectangle boundBox) {
        this.boundBox = boundBox;
    }
    public List<Node<T>> getChild() {
        return child;
    }

    public Node() {
        this.boundBox = null;
        this.rectanglables = new ArrayList<>();
        this.child = new ArrayList<>();
    }

    public void addRectanglable(T rectanglable) {
        rectanglables.add(rectanglable);
        Rectangle rect = rectanglable.getRectangle();
        if (boundBox == null) {
            boundBox = rect;
        } else {
            boundBox = Rectangle.combine(boundBox, rect);
        }
    }

    public void addChild(Node<T> childRectanglable) {
        child.add(childRectanglable);
        if (boundBox == null) {
            boundBox = childRectanglable.getBoundBox();
        } else {
            boundBox = Rectangle.combine(boundBox, childRectanglable.getBoundBox());
        }
    }

    public Rectangle getBoundBox() {
        return boundBox;
    }

    public boolean isLeaf() {
        return child.isEmpty();
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
