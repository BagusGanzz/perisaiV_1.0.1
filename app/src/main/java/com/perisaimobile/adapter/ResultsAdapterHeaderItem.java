package com.perisaimobile.adapter;


class ResultsAdapterHeaderItem implements IResultsAdapterItem {
    String _description = null;

    public ResultsAdapterHeaderItem(String description) {
        this._description = description;
    }

    public String getDescription() {
        return this._description;
    }

    public ResultsAdapterItemType getType() {
        return ResultsAdapterItemType.Header;
    }
}
