package org.brapi.test.BrAPITestServer.service;

import java.math.BigDecimal;
import java.util.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import io.swagger.model.FilterBy;
import io.swagger.model.GeoJSONSearchArea;
import io.swagger.model.sort.SortBy;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.dto.EntityColumnNameAndType;
import org.brapi.test.BrAPITestServer.model.dto.EntityType;
import org.springframework.http.HttpStatus;

public class SearchQueryBuilder<T> {

	private String selectClause;
	private String selectOnlyIds;
	private String selectOnlyIdsSubquery;
	private String whereClause;
	private String defaultSort;
	private String sortClause;
	private Map<String, Object> params;
	private List<String> joinedTables = new ArrayList<>();
	private List<String> joinedFetchedTables = new ArrayList<>();
	private Class<T> clazz;

	public SearchQueryBuilder(Class<T> clazz) {
		this.selectClause = "SELECT distinct entity FROM " + clazz.getSimpleName() + " entity ";
		// This is the top level query for selectOnlyIds.  It is used to construct the full id query in getIdQuery()
		this.selectOnlyIds = "SELECT entity.id FROM " + clazz.getSimpleName() + " entity ";
		// This subquery will contain all the joins and filters necessary for the selectOnlyIds query and protects against duplicates using distinct
		this.selectOnlyIdsSubquery = "SELECT distinct entity2.id FROM " + clazz.getSimpleName() + " entity2 ";
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

	/**
	 * The goal of this query is to return a query that will return only the BrAPI dbIds of the source entity, applying
	 * sorting, filtering, and pagination to the query.
	 *
	 * This has to be done in a very deliberate way because of both HQL and postgres constraints, so this query should
	 * end up looking something like this:
	 *
	 * SELECT entity.id
	 * FROM GermplasmEntity entity
	 * WHERE entity.id IN (
	 *     SELECT distinct entity2.id
	 *     FROM GermplasmEntity entity2
	 *     JOIN entity2.externalReferences externalReference
	 *     WHERE 1=1 AND externalReference.externalReferenceId in :externalReferenceId
	 *     AND externalReference.externalReferenceSource in :externalReferenceSource
	 *     AND entity2.program.id in :program_id
	 *     )
	 * ORDER BY entity.id ASC
	 *
	 * By containing the filtering and the joining in a subquery with a DISTINCT, we can control potential duplicates from breaking through the cracks.
	 * Then we can apply sorting outside of this query without a distinct to avoid postgres's requirement to include every order by column in the select clause.
	 * Distinct is not an issue here, since we are selecting only on id, which is always unique.
	 */
	public String getIdQuery() {
		if (sortClause.isEmpty()) {
			// By default, sort on entity id to have query result remain idempotent
			sortClause = defaultSort;
		}

		// Now build the subquery to apply all the same filters/joins built up on the original entity with the inner entity.

		// To do this, create a regex expression which can be used to identity all instances of an "entity" without any proceeding
		// words, dots, underscores, or colons, and ignore any existing instances of entity2, which exists already in selectOnlyIdsSubquery
		String entityRegex = "(?<![\\\\w.:])entity(?!2)";

		// Now put the subquery together with the where clause and apply the regex expression
		String fullIdsSubquery = (selectOnlyIdsSubquery + whereClause).replaceAll(entityRegex, "entity2");

		// Finally, apply outer expression and final where clause
		return selectOnlyIds + " WHERE entity.id IN (" + fullIdsSubquery + ") " + sortClause;
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

	public SearchQueryBuilder<T> appendLike(String like, String columnName) {
		String paramName = paramFilterPattern(columnName);

		if (like != null) {
			this.whereClause += "AND  lower(" + entityPrefix(columnName) + ") LIKE :" + paramName + " ";
			this.params.put(paramName, "%" + like + "%");
		}
		return this;
	}

	// Used to convert non-string fields to string and use a like filter comparison
	public SearchQueryBuilder<T> appendLikeString(String like, String columnName) {
		String paramName = paramFilterPattern(columnName);

		if (like != null) {
			this.whereClause += "AND cast(" + entityPrefix(columnName) + " as String) LIKE :" + paramName + " ";
			this.params.put(paramName, "%" + like + "%");
		}
		return this;
	}

	public SearchQueryBuilder<T> appendLikeDate(String like, String columnName) {
		String paramName = paramFilterPattern(columnName);

		if (like != null) {
			this.whereClause += " AND to_char(" + entityPrefix(columnName) + ", 'YYYY-MM-DD') LIKE :" + paramName + " ";
			this.params.put(paramName, "%" + like + "%");
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

		if (!this.joinedTables.contains(join) && !this.joinedFetchedTables.contains(join)) {
			this.selectClause += "JOIN " + entityPrefix(join) + " " + paramFilter(name) + " ";
			this.selectOnlyIdsSubquery += "JOIN " + entityPrefix(join) + " " + paramFilter(name) + " ";
			this.joinedTables.add(join);
		} else if (joinedFetchedTables.contains(join) && !this.joinedTables.contains(join)) {
			this.selectOnlyIdsSubquery += "JOIN " + entityPrefix(join) + " " + paramFilter(name) + " ";
		}
		return this;
	}

	public SearchQueryBuilder<T> leftJoinFetch(String join, String name) {
		return leftJoinFetch(join, name, false);
	}

	public SearchQueryBuilder<T> leftJoinFetch(String join, String name, boolean overrideExistingJoin) {

		if (this.joinedTables.contains(join) && overrideExistingJoin) {
			// Override existing normal join with join fetch if it already exists in a query
			// This override does not change the alias to the name provided, and assumes further usages will use the same alias.
			this.selectClause = this.selectClause.replace("JOIN " + entityPrefix(join), "LEFT JOIN FETCH " + entityPrefix(join));
			this.joinedFetchedTables.add(join);
			this.joinedTables.remove(join);
		} else if (!this.joinedFetchedTables.contains(join)) {
			this.selectClause += generateLeftJoinFetch(join, name);
			this.joinedFetchedTables.add(join);
		}
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

		this.joinedFetchedTables.remove(existingJoin);
		this.joinedFetchedTables.add(join);

		return this;
	}

	/**
	 * Use this method to remove left join fetches from specific collection attributes so you can leverage the same
	 * base query criteria to add another join fetch with leftJoinFetch()
	 */
	public SearchQueryBuilder<T> removeLeftJoinFetch(String join, String name) {
		this.selectClause =
				this.selectClause.replace(generateLeftJoinFetch(join, name), "");
		this.joinedFetchedTables.remove(join);
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

	private String paramFilterPattern(String param) {
		if (param == null)
			return "";
		return param.replace('.', '_').replace('*', '_') + "Pattern";
	}

	/**
	 * Takes a list of SortBy options that should typically come in a searchRequest, along with a map of the validated
	 * columns names.
	 * Applies the entries in the list to sort the SearchQuery.
	 *
	 * A SortBy has
	 *  - A column name
	 *  - An order (DESC, ASC)
	 */
	public SearchQueryBuilder<T> sortBy(List<SortBy> sortBy, Map<String, EntityColumnNameAndType> entityColAndTypeBySubmittedName) throws BrAPIServerException {

		if (sortBy == null || sortBy.isEmpty()) {
			return this;
		}

		for (SortBy sort : sortBy) {
			// At this point, the submitted sortBy name has been verified to be in entityColAndTypeBySubmittedName
			EntityColumnNameAndType entityColumnNameAndType = entityColAndTypeBySubmittedName.get(sort.getSortedOn());

			String entityColName = entityColumnNameAndType.getEntityColumnName();

			if (entityColName.startsWith("*")) {
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "Sorting on one to many relationships not supported");
			}

			String[] split = entityColName.split("\\.");

			if (split.length > 2) {
				// TODO: Implement this if it becomes a requirement
				throw new BrAPIServerException(HttpStatus.BAD_REQUEST, "Sorting on a table greater than one level from primary entity not allowed");
			}

			if (split.length == 2 && !split[1].equals("id")) {
				leftJoinFetch(split[0], split[0], true);
			}

			sort.setSortedOn(entityColName);

			if (sortBy.getFirst().equals(sort)) {
				this.sortClause += " ORDER BY ";
				buildSort(sort);
			} else {
				this.sortClause += ", ";
				buildSort(sort);
			}
		}

		return this;
	}

	// Used to continue utilizing the same search query, like in GermplasmService fetching without pagination use case
	public SearchQueryBuilder<T> resetSortClause() {
		this.sortClause = "";
		return this;
	}

	private void buildSort(SortBy sort) {
		this.sortClause += entityPrefix(sort.getSortedOn()) + " " + sort.getSortOrder() + " ";
	}

	/**
	 * Takes a list of FilterBy options that should typically come in a searchRequest, along with a map of the validated
	 * columns names and the data type they represent for accurate filtering on different data types.
	 * Applies the entries in the list to filter the SearchQuery.
	 *
	 * A FilterBy has
	 *  - A column name
	 *  - A value which the column name should be filtered on
	 */
	public SearchQueryBuilder<T> filterBy(List<FilterBy> filterBy, Map<String, EntityColumnNameAndType> entityColAndTypeBySubmittedName) throws BrAPIServerException {
		SearchQueryBuilder<T> searchQuery = this;

		if (filterBy == null || filterBy.isEmpty()) {
			return searchQuery;
		}

		for (FilterBy filter : filterBy) {
			// At this point, the submitted filterBy column name has been verified to be in entityColAndTypeBySubmittedName
			EntityColumnNameAndType entityColumnNameAndType = entityColAndTypeBySubmittedName.get(filter.getFilterOn());

			if (entityColumnNameAndType.getEntityColumnName().startsWith("*")) {
				joinCollectionColumn(entityColumnNameAndType.getEntityColumnName());
			}

			if (entityColumnNameAndType.getEntityType() == EntityType.TEXT) {
				searchQuery = appendLike(filter.getValue().toLowerCase(), entityColumnNameAndType.getEntityColumnName());
			} else if (entityColumnNameAndType.getEntityType() == EntityType.UUID) {
				searchQuery = appendLikeString(filter.getValue(), entityColumnNameAndType.getEntityColumnName());
			} else if (entityColumnNameAndType.getEntityType() == EntityType.NUMBER) {
				// Compare as String exact match for numbers, in case users send non-numeric characters.
				searchQuery = appendSingle(filter.getValue(), entityColumnNameAndType.getEntityColumnName());
			} else if (entityColumnNameAndType.getEntityType() == EntityType.DATE) {
				searchQuery = appendLikeDate(filter.getValue(), entityColumnNameAndType.getEntityColumnName());
			}
		}

		return searchQuery;
	}

	/**
	 * This helper method joins the table that a collection column name is related to if it doesn't exist already.
	 * This is particularly important for filter search requests because if the join doesn't exist, and it is referenced
	 * the query will not execute.
	 */
	private void joinCollectionColumn(String submittedSortFilterColumnName) {
		String joinTableName = submittedSortFilterColumnName.substring(1, submittedSortFilterColumnName.indexOf("."));

		this.join(joinTableName, joinTableName);
	}
}
