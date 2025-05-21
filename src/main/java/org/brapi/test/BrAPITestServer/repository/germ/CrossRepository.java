package org.brapi.test.BrAPITestServer.repository.germ;

import org.brapi.test.BrAPITestServer.model.entity.germ.CrossEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;

import java.util.UUID;

public interface CrossRepository extends BrAPIRepository<CrossEntity, UUID> {
	public CrossEntity findByIdAndPlanned(UUID crossDbId, Boolean planned);
}
