package io.swagger.model.core;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.BrAPIResponseResult;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

/**
 * ListItemsListResponseResult
 */
@Validated
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
public class ListItemsListResponseResult<T> implements BrAPIResponseResult<ListItemWithFullItem<T>>  {
    @JsonProperty("data")
    @Valid
    private List<ListItemWithFullItem<T>> data = new ArrayList<ListItemWithFullItem<T>>();

    public ListItemsListResponseResult data(List<ListItemWithFullItem<T>> data) {
        this.data = data;
        return this;
    }

    public ListItemsListResponseResult addDataItem(ListItemWithFullItem<T> dataItem) {
        this.data.add(dataItem);
        return this;
    }

    /**
     * Get data
     * @return data
     **/
    @ApiModelProperty(required = true, value = "")

    @Valid
    public List<ListItemWithFullItem<T>> getData() {
        return data;
    }

    public void setData(List<ListItemWithFullItem<T>> data) {
        this.data = data;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListItemsListResponseResult listItemsListResponseResult = (ListItemsListResponseResult) o;
        return Objects.equals(this.data, listItemsListResponseResult.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListItemsListResponseResult {\n");

        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

