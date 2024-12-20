package com.example.project3_algo;

import java.util.ArrayList;
import java.util.List;

public class MinHeap<T extends Comparable<T>> {
    private final List<T> heap;
    private final HMap<T, Integer> indexMap;

    public MinHeap() {
        this.heap = new ArrayList<>();
        this.indexMap = new HMap<>();
    }

    public int add(T element) {
        heap.add(element);
        int index = heap.size() - 1;
        indexMap.put(element, index);
        heapifyUp(index);
        return index;
    }

    public T delete() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        T min = heap.get(0);
        int lastIdx = heap.size() - 1;
        T lastElement = heap.get(lastIdx);

        swap(0, lastIdx);
        heap.remove(lastIdx);
        indexMap.remove(min);

        if (!heap.isEmpty()) {
            heapifyDown(0);
        }

        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void UpdateElement(T oldElement, T newElement) {
        int index = indexMap.get(oldElement);
        if (newElement.compareTo(heap.get(index)) > 0) {
            throw new IllegalArgumentException("New key is larger than current key");
        }
        heap.set(index, newElement);
        indexMap.remove(oldElement);
        indexMap.put(newElement, index);
        heapifyUp(index);
    }

    public HMap<T, Integer> getIndexMap() {
        return indexMap;
    }

    private void heapifyUp(int index) {
        while (hasParent(index) && getParent(index).compareTo(heap.get(index)) > 0) {
            int parentIndex = getParentIndex(index);
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    private void heapifyDown(int index) {
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);

            if (hasRightChild(index) && getRightChild(index).compareTo(getLeftChild(index)) < 0) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (heap.get(index).compareTo(heap.get(smallerChildIndex)) <= 0) {
                break;
            } else {
                swap(index, smallerChildIndex);
            }

            index = smallerChildIndex;
        }
    }

    private boolean hasParent(int i) {
        return i > 0;
    }

    private int getLeftChildIndex(int i) {
        return 2 * i + 1;
    }

    private int getRightChildIndex(int i) {
        return 2 * i + 2;
    }

    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    private boolean hasLeftChild(int i) {
        return getLeftChildIndex(i) < heap.size();
    }

    private boolean hasRightChild(int i) {
        return getRightChildIndex(i) < heap.size();
    }

    private T getLeftChild(int i) {
        return heap.get(getLeftChildIndex(i));
    }

    private T getRightChild(int i) {
        return heap.get(getRightChildIndex(i));
    }

    private T getParent(int i) {
        return heap.get(getParentIndex(i));
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        indexMap.put(heap.get(i), i);
        indexMap.put(heap.get(j), j);
    }

    public int getSize() {
        return heap.size();
    }
}
