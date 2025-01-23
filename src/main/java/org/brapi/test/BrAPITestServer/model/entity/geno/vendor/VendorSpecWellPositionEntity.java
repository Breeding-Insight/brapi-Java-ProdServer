package org.brapi.test.BrAPITestServer.model.entity.geno.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "vendor_spec_well_position")
public class VendorSpecWellPositionEntity extends BrAPIBaseEntity {
    @Column
    private UUID vendorSpecStandardRequirementDbId;
    @Column
    private String position;

    public UUID getVendorSpecStandardRequirementDbId() {
        return vendorSpecStandardRequirementDbId;
    }

    public void setVendorSpecStandardRequirementDbId(UUID vendorSpecStandardRequirementDbId) {
        this.vendorSpecStandardRequirementDbId = vendorSpecStandardRequirementDbId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
