package bearmaps;

import java.util.*;

/**
 * Implement the ExtrinsicMinPQ interface.
 * @author zetong
 *  */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    class Node {
        T item;
        double priority;
        int index;

        private Node(T item, double priority, int index) {
            this.item = item;
            this.priority = priority;
            this.index = index;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }
    }

    private List<Node> pQ;
    private Map<T, Integer> itemMap;
    private int size;

    public ArrayHeapMinPQ() {
        Node sentinel = new Node(null, 0, 0);
        pQ = new ArrayList<>();
        pQ.add(0, sentinel);
        itemMap = new HashMap<>();
        size = 0;
    }

    @Override
    public void add(T item, double priority) {
        int size = size();
        if (size == 0) {
            Node node = new Node(item, priority, 1);
            pQ.add(1, node);
            itemMap.put(item, 1);
        } else {
            Node node = new Node(item, priority, size + 1);
            pQ.add(size + 1, node);
            swimUp(node);
            itemMap.put(item, size + 1);
        }
        this.size++;
    }

    private void swimUp(Node node) {
        Node parent = pQ.get(node.getIndex() / 2);
        while (node.getPriority() < parent.getPriority()) {
            int nodeIndex = node.getIndex();
            int parentIndex = parent.getIndex();
            node.setIndex(parentIndex);
            parent.setIndex(nodeIndex);
            pQ.set(parentIndex, node);
            pQ.set(nodeIndex, parent);
            parent = pQ.get(node.getIndex() / 2);
        }
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        return pQ.get(1).item;
    }

    @Override
    public T removeSmallest() {
        T smallest = getSmallest();
        int size = size();
        Node lastNode = pQ.get(size);
        pQ.set(1, lastNode);
        lastNode.setIndex(1);
        pQ.set(size, null);
        this.size--;
        sink(lastNode);
        itemMap.remove(smallest);
        return smallest;
    }

    private void updateIndex(Node node) {
        itemMap.remove(node.item);
        itemMap.put(node.item, node.getIndex());
    }

    private void sink(Node node) {
        int leftChildIndex = node.getIndex() * 2;
        int rightChildIndex = node.getIndex() * 2 + 1;
        Node leftChild = null;
        Node rightChild = null;
        if (leftChildIndex <= size()) {
            leftChild = pQ.get(leftChildIndex);
        }
        if (rightChildIndex <= size()) {
            rightChild = pQ.get(rightChildIndex);
        }
        if (leftChild == null && rightChild == null) {
            return;
        }
        if (leftChild.getPriority() >= node.getPriority() && rightChild.getPriority() >= node.getPriority()) {
            return;
        }
        if (leftChild.getPriority() > rightChild.getPriority() || leftChild == null) {
            pQ.set(node.getIndex(), rightChild);
            pQ.set(rightChild.getIndex(), node);
            rightChild.setIndex(node.getIndex());
            node.setIndex(rightChildIndex);
            updateIndex(rightChild);
            updateIndex(node);
            sink(node);
        }
        if (leftChild.getPriority() <= rightChild.getPriority() || rightChild == null) {
            pQ.set(node.getIndex(), leftChild);
            pQ.set(leftChild.getIndex(), node);
            leftChild.setIndex(node.getIndex());
            node.setIndex(leftChildIndex);
            updateIndex(leftChild);
            updateIndex(node);
            sink(node);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
        int index = itemMap.get(item);
        Node targetNode = pQ.get(index);
        targetNode.setPriority(priority);
    }

    public static void main(String[] args) {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("Tim", 1);
        pq.add("Carol", 5);
        pq.add("Bob", 1);
        pq.add("Kate", 6);
        pq.add("Yoshiki", 5);
        pq.add("Tosh", 6);
        pq.add("Sen", 3);
        pq.add("Lin", 7);
        pq.add("Hai", 7);
        pq.add("Kiwi", 8);
        pq.add("Sil", 3);
        pq.add("Doom", 5);
        pq.removeSmallest();
        pq.removeSmallest();
        pq.removeSmallest();
        pq.removeSmallest();
        pq.removeSmallest();
        PrintHeapDemo.printFancyHeapDrawing(pq.pQ.toArray());
    }
}
