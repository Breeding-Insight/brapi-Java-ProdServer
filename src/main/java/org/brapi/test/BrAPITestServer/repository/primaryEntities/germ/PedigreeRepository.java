package org.brapi.test.BrAPITestServer.repository.primaryEntities.germ;

import java.util.List;
import java.util.UUID;

import org.brapi.test.BrAPITestServer.model.entity.germ.PedigreeNodeEntity;
import org.brapi.test.BrAPITestServer.repository.primaryEntities.BrAPIRepository;

public interface PedigreeRepository extends BrAPIRepository<PedigreeNodeEntity, UUID>, PedigreeRepositoryCustom {
	public List<PedigreeNodeEntity> findByGermplasm_Id(UUID germplasmDbId);

	public List<PedigreeNodeEntity> findByGermplasm_IdIn(List<String> germplasmDbIds);
}
