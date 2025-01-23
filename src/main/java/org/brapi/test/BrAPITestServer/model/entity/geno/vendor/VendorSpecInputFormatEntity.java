package org.brapi.test.BrAPITestServer.model.entity.geno.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "vendor_spec_input_format")
public class VendorSpecInputFormatEntity extends BrAPIBaseEntity {
    @Column
    private UUID vendorSpecStandardRequirementDbId;
    @Column
    private String inputFormat;

    public UUID getVendorSpecStandardRequirementDbId() {
        return vendorSpecStandardRequirementDbId;
    }

    public void setVendorSpecStandardRequirementDbId(UUID vendorSpecStandardRequirementDbId) {
        this.vendorSpecStandardRequirementDbId = vendorSpecStandardRequirementDbId;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }
}
