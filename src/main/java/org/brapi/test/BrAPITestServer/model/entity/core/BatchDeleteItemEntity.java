package org.brapi.test.BrAPITestServer.model.entity.core;

import javax.persistence.*;

import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;

@Entity
@Table(name = "batch_delete_item")
public class BatchDeleteItemEntity extends BrAPIBaseEntity {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private BatchDeleteEntity batchDelete;
	@Column
	private String item;
	
	public BatchDeleteEntity getBatchDelete() {
		return batchDelete;
	}
	public void setBatchDelete(BatchDeleteEntity batch) {
		this.batchDelete = batch;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
}