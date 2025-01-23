package org.brapi.test.BrAPITestServer.model.entity.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

import java.util.UUID;

@Entity
@Table(name = "contact")
public class ContactEntity extends BrAPIBaseEntity {
    @Column
    private UUID studyDbId;
    @Column
    private UUID trialDbId;
    @Column
    private String name;
    @Column
    private String instituteName;
    @Column
    private String email;
    @Column
    private String type;
    @Column
    private UUID orcid;

    public UUID getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(UUID studyDbId) {
        this.studyDbId = studyDbId;
    }

    public UUID getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(UUID trialDbId) {
        this.trialDbId = trialDbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getOrcid() {
        return orcid;
    }

    public void setOrcid(UUID orcid) {
        this.orcid = orcid;
    }
}
