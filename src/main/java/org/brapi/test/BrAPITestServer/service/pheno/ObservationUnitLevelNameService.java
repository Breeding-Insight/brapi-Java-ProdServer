package org.brapi.test.BrAPITestServer.service.pheno;

import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitLevelNameEntity;
import org.brapi.test.BrAPITestServer.repository.pheno.ObservationUnitLevelNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ObservationUnitLevelNameService {
    private final ObservationUnitLevelNameRepository observationUnitLevelNameRepository;

    @Autowired
    public ObservationUnitLevelNameService(ObservationUnitLevelNameRepository observationUnitLevelNameRepository) {
        this.observationUnitLevelNameRepository = observationUnitLevelNameRepository;
    }

    public Map<String, ObservationUnitLevelNameEntity> retrieveAndVerifyObservationUnitLevelNames(String programDbId,
                                                                                                  List<String> submittedLevelNames)
    {
        return retrieveAndVerifyObservationUnitLevelNames(programDbId, submittedLevelNames);
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

        // If there were no global names available,
    }


    // Single Program use case
    public List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(String programDbId)
            throws BrAPIServerException {
        return findObservationUnitLevelNames(List.of(programDbId));
    }

    // Finds observation unit level names by programDbIds, or if not available, globally (no associated program)
    private List<ObservationUnitLevelNameEntity> findObservationUnitLevelNames(List<String> programDbIds)
            throws BrAPIServerException {
        List<ObservationUnitLevelNameEntity> foundOULevelNames = new ArrayList<>();

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
