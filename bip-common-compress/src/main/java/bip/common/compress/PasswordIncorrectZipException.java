package bip.common.compress;

import net.sf.sevenzipjbinding.SevenZipException;

/**
 * Created by ramezani on 3/23/2019.
 */
public class PasswordIncorrectZipException extends SevenZipException {
    public PasswordIncorrectZipException() {
    }

    public PasswordIncorrectZipException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PasswordIncorrectZipException(String s) {
        super(s);
    }

    public PasswordIncorrectZipException(Throwable throwable) {
        super(throwable);
    }
}
