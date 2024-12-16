package org.brapi.test.BrAPITestServer.model.entity.core;

import io.swagger.model.core.BatchDeleteTypes;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "batch_delete")
public class BatchDeleteEntity extends BrAPIPrimaryEntity {
	@Column
	private Date dateCreated;
	@Column
	private Date dateModified;
	@Column
	private String description;
	@Column
	private String batchDeleteName;
	@Column
	private String batchDeleteOwnerName;
	@Column
	private String batchDeleteSource;
	@Column
	private BatchDeleteTypes batchDeleteType;

	@ManyToOne(fetch = FetchType.LAZY)
	private PersonEntity batchDeleteOwnerPerson;
	@OneToMany(mappedBy= "batchDelete", cascade = CascadeType.ALL)
	private List<BatchDeleteItemEntity> data;

	public PersonEntity getBatchDeleteOwnerPerson() {
		return batchDeleteOwnerPerson;
	}

	public void setBatchDeleteOwnerPerson(PersonEntity batchOwnerPerson) {
		this.batchDeleteOwnerPerson = batchOwnerPerson;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBatchDeleteName() {
		return batchDeleteName;
	}

	public void setBatchDeleteName(String batchName) {
		this.batchDeleteName = batchName;
	}

	public String getBatchDeleteOwnerName() {
		return batchDeleteOwnerName;
	}

	public void setBatchDeleteOwnerName(String batchOwnerName) {
		this.batchDeleteOwnerName = batchOwnerName;
	}

	public String getBatchDeleteSource() {
		return batchDeleteSource;
	}

	public void setBatchDeleteSource(String batchSource) {
		this.batchDeleteSource = batchSource;
	}

	public BatchDeleteTypes getBatchDeleteType() {
		return batchDeleteType;
	}

	public void setBatchDeleteType(BatchDeleteTypes batchType) {
		this.batchDeleteType = batchType;
	}

	public List<BatchDeleteItemEntity> getData() {
		return data;
	}

	public void setData(List<BatchDeleteItemEntity> data) {
		this.data = data;
	}
	
	
}