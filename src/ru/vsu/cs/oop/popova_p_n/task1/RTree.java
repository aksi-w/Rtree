package ru.vsu.cs.oop.popova_p_n.task1;

import java.util.*;


public class RTree<T extends Rectanglable> {
    private final int M;
    private Node<T> root;

    public RTree(int m) {
        this.M = m;
        root = new Node<T>();
    }

    public List<T> search(Rectangle searchRectangle) {
        List<T> result = new ArrayList<>();
        searchHelper(root, searchRectangle, result);
        return result;
    }

    private void searchHelper(Node<T> node, Rectangle searchRectangle, List<T> result) {
        if (!node.isLeaf()) {
            for (Node<T> childNode : node.getChild()) {
                if (searchRectangle.intersects(childNode.getBoundBox())) {
                    searchHelper(childNode, searchRectangle, result);
                }
            }
        } else {
            for (T rectangle : node.value()) {
                if (searchRectangle.intersects(rectangle.getRectangle())) {
                    result.add(rectangle);
                }
            }
        }
    }

    public void insert(T newRectanglable) {
        Stack<Node<T>> pathStack = chooseLeaf(root, newRectanglable);
        Node<T> leafNode = pathStack.pop();

        Node<T> newLeafNode = null;
        if (leafNode.size() < M) {
            leafNode.addRectanglable(newRectanglable);
        } else {
            newLeafNode = splitNode(leafNode, newRectanglable);
        }

        adjustTree(pathStack, leafNode, newLeafNode);

        if (pathStack.isEmpty() && newLeafNode != null) {
            Node<T> newRoot = new Node<>();
            newRoot.addChild(leafNode);
            newRoot.addChild(newLeafNode);
            root = newRoot;
        }
    }

    private Node<T> splitNode(Node<T> nodeToSplit, T newRectanglable) {
        nodeToSplit.addRectanglable(newRectanglable);
        return quadraticSplitY(nodeToSplit);
    }

    private Stack<Node<T>> chooseLeaf(Node<T> root, T rect) {
        Stack<Node<T>> leafStack = new Stack<>();
        Node<T> N = root;
        while (N != null && !N.isLeaf()) {
            double minDelta = Double.MAX_VALUE;
            Node<T> selectedChild = null;
            for (Node<T> child : N.getChild()) {
                Rectangle childRect = child.getBoundBox();
                Rectangle deltaRect = Rectangle.combine(childRect, rect.getRectangle());
                double delta = deltaRect.square() - childRect.square();
                if (delta < minDelta) {
                    minDelta = delta;
                    selectedChild = child;
                } else if (delta == minDelta) {
                    if (childRect.square() < selectedChild.getBoundBox().square()) {
                        selectedChild = child;
                    }
                }
            }
            leafStack.push(N);
            N = selectedChild;
        }
        if (N != null) {
            leafStack.push(N);
        }
        return leafStack;
    }

    private void adjustTree(Stack<Node<T>> pathStack, Node<T> N, Node<T> NN) {
        Node<T> P, PP = null;
        while (!pathStack.isEmpty()) {
            P = pathStack.pop();
            P.setBoundBox(Rectangle.combine(P.getBoundBox(), N.getBoundBox()));
            if (NN != null) {
                if (P.size() < M) {
                    P.addChild(NN);
                } else {
                    PP = quadraticSplitX(P);
                    if (pathStack.isEmpty()) {
                        Node<T> newRoot = new Node<>();
                        newRoot.addChild(P);
                        newRoot.addChild(PP);
                        root = newRoot;
                        return;
                    }
                }
            }

            N = P;
            NN = PP;
        }
    }

    private static class ListPair<T> {
        private final List<T> first;
        private final List<T> second;

        public ListPair(List<T> first, List<T> second) {
            this.first = first;
            this.second = second;
        }

        public List<T> getFirst() {
            return first;
        }

