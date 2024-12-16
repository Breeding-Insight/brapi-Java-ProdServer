package org.brapi.test.BrAPITestServer.factory.core;

import io.swagger.model.Metadata;
import io.swagger.model.core.BatchDeleteTypes;
import io.swagger.model.core.Trial;
import io.swagger.model.core.TrialSearchRequest;
import org.brapi.test.BrAPITestServer.factory.BrAPIComponent;
import org.brapi.test.BrAPITestServer.service.core.TrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrialComponent implements BrAPIComponent<Trial, TrialSearchRequest> {
    private final TrialService trialService;

    @Autowired
    public TrialComponent(TrialService trialService) {
        this.trialService = trialService;
    }

    @Override
    public List<Trial> findEntities(TrialSearchRequest request, Metadata metadata) {
        return trialService.findTrials(request, metadata);
    }

    @Override
    public BatchDeleteTypes getBatchDeleteType() {
        return BatchDeleteTypes.TRIALS;
    }


    @Override
    public List<String> collectDbIds(List<Trial> entities) {
        return entities.stream().map(Trial::getTrialDbId).collect(Collectors.toList());
    }

    @Override
    public void deleteBatchDeleteData(List<String> dbIds) {
        trialService.deleteTrialBatch(dbIds);
    }

    @Override
    public void softDeleteBatchDeleteData(List<String> dbIds) {
        trialService.softDeleteTrialBatch(dbIds);
    }
}
