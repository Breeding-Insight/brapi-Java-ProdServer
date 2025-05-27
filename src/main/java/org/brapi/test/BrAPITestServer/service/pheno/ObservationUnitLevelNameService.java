package org.brapi.test.BrAPITestServer.service.pheno;

import io.swagger.model.pheno.ObservationLevelNewRequest;
import io.swagger.model.pheno.ObservationUnitHierarchyLevel;
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

    @Autowired
    public ObservationUnitLevelNameService(ObservationUnitLevelNameRepository observationUnitLevelNameRepository,
                                           ProgramService programService) {
        this.observationUnitLevelNameRepository = observationUnitLevelNameRepository;
        this.programService = programService;
    }

    public Map<String, ObservationUnitLevelNameEntity> retrieveAndVerifyObservationUnitLevelNames(String programDbId,
                                                                                                  List<String> submittedLevelNames)
            throws BrAPIServerException {
        return retrieveAndVerifyObservationUnitLevelNames(List.of(programDbId), submittedLevelNames);
    }

    public Map<String, ObservationUnitLevelNameEntity> retrieveAndVerifyObservationUnitLevelNames(List<String> programDbIds,
                                                                                                   List<String> submittedLevelNames)
            throws BrAPIServerException {

        List<ObservationUnitLevelNameEntity> foundOULevelNames = findObservationUnitLevelNames(programDbIds);

        var levelNameEntitiesByName = foundOULevelNames.stream()
                .collect(Collectors.toMap(ObservationUnitLevelNameEntity::getLevelName, e -> e));

        // Now that we have found all the available level names, verify that all submitted level names are valid level names
        // in the DB.  If they aren't throw an error indicating the client must fix this.
        for (String submittedLevelName : submittedLevelNames) {
            if (!levelNameEntitiesByName.containsKey(submittedLevelName)) {
                throw new BrAPIServerException(HttpStatus.BAD_REQUEST,
                        String.format("Submitted observation unit level name [%s] does not exist " +
                                        "globally or for the following submitted programs [%s].  This can be fixed " +
                                        "by adding the level name using the POST endpoint brapi/[version-number]/observationunitlevelnames, " +
                                        "or by using a level name already in the system.",
                                submittedLevelName,
                                programDbIds.toString()));
            }
        }

        // Now that every submitted level name has been verified, return a map of the retrieved submitted levels.
        return submittedLevelNames.stream()
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

        if (level.getProgramDbId() != null) {
            var program = programService.getProgramEntity(level.getProgramDbId());
            entity.setProgram(program);
        } else if (level.getGlobal() == null || !level.getGlobal()) {
            throw new BrAPIServerException(HttpStatus.BAD_REQUEST, GLOBAL_NOT_SET_MSG);
        } else {
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

    // Single Program use case
    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(String programDbId)
            throws BrAPIServerException {
        return findObservationUnitLevelNames(List.of(programDbId), null);
    }

    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(String programDbId,
                                                                              Boolean all)
            throws BrAPIServerException {

        if (programDbId == null) {
            return findObservationUnitLevelNames(Collections.emptyList(),
                    all);
        } else {
            return findObservationUnitLevelNames(List.of(programDbId),
                    all);
        }
    }

    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(List<String> programDbIds)
            throws BrAPIServerException {
        return findObservationUnitLevelNames(programDbIds, null);
    }

    // Finds observation unit level names by programDbIds, or if not available, globally (no associated program).
    // If all is true, find every observation unit level name in the system.
    private List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(List<String> programDbIds,
                                                                               Boolean all)
            throws BrAPIServerException {
        List<ObservationUnitLevelNameEntity> foundOULevelNames = new ArrayList<>();

        if (all != null && all) {
            return observationUnitLevelNameRepository.findAllObservationUnitLevelNames();
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
