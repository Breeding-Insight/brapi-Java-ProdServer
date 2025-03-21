package org.brapi.test.BrAPITestServer.repository.germ;

import org.brapi.test.BrAPITestServer.model.entity.germ.BreedingMethodEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;

import java.util.List;
import java.util.Set;

public interface BreedingMethodRepository extends BrAPIRepository<BreedingMethodEntity, String>{
    public List<BreedingMethodEntity> findByIdIn(List<String> id);
}
