package org.brapi.test.BrAPITestServer.repository.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.brapi.test.BrAPITestServer.model.entity.core.TrialEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrialRepository extends BrAPIRepository<TrialEntity, String> {

	@Query("select t from TrialEntity t join t.studies s "
			+ "where ('' = :programDbId OR t.program.id = :programDbId) "
			+ "AND ('' = :commonCropName OR t.program.crop.cropName = :commonCropName) "
			+ "AND ('' = :locationDbId OR :locationDbId IN s.location.id) "
			+ "AND (:applyActiveFilter = false OR :active = t.active)")
	Page<TrialEntity> findBySearch(
			@Param("commonCropName") String commonCropName,
			@Param("programDbId") String programDbId, 
			@Param("locationDbId") String locationDbId, 
			@Param("applyActiveFilter") boolean applyActiveFilter, 
			@Param("active") boolean active, Pageable pageReq);

	@Modifying
	@Transactional
	@Query("UPDATE TrialEntity t SET t.softDeleted = :softDeleted WHERE t.id = :trialId")
	int updateSoftDeletedStatus(@Param("trialId") String trialId, @Param("softDeleted") boolean softDeleted);

	@Modifying
	@Transactional
	@Query("UPDATE TrialEntity t SET t.softDeleted = :softDeleted WHERE t.id IN :trialIds")
	int updateSoftDeletedStatusBatch(@Param("trialIds") List<String> trialIds, @Param("softDeleted") boolean softDeleted);

}
