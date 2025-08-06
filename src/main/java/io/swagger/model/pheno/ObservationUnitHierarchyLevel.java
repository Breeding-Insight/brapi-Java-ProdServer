package io.swagger.model.pheno;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ObservationUnitHierarchyLevel {

	@JsonProperty("levelNameDbId")
	private String levelNameDbId = null;

	@JsonProperty("levelName")
	private String levelName = null;

	@JsonProperty("levelOrder")
	private Integer levelOrder = null;

	@JsonProperty("programDbId")
	private String programDbId = null;

	// NOTE: This property is NOT used for lookups, only responses.
	@JsonProperty("programName")
	private String programName = null;

	public String getLevelNameDbId() {
		return levelNameDbId;
	}

	public void setLevelNameDbId(String value) {
		this.levelNameDbId = value;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public ObservationUnitHierarchyLevel levelOrder(Integer levelOrder) {
		this.levelOrder = levelOrder;
		return this;
	}

	public Integer getLevelOrder() {
		return levelOrder;
	}

	public void setLevelOrder(Integer levelOrder) {
		this.levelOrder = levelOrder;
	}

	public String getProgramDbId() {
		return programDbId;
	}

	public void setProgramDbId(String value) {
		this.programDbId = value;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String value) {
		this.programName = value;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ObservationUnitHierarchyLevel observationUnitHierarchyLevel = (ObservationUnitHierarchyLevel) o;
		return Objects.equals(this.levelNameDbId, observationUnitHierarchyLevel.levelNameDbId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(levelNameDbId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ObservationUnitHierarchyLevel {\n");

		sb.append("    levelName: ").append(toIndentedString(levelName)).append("\n");
		sb.append("    levelOrder: ").append(toIndentedString(levelOrder)).append("\n");
		sb.append("    levelNameDbId:  ").append(toIndentedString(levelNameDbId)).append("\n");
		sb.append("    programDbId: ").append(toIndentedString(programDbId)).append("\n");
		sb.append("    programName:  ").append(toIndentedString(programName)).append("\n");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
