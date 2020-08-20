package bip.common.batch.exceptions;

/**
 * Created by ramezani on 10/5/2017.
 */
public class CouldNotCheckExistsException extends Exception {
    public CouldNotCheckExistsException(String message) {
        super(message);
    }

    public CouldNotCheckExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
