package com.perisaimobile.adapter;

public interface IResultsAdapterItem {

    public enum ResultsAdapterItemType {
        Header,
        AppMenace,
        SystemMenace
    }

    ResultsAdapterItemType getType();
}
