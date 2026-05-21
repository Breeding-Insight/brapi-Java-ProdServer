package io.swagger.model.sort;

// TODO: Replace io.swagger.model.core.SortBy with this class and rename this class to SortBy
public class SortByElement {
    private String sortedOn;
    private SortOrder  sortOrder = SortOrder.ASC;

    public SortByElement(String sortedOn,
                         SortOrder sortOrder,
                         boolean addInfoColumn) {
        this.sortedOn = sortedOn;
        this.sortOrder = sortOrder;
    }

    public SortByElement() {}

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
