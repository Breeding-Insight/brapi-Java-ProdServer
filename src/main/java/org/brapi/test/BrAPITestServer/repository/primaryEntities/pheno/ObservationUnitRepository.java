package org.brapi.test.BrAPITestServer.repository.primaryEntities.pheno;

import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitEntity;
import org.brapi.test.BrAPITestServer.repository.primaryEntities.BrAPIRepository;
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

    @NativeQuery(
            "SELECT EXISTS(" +
                    "SELECT ou.id " +
                    "FROM observation_unit ou " +
                    "JOIN observation_unit_position oup ON ou.id = oup.observation_unit_id " +
                    "JOIN observation_unit_level_name ouln ON oup.level_name = ouln.id " +
                    "WHERE oup.level_name = :levelNameDbId AND ou.program_id = :programDbId " +
                    // The IS NULL checks allow for some moderate custom query generation based off of input params.
                    "AND (:trialDbId ::uuid IS NULL OR ou.trial_id = :trialDbId) AND (:studyDbId ::uuid IS NULL OR ou.study_id = :studyDbId) " +
                    ") OR EXISTS ( " +
                    "SELECT ou.id " +
                    "FROM observation_unit ou " +
                    "JOIN observation_unit_position oup ON oup.observation_unit_id = ou.id " +
                    "JOIN observation_unit_level oul ON oul.position_id = oup.id " +
                    "WHERE oul.level_name = :levelNameDbId AND ou.program_id = :programDbId " +
                    "AND (:trialDbId ::uuid IS NULL OR ou.trial_id = :trialDbId) AND (:studyDbId ::uuid IS NULL OR ou.study_id = :studyDbId))"
    )
    Boolean existsOUsWithLevelNameAndProgramAndTrialAndStudy(@Param("levelNameDbId") String levelNameDbId,
                                                             @Param("programDbId") String programDbId,
                                                             @Param("trialDbId") String trialDbId,
                                                             @Param("studyDbId") String studyDbId);
}
