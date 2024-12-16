package org.brapi.test.BrAPITestServer.exceptions;

import io.swagger.model.core.BatchDeleteTypes;
import org.springframework.http.HttpStatus;

public class BatchDeleteWrongTypeException extends BrAPIServerException {
    public BatchDeleteWrongTypeException(String msg, HttpStatus status) { super(status, ""); }

    public BatchDeleteWrongTypeException(BatchDeleteTypes requiredType, BatchDeleteTypes suppliedType, String batchDeleteDbId, HttpStatus status) {
        super(status, "");
        String msg = "The " + suppliedType.toString() + " batch delete " + batchDeleteDbId + " must be of type " + requiredType.toString();
        this.setResponseMessage(msg);
    }
}
