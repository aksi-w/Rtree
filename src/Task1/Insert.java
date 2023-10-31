package Task1;

import java.util.*;

public class Insert {
    private static final int M = 3;
    public Node<Rectanglable> root;

    public Insert(Node<Rectanglable> root) {
        this.root = root;
    }

    public void insert(Rectanglable newRectanglable) {
        Stack<Node<Rectanglable>> pathStack = chooseLeaf(root, newRectanglable);
        Node<Rectanglable> leafNode = pathStack.pop();

        Node<Rectanglable> newLeafNode = null;
        if (leafNode.size() < M) {
            leafNode.addRectanglable(newRectanglable);
        } else {
            newLeafNode = splitNode(leafNode, newRectanglable);
        }

        adjustTree(pathStack, leafNode, newLeafNode);

        if (pathStack.isEmpty() && newLeafNode != null) {
            Node<Rectanglable> newRoot = new Node<>();
            newRoot.addChild(leafNode);
            newRoot.addChild(newLeafNode);
            root = newRoot;
        }
    }

    private Node<Rectanglable> splitNode(Node<Rectanglable> nodeToSplit, Rectanglable newRectanglable) {
        nodeToSplit.addRectanglable(newRectanglable);
        return quadraticSplit(nodeToSplit);
    }

    public Stack<Node<Rectanglable>> chooseLeaf(Node<Rectanglable> root, Rectanglable rect) {
        Stack<Node<Rectanglable>> leafStack = new Stack<>();
        Node<Rectanglable> N = root;
        while (N != null && !N.isLeaf()) {
            double minDelta = Double.MAX_VALUE;
            Node<Rectanglable> selectedChild = null;
            for (Node<Rectanglable> child : N.getChild()) {
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

    public void adjustTree(Stack<Node<Rectanglable>> pathStack, Node<Rectanglable> N, Node<Rectanglable> NN) {
        Node<Rectanglable> P, PP = null;
        while (!pathStack.isEmpty()) {
            P = pathStack.pop();
            P.boundBox = Rectangle.combine(P.getBoundBox(), N.getBoundBox());
            if (NN != null) {
                if (P.size() < M) {
                    P.addChild(NN);
                } else {
                    PP = quadraticSplit(P);
                    if (pathStack.isEmpty()) {
                        Node<Rectanglable> newRoot = new Node<>();
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

    public Node<Rectanglable> quadraticSplit(Node<Rectanglable> node) {
        Node<Rectanglable> newNode = new Node<>();

        List<Rectanglable> rectanglables = new ArrayList<>(node.value());

        Group seeds = pickSeeds(rectanglables);
        Rectanglable seed1 = rectanglables.remove(seeds.group1);
        Rectanglable seed2 = rectanglables.remove(seeds.group2 > seeds.group1 ? seeds.group2 - 1 : seeds.group2);

        node.clear();
        node.addRectanglable(seed1);
        newNode.addRectanglable(seed2);

        while (!rectanglables.isEmpty()) {
            int selectedIndex = pickNext(rectanglables, node.getBoundBox(), newNode.getBoundBox());
            Rectanglable nextRectanglable = rectanglables.remove(selectedIndex);

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

    public Group pickSeeds(List<Rectanglable> rectanglables) {
        double maxInefficiency = Double.MIN_VALUE;
        int group1 = -1;
        int group2 = -1;
        for (int i = 0; i < rectanglables.size(); i++) {
            for (int j = i + 1; j < rectanglables.size(); j++) {
                Rectanglable rect1 = rectanglables.get(i);
                Rectanglable rect2 = rectanglables.get(j);
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


    public int pickNext(List<Rectanglable> rectanglables, Rectangle groupBoundBox1, Rectangle groupBoundBox2) {
        double maxDifference = Double.MIN_VALUE;
        int selectedIndex = -1;
        for (int i = 0; i < rectanglables.size(); i++) {
            Rectanglable rectanglable = rectanglables.get(i);
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

        Queue<Node<Rectanglable>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<Rectanglable> currentNode = queue.poll();

            for (Rectanglable rect : currentNode.value()) {
                System.out.println(rect.getRectangle());
            }

            for (Node<Rectanglable> child : currentNode.getChild()) {
                queue.add(child);
            }
        }
    }


}
