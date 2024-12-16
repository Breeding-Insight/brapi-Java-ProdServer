package io.swagger.model.core;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.ExternalReferences;

import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

/**
 * BatchBaseFields
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
public class BatchDeleteBaseFields implements BatchDeleteBaseFieldsInterface {
  @JsonProperty("additionalInfo")
  @Valid
  private Object additionalInfo = null;

  @JsonProperty("dateCreated")
  private OffsetDateTime dateCreated = null;

  @JsonProperty("dateModified")
  private OffsetDateTime dateModified = null;

  @JsonProperty("externalReferences")
  private ExternalReferences externalReferences = null;

  @JsonProperty("batchDeleteDescription")
  private String batchDeleteDescription = null;

  @JsonProperty("batchDeleteName")
  private String batchDeleteName = null;

  @JsonProperty("batchDeleteOwnerName")
  private String batchDeleteOwnerName = null;

  @JsonProperty("batchDeleteOwnerPersonDbId")
  private String batchDeleteOwnerPersonDbId = null;

  @JsonProperty("batchDeleteSize")
  private Integer batchDeleteSize = null;

  @JsonProperty("batchDeleteSource")
  private String batchDeleteSource = null;

  @JsonProperty("batchDeleteType")
  private BatchDeleteTypes batchDeleteType = null;

  public BatchDeleteBaseFields additionalInfo(Object additionalInfo) {
    this.additionalInfo = additionalInfo;
    return this;
  }

  /**
   * Additional arbitrary info
   *
   * @return additionalInfo
   **/
  @ApiModelProperty(example = "{}", value = "Additional arbitrary info")
  
    public Object getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(Object additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public BatchDeleteBaseFields dateCreated(OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
    return this;
  }

  /**
   * Timestamp when the entity was first created
   * @return dateCreated
  **/
  @ApiModelProperty(value = "Timestamp when the entity was first created")
  
    @Valid
    public OffsetDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public BatchDeleteBaseFields dateModified(OffsetDateTime dateModified) {
    this.dateModified = dateModified;
    return this;
  }

  /**
   * Timestamp when the entity was last updated
   * @return dateModified
  **/
  @ApiModelProperty(value = "Timestamp when the entity was last updated")
  
    @Valid
    public OffsetDateTime getDateModified() {
    return dateModified;
  }

  public void setDateModified(OffsetDateTime dateModified) {
    this.dateModified = dateModified;
  }

  public BatchDeleteBaseFields externalReferences(ExternalReferences externalReferences) {
    this.externalReferences = externalReferences;
    return this;
  }

  /**
   * Get externalReferences
   * @return externalReferences
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public ExternalReferences getExternalReferences() {
    return externalReferences;
  }

  public void setExternalReferences(ExternalReferences externalReferences) {
    this.externalReferences = externalReferences;
  }

  public BatchDeleteBaseFields batchDeleteDescription(String batchDeleteDescription) {
    this.batchDeleteDescription = batchDeleteDescription;
    return this;
  }

  /**
   * Description of a Batch
   * @return batchDescription
  **/
  @ApiModelProperty(example = "This is a batch of germplasm I would like to investigate next season", value = "Description of a Batch")
  
    public String getBatchDeleteDescription() {
    return batchDeleteDescription;
  }

  public void setBatchDeleteDescription(String batchDeleteDescription) {
    this.batchDeleteDescription = batchDeleteDescription;
  }

  public BatchDeleteBaseFields batchDeleteName(String batchDeleteName) {
    this.batchDeleteName = batchDeleteName;
    return this;
  }

  /**
   * Human readable name of a Batch
   * @return batchName
  **/
  @ApiModelProperty(example = "MyGermplasm_Sept_2020", value = "Human readable name of a Batch")
  
    public String getBatchDeleteName() {
    return batchDeleteName;
  }

  public void setBatchDeleteName(String batchDeleteName) {
    this.batchDeleteName = batchDeleteName;
  }

  public BatchDeleteBaseFields batchDeleteOwnerName(String batchDeleteOwnerName) {
    this.batchDeleteOwnerName = batchDeleteOwnerName;
    return this;
  }

  /**
   * Human readable name of a Batch Owner. (usually a user or person)
   * @return batchOwnerName
  **/
  @ApiModelProperty(example = "Bob Robertson", value = "Human readable name of a Batch Owner. (usually a user or person)")
  
    public String getBatchDeleteOwnerName() {
    return batchDeleteOwnerName;
  }

  public void setBatchDeleteOwnerName(String batchDeleteOwnerName) {
    this.batchDeleteOwnerName = batchDeleteOwnerName;
  }

  public BatchDeleteBaseFields batchDeleteOwnerPersonDbId(String batchDeleteOwnerPersonDbId) {
    this.batchDeleteOwnerPersonDbId = batchDeleteOwnerPersonDbId;
    return this;
  }

  /**
   * The unique identifier for a Batch Owner. (usually a user or person)
   * @return batchOwnerPersonDbId
  **/
  @ApiModelProperty(example = "58db0628", value = "The unique identifier for a Batch Owner. (usually a user or person)")
  
    public String getBatchDeleteOwnerPersonDbId() {
    return batchDeleteOwnerPersonDbId;
  }

  public void setBatchDeleteOwnerPersonDbId(String batchDeleteOwnerPersonDbId) {
    this.batchDeleteOwnerPersonDbId = batchDeleteOwnerPersonDbId;
  }

  public BatchDeleteBaseFields batchDeleteSize(Integer batchDeleteSize) {
    this.batchDeleteSize = batchDeleteSize;
    return this;
  }

  /**
   * The number of elements in a Batch
   * @return batchSize
  **/
  @ApiModelProperty(example = "53", value = "The number of elements in a Batch")
  
    public Integer getBatchDeleteSize() {
    return batchDeleteSize;
  }

  public void setBatchDeleteSize(Integer batchDeleteSize) {
    this.batchDeleteSize = batchDeleteSize;
  }

  public BatchDeleteBaseFields batchDeleteSource(String batchDeleteSource) {
    this.batchDeleteSource = batchDeleteSource;
    return this;
  }

  /**
   * The description of where a Batch originated from
   * @return batchSource
  **/
  @ApiModelProperty(example = "GeneBank Repository 1.3", value = "The description of where a Batch originated from")
  
    public String getBatchDeleteSource() {
    return batchDeleteSource;
  }

  public void setBatchDeleteSource(String batchDeleteSource) {
    this.batchDeleteSource = batchDeleteSource;
  }

  public BatchDeleteBaseFields batchDeleteType(BatchDeleteTypes batchDeleteType) {
    this.batchDeleteType = batchDeleteType;
    return this;
  }

  /**
   * Get batchType
   * @return batchType
  **/
  @ApiModelProperty(required = true, value = "")
      

    @Valid
    public BatchDeleteTypes getBatchDeleteType() {
    return batchDeleteType;
  }

  public void setBatchDeleteType(BatchDeleteTypes batchDeleteType) {
    this.batchDeleteType = batchDeleteType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BatchDeleteBaseFields batchBaseFields = (BatchDeleteBaseFields) o;
    return Objects.equals(this.additionalInfo, batchBaseFields.additionalInfo) &&
        Objects.equals(this.dateCreated, batchBaseFields.dateCreated) &&
        Objects.equals(this.dateModified, batchBaseFields.dateModified) &&
        Objects.equals(this.externalReferences, batchBaseFields.externalReferences) &&
        Objects.equals(this.batchDeleteDescription, batchBaseFields.batchDeleteDescription) &&
        Objects.equals(this.batchDeleteName, batchBaseFields.batchDeleteName) &&
        Objects.equals(this.batchDeleteOwnerName, batchBaseFields.batchDeleteOwnerName) &&
        Objects.equals(this.batchDeleteOwnerPersonDbId, batchBaseFields.batchDeleteOwnerPersonDbId) &&
        Objects.equals(this.batchDeleteSize, batchBaseFields.batchDeleteSize) &&
        Objects.equals(this.batchDeleteSource, batchBaseFields.batchDeleteSource) &&
        Objects.equals(this.batchDeleteType, batchBaseFields.batchDeleteType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionalInfo, dateCreated, dateModified, externalReferences, batchDeleteDescription, batchDeleteName, batchDeleteOwnerName, batchDeleteOwnerPersonDbId, batchDeleteSize, batchDeleteSource, batchDeleteType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BatchBaseFields {\n");
    
    sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
    sb.append("    dateCreated: ").append(toIndentedString(dateCreated)).append("\n");
    sb.append("    dateModified: ").append(toIndentedString(dateModified)).append("\n");
    sb.append("    externalReferences: ").append(toIndentedString(externalReferences)).append("\n");
    sb.append("    batchDescription: ").append(toIndentedString(batchDeleteDescription)).append("\n");
    sb.append("    batchName: ").append(toIndentedString(batchDeleteName)).append("\n");
    sb.append("    batchOwnerName: ").append(toIndentedString(batchDeleteOwnerName)).append("\n");
    sb.append("    batchOwnerPersonDbId: ").append(toIndentedString(batchDeleteOwnerPersonDbId)).append("\n");
    sb.append("    batchSize: ").append(toIndentedString(batchDeleteSize)).append("\n");
    sb.append("    batchSource: ").append(toIndentedString(batchDeleteSource)).append("\n");
    sb.append("    batchType: ").append(toIndentedString(batchDeleteType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}