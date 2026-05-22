package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.model.sort.SortByElement;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SearchRequest {
	@JsonIgnore
	public abstract Integer getTotalParameterCount();

	@JsonProperty("page")
	protected Integer page = null;

	@JsonProperty("pageSize")
	protected Integer pageSize = null;

	@JsonProperty("pageToken")
	protected String pageToken = null;

	@JsonProperty("externalReferenceIds")
	protected List<String> externalReferenceIds = null;

	@JsonProperty("externalReferenceSources")
	protected List<String> externalReferenceSources = null;

	@JsonProperty("filterBy")
	protected List<FilterBy> filterBy = null;

	@JsonProperty("sortBy")
	protected List<SortByElement> sortBy = null;

	@JsonIgnore
	protected Map<String, String> sortFilterEntityColumnNamesByRequestName = null;

	@JsonIgnore
	public List<String> getExternalReferenceIds() {
		return externalReferenceIds;
	}

	final public SearchRequest page(Integer page) {
		this.page = page;
		return this;
	}

	final public Integer getPage() {
		return page;
	}

	final public void setPage(Integer page) {
		this.page = page;
	}

	final public SearchRequest pageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	final public Integer getPageSize() {
		return pageSize;
	}

	final public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public SearchRequest pageToken(String pageToken) {
		this.pageToken = pageToken;
		return this;
	}

	public String getPageToken() {
		return pageToken;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	final public SearchRequest externalReferenceIDs(List<String> externalReferenceIds) {
		this.externalReferenceIds = externalReferenceIds;
		return this;
	}

	final private SearchRequest addExternalReferenceIDsItem(String externalReferenceId) {
		if (this.externalReferenceIds == null) {
			this.externalReferenceIds = new ArrayList<String>();
		}
		this.externalReferenceIds.add(externalReferenceId);
		return this;
	}

	final public List<String> getExternalReferenceIDs() {
		return externalReferenceIds;
	}

	final public void setExternalReferenceIDs(List<String> externalReferenceIds) {
		this.externalReferenceIds = externalReferenceIds;
	}

	final public SearchRequest externalReferenceSources(List<String> externalReferenceSources) {
		this.externalReferenceSources = externalReferenceSources;
		return this;
	}

	final private SearchRequest addExternalReferenceSourcesItem(String externalReferenceSourcesItem) {
		if (this.externalReferenceSources == null) {
			this.externalReferenceSources = new ArrayList<String>();
		}
		this.externalReferenceSources.add(externalReferenceSourcesItem);
		return this;
	}

	final public List<String> getExternalReferenceSources() {
		return externalReferenceSources;
	}

	final public void setExternalReferenceSources(List<String> externalReferenceSources) {
		this.externalReferenceSources = externalReferenceSources;
	}

	public void addExternalReferenceItem(String externalReferenceId, String externalReferenceID,
			String externalReferenceSource) {
		if (externalReferenceId != null) {
			this.addExternalReferenceIDsItem(externalReferenceId);
		} else if (externalReferenceID != null) {
			this.addExternalReferenceIDsItem(externalReferenceID);
		}

		if (externalReferenceSource != null) {
			this.addExternalReferenceSourcesItem(externalReferenceSource);
		}

	}

	public List<FilterBy> getFilterBy() {
		return filterBy;
	}

	@JsonSetter("filterBy")
	public void setFilterBy(List<FilterBy> filterBy) throws BrAPIServerException {

		if (filterBy == null || filterBy.isEmpty()) {
			return;
		}

		Map<String, String> allowedSortFilterNames = getSortFilterEntityColumnNamesByRequestName();

		for (FilterBy filterByItem : filterBy) {

			if (filterByItem.getFilterOn() == null || filterByItem.getFilterOn().isEmpty()) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "filterOn attribute not provided in element of filterBy list.");
			}

			if (filterByItem.getValue() == null || filterByItem.getValue().isEmpty()) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "value attribute not provided in element of filterBy list.");
			}

			String filterColumnEntityName = getSortFilterEntityColumnNamesByRequestName().get(filterByItem.getFilterOn());

			if (filterColumnEntityName == null) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
						String.format("Supplied filterColumn [%s] not available in allowed names [%s]", filterByItem.getFilterOn(), allowedSortFilterNames.keySet())
				);
			} else {
				// Remap suppliedFilterColumn to actual entity name supplied by mapper
				filterByItem.setFilterOn(filterColumnEntityName);
			}
		}
		this.filterBy = filterBy;
	}

	public List<SortByElement> getSortByElements() {
		return sortBy;
	}

	@JsonSetter("sortBy")
	public void setSortByElements(List<SortByElement> sortBy) throws BrAPIServerException {

		if (sortBy == null || sortBy.isEmpty()) {
			return;
		}

		Map<String, String> allowedSortFilterNames = getSortFilterEntityColumnNamesByRequestName();

		for (SortByElement sortByItem : sortBy) {
			if (sortByItem.getSortedOn() == null || sortByItem.getSortedOn().isEmpty()) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "sortedOn attribute not provided in element of sortBy list");
			}

			String sortColumnEntityName = getSortFilterEntityColumnNamesByRequestName().get(sortByItem.getSortedOn());

			if (sortColumnEntityName == null) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
						String.format("Supplied sortColumn [%s] not available in allowed names [%s]", sortByItem.getSortedOn(), allowedSortFilterNames.keySet())
				);
			} else {
				// Remap suppliedFilterColumn to actual entity name supplied by mapper
				sortByItem.setSortedOn(sortColumnEntityName);
			}
		}

		this.sortBy = sortBy;
	}

	public Map<String, String> getSortFilterEntityColumnNamesByRequestName() {
		throw new UnsupportedOperationException(String.format("Sort/Filtering not implemented for %s", this.getClass().getSimpleName()));
	}
}