        public List<T> getSecond() {
            return second;
        }
    }
    private Node<T> quadraticSplitX(Node<T> node) {
        ListPair<Node<T>> rrr = quadraticSplit(node.getChild(), M);
        node.clear();
        for (Node<T> q : rrr.getFirst()) {
            node.addChild(q);
        }
        Node<T> newNode = new Node<>();
        for (Node<T> q : rrr.getSecond()) {
            newNode.addChild(q);
        }
        return newNode;
    }
    private Node<T> quadraticSplitY(Node<T> node) {
        ListPair<T> rrr = quadraticSplit(node.value(), M);
        node.clear();
        for (T q : rrr.getFirst()) {
            node.addRectanglable(q);
        }
        Node<T> newNode = new Node<>();
        for (T q : rrr.getSecond()) {
            newNode.addRectanglable(q);
        }
        return newNode;
    }

    private static <K extends Rectanglable> ListPair<K> quadraticSplit(List<K> node, int M) {
        List<K> newNode = new ArrayList<>();

        List<K> rectanglables = new ArrayList<>(node);

        Group seeds = pickSeeds(rectanglables);
        K seed1 = rectanglables.remove(seeds.group1);
        K seed2 = rectanglables.remove(seeds.group2 > seeds.group1 ? seeds.group2 - 1 : seeds.group2);

        node.clear();
        node.add(seed1);
        newNode.add(seed2);
        Rectangle a = seed1.getRectangle();
        Rectangle b = seed2.getRectangle();

        while (!rectanglables.isEmpty()) {
            int selectedIndex = pickNext(rectanglables, a, b);
            K nextRectanglable = rectanglables.remove(selectedIndex);

            if (a.calculateDifference(nextRectanglable.getRectangle()) <
                    b.calculateDifference(nextRectanglable.getRectangle()) ||
                    newNode.size() >= M) {
                node.add(nextRectanglable);
                a = Rectangle.combine(a, nextRectanglable.getRectangle());
            } else {
                newNode.add(nextRectanglable);
                b = Rectangle.combine(b, nextRectanglable.getRectangle());
            }
        }

        return new ListPair<>(node, newNode);
    }



    private static <T extends Rectanglable> Group pickSeeds(List<T> rectanglables) {
        double maxInefficiency = Double.MIN_VALUE;
        int group1 = -1;
        int group2 = -1;
        for (int i = 0; i < rectanglables.size(); i++) {
            for (int j = i + 1; j < rectanglables.size(); j++) {
                T rect1 = rectanglables.get(i);
                T rect2 = rectanglables.get(j);
                Rectangle combinedRectangle = Rectangle.combine(rect1.getRectangle(), rect2.getRectangle());
                double inefficiency = combinedRectangle.square() - rect1.getRectangle().square() - rect2.getRectangle().square();
                if (inefficiency > maxInefficiency) {
                    maxInefficiency = inefficiency;
                    group1 = i;
                    group2 = j;
                }
            }
        }
        return new Group(group1, group2);
    }

    private static class Group {
        public final int group1;
        public final int group2;

        public Group(int group1, int group2) {
            this.group1 = group1;
            this.group2 = group2;
        }

    }

    private static <T extends Rectanglable> int pickNext(List<T> rectanglables, Rectangle groupBoundBox1, Rectangle groupBoundBox2) {
        double maxDifference = Double.MIN_VALUE;
        int selectedIndex = -1;
        for (int i = 0; i < rectanglables.size(); i++) {
            T rectanglable = rectanglables.get(i);
            double d1 = groupBoundBox1.calculateDifference(rectanglable.getRectangle());
            double d2 = groupBoundBox2.calculateDifference(rectanglable.getRectangle());
            double difference = Math.abs(d1 - d2);

            if (difference > maxDifference) {
                maxDifference = difference;
                selectedIndex = i;
            }
        }

        if (selectedIndex == -1) {
            selectedIndex = 0;
        }

        return selectedIndex;
    }

    public void print() {

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<T> currentNode = queue.poll();

            for (T rect : currentNode.value()) {
                System.out.println(rect.getRectangle());
            }

            for (Node<T> child : currentNode.getChild()) {
                queue.add(child);
            }
        }
    }
}
