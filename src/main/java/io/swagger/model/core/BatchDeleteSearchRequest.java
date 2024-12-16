package io.swagger.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.model.SearchRequest;
import io.swagger.model.geno.SampleSearchRequest;
import io.swagger.model.germ.GermplasmSearchRequest;

public class BatchDeleteSearchRequest extends SearchRequest {
	@JsonProperty("batchDeleteType")
	private BatchDeleteTypes batchDeleteType = null;

	@JsonProperty("search")
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "batchDeleteType")
	@JsonSubTypes({
			@JsonSubTypes.Type(value = GermplasmSearchRequest.class, name = "germplasm"),
			@JsonSubTypes.Type(value = ListSearchRequest.class, name = "lists"),
			@JsonSubTypes.Type(value = TrialSearchRequest.class, name = "trials"),
			@JsonSubTypes.Type(value = SampleSearchRequest.class, name = "samples")
	})
	private SearchRequest searchRequest = null;

	public BatchDeleteSearchRequest batchDeleteType(BatchDeleteTypes batchDeleteType) {
		this.batchDeleteType = batchDeleteType;
		return this;
	}

	public BatchDeleteTypes getBatchDeleteType() {
		return batchDeleteType;
	}

	public void setBatchDeleteType(BatchDeleteTypes batchDeleteType) {
		this.batchDeleteType = batchDeleteType;
	}

	public SearchRequest getSearchRequest() {
		return searchRequest;
	}

	public void setSearchRequest(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		return searchRequest.equals(o);
	}

	@Override
	public int hashCode() {
		return searchRequest.hashCode();
	}

	@Override
	public String toString() {
		return searchRequest.toString();
	}

	@Override
	public Integer getTotalParameterCount() {
		return searchRequest.getTotalParameterCount();
	}
}