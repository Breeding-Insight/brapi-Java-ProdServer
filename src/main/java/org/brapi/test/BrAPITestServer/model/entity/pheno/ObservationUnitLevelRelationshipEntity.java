package org.brapi.test.BrAPITestServer.model.entity.pheno;

import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

@Entity
@Table(name = "observation_unit_level")
public class ObservationUnitLevelRelationshipEntity extends BrAPIBaseEntity {
	@Column
	private String levelCode;
	@ManyToOne
	@JoinColumn(name = "level_name")
	private ObservationUnitLevelNameEntity levelName;
	@ManyToOne(fetch = FetchType.LAZY)
	private ObservationUnitEntity observationUnit;
	@ManyToOne(fetch = FetchType.LAZY)
	private ObservationUnitPositionEntity position;
	
	public ObservationUnitEntity getObservationUnit() {
		return observationUnit;
	}
	public void setObservationUnit(ObservationUnitEntity observationUnit) {
		this.observationUnit = observationUnit;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public ObservationUnitLevelNameEntity getLevelName() {
		return levelName;
	}
	public void setLevelName(ObservationUnitLevelNameEntity levelName) {
		this.levelName = levelName;
	}
	public ObservationUnitPositionEntity getPosition() {
		return position;
	}
	public void setPosition(ObservationUnitPositionEntity position) {
		this.position = position;
	}
}
