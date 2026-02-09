package org.brapi.test.BrAPITestServer.model.entity.pheno;


import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.core.ProgramEntity;

@Entity
@Table(name = "observation_unit_level_name")
public class ObservationUnitLevelNameEntity extends BrAPIBaseEntity {
    @Column(name = "level_name")
    private String levelName;

    @Column(name = "level_order")
    private Integer levelOrder;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String value) {
        this.levelName = value;
    }

    public ProgramEntity getProgram() {
        return program;
    }

    public void setProgram(ProgramEntity program) {
        this.program = program;
    }

    public Integer getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(Integer value) {
        this.levelOrder = value;
    }
}
