package bip.common.compress.detect;


import net.lingala.zip4j.core.HeaderReader;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by ramezani on 6/4/2019.
 */
public class Zip4jEncryptionDetector implements EncryptionDetector {
    public boolean isEncrypt(File file) throws ZipException {
/*
        RandomAccessFile raf = new RandomAccessFile();

        HeaderReader headerReader = new HeaderReader(raf);
        ZipModel zipModel = headerReader.readAllHeaders(fileNameCharset);
        //if(this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null) {
        //fileHeader != null
        ((FileHeader)zipModel.getCentralDirectory().getFileHeaders().get(0)).isEncrypted();



        return new ZipFile(file).isEncrypted();
*/
        throw new RuntimeException("Not yet implemented!");
    }
}
