package org.brapi.test.BrAPITestServer.model.entity.pheno;

import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.core.ProgramEntity;

import java.util.List;

@Entity
@Table(name="observation_variable")
public class ObservationVariableEntity extends VariableBaseEntity{
	@Column
	private String name;
	@Column
	private String pui;
	@OneToMany(mappedBy="observationVariable", fetch = FetchType.LAZY)
	private List<ObservationEntity> observations;
	@ManyToOne(fetch = FetchType.LAZY)
	private ProgramEntity program;
	
	public String getPui() {
		return pui;
	}
	public void setPui(String pui) {
		this.pui = pui;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ObservationEntity> getObservations() {
		return observations;
	}
	public void setObservations(List<ObservationEntity> observations) {
		this.observations = observations;
	}
	public ProgramEntity getProgram() {
		return program;
	}
	public void setProgram(ProgramEntity program) {
		this.program = program;
	}
}
