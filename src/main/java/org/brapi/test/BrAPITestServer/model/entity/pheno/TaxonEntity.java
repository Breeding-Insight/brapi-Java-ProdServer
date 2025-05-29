package org.brapi.test.BrAPITestServer.model.entity.pheno;

import jakarta.persistence.*;

import org.brapi.test.BrAPITestServer.model.entity.BrAPIBaseEntity;
import org.brapi.test.BrAPITestServer.model.entity.germ.GermplasmEntity;

import java.util.UUID;

@Entity
@Table(name = "germplasm_taxon")
public class TaxonEntity extends BrAPIBaseEntity {
	@Column
	private String sourceName;
	@Column
	private UUID taxonId;
	@ManyToOne(fetch = FetchType.LAZY)
	private GermplasmEntity germplasm;
	
	public GermplasmEntity getGermplasm() {
		return germplasm;
	}
	public void setGermplasm(GermplasmEntity germplasm) {
		this.germplasm = germplasm;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public UUID getTaxonId() {
		return taxonId;
	}
	public void setTaxonId(UUID taxonId) {
		this.taxonId = taxonId;
	}
}
