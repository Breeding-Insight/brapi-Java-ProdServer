package io.swagger.model;

public class FilterBy {
    private String filterColumn;
    private String value;
    private boolean addInfoColumn = false;

    public String getFilterColumn() {
        return filterColumn;
    }

    public void setFilterColumn(String filterColumn) {
        this.filterColumn = filterColumn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAddInfoColumn() {
        return addInfoColumn;
    }

    public void setAddInfoColumn(boolean addInfoColumn) {
        this.addInfoColumn = addInfoColumn;
    }

}
