package bip.common.compress.detect;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;

/**
 * Created by ramezani on 6/4/2019.
 */
public interface EncryptionDetector {
    boolean isEncrypt(File file) throws ZipException;
    //boolean isEncrypt(byte[] file);
    //boolean isEncrypt(InputStream file);
}
