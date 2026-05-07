package io.swagger.model.sort;

// TODO: Replace io.swagger.model.core.SortBy with this class and rename this class to SortBy
public class SortByEntry {
    private String sortedOn;
    private SortOrder  sortOrder = SortOrder.ASC;
    private boolean addInfoColumn = false;

    public SortByEntry(String sortedOn,
                       SortOrder sortOrder,
                       boolean addInfoColumn) {
        this.sortedOn = sortedOn;
        this.sortOrder = sortOrder;
        this.addInfoColumn = addInfoColumn;
    }

    public String getSortedOn() {
        return sortedOn;
    }

    public void setSortedOn(String sortedOn) {
        this.sortedOn = sortedOn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isAddInfoColumn() {
        return addInfoColumn;
    }

    public void setAddInfoColumn(boolean addInfoColumn) {
        this.addInfoColumn = addInfoColumn;
    }
}
