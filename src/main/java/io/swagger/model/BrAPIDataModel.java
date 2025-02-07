package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class BrAPIDataModel {

	@JsonProperty("additionalInfo")
	protected JsonNode additionalInfo = null;

	@JsonProperty("externalReferences")
	protected ExternalReferences externalReferences = null;

	final public BrAPIDataModel additionalInfo(JsonNode additionalInfo) {
		this.additionalInfo = additionalInfo;
		return this;
	}

	final public JsonNode getAdditionalInfo() {
		return additionalInfo;
	}

	final public void setAdditionalInfo(JsonNode additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	

	final public BrAPIDataModel externalReferences(ExternalReferences externalReferences) {
		this.externalReferences = externalReferences;
		return this;
	}

	final public ExternalReferences getExternalReferences() {
		return externalReferences;
	}

	final public void setExternalReferences(ExternalReferences externalReferences) {
		this.externalReferences = externalReferences;
	}
}
