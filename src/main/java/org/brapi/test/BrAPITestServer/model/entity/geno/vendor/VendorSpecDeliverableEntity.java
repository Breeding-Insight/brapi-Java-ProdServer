package org.brapi.test.BrAPITestServer.model.entity.geno.vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "vendor_spec_deliverable")
public class VendorSpecDeliverableEntity extends BrAPIBaseEntity {
    @Column
    private UUID vendorSpecPlatformDbId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String format;

    public UUID getVendorSpecPlatformDbId() {
        return vendorSpecPlatformDbId;
    }

    public void setVendorSpecPlatformDbId(UUID vendorSpecPlatformDbId) {
        this.vendorSpecPlatformDbId = vendorSpecPlatformDbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
