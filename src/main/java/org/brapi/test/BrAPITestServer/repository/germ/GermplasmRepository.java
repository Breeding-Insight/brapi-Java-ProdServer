package org.brapi.test.BrAPITestServer.repository.germ;

import org.brapi.test.BrAPITestServer.model.entity.germ.GermplasmEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface GermplasmRepository extends BrAPIRepository<GermplasmEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE GermplasmEntity g SET g.softDeleted = :softDeleted WHERE g.id IN :germplasmIds")
    int updateSoftDeletedStatusBatch(@Param("germplasmIds") List<String> germplasmIds, @Param("softDeleted") boolean softDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE GermplasmEntity g SET g.softDeleted = :softDeleted WHERE g.id = :germplasmId")
    int updateSoftDeletedStatus(@Param("germplasmId") String listId, @Param("softDeleted") boolean softDeleted);

    List<GermplasmEntity> findByGermplasmNameIn(List<String> germplasmNames);
}
