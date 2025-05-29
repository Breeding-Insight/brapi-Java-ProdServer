package org.brapi.test.BrAPITestServer.model.entity.geno.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "vendor_spec_status")
public class VendorSpecStatusEntity extends BrAPIBaseEntity {
    @Column
    private UUID vendorSpecPlatformDbId;
    @Column
    private String statusName;
    @Column
    private String statusDescription;

    public UUID getVendorSpecPlatformDbId() {
        return vendorSpecPlatformDbId;
    }

    public void setVendorSpecPlatformDbId(UUID vendorSpecPlatformDbId) {
        this.vendorSpecPlatformDbId = vendorSpecPlatformDbId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
