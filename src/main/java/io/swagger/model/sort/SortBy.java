package io.swagger.model.sort;

public class SortBy {
    private String sortedOn;
    private SortOrder  sortOrder = SortOrder.ASC;

    public SortBy(String sortedOn,
                  SortOrder sortOrder) {
        this.sortedOn = sortedOn;
        this.sortOrder = sortOrder;
    }

    public SortBy() {}

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
}
