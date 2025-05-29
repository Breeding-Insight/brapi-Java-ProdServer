package org.brapi.test.BrAPITestServer.repository.geno;

import org.brapi.test.BrAPITestServer.model.entity.geno.SampleEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SampleRepository extends BrAPIRepository<SampleEntity, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE SampleEntity s SET s.softDeleted = :softDeleted WHERE s.id = :sampleId")
    int updateSoftDeletedStatus(@Param("sampleId") String sampleId, @Param("softDeleted") boolean softDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE SampleEntity s SET s.softDeleted = :softDeleted WHERE s.id IN :sampleIds")
    int updateSoftDeletedStatusBatch(@Param("sampleIds") List<String> sampleIds, @Param("softDeleted") boolean softDeleted);
}
