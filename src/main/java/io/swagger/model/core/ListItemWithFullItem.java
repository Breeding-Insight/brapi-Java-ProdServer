package io.swagger.model.core;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * ListItemWithFullItem
 */
@Validated
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
public class ListItemWithFullItem<T>  {
    @JsonProperty("listItem")
    private ListItem listItem = null;

    @JsonProperty("item")
    private T item = null;

    /**
     * The unique identifier for a List
     * @return listItem
     **/
    @ApiModelProperty(example = "6f621cfa", required = true, value = "The unique identifier for a List")


    public ListItem getlistItem() {
        return listItem;
    }

    public void setlistItem(ListItem listItem) {
        this.listItem = listItem;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListItemWithFullItem listItemWithFullItem = (ListItemWithFullItem) o;
        return Objects.equals(this.listItem, listItemWithFullItem.listItem) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listItem, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListItemWithFullItem {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    listItem: ").append(toIndentedString(listItem)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

