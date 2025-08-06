package org.brapi.test.BrAPITestServer.service.pheno;

import io.swagger.model.pheno.ObservationLevelNewRequest;
import io.swagger.model.pheno.ObservationUnitHierarchyLevel;
import org.apache.commons.lang3.StringUtils;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerDbIdNotFoundException;
import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitLevelNameEntity;
import org.brapi.test.BrAPITestServer.repository.baseEntities.ObservationUnitLevelNameRepository;
import org.brapi.test.BrAPITestServer.service.core.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static String GLOBAL_KEY_FOR_FOUND_ENTITIES = "global";

    @Autowired
    public ObservationUnitLevelNameService(ObservationUnitLevelNameRepository observationUnitLevelNameRepository,
                                           ProgramService programService) {
        this.observationUnitLevelNameRepository = observationUnitLevelNameRepository;
        this.programService = programService;
    }

    /**
     * Returns the verified level name entity.  Should only be used with a list of size 1.
     */
    public ObservationUnitLevelNameEntity verifyObservationUnitLevelName(String parentProgramDbId,
                                                                         List<? extends ObservationUnitHierarchyLevel> submittedLevelName,
                                                                         Map<String, ObservationUnitLevelNameEntity> foundLevelEntitiesByDbId,
                                                                         Map<String, List<ObservationUnitLevelNameEntity>> foundLevelEntitiesGroupedByProgramId)
        throws BrAPIServerException {

        return verifyObservationUnitLevelNames(parentProgramDbId, submittedLevelName,
                foundLevelEntitiesByDbId,
                foundLevelEntitiesGroupedByProgramId).get(submittedLevelName.getFirst().getLevelName());
    }

    public Map<String, ObservationUnitLevelNameEntity> verifyObservationUnitLevelNames(String parentProgramDbId,
                                                                                       List<? extends ObservationUnitHierarchyLevel> submittedLevelNames,
                                                                                       Map<String, ObservationUnitLevelNameEntity> foundLevelEntitiesByDbId,
                                                                                       Map<String, List<ObservationUnitLevelNameEntity>> foundLevelEntitiesGroupedByProgramId)
        throws BrAPIServerException {

        Map<String, ObservationUnitLevelNameEntity> verifiedEntitiesByLevelName = new HashMap<>();
        List<ObservationUnitHierarchyLevel> levelNamesNotFound = new ArrayList<>();

        submittedLevelNames.forEach(sln -> {

            var verifiedLevelNamesCurrentSize = verifiedEntitiesByLevelName.size();

            if (StringUtils.isNotBlank(sln.getLevelNameDbId()) && foundLevelEntitiesByDbId.containsKey(sln.getLevelNameDbId())) {
                var entity = foundLevelEntitiesByDbId.get(sln.getLevelNameDbId());
                verifiedEntitiesByLevelName.put(entity.getLevelName(), entity);
            } else if (StringUtils.isNotBlank(parentProgramDbId) && StringUtils.isNotBlank(sln.getLevelName()) && foundLevelEntitiesGroupedByProgramId.get(parentProgramDbId) != null) {
                var entities = foundLevelEntitiesGroupedByProgramId.get(parentProgramDbId);

                entities.stream()
                        .filter(ouln -> ouln.getLevelName().equals(sln.getLevelName()))
                        .findFirst()
                        .ifPresent(ouln -> verifiedEntitiesByLevelName.put(ouln.getLevelName(), ouln));
            } else if (StringUtils.isNotBlank(sln.getProgramDbId())  && StringUtils.isNotBlank(sln.getLevelName()) && foundLevelEntitiesGroupedByProgramId.get(parentProgramDbId) != null) {
                var entities = foundLevelEntitiesGroupedByProgramId.get(sln.getProgramDbId());

                entities.stream()
                        .filter(ouln -> ouln.getLevelName().equals(sln.getLevelName()))
                        .findFirst()
                        .ifPresent(ouln -> verifiedEntitiesByLevelName.put(ouln.getLevelName(), ouln));
            }

            if (verifiedLevelNamesCurrentSize == verifiedEntitiesByLevelName.size()) {
                // All other ways of detecting the level name have failed so far, try the global ones as a last-ditch effort
                var globalEntities = foundLevelEntitiesGroupedByProgramId.get(GLOBAL_KEY_FOR_FOUND_ENTITIES);

                globalEntities.stream()
                        .filter(ouln -> ouln.getLevelName().equals(sln.getLevelName()))
                        .findFirst()
                        .ifPresent(ouln -> verifiedEntitiesByLevelName.put(ouln.getLevelName(), ouln));
            }

            if (verifiedLevelNamesCurrentSize == verifiedEntitiesByLevelName.size()) {
                // This level name was not found. Add it to the list to notify user which level names are invalid.
                levelNamesNotFound.add(sln);
            }
        });

        if (!levelNamesNotFound.isEmpty()) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, String.format("The following submitted level names were not found: [%s]. " +
                    " Please check that these level names exist in the DB by using the /observationlevelnames endpoints.  If they do not exist, they can be added there.",
                    levelNamesNotFound));
        }

        return verifiedEntitiesByLevelName;
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

    public Map<String, ObservationUnitLevelNameEntity> retrieveAndVerifyObservationUnitLevelNames(String programDbIdOverride,
                                                                                                   List<? extends ObservationUnitHierarchyLevel> submittedLevelNames)
            throws BrAPIServerException {

        List<String> programDbIds = new ArrayList<>();

        if (programDbIdOverride != null && !programDbIdOverride.isBlank()) {
            // If an programDbIdOverride was given, that means that the caller wants all submitted level names to be
            // searched by a single programDbId. This programDbId is usually associated with a parent entity the level names
            // belong to, like StudyEntity, ObservationUnitEntity, etc.
            programDbIds.add(programDbIdOverride);
        } else {
            // If the override doesn't exist, use programDbIds from submitted level name object, if they are
            // available at all.
            programDbIds.addAll(submittedLevelNames.stream()
                    .map(ObservationUnitHierarchyLevel::getProgramDbId)
                    .filter(Objects::nonNull)
                    .toList());
        }

        List<String> levelNameDbIds = submittedLevelNames.stream()
                .map(ObservationUnitHierarchyLevel::getLevelNameDbId)
                .filter(Objects::nonNull)
                .toList();

        if (levelNameDbIds.size() != submittedLevelNames.size()) {
            // Only lookup by levelNameDbIds if every submitted level name has one.
            levelNameDbIds = new ArrayList<>();
        }

        List<ObservationUnitLevelNameEntity> foundOULevelNames = findObservationUnitLevelNames(programDbIds, levelNameDbIds);

        var levelNameEntitiesByName = foundOULevelNames.stream()
                .collect(Collectors.toMap(ouln -> ouln.getLevelName().toLowerCase(), e -> e));

        var submittedNames = submittedLevelNames.stream().map(ouhl -> ouhl.getLevelName().toLowerCase()).toList();
        // Now that we have found all the available level names, verify that all submitted level names are valid level names
        // in the DB.
        for (String submittedLevelName : submittedNames) {
            if (!levelNameEntitiesByName.containsKey(submittedLevelName.toLowerCase())) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                        String.format("Submitted observation unit level name [%s] does not exist " +
                                        "globally or for the following submitted programs [%s].  This can be fixed " +
                                        "by adding the level name using the POST endpoint brapi/[version-number]/observationunitlevelnames, " +
                                        "or by using a level name already in the system.",
                                submittedLevelName,
                                programDbIds));
            }
        }

        // Now that every submitted level name has been verified, return a map of the retrieved submitted levels.
        return submittedNames.stream()
                .collect(Collectors.toMap(ln -> ln, levelNameEntitiesByName::get));
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

        var programDbIds = levels.stream()
                .map(ObservationUnitHierarchyLevel::getProgramDbId)
                .filter(Objects::nonNull)
                .toList();

        var programEntitiesById = programService.findByIds(programDbIds).stream()
                .collect(Collectors.toMap(p -> p.getId().toString(), p -> p));

        for (ObservationLevelNewRequest level : levels) {
            var entity = new ObservationUnitLevelNameEntity();

            entity.setLevelName(level.getLevelName());
            entity.setLevelOrder(level.getLevelOrder());

            if (level.getProgramDbId() != null) {
                var foundProgram = programEntitiesById.get(level.getProgramDbId());
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
        var entityOpt = observationUnitLevelNameRepository.findById(UUID.fromString(observationLevelNameDbId));

        if (entityOpt.isEmpty()) {
            throw new BrAPIServerDbIdNotFoundException("ObservationUnitLevelName", observationLevelNameDbId, HttpStatus.BAD_REQUEST);
        }

        var entity = entityOpt.get();

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

        var savedEntity = observationUnitLevelNameRepository.save(entity);

        return convertFromEntitiesInBatch(List.of(savedEntity)).getFirst();
    }

    public List<ObservationUnitHierarchyLevel> save(List<ObservationLevelNewRequest> request)
        throws BrAPIServerException {
        var entities = convertToEntitiesInBatch(request);
        var savedEntities = observationUnitLevelNameRepository.saveAll(entities);
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

            var result = observationUnitLevelNameRepository.findAllById(levelNameDbIds.stream().map(UUID::fromString).toList());

            if (result.size() != levelNameDbIds.size()) {

                var foundDbIds = result.stream()
                        .map(ouln -> ouln.getId().toString())
                        .toList();
                var levelNameDbIdsNotFound = levelNameDbIds.stream()
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
