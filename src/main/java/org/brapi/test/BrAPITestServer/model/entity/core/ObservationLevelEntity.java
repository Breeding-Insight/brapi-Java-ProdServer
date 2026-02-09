package org.brapi.test.BrAPITestServer.model.entity.core;

import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.pheno.ObservationUnitLevelNameEntity;

@Entity
@Table(name = "study_observation_level")
public class ObservationLevelEntity extends BrAPIBaseEntity {

	@ManyToOne
	@JoinColumn(name = "level_name")
	private ObservationUnitLevelNameEntity levelName;
	@ManyToOne(fetch = FetchType.LAZY)
	private StudyEntity study;
	
	public ObservationUnitLevelNameEntity getLevelName() {
		return levelName;
	}
	public void setLevelName(ObservationUnitLevelNameEntity levelName) {
		this.levelName = levelName;
	}

	public StudyEntity getStudy() {
		return study;
	}

	public void setStudy(StudyEntity study) {
		this.study = study;
	}
}
