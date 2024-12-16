package io.swagger.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.BrAPIResponse;
import io.swagger.model.Context;
import io.swagger.model.Metadata;

import java.util.Objects;

public class BatchDeletesSingleResponse implements BrAPIResponse<BatchDeleteDetails> {
	@JsonProperty("@context")
	private Context _atContext = null;

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private BatchDeleteDetails result = null;

	public BatchDeletesSingleResponse() {
		this._atContext = new Context();
		this._atContext.add("context");
	}

	public BatchDeletesSingleResponse metadata(Metadata metadata) {
		this.metadata = metadata;
		return this;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public BatchDeletesSingleResponse result(BatchDeleteDetails result) {
		this.result = result;
		return this;
	}

	public BatchDeleteDetails getResult() {
		return result;
	}

	public void setResult(BatchDeleteDetails result) {
		this.result = result;
	}

	@Override
	public void set_atContext(Context _atContext) {
		this._atContext = _atContext;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchDeletesSingleResponse BatchDeletesSingleResponse = (BatchDeletesSingleResponse) o;
		return Objects.equals(this._atContext, BatchDeletesSingleResponse._atContext)
				&& Objects.equals(this.metadata, BatchDeletesSingleResponse.metadata)
				&& Objects.equals(this.result, BatchDeletesSingleResponse.result);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_atContext, metadata, result);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BatchesSingleResponse {\n");

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
