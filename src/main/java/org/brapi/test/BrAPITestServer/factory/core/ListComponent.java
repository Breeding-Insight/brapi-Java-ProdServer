package org.brapi.test.BrAPITestServer.factory.core;

import io.swagger.model.Metadata;
import io.swagger.model.core.BatchDeleteTypes;
import io.swagger.model.core.ListSearchRequest;
import io.swagger.model.core.ListSummary;
import jakarta.validation.Valid;
import org.brapi.test.BrAPITestServer.factory.BrAPIComponent;
import org.brapi.test.BrAPITestServer.service.core.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListComponent implements BrAPIComponent<ListSummary, ListSearchRequest> {
    private final ListService listService;

    @Autowired
    public ListComponent(ListService listService) {
        this.listService = listService;
    }

    @Override
    public List<ListSummary> findEntities(@Valid ListSearchRequest request, Metadata metadata) {
        return listService.findLists(request, metadata);
    }

    @Override
    public BatchDeleteTypes getBatchDeleteType() {
        return BatchDeleteTypes.LISTS;
    }


    @Override
    public List<String> collectDbIds(List<ListSummary> entities) {
        return entities.stream().map(ListSummary::getListDbId).collect(Collectors.toList());
    }

    @Override
    public void deleteBatchDeleteData(List<String> dbIds) {
        listService.deleteListBatch(dbIds);
    }

    @Override
    public void softDeleteBatchDeleteData(List<String> dbIds) {
        listService.softDeleteListBatch(dbIds);
    }
}