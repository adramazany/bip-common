package bip.common.export.exception;

/**
 * Created by ramezani on 2/5/2019.
 */
public class ExportServiceException extends Exception {
    public ExportServiceException() {
    }

    public ExportServiceException(String message) {
        super(message);
    }

    public ExportServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExportServiceException(Throwable cause) {
        super(cause);
    }

    public ExportServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
