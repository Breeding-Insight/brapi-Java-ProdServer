package org.brapi.test.BrAPITestServer.factory.geno;

import io.swagger.model.Metadata;
import io.swagger.model.core.BatchDeleteTypes;
import io.swagger.model.geno.Plate;
import io.swagger.model.geno.PlateSearchRequest;
import org.apache.commons.lang3.NotImplementedException;
import org.brapi.test.BrAPITestServer.factory.BrAPIComponent;
import org.brapi.test.BrAPITestServer.service.geno.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlateComponent implements BrAPIComponent<Plate, PlateSearchRequest> {
    private final PlateService plateService;

    @Autowired
    public PlateComponent(PlateService plateService) {
        this.plateService = plateService;
    }

    @Override
    public List<Plate> findEntities(PlateSearchRequest request, Metadata metadata) {
        return plateService.findPlates(request, metadata);
    }

    @Override
    public BatchDeleteTypes getBatchDeleteType() {
        return BatchDeleteTypes.PLATES;
    }


    @Override
    public List<String> collectDbIds(List<Plate> entities) {
        return entities.stream().map(Plate::getPlateDbId).collect(Collectors.toList());
    }

    @Override
    public void deleteBatchDeleteData(List<String> dbIds) {
        plateService.deletePlateBatch(dbIds);
    }

    @Override
    public void softDeleteBatchDeleteData(List<String> dbIds) {
        //TODO: implement if needed in future
        throw new NotImplementedException("plate softDeleteBatchDeleteData not implemented");
    }
}
