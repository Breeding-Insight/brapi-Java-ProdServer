package org.brapi.test.BrAPITestServer.model.entity.pheno;

import java.util.List;
import java.util.UUID;

public interface OntologyReferenceHolder {
    public OntologyEntity getOntology();

    public void setOntology(OntologyEntity ontology);

    public List<OntologyReferenceEntity> getOntologyReference();

    public void setOntologyReference(List<OntologyReferenceEntity> ontologyReference);

    public UUID getId();
}
