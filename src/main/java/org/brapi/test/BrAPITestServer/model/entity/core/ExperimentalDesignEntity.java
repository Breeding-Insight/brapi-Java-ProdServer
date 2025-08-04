package org.brapi.test.BrAPITestServer.model.entity.core;

import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

@Embeddable
public class ExperimentalDesignEntity {
	@Column(name = "pui", table = "study_experimental_design")
	private String PUI;
    @Column(name = "description", table = "study_experimental_design")
	private String description;

    public String getPUI() {
        return PUI;
    }

    public void setPUI(String pUI) {
        PUI = pUI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
