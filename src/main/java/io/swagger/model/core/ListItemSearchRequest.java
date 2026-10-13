package io.swagger.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ListItemSearchRequest {

    @JsonProperty("searchRequest")
    private JsonNode searchRequest;

    // List-item-specific filters and sorting can go here.

    public JsonNode getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(JsonNode searchRequest) {
        this.searchRequest = searchRequest;
    }
}
