package com.perisaimobile.iface;

public interface IFactory<T> {
    T createInstance(String str);
}
