package org.neoflex;

public class MyBlockingQueue<T> {

    private final int capacity;
    private final Object[] queue;
    private int size;
    private int addCursor = 0;
    private int getCursor = 0;

    public MyBlockingQueue() {
        this(10);
    }

    public MyBlockingQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Требуется положительное число");
        }
        this.capacity = capacity;
        this.queue = new Object[capacity];
    }

    public synchronized void enqueue(T element) {
        while (size == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        queue[addCursor] = element;
        addCursor = (addCursor + 1) % capacity;
        size++;

        notifyAll();
    }

    public synchronized T dequeue() {
        while (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        T element = (T) queue[getCursor];
        queue[getCursor] = null;
        getCursor = (getCursor + 1) % capacity;
        size--;

        notifyAll();
        return element;
    }

    public synchronized int size() {
        return size;
    }
}