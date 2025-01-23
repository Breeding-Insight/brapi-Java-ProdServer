package org.brapi.test.BrAPITestServer.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.brapi.test.BrAPITestServer.exceptions.InvalidPagingException;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;
import org.brapi.test.BrAPITestServer.model.entity.ExternalReferenceEntity;
import org.brapi.test.BrAPITestServer.service.SearchQueryBuilder;
import org.brapi.test.BrAPITestServer.service.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class BrAPIRepositoryImpl<T extends BrAPIPrimaryEntity, ID extends Serializable>
		extends SimpleJpaRepository<T, ID> implements BrAPIRepository<T, ID> {
	private final EntityManager entityManager;

	public BrAPIRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	/**
	 * 	Use this method to page simple entities with no lazily loaded collections or attributes.
	 * 	WARN: Failure to do so can easily exhaust memory at scale and will cause hibernate warnings.
	 */
	public Page<T> findAllBySearchAndPaginate(SearchQueryBuilder<T> searchQuery, Pageable pageReq)
		throws InvalidPagingException {
		applyUserId(searchQuery);

		Long totalCount = getTotalCount(searchQuery);

		validatePageNumber(pageReq, totalCount);

		if (totalCount == 0) {
			// Short-circuit to avoid running possible long-running query
			return new PageImpl<>(Collections.emptyList(), pageReq, totalCount);
		}

		List<T> content = getPagedContent(searchQuery, pageReq);

        return new PageImpl<>(content, pageReq, totalCount);
	}

	/**
	 * This method should be used when there is a need to page entities that are complex and have lots of lazily loaded,
	 * one-to-many collection attributes. Call this method with the query you've built.
	 * WARN: To avoid multiple bag fetch hibernate errors, only one collection that is one-many can be fetched at a time.
	 * Ensure only one of these types of entity members is being fetched at time.
	 * See {@link org.brapi.test.BrAPITestServer.service.germ.GermplasmService#findGermplasmEntities for example usage.}
	 *
	 * Once you fetch the first lazy loaded collection, if there are others you need to fetch for your entity it is
	 * recommended to utilize the non-paging findAllBySearch() method, creating a searchQuery that inserts all the ids
	 * of the entities found in the results from this method.  Again, follow code path of above example usage to see how this is done.
	 *
	 * If the need is to fetch according to an entirely different base criteria, feel free to call this method again.
	 *
	 * The results will be paged and ordered based on the submitted searchQuery and pageReq.
    */
	public Page<T> findAllBySearchPaginatingWithFetches(SearchQueryBuilder<T> searchQuery, Pageable pageReq)
		throws InvalidPagingException {
		applyUserId(searchQuery);

		Long totalCount = getTotalCount(searchQuery);

		validatePageNumber(pageReq, totalCount);

		// First grab all the ids of the entities according to the criteria of the searchQuery, paging as specified in the pageReq.
		List<String> content = getPagedContentIdsOnly(searchQuery, pageReq);

		Page<String> pagedIds = new PageImpl<>(content, pageReq, totalCount);

		// Now execute another query to fetch all the entities requested in the searchQuery, passing the pagedIds found
		// in the previous query.  We will fetch them utilizing the ids from the paged query, avoiding the hibernate
		// warning of paging while fetching and consuming considerably less memory.
        return findAllBySearchUsingIds(searchQuery, pagedIds, pageReq);
	}

	private Page<T> findAllBySearchUsingIds(SearchQueryBuilder<T> searchQuery, Page<String> pagedIds, Pageable pageReq) {
		List<T> entities = searchEntitiesWithIds(searchQuery, pagedIds.toList());

		return new PageImpl<>(entities, pageReq, pagedIds.getTotalElements());
	}

	/**
	 * Use this method to run the searchQuery without pagination.  Useful for use cases where calls need to grab
	 * every result at once, but use sparingly for use cases returning large result sets.
	 */
	public List<T> findAllBySearch(SearchQueryBuilder<T> searchQuery) {
		applyUserId(searchQuery);
		return searchEntities(searchQuery);
	}

	public Optional<T> findById(ID id) {
		Optional<T> response = super.findById(id);
		if (response.isPresent()) {
			UUID userId = SecurityUtils.getCurrentUserId();
			if (!(null == response.get().getAuthUserId()
					|| userId.equals(response.get().getAuthUserId())
					|| UUID.fromString("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA").equals(response.get().getAuthUserId()))) {
				response = Optional.empty();
			}
		}
		return response;
	}

	public List<T> findByIdIn(List<ID> ids) {
		return super.findAllById(ids);
	}

	public <S extends T> S save(S entity) {
		entity.setAuthUserId(SecurityUtils.getCurrentUserId());
		return super.save(entity);
	}

	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		for (S entity : entities) {
			entity.setAuthUserId(SecurityUtils.getCurrentUserId());
		}
		return super.saveAll(entities);
	}
	
	public <S extends T> void refresh(S entity) {
		this.entityManager.refresh(entity);
	}

	public void fetchXrefs(Page<T> page, Class<T> searchClass) throws InvalidPagingException {
		SearchQueryBuilder<T> searchQuery = new SearchQueryBuilder<T>(searchClass);
		searchQuery.leftJoinFetch("externalReferences", "externalReferences")
				   .appendList(page.stream().map(p -> p.getId().toString()).collect(Collectors.toList()), "id");

		Page<T> xrefs = findAllBySearchAndPaginate(searchQuery, PageRequest.of(0, page.getSize()));

		Map<UUID, List<ExternalReferenceEntity>> xrefByEntity = new HashMap<>();
		xrefs.forEach(entity -> xrefByEntity.put(entity.getId(), entity.getExternalReferences()));

		page.forEach(entity -> entity.setExternalReferences(xrefByEntity.get(entity.getId())));
	}

	private void applyUserId(SearchQueryBuilder<T> searchQuery) {

		SecurityContext context = SecurityContextHolder.getContext();
		Set<String> userRolesSet = context.getAuthentication().getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

		List<String> userIds = new ArrayList<>();
		userIds.add(SecurityUtils.getCurrentUserId().toString());
		if (userRolesSet.contains("ROLE_ADMIN")) {
			return;
		} else if (userRolesSet.contains("ROLE_USER")) {
			userIds.add("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");
		}

		searchQuery.appendList(userIds, "authUserId");
	}

	private List<T> getPagedContent(SearchQueryBuilder<T> searchQuery, Pageable pageReq) {
		TypedQuery<T> query = entityManager.createQuery(searchQuery.getQuery(), searchQuery.getClazz());

		setQueryParams(query, searchQuery);

		query.setFirstResult((int) pageReq.getOffset());
		query.setMaxResults(pageReq.getPageSize());

        return query.getResultList();
	}

	private List<T> searchEntitiesWithIds(SearchQueryBuilder<T> searchQuery, List<String> ids) {
		searchQuery.appendList(ids.stream().map(Object::toString).collect(Collectors.toList()), "id");

		TypedQuery<T> query = entityManager.createQuery(searchQuery.getQuery(), searchQuery.getClazz());

		setQueryParams(query, searchQuery);

        return query.getResultList();
	}

	private List<T> searchEntities(SearchQueryBuilder<T> searchQuery) {
		TypedQuery<T> query = entityManager.createQuery(searchQuery.getQuery(), searchQuery.getClazz());

		setQueryParams(query, searchQuery);

		return query.getResultList();
	}

	private void setQueryParams(TypedQuery<T> query, SearchQueryBuilder<T> searchQuery) {
		for (Entry<String, Object> entry : searchQuery.getParams().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	private List<String> getPagedContentIdsOnly(SearchQueryBuilder<T> searchQuery, Pageable pageReq) {

		TypedQuery<String> query = entityManager.createQuery(searchQuery.getIdQuery(), String.class);

		for (Entry<String, Object> entry : searchQuery.getParams().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		query.setFirstResult((int) pageReq.getOffset());
		query.setMaxResults(pageReq.getPageSize());

        return query.getResultList();
	}




	private Long getTotalCount(SearchQueryBuilder<T> searchQuery) {
		String countQueryStr = searchQuery.getQuery()
				.replaceFirst("(select|Select|SELECT)( distinct)? ([^\\s]*) ", "select count($2 $3) ")
			    .replaceAll("LEFT JOIN FETCH", "LEFT JOIN")
				.replaceFirst("(order by|Order By|ORDER BY) .*", "");

		TypedQuery<Long> query = entityManager.createQuery(countQueryStr, Long.class);
		for (Entry<String, Object> entry : searchQuery.getParams().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		List<Long> content = query.getResultList();
		if (content.size() > 0) {
			return content.get(0);
		}
		return 0L;
	}

	private void validatePageNumber(Pageable pageReq, Long totalCount) throws InvalidPagingException {
		int reqPageNum = pageReq.getPageNumber();
		if (reqPageNum != 0 && reqPageNum > Math.floor(((double) totalCount) / pageReq.getPageSize())) {
			// Condition indicates a request was sent where the page requested exceeds the total number of pages.
			// Instruct requester to send the correct request.
			throw new InvalidPagingException();
		}
	}
}
