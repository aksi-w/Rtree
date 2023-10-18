package Task1;

import java.util.List;
import java.util.Stack;

public class Insert {
    private static final int m = 2;

    public Stack<Node<Rectanglable>> chooseLeaf(Node<Rectanglable> root, Rectanglable rect) {
        Stack<Node<Rectanglable>> leafStack = new Stack<>();
        Node<Rectanglable> N = root;

        while (!N.isLeaf()) {
            double minDelta = Double.MAX_VALUE;
            Node<Rectanglable> selectedChild = null;

            for (Rectanglable child : N.rectanglables) {
                Rectangle childRect = child.getRectangle();
                Rectangle deltaRect = childRect.combine(childRect, rect.getRectangle());
                double delta = deltaRect.square() - childRect.square();

                if (delta < minDelta) {
                    minDelta = delta;
                    selectedChild = (Node<Rectanglable>) child;
                } else if (delta == minDelta) {
                    if (childRect.square() < selectedChild.getBoundBox().square()) {
                        selectedChild = (Node<Rectanglable>) child;
                    }
                }
            }

            leafStack.push(N);
            N = selectedChild;
        }

        leafStack.push(N);

        return leafStack;
    }

    public void quadraticSplit(Node<Rectanglable> node1, Node<Rectanglable> node2) { // на вход список из ректанглаблов , которые нужно разделить
        // на выход 2 списка разделенных ректанглаблов
        //УДАЛЕНИЕ ПРОИСХОДИТ В ЭТОМ МЕТОДЕ ПО ИНДЕКСУ
        //выборка наиболее крутых кандидатов при разделении и создаем 2 ректанглабла , которые будут меняться , сначала он равен командиру команды
        // метод нужен для возврата 2 списков , который будет хранить 2 поля(1 и 2 кучки) и этот метод будет возвращать экземпляр этого класса, который будет хранить эти 2 списка, которые мы завели ранее
        pickSeeds(node1, node2);
        while (!node1.rectanglables.isEmpty() && !node2.rectanglables.isEmpty()) {
            pickNext(node1, node2);
        }
        if (!node1.rectanglables.isEmpty()) {
            while (node1.rectanglables.size() < m) {
                pickNext(node1, node2);
            }
        } else if (!node2.rectanglables.isEmpty()) {
            while (node2.rectanglables.size() < m) {
                pickNext(node1, node2);
            }
        }
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
        // за удаление отвечает quadraticSplit
//        if (selectedIndex != -1) {
//            Rectanglable selectedRectanglable = rectanglables.get(selectedIndex);
//            rectanglables.remove(selectedIndex);
//
//            if (node1.getBoundBox().calculateDifference(selectedRectanglable.getRectangle()) <=
//                    node2.getBoundBox().calculateDifference(selectedRectanglable.getRectangle())) {
//                node1.addRectanglable(selectedRectanglable);
//            } else {
//                node2.addRectanglable(selectedRectanglable);
//            }
//        }

        return selectedIndex;
    }
}
