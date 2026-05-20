package org.brapi.test.BrAPITestServer.exceptions;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class ExceptionUtils {
    private static final String SQL_CONFLICT_ERROR_CODE = "23505";

    public static <T extends Throwable> T findCause(
            Throwable throwable,
            Class<T> targetType
    ) {
        Throwable t = throwable;
        while (t != null) {
            if (targetType.isInstance(t)) {
                return targetType.cast(t);
            }
            t = t.getCause();
        }
        return null;
    }

    /**
     * Throws a BrAPIServerException with status 409 (CONFLICT) if a duplicate is detected in the database.
     * The offending table and key that failed will also be sent in the response.
     */
    public static <T extends Throwable> void checkForPSQLConflict(Throwable possibleConflict) throws BrAPIServerException {
        PSQLException possibleConflictException = findCause(possibleConflict, PSQLException.class);

        String sqlState = Optional.ofNullable(possibleConflictException)
                .map(PSQLException::getServerErrorMessage)
                .map(ServerErrorMessage::getSQLState)
                .orElse(null);

        if (sqlState != null && sqlState.equals(SQL_CONFLICT_ERROR_CODE)) {
            String conflictDetailMessage = possibleConflictException.getServerErrorMessage().getDetail();
            String offendingTable = possibleConflictException.getServerErrorMessage().getTable();

            String errorResponse = String.format("A duplicate of an existing record is trying be submitted on table %s.\n\n %s", offendingTable, conflictDetailMessage);

            throw new BrAPIServerException(HttpStatus.CONFLICT, errorResponse);
        }
    }
}
