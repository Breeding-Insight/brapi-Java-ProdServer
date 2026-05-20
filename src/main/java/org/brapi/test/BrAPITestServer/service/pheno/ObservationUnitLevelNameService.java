package org.brapi.test.BrAPITestServer.service.pheno;

import io.swagger.model.pheno.ObservationLevelNewRequest;
import io.swagger.model.pheno.ObservationUnitHierarchyLevel;
import io.swagger.model.pheno.ObservationUnitLevel;
import org.apache.commons.lang3.StringUtils;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.exceptions.ExceptionUtils;
import org.brapi.test.BrAPITestServer.model.entity.core.ProgramEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitLevelNameEntity;
import org.brapi.test.BrAPITestServer.repository.baseEntities.ObservationUnitLevelNameRepository;
import org.brapi.test.BrAPITestServer.service.core.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObservationUnitLevelNameService {
    private final ObservationUnitLevelNameRepository observationUnitLevelNameRepository;
    private final ProgramService programService;

    private final static String GLOBAL_NOT_SET_MSG = "No observation level names were found for the programDbId provided, nor are any global level" +
            " names available.  Please add via endpoint to attach level names to a program or define " +
            "without a program to make them globally accessible.";

    private final static String GLOBAL_AND_PROGRAMMATIC_SET_MSG = "Both programDbId and global=true attributes are set. " +
            "A level name cannot be both related to a program and be globally accessible.  Choose one.";

    private final static String NO_DB_ID_PROVIDED_MSG = "A level name associated with a program was provided that has more than one level order associated with it." +
            " Once this kind of level name is in the database it is recommended to always provide levelNameDbIds for the level names submitted to get the correct level order." +
            "  Please search for the levelNameDbId for the submitted level names in the GET /observationlevelnames endpoint and provide them.";

    private final static String GLOBAL_KEY_FOR_FOUND_ENTITIES = "global";

    @Autowired
    public ObservationUnitLevelNameService(ObservationUnitLevelNameRepository observationUnitLevelNameRepository,
                                           ProgramService programService) {
        this.observationUnitLevelNameRepository = observationUnitLevelNameRepository;
        this.programService = programService;
    }

    /**
     * Returns the verified level name entity.  Should only be used with a list of size 1, lists are only used here
     * to use the wildcard since there are different children of ObservationUnitLevel used in the callers of this method.
     */
    public ObservationUnitLevelNameEntity verifyObservationUnitLevelName(String parentProgramDbId,
                                                                         List<? extends ObservationUnitLevel> submittedLevelNames,
                                                                         Map<String, ObservationUnitLevelNameEntity> foundLevelEntitiesByDbId,
                                                                         Map<String, List<ObservationUnitLevelNameEntity>> foundLevelEntitiesGroupedByProgramId)
        throws BrAPIServerException {

        if (submittedLevelNames.size() > 1) {
            throw new IllegalArgumentException();
        }

        ObservationUnitLevel submittedLevelName = submittedLevelNames.getFirst();

        ObservationUnitLevelNameEntity verifiedEntity = null;

        if (StringUtils.isNotBlank(submittedLevelName.getLevelNameDbId()) && foundLevelEntitiesByDbId.containsKey(submittedLevelName.getLevelNameDbId())) {
            verifiedEntity = foundLevelEntitiesByDbId.get(submittedLevelName.getLevelNameDbId());
        } else if (StringUtils.isNotBlank(parentProgramDbId) && StringUtils.isNotBlank(submittedLevelName.getLevelName()) && foundLevelEntitiesGroupedByProgramId.get(parentProgramDbId) != null) {
            // If parent programDbId is provided, utilize it to look up the level name.
            List<ObservationUnitLevelNameEntity> entitiesMatchedByName = foundLevelEntitiesGroupedByProgramId.get(parentProgramDbId)
                    .stream()
                    .filter(ouln -> ouln.getLevelName().equals(submittedLevelName.getLevelName()))
                    .limit(2)
                    .toList();

            if (entitiesMatchedByName.size() > 1) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST, NO_DB_ID_PROVIDED_MSG);
            } else if (entitiesMatchedByName.size() == 1) {
                verifiedEntity = entitiesMatchedByName.getFirst();
            }
        } else if (StringUtils.isNotBlank(submittedLevelName.getProgramDbId())  && StringUtils.isNotBlank(submittedLevelName.getLevelName()) && foundLevelEntitiesGroupedByProgramId.get(submittedLevelName.getProgramDbId()) != null) {
            // Parent programDbId was not provided, if there's a programDbId inside the levelName itself try using that to get the level name.
            List<ObservationUnitLevelNameEntity> entitiesMatchByName = foundLevelEntitiesGroupedByProgramId.get(submittedLevelName.getProgramDbId())
                    .stream()
                    .filter(ouln -> ouln.getLevelName().equals(submittedLevelName.getLevelName()))
                    .limit(2)
                    .toList();

            if (entitiesMatchByName.size() > 1) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST, NO_DB_ID_PROVIDED_MSG);
            } else if (entitiesMatchByName.size() == 1) {
                verifiedEntity = entitiesMatchByName.getFirst();
            }
        }

        if (verifiedEntity == null && foundLevelEntitiesGroupedByProgramId.get(GLOBAL_KEY_FOR_FOUND_ENTITIES) != null) {
            // All other ways of detecting the level name have failed so far, try the global ones as a last-ditch effort
            List<ObservationUnitLevelNameEntity> globalEntities = foundLevelEntitiesGroupedByProgramId.get(GLOBAL_KEY_FOR_FOUND_ENTITIES);

            verifiedEntity = globalEntities.stream()
                    .filter(ouln -> ouln.getLevelName().equals(submittedLevelName.getLevelName()))
                    .findFirst()
                    .orElse(null);
        }

        if (verifiedEntity == null) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, String.format("The following submitted level names were not found: [%s]. " +
                            " Please check that these level names exist in the DB by using the /observationlevelnames endpoints.  If they do not exist, they can be added there.",
                    submittedLevelName));
        }

        return verifiedEntity;
    }

    /**
     * This allows for all found level names to be successfully grouped by programId, because in the case of global level names,
     * no programId is allowed. This placeholder returned in this function can be used for global level names.  Useful for batching operations.
     *
     * Example usage:
     *
     *      var foundLevelNamesGroupedByProgramDbId = observationUnitLevelNames.stream()
     *          .collect(Collectors.groupingBy(ouln ->
     *              Optional.ofNullable(ouln.getProgram())
     *                  .map(p -> p.getId().toString())
     *                  .orElse(observationUnitLevelNameService.getGlobalKeyForFoundEntities())));
     */
    public String getGlobalKeyForFoundEntities() {
        return GLOBAL_KEY_FOR_FOUND_ENTITIES;
    }

    public List<ObservationUnitHierarchyLevel> convertFromEntitiesInBatch(List<ObservationUnitLevelNameEntity> entities) {
        List<ObservationUnitHierarchyLevel> result = new ArrayList<>();

        for (ObservationUnitLevelNameEntity entity : entities) {
            var level = new ObservationUnitHierarchyLevel();

            level.setLevelNameDbId(entity.getId().toString());
            level.setLevelName(entity.getLevelName());
            level.setLevelOrder(entity.getLevelOrder());

            if (entity.getProgram() != null) {
                level.setProgramDbId(entity.getProgram().getId().toString());
                level.setProgramName(entity.getProgram().getName());
            }

            result.add(level);
        }
        return result;
    }

    public List<ObservationUnitLevelNameEntity> convertToEntitiesInBatch(List<ObservationLevelNewRequest> levels)
        throws BrAPIServerException {
        List<ObservationUnitLevelNameEntity> result = new ArrayList<>();

        List<String> programDbIds = levels.stream()
                .map(ObservationUnitHierarchyLevel::getProgramDbId)
                .filter(Objects::nonNull)
                .toList();

        Map<String, ProgramEntity> programEntitiesById = programService.findByIds(programDbIds).stream()
                .collect(Collectors.toMap(p -> p.getId().toString(), p -> p));

        for (ObservationLevelNewRequest level : levels) {
            var entity = new ObservationUnitLevelNameEntity();

            entity.setLevelName(level.getLevelName());
            entity.setLevelOrder(level.getLevelOrder());

            if (level.getProgramDbId() != null) {
                ProgramEntity foundProgram = programEntitiesById.get(level.getProgramDbId());
                if (foundProgram != null) {
                    entity.setProgram(foundProgram);
                } else {
                    throw new BrAPIServerDbIdNotFoundException("program", level.getProgramDbId(), HttpStatus.BAD_REQUEST);
                }
            } else if (level.getGlobal() == null || !level.getGlobal()){
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST, GLOBAL_NOT_SET_MSG);
            }

            result.add(entity);
        }

        return result;
    }

    public ObservationUnitHierarchyLevel update(String observationLevelNameDbId,
                                                ObservationLevelNewRequest level)
        throws BrAPIServerException {
        Optional<ObservationUnitLevelNameEntity> entityOpt = observationUnitLevelNameRepository.findById(UUID.fromString(observationLevelNameDbId));

        if (entityOpt.isEmpty()) {
            throw new BrAPIServerDbIdNotFoundException("ObservationUnitLevelName", observationLevelNameDbId, HttpStatus.BAD_REQUEST);
        }

        ObservationUnitLevelNameEntity entity = entityOpt.get();

        if (level.getLevelName() != null) {
            entity.setLevelName(level.getLevelName());
        }

        if (level.getLevelOrder() != null) {
            entity.setLevelOrder(level.getLevelOrder());
        }

        if (level.getProgramDbId() != null && level.getGlobal() != null && level.getGlobal()) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, GLOBAL_AND_PROGRAMMATIC_SET_MSG);
        }

        if (level.getProgramDbId() != null) {
            var program = programService.getProgramEntity(level.getProgramDbId());
            entity.setProgram(program);
        }

        if (level.getGlobal() != null && level.getGlobal()) {
            // The user sent in a request to make this observation level name global for the system.
            // In the DB impl, this simply means it doesn't belong to any program.
            entity.setProgram(null);
        }

        ObservationUnitLevelNameEntity savedEntity = observationUnitLevelNameRepository.save(entity);

        return convertFromEntitiesInBatch(List.of(savedEntity)).getFirst();
    }

    public List<ObservationUnitHierarchyLevel> save(List<ObservationLevelNewRequest> request)
        throws BrAPIServerException {
        List<ObservationUnitLevelNameEntity> entities = convertToEntitiesInBatch(request);

        List<ObservationUnitLevelNameEntity> savedEntities = null;

        try {
            savedEntities = observationUnitLevelNameRepository.saveAll(entities);
        } catch (DataIntegrityViolationException e) {
            ExceptionUtils.checkForPSQLConflict(e.getMostSpecificCause());

            // If a conflict didn't occur, something else happened on the DB server.  Throw a 500 and attach the root cause to the message.
            throw new BrAPIServerException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("An DB error occurred while processing level names %s\n\n%s", request, e.getMostSpecificCause()));
        }

        return convertFromEntitiesInBatch(savedEntities);
    }

    public void deleteObservationLevelName(String observationLevelNameDbId) {
        observationUnitLevelNameRepository.deleteById(UUID.fromString(observationLevelNameDbId));
    }

    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(String programDbId,
                                                                              Boolean all)
            throws BrAPIServerException {

        if (programDbId == null) {
            return findObservationUnitLevelNames(Collections.emptyList(),
                    Collections.emptyList(),
                    all);
        } else {
            return findObservationUnitLevelNames(List.of(programDbId),
                    Collections.emptyList(),
                    all);
        }
    }

    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(List<String> programDbIds,
                                                                              List<String> levelNameDbIds)
            throws BrAPIServerException {
        return findObservationUnitLevelNames(programDbIds, levelNameDbIds, null);
    }

    // Finds observation unit level names by programDbIds, or if not available, globally (no associated program).
    // If all is true, find every observation unit level name in the system.
    private List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(List<String> programDbIds,
                                                                               List<String> levelNameDbIds,
                                                                               Boolean all)
            throws BrAPIServerException {
        List<ObservationUnitLevelNameEntity> foundOULevelNames = new ArrayList<>();

        if (all != null && all) {
            return observationUnitLevelNameRepository.findAllObservationUnitLevelNames();
        }

        if (levelNameDbIds != null && !levelNameDbIds.isEmpty()) {

            List<ObservationUnitLevelNameEntity> result = observationUnitLevelNameRepository.findAllById(levelNameDbIds.stream().map(UUID::fromString).toList());

            if (result.size() != levelNameDbIds.size()) {

                List<String> foundDbIds = result.stream()
                        .map(ouln -> ouln.getId().toString())
                        .toList();
                List<String> levelNameDbIdsNotFound = levelNameDbIds.stream()
                        .filter(lnId -> !foundDbIds.contains(lnId))
                        .toList();

                throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                        String.format("Level name DB Ids supplied [%s] were not found in the database. " +
                        "Utilize the /observationlevelnames GET method to find the level name DB Ids you would like to search, " +
                        "or forgo supplying level name dbIds and try looking up on a programDbId.", levelNameDbIdsNotFound));
            }

            return result;
        }

        if (programDbIds != null && !programDbIds.isEmpty()) {
            // First look up all level names related to submitted programs if available
            foundOULevelNames = observationUnitLevelNameRepository.findObservationUnitLevelNamesByProgram(programDbIds);
        }

        if (foundOULevelNames.isEmpty()) {
            // None were found.  Now try to see if there are globally available level names.
            foundOULevelNames = observationUnitLevelNameRepository.findDefaultObservationUnitLevelNames();
        }

        if (foundOULevelNames.isEmpty()) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                    "No observation level names were found for the programDbId provided, nor are any global level" +
                            " names available.  Please add via endpoint to attach level names to a program or define " +
                            "without a program to make them globally accessible.");
        }

        return foundOULevelNames;
    }
}
