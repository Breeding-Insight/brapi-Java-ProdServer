package org.brapi.test.BrAPITestServer.model.entity.geno.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "vendor_spec_sample_type")
public class VendorSpecSampleTypeEntity extends BrAPIBaseEntity {
    @Column
    private UUID vendorSpecStandardRequirementDbId;
    @Column
    private String sampleType;

    public UUID getVendorSpecStandardRequirementDbId() {
        return vendorSpecStandardRequirementDbId;
    }

    public void setVendorSpecStandardRequirementDbId(UUID vendorSpecStandardRequirementDbId) {
        this.vendorSpecStandardRequirementDbId = vendorSpecStandardRequirementDbId;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }
}
