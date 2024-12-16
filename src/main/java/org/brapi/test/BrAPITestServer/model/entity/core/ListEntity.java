package org.brapi.test.BrAPITestServer.model.entity.core;

import io.swagger.model.core.ListTypes;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "list")
@Where(clause = "soft_deleted = false")
public class ListEntity extends BrAPIPrimaryEntity {
	@Column
	private Date dateCreated;
	@Column
	private Date dateModified;
	@Column
	private String description;
	@Column
	private String listName;
	@Column
	private String listOwnerName;
	@Column
	private String listSource;
	@Column
	private ListTypes listType;
	@Column(name = "soft_deleted")
	private boolean softDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	private PersonEntity listOwnerPerson;
	@OneToMany(mappedBy="list", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderColumn(name = "position")
	private List<ListItemEntity> data;


    public PersonEntity getListOwnerPerson() {
        return listOwnerPerson;
    }

    public void setListOwnerPerson(PersonEntity listOwnerPerson) {
        this.listOwnerPerson = listOwnerPerson;
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

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListOwnerName() {
        return listOwnerName;
    }

    public void setListOwnerName(String listOwnerName) {
        this.listOwnerName = listOwnerName;
    }

    public String getListSource() {
        return listSource;
    }

    public void setListSource(String listSource) {
        this.listSource = listSource;
    }

    public ListTypes getListType() {
        return listType;
    }

    public void setListType(ListTypes listType) {
        this.listType = listType;
    }

    public void setData(List<ListItemEntity> data) {
        this.data = data;
    }

	public boolean getSoftDeleted() { return softDeleted; }

	public void setSoftDeleted(boolean sofDeleted) { this.softDeleted = sofDeleted; }

	public List<ListItemEntity> getData() {
		return data;
	}

}