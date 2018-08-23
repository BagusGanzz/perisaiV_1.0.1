package com.perisaimobile.iface;

import java.util.Collection;
import java.util.Set;

public interface IDataSet<T> {
    boolean addItem(T t);

    boolean addItems(Collection<? extends T> collection);

    int getItemCount();

    Set<T> getSet();

    boolean removeItem(T t);
}
