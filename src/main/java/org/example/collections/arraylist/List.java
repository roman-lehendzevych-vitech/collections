package org.example.collections.arraylist;

import java.util.Comparator;
import java.util.function.UnaryOperator;

public interface List<E> {
    void add(E element);

    void add(E element, int index);

    void addAll(List<E> list);

    E get(int index);

    void set(E element, int index);

    E remove(int index);

    E remove(E element);

    int size();

    boolean isEmpty();

    void clear();

    boolean contains(Object object);

    boolean containsAll(List<E> collection);

    int indexOf(Object object);

    int lastIndexOf(Object object);

    int hashCode();

    List<E> subList(int fromIndex, int toIndex);

    boolean addAll(int index, List<E> collection);

    boolean removeAll(List<E> collection);

    boolean retainAll(List<E> collection);

    void replaceAll(UnaryOperator<E> operator);

    void sort(Comparator<? super E> comparator);

    boolean equals(Object object);
}
