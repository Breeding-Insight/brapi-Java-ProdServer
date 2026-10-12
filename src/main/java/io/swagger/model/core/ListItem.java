package io.swagger.model.core;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * ListItem
 */
@Validated
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
public class ListItem  {
    @JsonProperty("listItemDbId")
    private String listItemDbId = null;

    @JsonProperty("position")
    private Integer position = null;

    // This represents the actual value stored in the list_item table
    @JsonProperty("item")
    private String item = null;

    public ListItem listDbId(String listDbId) {
        this.listItemDbId = listDbId;
        return this;
    }

    /**
     * The unique identifier for a List
     * @return listDbId
     **/
    @ApiModelProperty(example = "6f621cfa", required = true, value = "The unique identifier for a List")


    public String getListItemDbId() {
        return listItemDbId;
    }

    public void setListItemDbId(String listItemDbId) {
        this.listItemDbId = listItemDbId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
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
        ListItem ListItem = (ListItem) o;
        return Objects.equals(this.listItemDbId, ListItem.listItemDbId) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listItemDbId, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListItem {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    listDbId: ").append(toIndentedString(listItemDbId)).append("\n");
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


