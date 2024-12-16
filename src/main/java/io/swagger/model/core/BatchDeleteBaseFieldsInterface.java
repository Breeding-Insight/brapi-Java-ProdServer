package io.swagger.model.core;

import java.time.OffsetDateTime;

import io.swagger.model.ExternalReferences;

public interface BatchDeleteBaseFieldsInterface {

	public BatchDeleteBaseFieldsInterface additionalInfo(Object additionalInfo);

	public Object getAdditionalInfo();

	public void setAdditionalInfo(Object additionalInfo);

	public BatchDeleteBaseFieldsInterface dateCreated(OffsetDateTime dateCreated);

	public OffsetDateTime getDateCreated();

	public void setDateCreated(OffsetDateTime dateCreated);

	public BatchDeleteBaseFieldsInterface dateModified(OffsetDateTime dateModified);

	public OffsetDateTime getDateModified();

	public void setDateModified(OffsetDateTime dateModified);

	public BatchDeleteBaseFieldsInterface externalReferences(ExternalReferences externalReferences);

	public ExternalReferences getExternalReferences();

	public void setExternalReferences(ExternalReferences externalReferences);

	public BatchDeleteBaseFieldsInterface batchDeleteDescription(String batchDeleteDescription);

	public String getBatchDeleteDescription();

	public void setBatchDeleteDescription(String batchDeleteDescription);

	public BatchDeleteBaseFieldsInterface batchDeleteName(String batchDeleteName);

	public String getBatchDeleteName();

	public void setBatchDeleteName(String batchDeleteName);

	public BatchDeleteBaseFieldsInterface batchDeleteOwnerName(String batchDeleteOwnerName);

	public String getBatchDeleteOwnerName();

	public void setBatchDeleteOwnerName(String batchDeleteOwnerName);

	public BatchDeleteBaseFieldsInterface batchDeleteOwnerPersonDbId(String batchDeleteOwnerPersonDbId);

	public String getBatchDeleteOwnerPersonDbId();

	public void setBatchDeleteOwnerPersonDbId(String batchDeleteOwnerPersonDbId);

	public BatchDeleteBaseFieldsInterface batchDeleteSize(Integer batchDeleteSize);

	public Integer getBatchDeleteSize();

	public void setBatchDeleteSize(Integer batchDeleteSize);

	public BatchDeleteBaseFieldsInterface batchDeleteSource(String batchDeleteSource);

	public String getBatchDeleteSource();

	public void setBatchDeleteSource(String batchDeleteSource);

	public BatchDeleteBaseFieldsInterface batchDeleteType(BatchDeleteTypes batchDeleteType);

	public BatchDeleteTypes getBatchDeleteType();

	public void setBatchDeleteType(BatchDeleteTypes batchDeleteType);

}