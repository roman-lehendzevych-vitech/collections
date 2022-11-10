package org.example.collections.arraylist;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class ArrayList<E> implements List<E> {
    private static final String MESSAGE = "Error! Index must be greater then 0 and less then ";
    private static final int INITIAL_SIZE = 10;
    private int size;
    private E[] elements;

    public ArrayList() {
        elements = (E[]) new Object[INITIAL_SIZE];
    }

    @Override
    public void add(E element) {
        checkSize();
        elements[size] = element;
        size++;
    }

    @Override
    public void add(E element, int index) {
        checkIndexRange(index);
        checkSize();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public void addAll(List<E> list) {
        IntStream.range(0, list.size()).forEach(i -> add(list.get(i)));
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public void set(E element, int index) {
        checkIndex(index);
        elements[index] = element;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E removedElement = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        return removedElement;
    }

    @Override
    public E remove(E element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, elements[i])) {
                return remove(i);
            }
        }
        throw new NoSuchElementException("Error! Input element wasn't found!");
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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object object) {
        return indexOf(object) >= 0;
    }

    @Override
    public int indexOf(Object object) {
        return indexOfRange(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return lastIndexOfRange(object);
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
    public int hashCode() {
        final Object[] es = elements;
        int hashCode = 1;
        for (int i = 0; i < size; i++) {
            Object element = es[i];
            hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
        }
        return hashCode;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex);
        E[] newArray = (E[]) new Object[toIndex - fromIndex];
        System.arraycopy(elements, fromIndex, newArray, 0, toIndex - fromIndex);
        List<E> list = new ArrayList<>();
        for (E e : newArray) {
            list.add(e);
        }
        return list;
    }

    @Override
    public boolean addAll(int index, List<E> collection) {
        checkIndexRange(index);
        boolean modified = false;
        for (int i = 0; i < collection.size(); i++) {
            add(collection.get(i), index++);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(List<E> collection) {
        return batchRemove(collection, false);
    }

    @Override
    public boolean retainAll(List<E> collection) {
        return batchRemove(collection, true);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        E[] es = this.elements;
        for (int i = 0; i < es.length; i++) {
            es[i] = operator.apply(es[i]);
        }
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        Arrays.sort(elements, comparator);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof List)) {
            return false;
        }

        return equalsArrayList((ArrayList<?>) object);
    }

    private boolean equalsArrayList(ArrayList<?> other) {
        final int s = size;
        boolean equal = (s == other.size);
        if (equal) {
            final Object[] otherEs = other.elements;
            final Object[] es = elements;
            for (int i = 0; i < s; i++) {
                if (!Objects.equals(es[i], otherEs[i])) {
                    equal = false;
                    break;
                }
            }
        }
        return equal;
    }

    private boolean batchRemove(List<E> collection, boolean complement) {
        Objects.requireNonNull(collection);
        final Object[] es = elements;
        int count;
        for (count = 0;; count++) {
            if (count == size) {
                return false;
            }
            if (collection.contains(es[count]) != complement) {
                break;
            }
        }
        int min = count++;
        try {
            for (Object element; count < size; count++) {
                if (collection.contains(element = es[count]) == complement) {
                    es[min++] = element;
                }
            }
        } catch (Throwable ex) {
            System.arraycopy(es, count, es, min, size - count);
            min += size - count;
            throw ex;
        } finally {
            shiftTailOverGap(es, min, size);
        }
        return true;
    }

    private void shiftTailOverGap(Object[] es, int min, int max) {
        System.arraycopy(es, max, es, min, size - max);
        for (int to = size, i = (size -= max - min); i < to; i++) {
            es[i] = null;
        }
    }

    private void subListRangeCheck(int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
    }

    private int indexOfRange(Object object) {
        Object[] es = elements;
        if (object == null) {
            for (int i = 0; i < size; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (object.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lastIndexOfRange(Object object) {
        Object[] es = elements;
        if (object == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (object.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(MESSAGE + size);
        }
    }

    private void checkIndexRange(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(MESSAGE + size);
        }
    }

    private void checkSize() {
        if (size == elements.length) {
            grow();
        }
    }

    private void grow() {
        E[] newArray = (E[]) new Object[size + (size >> 1)];
        System.arraycopy(elements, 0, newArray, 0, elements.length);
        elements = newArray;
    }
}
