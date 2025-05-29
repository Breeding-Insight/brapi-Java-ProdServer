package org.brapi.test.BrAPITestServer.model.entity.pheno;

import io.swagger.model.pheno.EntryTypeEnum;
import io.swagger.model.pheno.PositionCoordinateTypeEnum;
import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.GeoJSONEntity;

import java.util.List;

@Entity
@Table(name = "observation_unit_position")
public class ObservationUnitPositionEntity extends BrAPIBaseEntity {
    @Column
    private EntryTypeEnum entryType;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GeoJSONEntity geoCoordinates;
    @Column
    private String levelCode;
    @ManyToOne
    @JoinColumn(name = "level_name_new")
    private ObservationUnitLevelNameEntity levelName;
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<ObservationUnitLevelRelationshipEntity> observationLevelRelationships;
    @Column
    private String positionCoordinateX;
    @Column
    private PositionCoordinateTypeEnum positionCoordinateXType;
    @Column
    private String positionCoordinateY;
    @Column
    private PositionCoordinateTypeEnum positionCoordinateYType;
    @OneToOne(fetch = FetchType.LAZY)
    private ObservationUnitEntity observationUnit;

    public ObservationUnitEntity getObservationUnit() {
        return observationUnit;
    }

    public void setObservationUnit(ObservationUnitEntity observationUnit) {
        this.observationUnit = observationUnit;
    }

    public EntryTypeEnum getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryTypeEnum entryType) {
        this.entryType = entryType;
    }

    public GeoJSONEntity getGeoCoordinates() {
        return geoCoordinates;
    }

    public void setGeoCoordinates(GeoJSONEntity geoCoordinates) {
        this.geoCoordinates = geoCoordinates;
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

    public List<ObservationUnitLevelRelationshipEntity> getObservationLevelRelationships() {
        return observationLevelRelationships;
    }

    public void setObservationLevelRelationships(
            List<ObservationUnitLevelRelationshipEntity> observationLevelRelationships) {
        this.observationLevelRelationships = observationLevelRelationships;
    }

    public String getPositionCoordinateX() {
        return positionCoordinateX;
    }

    public void setPositionCoordinateX(String positionCoordinateX) {
        this.positionCoordinateX = positionCoordinateX;
    }

    public PositionCoordinateTypeEnum getPositionCoordinateXType() {
        return positionCoordinateXType;
    }

    public void setPositionCoordinateXType(PositionCoordinateTypeEnum positionCoordinateXType) {
        this.positionCoordinateXType = positionCoordinateXType;
    }

    public String getPositionCoordinateY() {
        return positionCoordinateY;
    }

    public void setPositionCoordinateY(String positionCoordinateY) {
        this.positionCoordinateY = positionCoordinateY;
    }

    public PositionCoordinateTypeEnum getPositionCoordinateYType() {
        return positionCoordinateYType;
    }

    public void setPositionCoordinateYType(PositionCoordinateTypeEnum positionCoordinateYType) {
        this.positionCoordinateYType = positionCoordinateYType;
    }
}
