package org.brapi.test.BrAPITestServer.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.brapi.test.BrAPITestServer.exceptions.InvalidPagingException;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;
import org.brapi.test.BrAPITestServer.service.SearchQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BrAPIRepository<T extends BrAPIPrimaryEntity, ID extends Serializable> extends JpaRepository<T, ID> {

	public Page<T> findAllBySearchAndPaginate(SearchQueryBuilder<T> searchQuery, Pageable pageReq) throws InvalidPagingException;

	public Page<T> findAllBySearchPaginatingWithFetches(SearchQueryBuilder<T> searchQuery, Pageable pageReq) throws InvalidPagingException;

	public List<T> findAllBySearch(SearchQueryBuilder<T> searchQuery);

	public Optional<T> findById(ID id);

	public <S extends T> S save(S entity);

	public <S extends T> List<S> saveAll(Iterable<S> entities);
	
	public <S extends T> void refresh(S entity);

	public void fetchXrefs(List<UUID> page, Page<T> pagedEntities, Class<T> searchClass) throws InvalidPagingException;

	List<T> findByIdIn(List<ID> ids);
}