package io.swagger.model;

import io.swagger.model.sort.SortOrder;

public class FilterBy {
    private String filterOn;
    private boolean addInfoColumn = false;

    public String getFilterOn() {
        return filterOn;
    }

    public void setFilterOn(String filterOn) {
        this.filterOn = filterOn;
    }

    public boolean isAddInfoColumn() {
        return addInfoColumn;
    }

    public void setAddInfoColumn(boolean addInfoColumn) {
        this.addInfoColumn = addInfoColumn;
    }

}
