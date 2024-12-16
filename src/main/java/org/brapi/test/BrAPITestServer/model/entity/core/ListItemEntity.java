package org.brapi.test.BrAPITestServer.model.entity.core;

import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "list_item")
@Where(clause = "soft_deleted = false")
public class ListItemEntity extends BrAPIBaseEntity {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private ListEntity list;
	@Column
	private String item;
	@Column
	private Integer position;
	@Column(name = "soft_deleted")
	private boolean softDeleted;
	
	public ListEntity getList() {
		return list;
	}
	public void setList(ListEntity list) {
		this.list = list;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public boolean getSoftDeleted() { return softDeleted; }
	public void setSoftDeleted(boolean softDeleted) { this.softDeleted = softDeleted; }
}
