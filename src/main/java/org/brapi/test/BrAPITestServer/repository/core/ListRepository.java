package org.brapi.test.BrAPITestServer.repository.core;

import org.brapi.test.BrAPITestServer.model.entity.core.ListEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ListRepository extends BrAPIRepository<ListEntity, UUID>{
    @Modifying
    @Transactional
    @Query("UPDATE ListEntity l SET l.softDeleted = :softDeleted WHERE l.id = :listId")
    int updateSoftDeletedStatus(@Param("listId") String listId, @Param("softDeleted") boolean softDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE ListEntity l SET l.softDeleted = :softDeleted WHERE l.id IN :listIds")
    int updateSoftDeletedStatusBatch(@Param("listIds") List<String> listIds, @Param("softDeleted") boolean softDeleted);
}