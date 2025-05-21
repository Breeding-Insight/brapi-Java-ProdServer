package org.brapi.test.BrAPITestServer.service;

import java.math.BigDecimal;
import java.util.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import io.swagger.model.GeoJSONSearchArea;
import io.swagger.model.core.SortOrder;

public class SearchQueryBuilder<T> {

	private String selectClause;
	private String selectOnlyIds;
	private String whereClause;
	private String defaultSort;
	private String sortClause;
	private Map<String, Object> params;
	private Class<T> clazz;

	public SearchQueryBuilder(Class<T> clazz) {
		this.selectClause = "SELECT distinct entity FROM " + clazz.getSimpleName() + " entity ";
		this.selectOnlyIds = "SELECT entity.id FROM " + clazz.getSimpleName() + " entity ";
		this.whereClause = "WHERE 1=1 ";
		this.defaultSort = " ORDER BY entity.id ASC ";
		this.sortClause = "";
		this.params = new HashMap<>();
		this.clazz = clazz;
	}

	public String getQuery() {
		if (sortClause.isEmpty()) {
			// By default, sort on entity id to have query result remain idempotent
			sortClause = defaultSort;
		}

		return selectClause + whereClause + sortClause;
	}

	public String getIdQuery() {
		if (sortClause.isEmpty()) {
			// By default, sort on entity id to have query result remain idempotent
			sortClause = defaultSort;
		}

		return selectOnlyIds + whereClause + sortClause;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public SearchQueryBuilder<T> appendList(List<String> list, String columnName) {
		String paramName = paramFilter(columnName);
		if (list != null && !list.isEmpty()) {
			this.whereClause += "AND " + entityPrefix(columnName) + " in :" + paramName + " ";
			this.params.put(paramName, list);
		}
		return this;
	}


	public SearchQueryBuilder<T> appendIds(List<UUID> ids) {
		return appendIds(ids, null);
	}

	public SearchQueryBuilder<T> appendIds(List<UUID> ids, String inputColumnName) {
		String columnName = inputColumnName == null ? "id" : inputColumnName;
		String paramName = paramFilter(columnName);
		if (ids != null && !ids.isEmpty()) {
			this.whereClause += "AND " + entityPrefix(columnName) + " in :" + paramName + " ";
			this.params.put(paramName, ids);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendIntList(List<Integer> list, String columnName) {
		String paramName = paramFilter(columnName);
		if (list != null && !list.isEmpty()) {
			this.whereClause += "AND " + entityPrefix(columnName) + " in :" + paramName + " ";
			this.params.put(paramName, list);
		}
		return this;
	}

	public <E extends Enum<E>> SearchQueryBuilder<T> appendEnumList(List<E> list, String columnName) {
		String paramName = paramFilter(columnName);
		if (list != null && !list.isEmpty()) {
			this.whereClause += "AND " + entityPrefix(columnName) + " in :" + paramName + " ";
			this.params.put(paramName, list);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendSingle(Boolean single, String columnName) {
		String paramName = paramFilter(columnName);
		if (single != null) {
			this.whereClause += "AND " + entityPrefix(columnName) + " = :" + paramName + " ";
			this.params.put(paramName, single);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendSingle(Integer single, String columnName) {
		String paramName = paramFilter(columnName);
		if (single != null) {
			this.whereClause += "AND " + entityPrefix(columnName) + " = :" + paramName + " ";
			this.params.put(paramName, single);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendSingle(String single, String columnName) {
		String paramName = paramFilter(columnName);
		if (single != null && !single.isEmpty()) {
			this.whereClause += "AND " + entityPrefix(columnName) + " = :" + paramName + " ";
			this.params.put(paramName, single);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendSingle(UUID single, String columnName) {
		String paramName = paramFilter(columnName);
		if (single != null) {
			this.whereClause += "AND " + entityPrefix(columnName) + " = :" + paramName + " ";
			this.params.put(paramName, single);
		}
		return this;
	}

	public <E extends Enum<E>> SearchQueryBuilder<T> appendEnum(E enumVal, String columnName) {
		String paramName = paramFilter(columnName);
		if (enumVal != null) {
			this.whereClause += "AND " + entityPrefix(columnName) + " = :" + paramName + " ";
			this.params.put(paramName, enumVal);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendDateRange(LocalDate start, LocalDate end, String columnName) {
		return appendDateRange(DateUtility.toDate(start), DateUtility.toDate(end), columnName);
	}

	public SearchQueryBuilder<T> appendDateRange(OffsetDateTime start, OffsetDateTime end, String columnName) {
		return appendDateRange(DateUtility.toDate(start), DateUtility.toDate(end), columnName);
	}

	public SearchQueryBuilder<T> appendDateRange(Date start, Date end, String columnName) {
		String paramNameStart = paramFilter(columnName) + "Start";
		String paramNameEnd = paramFilter(columnName) + "End";
		if (start != null && end != null) {
			whereClause += "AND " + entityPrefix(columnName) + " BETWEEN :" + paramNameStart + " AND :" + paramNameEnd
					+ " ";
			params.put(paramNameStart, start);
			params.put(paramNameEnd, end);
		} else if (start != null) {
			whereClause += "AND " + entityPrefix(columnName) + " >= :" + paramNameStart + " ";
			params.put(paramNameStart, start);
		} else if (end != null) {
			whereClause += "AND " + entityPrefix(columnName) + " <= :" + paramNameEnd + " ";
			params.put(paramNameEnd, end);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendNumberRange(Integer min, Integer max, String columnName) {
		String paramNameMin = paramFilter(columnName) + "Min";
		String paramNameMax = paramFilter(columnName) + "Max";
		if (min != null && max != null) {
			whereClause += "AND " + entityPrefix(columnName) + " BETWEEN :" + paramNameMin + " AND :" + paramNameMax
					+ " ";
			params.put(paramNameMin, min);
			params.put(paramNameMax, max);
		} else if (min != null) {
			whereClause += "AND " + entityPrefix(columnName) + " >= :" + paramNameMin + " ";
			params.put(paramNameMin, min);
		} else if (max != null) {
			whereClause += "AND " + entityPrefix(columnName) + " <= :" + paramNameMax + " ";
			params.put(paramNameMax, max);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendNumberRange(BigDecimal min, BigDecimal max, String columnName) {
		String paramNameMin = paramFilter(columnName) + "Min";
		String paramNameMax = paramFilter(columnName) + "Max";
		if (min != null && max != null) {
			whereClause += "AND " + entityPrefix(columnName) + " BETWEEN :" + paramNameMin + " AND :" + paramNameMax
					+ " ";
			params.put(paramNameMin, min);
			params.put(paramNameMax, max);
		} else if (min != null) {
			whereClause += "AND " + entityPrefix(columnName) + " >= :" + paramNameMin + " ";
			params.put(paramNameMin, min);
		} else if (max != null) {
			whereClause += "AND " + entityPrefix(columnName) + " <= :" + paramNameMax + " ";
			params.put(paramNameMax, max);
		}
		return this;
	}

	public SearchQueryBuilder<T> appendNamesList(List<String> list, String columnFirst, String columnMiddle,
			String columnLast) {
		if (list != null && !list.isEmpty()) {
			this.params.put("namesList", list);
			this.whereClause += "AND (" + entityPrefix(columnFirst) + " in :namesList ";
			this.whereClause += "OR " + entityPrefix(columnMiddle) + " in :namesList ";
			this.whereClause += "OR " + entityPrefix(columnLast) + " in :namesList ";
			this.whereClause += "OR concat(" + entityPrefix(columnFirst) + ", ' ', " + entityPrefix(columnMiddle)
					+ ") in :namesList ";
			this.whereClause += "OR concat(" + entityPrefix(columnFirst) + ", ' ', " + entityPrefix(columnLast)
					+ ") in :namesList ";
			this.whereClause += "OR concat(" + entityPrefix(columnMiddle) + ", ' ', " + entityPrefix(columnLast)
					+ ") in :namesList ";
			this.whereClause += "OR concat(" + entityPrefix(columnFirst) + ", ' ', " + entityPrefix(columnMiddle)
					+ ", ' ', " + entityPrefix(columnLast) + ") in :namesList ";
			this.whereClause += ") ";
		}
		return this;
	}

	public SearchQueryBuilder<T> appendGeoJSONArea(GeoJSONSearchArea area) {
		// if (single != null && !single.isEmpty()) {
		// this.query += "AND " + entityPrefix(columnName) + " = :" + columnName + " ";
		// this.params.put(columnName, single);
		// }
		return this;
	}

	public SearchQueryBuilder<T> withExRefs(String externalReferenceID, String externalReferenceSource) {
		List<String> exRefIds = new ArrayList<>();
		List<String> exRefSources = new ArrayList<>();
		if (externalReferenceID != null)
			exRefIds.add(externalReferenceID);
		if (externalReferenceSource != null)
			exRefSources.add(externalReferenceSource);
		return withExRefs(exRefIds, exRefSources);
	}

	public SearchQueryBuilder<T> withExRefs(List<String> exRefIds, List<String> exRefSources) {
		if ((exRefIds != null && !exRefIds.isEmpty()) || (exRefSources != null && !exRefSources.isEmpty())) {
			this.join("externalReferences", "externalReference");
		}
		if (exRefIds != null && !exRefIds.isEmpty()) {
			this.whereClause += "AND externalReference.externalReferenceId in :externalReferenceId ";
			this.params.put("externalReferenceId", exRefIds);
		}
		if (exRefSources != null && !exRefSources.isEmpty()) {
			this.whereClause += "AND externalReference.externalReferenceSource in :externalReferenceSource ";
			this.params.put("externalReferenceSource", exRefSources);
		}
		return this;
	}

	public SearchQueryBuilder<T> join(String join, String name) {
		this.selectClause += "JOIN " + entityPrefix(join) + " " + paramFilter(name) + " ";
		this.selectOnlyIds += "JOIN " + entityPrefix(join) + " " + paramFilter(name) + " ";
		return this;
	}

	public SearchQueryBuilder<T> leftJoinFetch(String join, String name) {
		this.selectClause += generateLeftJoinFetch(join, name);
		return this;
	}

	/**
	 * Use this method to remove left join fetches from specific collection attributes so you can leverage the same query to
	 * iterate through other lazily loaded collections on an entity you need to fetch.
	 */
	public SearchQueryBuilder<T> removeAndReplaceLeftJoinFetch(String join,
															   String name,
															   String existingJoin,
															   String existingName) {

		this.selectClause =
				this.selectClause.replace(generateLeftJoinFetch(existingJoin, existingName), generateLeftJoinFetch(join, name));

		return this;
	}

	/**
	 * Use this method to remove left join fetches from specific collection attributes so you can leverage the same
	 * base query criteria to add another join fetch with leftJoinFetch()
	 */
	public SearchQueryBuilder<T> removeLeftJoinFetch(String join, String name) {
		this.selectClause =
				this.selectClause.replace(generateLeftJoinFetch(join, name), "");
		return this;
	}

	private String generateLeftJoinFetch(String join, String paramName) {
		return "LEFT JOIN FETCH " + entityPrefix(join) + " " + paramFilter(paramName) + " ";
	}

	private String entityPrefix(String field) {
		if (field.startsWith("*")) {
			return field.substring(1);
		} else {
			return "entity." + field;
		}
	}

	private String paramFilter(String param) {
		if (param == null)
			return "";
		return param.replace('.', '_').replace('*', '_');
	}

	public SearchQueryBuilder<T> withSort(String sortByStr, SortOrder sortOrder) {
		String sortOrderStr = "ASC";
		if (sortOrder != null) {
			sortOrderStr = sortOrder.toString();
		}

		this.sortClause += " ORDER BY " + entityPrefix(sortByStr) + " " + sortOrderStr;

		return this;
	}
}
