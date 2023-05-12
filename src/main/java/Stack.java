import java.util.ArrayList;
import java.util.List;

/**
 * Simple stack implementation
 */
public class Stack<T> {
    private final ArrayList<T> storage = new ArrayList<>();

    public void clear() {
        storage.clear();
    }

    public void push(T value) {
        storage.add(value);
    }

    public void pushAll(List<T> values) {
        storage.addAll(values);
    }

    public T pop() {
        if (storage.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return storage.remove(storage.size() - 1);
    }

    public int size() {
        return storage.size();
    }

    public Stack<T> duplicate() {
        Stack<T> clone = new Stack<>();
        clone.pushAll(storage);
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Stack of ").append(size()).append(" items, bottom-up:");
        if (size() == 0) {
            b.append("\n  (empty)");
            return b.toString();
        }
        int i = 1;
        for (T item : storage) {
            b.append("\n  ").append(i++).append(": ").append(item.toString());
        }
        return b.toString();
    }
}
