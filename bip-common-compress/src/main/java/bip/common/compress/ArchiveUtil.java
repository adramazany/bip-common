package bip.common.compress;

import bip.common.util.BIPUtil;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import net.sf.sevenzipjbinding.util.ByteArrayStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

/**
 * Created by ramezani on 6/4/2019.
 */
public class ArchiveUtil {
    static final Logger logger = LoggerFactory.getLogger(ArchiveUtil.class);

    /***********************************************
    * detect archive type
    * **********************************************/
//    public static boolean isRar(File file){
//    }
//    public static boolean is7Zip(File file){
//    }
//    public static boolean isZip(File file){
//    }

    /***********************************************
     * detect encryption
     * **********************************************/
    public static boolean isEncrypted(File sourceZipFile) throws FileNotFoundException, SevenZipException {
        RandomAccessFile randomAccessFile= new RandomAccessFile(sourceZipFile, "r");
        IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                new RandomAccessFileInStream(randomAccessFile));

        boolean result = isEncrypted(inArchive);
        logger.info(String.format("isEncrypted$File(%s)=%s",sourceZipFile.getName(),result));
        return result;
    }
    public static boolean isEncrypted(byte[] content) throws SevenZipException {
        IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                new ByteArrayStream(content,false));

        boolean result = isEncrypted(inArchive);
        logger.info(String.format("isEncrypted$content(%s)=%s",new String(content,0,10)+" ...",result));
        return result;
    }
    private static boolean isEncrypted(IInArchive inArchive) throws SevenZipException {
        ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

        int counter=0;
        while(counter<simpleInArchive.getNumberOfItems() && simpleInArchive.getArchiveItem(counter).isFolder()){
            counter++;
        }
        boolean result = simpleInArchive.getArchiveItem(counter).isEncrypted();
        inArchive.close();
        return result;
    }

    /***********************************************
     * find valid password
     * **********************************************/
    public static String verifyPasswords(File sourceZipFile,String[] passwords) throws FileNotFoundException, SevenZipException {
        if(passwords==null || passwords.length==0)return null;
        RandomAccessFile randomAccessFile= new RandomAccessFile(sourceZipFile, "r");
        IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                new RandomAccessFileInStream(randomAccessFile));
        ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

        int counter=0;
        while(counter<simpleInArchive.getNumberOfItems() && simpleInArchive.getArchiveItem(counter).isFolder()){
            counter++;
        }
        ISimpleInArchiveItem archiveItem = simpleInArchive.getArchiveItem(counter);
        for (int i = 0; i < passwords.length; i++) {
            ByteArrayStream sequentialOutStream=new ByteArrayStream(archiveItem.getSize().intValue());
            try {
                ExtractOperationResult result = archiveItem.extractSlow(sequentialOutStream, passwords[i]);
                if(result==ExtractOperationResult.OK){
                    logger.info(String.format("verifyPasswords(%s)=%d",sourceZipFile.getName(),i));
                    return passwords[i];
                }
            }catch(Exception ex){
                logger.error(String.format("error extracting encrypted %s with password:%s",sourceZipFile.getName(),passwords[i]),ex);
            }
        }
        logger.warn(String.format("verifyPasswords(%s)=null",sourceZipFile.getName()));
        return null;
    }
    /***********************************************
     * extract
     * **********************************************/
    public static File extractToTemp(final File sourceZipFile,final String[] passwords) throws IOException {
        return extractToTemp("bip-","",sourceZipFile,passwords);
    }
    public static File extractToTemp(String tempPrefix,String tempSuffix, final File sourceZipFile,final String[] passwords) throws IOException {
        File outputFolder = File.createTempFile(tempPrefix,tempSuffix);
        if(!(outputFolder.delete())){
            throw new IOException("Could not delete temp file: " + outputFolder.getPath());
        }

        String filenameNoExt = BIPUtil.getFileNameNoPathNoExt(sourceZipFile.getName());
        File destFolder = new File(outputFolder,filenameNoExt);
        destFolder.mkdirs();

        extract(sourceZipFile,destFolder,passwords);

        return outputFolder;
    }
    public static void extract(final File sourceZipFile,final File destFolder,final String[] passwords) throws FileNotFoundException, SevenZipException {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        ISimpleInArchive simpleInArchive = null;
        try{

        randomAccessFile= new RandomAccessFile(sourceZipFile, "r");
        inArchive = SevenZip.openInArchive(null, // autodetect archive type
                new RandomAccessFileInStream(randomAccessFile));
        simpleInArchive = inArchive.getSimpleInterface();

        boolean isEncrypted = isEncrypted(sourceZipFile);
        String verifiedPassword = null;
        if(isEncrypted){
            verifiedPassword = verifyPasswords(sourceZipFile,passwords);
            if(verifiedPassword==null){
                throw new PasswordIncorrectZipException(String.format("provided passwords %s is incorrect for %s zip file!", Arrays.toString(passwords),sourceZipFile.getName()));
            }
        }

        for (int i = 0; i < inArchive.getNumberOfItems(); i++) {
            ISimpleInArchiveItem archiveItem = simpleInArchive.getArchiveItem(i);

            if(!archiveItem.isFolder()) {
                final File outFile = new File(destFolder, archiveItem.getPath());
                outFile.getParentFile().mkdirs();
                logger.debug(String.format("extract(%s) in progress: %s", sourceZipFile.getName(), archiveItem.getPath()));
                RandomAccessFile out = new RandomAccessFile(outFile, "rw");
                ExtractOperationResult result = inArchive.extractSlow(i,new RandomAccessFileOutStream(out),verifiedPassword);
/*
                final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
                ExtractOperationResult result = archiveItem.extractSlow(new ISequentialOutStream() {
                    public int write(byte[] data) throws SevenZipException {
                        try {
                            out.write(data);
                        } catch (IOException e) {
                            throw new SevenZipException(String.format("error in writing extracted data from:%s to:%s ", sourceZipFile.getName(), outFile.getName()), e);
                        }
                        return data.length; // return amount of consumed data
                    }
                },verifiedPassword);
*/
                try {
                    out.close();
                }catch(Exception e){
                    logger.warn(String.format("extracted file:%s is not closed! %s",archiveItem.getPath(),e.getMessage()));
                }
                if (result != ExtractOperationResult.OK) {
                    throw new SevenZipException(String.format(" %s error occured in extracting : %s item of file : %s ", result.name(), archiveItem.getPath(), sourceZipFile.getName()));
                }
            }
        }

        }finally{
            try {
                //inArchive.getSimpleInterface().close();
                if(inArchive!=null){
                    inArchive.close();
                }
                if(randomAccessFile!=null){
                    randomAccessFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
