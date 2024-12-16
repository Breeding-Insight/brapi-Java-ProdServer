package io.swagger.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.BrAPIResponse;
import io.swagger.model.Context;
import io.swagger.model.Metadata;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * BatchessBatchResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:31:52.030Z[GMT]")
public class BatchDeletesListResponse implements BrAPIResponse<BatchDeletesListResponseResult> {
	@JsonProperty("@context")
	private Context _atContext = null;

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private BatchDeletesListResponseResult result = null;

	public BatchDeletesListResponse() {
		this._atContext = new Context();
		this._atContext.add("context");
	}
	

	public void set_atContext(Context _atContext) {
		this._atContext = _atContext;
	}
	public BatchDeletesListResponse metadata(Metadata metadata) {
		this.metadata = metadata;
		return this;
	}

	/**
	 * Get metadata
	 * 
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

	public BatchDeletesListResponse result(BatchDeletesListResponseResult result) {
		this.result = result;
		return this;
	}

	/**
	 * Get result
	 * 
	 * @return result
	 **/
	@ApiModelProperty(required = true, value = "")
	

	@Valid
	public BatchDeletesListResponseResult getResult() {
		return result;
	}

	public void setResult(BatchDeletesListResponseResult result) {
		this.result = result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchDeletesListResponse BatchDeletesListResponse = (BatchDeletesListResponse) o;
		return Objects.equals(this._atContext, BatchDeletesListResponse._atContext)
				&& Objects.equals(this.metadata, BatchDeletesListResponse.metadata)
				&& Objects.equals(this.result, BatchDeletesListResponse.result);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_atContext, metadata, result);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BatchesBatchResponse {\n");

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
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
