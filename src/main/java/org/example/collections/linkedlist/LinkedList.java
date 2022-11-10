package org.example.collections.linkedlist;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class LinkedList<E> implements List<E> {
    private static final String MESSAGE = "Input index is wrong!";

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        private E element;
        private Node<E> next;
        private Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void add(E element) {
        add(element, size);
    }

    @Override
    public void add(E element, int index) {
        checkAddIndex(index);
        addIndex(element, index);
    }

    @Override
    public void addAll(List<E> list) {
        IntStream.range(0, list.size()).forEach(i -> add(list.get(i)));
    }

    @Override
    public boolean addAll(int index, List<E> collection) {
        checkAddIndex(index);

        int numNew = collection.size();
        if (numNew == 0) {
            return false;
        }

        if (numNew == 1) {
            add(collection.get(0), index);
            return true;
        }


        if (index == size) {
            addAll(collection);
            return true;
        }

        Node<E> node = head;
        Node<E> tmp;
        Node<E> newNode = null;

        if (index != 0) {
            if (index > 1) {
                for (int i = 0; i < index - 1; i++) {
                    node = node.next;
                }
            }

            tmp = node.next;

            for (int i = 0; i < collection.size(); i++) {
                newNode = new Node<>(node, collection.get(i), null);
                node.next = newNode;
                node = node.next;
            }

            newNode.next = tmp;
            tmp.prev = newNode;
        } else {
            Node<E> firstNode = null;
            for (int i = 0; i < collection.size(); i++) {
                if (i == 0) {
                    firstNode = new Node<>(null, collection.get(i), null);
                    head = firstNode;
                } else {
                    newNode = new Node<>(firstNode, collection.get(i), null);
                    firstNode.next = newNode;
                    firstNode = firstNode.next;
                }
            }

            firstNode.next = node;
            node.prev = firstNode;
        }

        size += collection.size();
        return true;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        Node<E> node = findNode(index);
        return node.element;
    }

    @Override
    public E set(E element, int index) {
        checkIndex(index);
        Node<E> node = findNode(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        return unLink(findNode(index));
    }

    @Override
    public boolean remove(E element) {
        for (Node<E> node = head; node != null; node = node.next) {
            if (Objects.equals(node.element, element)) {
                unLink(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (Node<E> node = tail; node != null; ) {
            Node<E> next = node.next;
            node.element = null;
            node.next = null;
            node.prev = null;
            node = next;
        }
        tail = head = null;
        size = 0;
    }

    @Override
    public boolean contains(Object object) {
        return indexOf(object) >= 0;
    }

    @Override
    public boolean containsAll(List<E> collection) {
        for (int i = 0; i < collection.size(); i++) {
            if (!contains(collection.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int indexOf(Object object) {
        int index = 0;
        if (object == null) {
            for (Node<E> node = tail; node != null; node = node.next) {
                if (node.element == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node<E> node = tail; node != null; node = node.next) {
                if (object.equals(node.element)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object object) {
        int index = size;
        if (object == null) {
            for (Node<E> node = head; node != null; node = node.prev) {
                index--;
                if (node.element == null) {
                    return index;
                }
            }
        } else {
            for (Node<E> node = head; node != null; node = node.prev) {
                index--;
                if (object.equals(node.element)) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(MESSAGE);
        }
        checkIndex(fromIndex);
        checkAddIndex(toIndex);

        Node<E> node = head;
        for (int i = 0; i < fromIndex; i++) {
            node = node.next;
        }

        List<E> newList = new LinkedList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            newList.add(node.element);
            node = node.next;
        }
        return newList;
    }

    @Override
    public boolean removeAll(List<E> collection) {
        boolean status = false;
        for (int i = 0; i < collection.size(); i++) {
            for (Node<E> node = head; node != null; node = node.next) {
                if (node.element.equals(collection.get(i))) {
                    remove(collection.get(i));
                    status = true;
                }
            }
        }
        return status;
    }

    @Override
    public boolean retainAll(List<E> collection) {
        boolean result = false;
        Node<E> node = head;
        while (node != null) {
            boolean status = true;
            for (int i = 0; i < collection.size(); i++) {
                if (node.element.equals(collection.get(i))) {
                    status = false;
                }
            }
            E element = node.element;
            node = node.next;
            if (status) {
                remove(element);
                result = true;
            }
        }
        return result;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        for (Node<E> node = head; node != null; node = node.next) {
            node.element = operator.apply(node.element);
        }
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        // TODO: implement sort method
    }

    @Override
    public int hashCode() {
        Node<E> node = head;
        int hashCode = 1;
        for (int i = 0; i < size; i++) {
            Object element = node.element;
            hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
            node = node.next;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof List)) {
            return false;
        }

        return equalsLinkedList((LinkedList<?>) object);
    }

    private boolean equalsLinkedList(LinkedList<?> other) {
        final int s = size;
        boolean equal = (s == other.size);
        if (equal) {
            Node<?> otherEs = other.head;
            for (Node<E> node = head; node != null; node = node.next) {
                if (!Objects.equals(otherEs.element, node.element)) {
                    equal = false;
                    break;
                }
                otherEs = otherEs.next;
            }
        }
        return equal;
    }

    private void checkAddIndex(int index) {
        if (!(index >= 0 && index <= size)) {
            throw new IndexOutOfBoundsException(MESSAGE);
        }
    }

    private void addIndex(E element, int index) {
        if (index == size) {
            link(element);
        } else {
            Node<E> node = findNode(index);
            Node<E> prevNode = node.prev;
            Node<E> newNode = new Node<>(prevNode, element, node);
            node.prev = newNode;
            if (prevNode == null) {
                head = newNode;
            } else {
                prevNode.next = newNode;
            }
        }
        size++;
    }

    private void link(E element) {
        Node<E> prevNode = tail;
        Node<E> newNode = new Node<>(tail, element, null);
        tail = newNode;
        if (prevNode == null) {
            head = newNode;
        } else {
            prevNode.next = newNode;
        }
    }

    private Node<E> findNode(int index) {
        Node<E> node;
        if (index > size) {
            node = tail;
            for (int i = index; i < size - 1; i++) {
                node = node.prev;
            }
        } else {
            node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }
        return node;
    }

    private void checkIndex(int index) {
        if (!(index >= 0 && index < size)) {
            throw new IndexOutOfBoundsException(MESSAGE);
        }
    }

    private E unLink(Node<E> findNode) {
        final E element = findNode.element;
        Node<E> next = findNode.next;
        Node<E> prev = findNode.prev;
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            findNode.next = null;
        }
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            findNode.prev = null;
        }
        findNode.element = null;
        size--;
        return element;
    }
}
