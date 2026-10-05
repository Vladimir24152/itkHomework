package org.neoflex;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class MyStringBuilder {

    private char[] chars;
    private int count;

    private Deque<Snapshot> history;

    private static final int INITIAL_CAPACITY = 16;
    private static final int MULTIPLICATION_FACTOR = 2;
    private static final int MULTIPLICATION_ADDITION = 2;

    public MyStringBuilder() {
        this.chars = new char[INITIAL_CAPACITY];
        this.count = 0;
        this.history = new ArrayDeque<>();
    }

    public MyStringBuilder append(String str) {
        if (Objects.isNull(str)) {
            return this;
        }
        saveSnapshot();
        ensureCapacity(count + str.length());
        str.getChars(0, str.length(), chars, count);
        count += str.length();
        return this;
    }

    @Override
    public String toString() {
        return new String(chars, 0, count);
    }

    public void undo() {
        if (history.isEmpty()) {
            return;
        }
        Snapshot snapshot = history.pop();
        restore(snapshot);
    }

    private void saveSnapshot() {
        history.push(new Snapshot(chars, count));
    }

    private void restore(Snapshot snapshot) {
        this.chars = snapshot.getChars();
        this.count = snapshot.getCount();
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= chars.length) {
            return;
        }
        int newCapacity = chars.length * MULTIPLICATION_FACTOR + MULTIPLICATION_ADDITION;
        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }
        char[] newChars = new char[newCapacity];
        System.arraycopy(chars, 0, newChars, 0, count);
        this.chars = newChars;
    }

    private class Snapshot {
        private final char[] chars;
        private final int count;

        Snapshot(char[] source, int count) {
            this.chars = new char[source.length];
            System.arraycopy(source, 0, this.chars, 0, count);
            this.count = count;
        }

        char[] getChars() {
            return chars;
        }

        int getCount() {
            return count;
        }
    }
}
