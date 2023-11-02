package Task1;

import java.util.*;


public class RTree<T extends Rectanglable> {
    private static final int M = 5;
    private Node<T> root;

    public RTree() {
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
        return quadraticSplit(nodeToSplit);
    }

    public Stack<Node<T>> chooseLeaf(Node<T> root, T rect) {
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

    public void adjustTree(Stack<Node<T>> pathStack, Node<T> N, Node<T> NN) {
        Node<T> P, PP = null;
        while (!pathStack.isEmpty()) {
            P = pathStack.pop();
            P.setBoundBox(Rectangle.combine(P.getBoundBox(), N.getBoundBox()));
            if (NN != null) {
                if (P.size() < M) {
                    P.addChild(NN);
                } else {
                    PP = quadraticSplit(P);
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

    private Node<T> quadraticSplit(Node<T> node) {
        Node<T> newNode = new Node<>();

        List<T> rectanglables = new ArrayList<>(node.value());

        Group seeds = pickSeeds(rectanglables);
        T seed1 = rectanglables.remove(seeds.group1);
        T seed2 = rectanglables.remove(seeds.group2 > seeds.group1 ? seeds.group2 - 1 : seeds.group2);

        node.clear();
        node.addRectanglable(seed1);
        newNode.addRectanglable(seed2);

        while (!rectanglables.isEmpty()) {
            int selectedIndex = pickNext(rectanglables, node.getBoundBox(), newNode.getBoundBox());
            T nextRectanglable = rectanglables.remove(selectedIndex);

            if (node.getBoundBox().calculateDifference(nextRectanglable.getRectangle()) <
                    newNode.getBoundBox().calculateDifference(nextRectanglable.getRectangle()) ||
                    newNode.size() >= M) {
                node.addRectanglable(nextRectanglable);
            } else {
                newNode.addRectanglable(nextRectanglable);
            }
        }

        return newNode;
    }

    private Group pickSeeds(List<T> rectanglables) {
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

private class Group {
    public final int group1;
    public final int group2;

    public Group(int group1, int group2) {
        this.group1 = group1;
        this.group2 = group2;
    }

}

    private int pickNext(List<T> rectanglables, Rectangle groupBoundBox1, Rectangle groupBoundBox2) {
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
