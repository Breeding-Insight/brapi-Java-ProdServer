package org.brapi.test.BrAPITestServer.factory;

import io.swagger.model.Metadata;
import io.swagger.model.SearchRequest;
import io.swagger.model.core.BatchDeleteTypes;
import jakarta.validation.Valid;

import java.util.List;

public interface BrAPIComponent<T, R extends SearchRequest> {
    List<T> findEntities(@Valid R request, Metadata metadata);
    BatchDeleteTypes getBatchDeleteType();
    List<String> collectDbIds(List<T> entities);
    void deleteBatchDeleteData(List<String> dbIds);
    void softDeleteBatchDeleteData(List<String> dbIds);
}