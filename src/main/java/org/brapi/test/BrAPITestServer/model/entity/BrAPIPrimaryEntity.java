package org.brapi.test.BrAPITestServer.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.model.ExternalReferences;
import io.swagger.model.ExternalReferencesInner;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@MappedSuperclass
public class BrAPIPrimaryEntity extends BrAPIBaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition="jsonb")
	private JsonNode additionalInfo;

    @BatchSize(size = 50)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {@JoinColumn(referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(referencedColumnName = "id")})
    private List<ExternalReferenceEntity> externalReferences;

    @Column
    private UUID authUserId;

    public UUID getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(UUID authUserId) {
        this.authUserId = authUserId;
    }

	public JsonNode getAdditionalInfo() {
		return this.additionalInfo;
	}

	public void setAdditionalInfo(JsonNode info) {
		this.additionalInfo = info;
	}

    public List<ExternalReferenceEntity> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReferenceEntity> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public ExternalReferences getExternalReferencesMap() {
        ExternalReferences exRefs = new ExternalReferences();
        if (getExternalReferences() != null) {
            for (ExternalReferenceEntity entity : getExternalReferences()) {
                ExternalReferencesInner exRef = new ExternalReferencesInner();
                exRef.setReferenceID(entity.getExternalReferenceId().toString());
                exRef.setReferenceSource(entity.getExternalReferenceSource());
                exRefs.add(exRef);
            }
        }
        return exRefs;
    }

    public void setExternalReferences(ExternalReferences exRefs) {
        if (exRefs != null) {
            setExternalReferences(new ArrayList<>());
            for (ExternalReferencesInner exRef : exRefs) {
                ExternalReferenceEntity entity = new ExternalReferenceEntity();
                entity.setExternalReferenceId(exRef.getReferenceID());
                entity.setExternalReferenceSource(exRef.getReferenceSource());
                getExternalReferences().add(entity);
            }
        }
    }

}
