package io.swagger.model.pheno;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.BrAPIResponse;
import io.swagger.model.Context;
import io.swagger.model.Metadata;

import io.swagger.model.pheno.ObservationLevelListResponseResult;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;


/**
 * ObservationLevelListResponse
 */
@Validated
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:32:22.556Z[GMT]")
public class ObservationLevelSingleResponse implements BrAPIResponse<ObservationUnitHierarchyLevel>  {
    @JsonProperty("@context")
    private Context _atContext = null;

    @JsonProperty("metadata")
    private Metadata metadata = null;

    @JsonProperty("result")
    private ObservationUnitHierarchyLevel result = null;

    public ObservationLevelSingleResponse _atContext(Context _atContext) {
        this._atContext = _atContext;
        return this;
    }



    public void set_atContext(Context _atContext) {
        this._atContext = _atContext;
    }

    public ObservationLevelSingleResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Get metadata
     * @return metadata
     **/
    @ApiModelProperty(required = true, value = "")


    @Valid
    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ObservationLevelSingleResponse result(ObservationUnitHierarchyLevel result) {
        this.result = result;
        return this;
    }

    /**
     * Get result
     * @return result
     **/
    @ApiModelProperty(required = true, value = "")


    @Valid
    public ObservationUnitHierarchyLevel getResult() {
        return result;
    }

    public void setResult(ObservationUnitHierarchyLevel result) {
        this.result = result;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObservationLevelSingleResponse observationLevelSingleResponse = (ObservationLevelSingleResponse) o;
        return Objects.equals(this._atContext, observationLevelSingleResponse._atContext) &&
                Objects.equals(this.metadata, observationLevelSingleResponse.metadata) &&
                Objects.equals(this.result, observationLevelSingleResponse.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_atContext, metadata, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ObservationLevelListResponse {\n");

        sb.append("    _atContext: ").append(toIndentedString(_atContext)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
        sb.append("    result: ").append(toIndentedString(result)).append("\n");
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
