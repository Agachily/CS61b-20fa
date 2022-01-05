package bearmaps.proj2ab;

import java.util.*;

/**
 * Implement the ExtrinsicMinPQ interface.
 * @author zetong
 *  */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    class Node {
        private T item;
        private double priority;
        private int index;

        private Node(T item, double priority, int index) {
            this.item = item;
            this.priority = priority;
            this.index = index;
        }

        private T getItem() {
            return item;
        }

        private double getPriority() {
            return priority;
        }

        private int getIndex() {
            return index;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private void setPriority(double priority) {
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
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        int size = size();
        if (size == 0) {
            Node node = new Node(item, priority, 1);
            pQ.add(1, node);
            itemMap.put(item, 1);
        } else {
            Node node = new Node(item, priority, size + 1);
            pQ.add(size + 1, node);
            itemMap.put(node.getItem(), size + 1);
            swimUp(node);
        }
        this.size++;
    }

    private void swimUp(Node node) {
        int parentIndex = node.getIndex() / 2;
        if (parentIndex < 1) {
            return;
        }
        Node parent = pQ.get(parentIndex);
        while (node.getPriority() < parent.getPriority()) {
            int nodeIndex = node.getIndex();
            parentIndex = parent.getIndex();
            node.setIndex(parentIndex);
            parent.setIndex(nodeIndex);
            pQ.set(parentIndex, node);
            pQ.set(nodeIndex, parent);
            updateIndex(parent);
            parent = pQ.get(node.getIndex() / 2);
        }
        updateIndex(node);
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size() < 1) {
            throw new NoSuchElementException();
        }
        return pQ.get(1).item;
    }

    @Override
    public T removeSmallest() {
        T smallest;
        try {
            smallest = getSmallest();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }
        int size = size();
        this.size--;
        itemMap.remove(smallest);
        Node lastNode = pQ.get(size);
        pQ.set(1, lastNode);
        lastNode.setIndex(1);
        pQ.set(size, null);
        sink(lastNode);
        return smallest;
    }

    private void updateIndex(Node node) {
        itemMap.remove(node.item);
        itemMap.put(node.item, node.getIndex());
    }

    private void leftSink(Node node, Node leftChild) {
        int leftChildIndex = leftChild.getIndex();
        pQ.set(node.getIndex(), leftChild);
        pQ.set(leftChild.getIndex(), node);
        leftChild.setIndex(node.getIndex());
        node.setIndex(leftChildIndex);
        updateIndex(leftChild);
        updateIndex(node);
    }

    private void rightSink(Node node, Node rightChild) {
        int rightChildIndex = rightChild.getIndex();
        pQ.set(node.getIndex(), rightChild);
        pQ.set(rightChild.getIndex(), node);
        rightChild.setIndex(node.getIndex());
        node.setIndex(rightChildIndex);
        updateIndex(rightChild);
        updateIndex(node);
        sink(node);
    }

    private void sink(Node node) {
        int leftChildIndex = node.getIndex() * 2;
        int rightChildIndex = node.getIndex() * 2 + 1;
        Node leftChild;
        Node rightChild = null;
        if (leftChildIndex > this.size) {
            return;
        } else {
            leftChild = pQ.get(leftChildIndex);
            if (rightChildIndex <= this.size) {
                rightChild = pQ.get(rightChildIndex);
            }
        }
        if (rightChild == null && leftChild.getPriority() > node.getPriority()) {
            return;
        }
        if (rightChild == null && leftChild.getPriority() <= node.getPriority()) {
            leftSink(node, leftChild);
            return;
        }
        if (leftChild.getPriority() >= node.getPriority() && rightChild.getPriority() >= node.getPriority()) {
            return;
        }
        if (leftChild.getPriority() > rightChild.getPriority()) {
            rightSink(node, rightChild);
            sink(node);
            return;
        }
        if (leftChild.getPriority() <= rightChild.getPriority()) {
            leftSink(node, leftChild);
            sink(node);
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!itemMap.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int index = itemMap.get(item);
        Node targetNode = pQ.get(index);
        targetNode.setPriority(priority);
        /* Rearrange the node after changing its priority */
        int parentIndex = targetNode.getIndex() / 2;
        swimUp(targetNode);
        sink(targetNode);
    }
}
