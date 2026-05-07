package io.swagger.model.sort;

public class SortBy {
    private String sortedOn;
    private SortOrder  sortOrder = SortOrder.ASC;
    private boolean addInfoColumn = false;

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
