package org.brapi.test.BrAPITestServer.repository.pheno;

import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitLevelNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ObservationUnitLevelNameRepository extends JpaRepository<ObservationUnitLevelNameEntity, UUID> {


    @Query("SELECT ouln FROM ObservationUnitLevelNameEntity ouln " +
            "WHERE ouln.program IN :levelName")
    List<ObservationUnitLevelNameEntity> findObservationUnitLevelNamesByProgram(@Param("programIds") List<String> programDbIds);

    @Query("SELECT ouln FROM ObservationUnitLevelNameEntity ouln " +
            "WHERE ouln.program = NULL")
    List<ObservationUnitLevelNameEntity> findDefaultObservationUnitLevelNames();
}
