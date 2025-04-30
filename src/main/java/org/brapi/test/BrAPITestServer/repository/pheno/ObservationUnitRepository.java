package org.brapi.test.BrAPITestServer.repository.pheno;

import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ObservationUnitRepository extends BrAPIRepository<ObservationUnitEntity, UUID> {
    @Query("SELECT ou from ObservationUnitEntity ou " +
            "LEFT JOIN FETCH ou.position " +
            "WHERE ou.id IN :ids")
    List<ObservationUnitEntity> findByIds(@Param("ids") List<String> ids);

    @NativeQuery(
            "SELECT ou.id, g.id, g.germplasm_name " +
            "FROM  observation_unit ou " +
            "JOIN germplasm g ON g.id = ou.germplasm_id " +
            "WHERE ou.id IN :ouIds"
    )
    List<Object[]> fetchGermplasmDataForOUs(@Param("ouIds") List<UUID> ouIds);
}
