package org.brapi.test.BrAPITestServer.repository.geno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.brapi.test.BrAPITestServer.model.entity.geno.vendor.VendorOrderEntity;
import org.brapi.test.BrAPITestServer.repository.BrAPIRepository;

import java.util.UUID;

public interface VendorOrderRepository extends BrAPIRepository<VendorOrderEntity, UUID> {
	public Page<VendorOrderEntity> findByPlateSubmission_Id(String submissionId, Pageable pageReq);
}
