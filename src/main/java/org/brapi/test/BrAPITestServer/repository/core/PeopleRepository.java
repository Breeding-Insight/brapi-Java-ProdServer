package org.brapi.test.BrAPITestServer.repository.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.brapi.test.BrAPITestServer.model.entity.core.PersonEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PeopleRepository extends BrAPIRepository<PersonEntity, UUID>{

	@Query("select p from PersonEntity p "
			+ "where ('' = :firstName OR p.firstName LIKE :firstName) "
			+ "AND ('' = :lastName OR p.lastName LIKE :lastName) "
			+ "AND (:personDbId IS NULL OR p.id = :personDbId) "
			+ "AND (:userID IS NULL OR p.userID = :userID) ")
	public Page<PersonEntity> findBySearch(
			@Param("firstName") String firstName, 
			@Param("lastName") String lastName, 
			@Param("personDbId") UUID personDbId,
			@Param("userID") UUID userID, Pageable pageReq);

}
