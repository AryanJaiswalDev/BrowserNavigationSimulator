package ds;

import java.util.ArrayList;
import java.util.List;

/**
 * Our own Stack (LIFO) built with a linked list.
 * No java.util.Stack used, so the data structure is clearly visible.
 */
public class Stack<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    // Add on top
    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
        size++;
    }

    // Remove from top
    public T pop() {
        if (isEmpty()) return null;
        T item = top.data;
        top = top.next;
        size--;
        return item;
    }

    // Look at top without removing
    public T peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }

    // Items from top to bottom (used by UI to display the stack)
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> curr = top;
        while (curr != null) {
            list.add(curr.data);
            curr = curr.next;
        }
        return list;
    }
}
