package bip.common.compress;

/**
 * Created by ramezani on 3/19/2019.
 */

import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.compress.PasswordRequiredException;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//import net.sf.sevenzipjbinding.ISevenZipInArchive;

//import de.idyl.crypto.zip.AesZipFileEncrypter;

/**
 * This is a utility class having utility method for various kinds of
 * zip and unzip operation.
 * This class performs the following operations.
 * 1. Normal zipping a directory
 * 2. Zipping a directory with password
 * 3. Normal unzipping a zip file
 * 4. Unzipping a password protected zip file
 *
 * @author Debadatta Mishra(PIKU)
 *
 */
public final class SevenZipUtil
{

    static final Logger logger = LoggerFactory.getLogger(SevenZipUtil.class);

    public static String unzip( final String sourceZipFile , final String destinationDir , final String[] passwords ) throws IOException {
/*
        try {
            unzipDir(sourceZipFile,destinationDir);
        }catch(PasswordRequiredException e){
            logger.error(e.getMessage());
*/
            int incorrectPasswordCount=0;
            for (String password:passwords) {
                try {
                    SevenZipUtil.unzipDirWithPassword(sourceZipFile, destinationDir, password);
                    logger.info(String.format("%s file uncompressed by password:%s",sourceZipFile,password));
                    return password;
                }catch (PasswordIncorrectZipException e2) {
                    incorrectPasswordCount++;
                    logger.error(String.format("password %s is incorrect for %s zip file!",password,sourceZipFile));
                }
            }
            if(incorrectPasswordCount==passwords.length) {
                throw new PasswordIncorrectZipException(String.format("provided passwords %s is incorrect for %s zip file!", Arrays.toString(passwords),sourceZipFile));
            }
//        }
        return null;
    }


    /**This method is used to write the contents from a zip file to a file
     * @param file of type {@link File}
     * @param zipIn of type {@link ZipInputStream}
     */
    private static void writeFile( File file , ZipInputStream zipIn) throws IOException {
//        try
//        {
            OutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = zipIn.read(buff)) > 0)
            {
                outStream.write(buff, 0, len);
            }
            outStream.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    /**This method is used to extract the zip file to a destination directory
     * @param srcZipfile of type {@link File} indicating the source zip file
     * @param destinationDir of type {@link File} indicating the destination
     * directory where the zip file will be extracted.
     * @throws IOException
     */
    private static void extract(File srcZipfile, File destinationDir) throws IOException
    {
        ZipInputStream zipIn = null;
        try
        {
            new ZipFile(srcZipfile);//check could extract zip file

            zipIn = new ZipInputStream(new FileInputStream(srcZipfile));
            ZipEntry entry = null;

            while ((entry = zipIn.getNextEntry()) != null)
            {
                String outFilename = entry.getName();

                if( !new File(destinationDir, outFilename).getParentFile().exists() )
                    new File(destinationDir, outFilename).getParentFile().mkdirs();

                if( !entry.isDirectory() )
                    writeFile(new File(destinationDir,outFilename), zipIn);
            }
            System.out.println("Zip file extracted successfully...");
//        }
//        catch( Exception e )
//        {
//            e.printStackTrace();
        }
        finally
        {
            if (zipIn != null)
            {
                zipIn.close();
            }
        }
    }

    /**This method is used to zip or compress a directory to create a zip file.
     * @param directory of type String indicating the source directory to be zipped
     * @param zos of type {@link ZipOutputStream}
     * @param path of type String indicating the path
     * @throws IOException
     */
    private static void compressDir(String directory, ZipOutputStream zos, String path) throws IOException {
        File zipDir = new File(directory);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++)
        {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory())
            {
                String filePath = f.getPath();
                compressDir(filePath, zos, path + f.getName() + "/");
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            try
            {
                ZipEntry anEntry = new ZipEntry(path + f.getName());
                zos.putNextEntry(anEntry);
                bytesIn = fis.read(readBuffer);
                while (bytesIn != -1)
                {
                    zos.write(readBuffer, 0, bytesIn);
                    bytesIn = fis.read(readBuffer);
                }
//            }
//            catch( Exception e )
//            {
//                e.printStackTrace();
            }
            finally
            {
                fis.close();
            }
        }
    }

    /**This method is used to zip a directory
     * @param dirName of type String indicating the path of the directory to be zipped
     * @param zipFileName of type String indicating the file name for the zip file
     */
    public static void zipDir( String dirName , String zipFileName ) throws IOException {
        if( zipFileName == null )
        {
            File tempFile = new File(dirName);
            zipFileName = tempFile.getAbsoluteFile().getParent()+ File.separator+tempFile.getName()+".zip";
        }

//        try
//        {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
            compressDir(dirName, zos, new File(dirName).getName()+File.separator);
            zos.close();
//        }
//        catch( NullPointerException npe )
//        {
//            npe.printStackTrace();
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch( IOException ie )
//        {
//            ie.printStackTrace();
//        }
    }

    /**This method is used to create a password protected zip file.
     * @param dirName of type String indicating the name of the directory to be zipped
     * @param zipFileName of type String indicating the name of the zip file to be created
     * @param password of type String indicating the password
     */
    public static void zipDirWithPassword( String dirName , String zipFileName , String password) throws IOException {
        if( zipFileName == null )
        {
            File tempFile = new File(dirName);
            zipFileName = tempFile.getAbsoluteFile().getParent() +File.separator+tempFile.getName()+".zip";
        }
        zipDir(dirName, zipFileName);
        String tempZipFileName = new File( dirName ).getAbsoluteFile() .getParent()+File.separator+"tempencrypted.zip";
//        try
//        {
            final AESEncrypter aesEncrypter = new AESEncrypterBC();
            AesZipFileEncrypter enc = new AesZipFileEncrypter (tempZipFileName,aesEncrypter);
            enc.add( new File(zipFileName), password);
            new File(zipFileName).delete();
            new File( tempZipFileName ).renameTo( new File (zipFileName));
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }

    /**This method is used to unzip a zip file to a directory
     * @param sourceZipFile of type String indicating the source zip file
     * @param destinationDir of type String indicating the destination directory
     * where the zip file will be extracted.
     */
    public static void unzipDir(String sourceZipFile, String destinationDir) throws IOException {
        try{
            extract( new File(sourceZipFile), new File(destinationDir));
        }catch( ZipException e ){
            if(e.getMessage().equalsIgnoreCase("encrypted ZIP entry not supported")){
                throw new PasswordRequiredException(sourceZipFile);
            }else{
                throw e;
            }
        }
    }

    /**This method is used to unzip a password protected zip file.
     * @param sourceZipFile of type String indicating the source zip file
     * @param destinationDir of type String indicating the directory where
     * the zip file will be extracted.
     * @param password of type String indicating the password.
     */
    public static void unzipDirWithPassword( final String sourceZipFile , final String destinationDir , final String password ) throws FileNotFoundException, SevenZipException{
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try
        {
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            inArchive = SevenZip.openInArchive(null, // autodetect archive type
                    new RandomAccessFileInStream(randomAccessFile));

// Getting simple interface of the archive inArchive
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();


            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems())
            {

                final int[] hash = new int[] { 0 };
                if (!item.isFolder())
                {
                    ExtractOperationResult result;
                    result = item.extractSlow(new ISequentialOutStream()
                    {
                        public int write(final byte[] data) throws SevenZipException
                        {
                            try
                            {
                                if(item.getPath().indexOf(File.separator)>0)
                                {
                                    String path = destinationDir+File.separator+item.getPath(). substring(0,item.getPath().lastIndexOf(File.separator));

                                    File folderExisting = new File(path);
                                    if (!folderExisting.exists())
                                        new File(path).mkdirs();
                                }
                                OutputStream out = new FileOutputStream(destinationDir+ File.separator+item.getPath());
                                out.write(data);
                                out.close();
                            }
                            catch( Exception e )
                            {
                                e.printStackTrace();
                            }
                            hash[0] |= Arrays.hashCode(data);
                            return data.length; // Return amount of proceed data
                        }
                    },password); /// password.
                    if (result == ExtractOperationResult.OK)
                    {
                        System.out.println(String.format("%9X | %s",
                                hash[0], item.getPath()));
                    }
                    else
                    {
                        //System.err.println("Error extracting item: " + result);
                        throw new SevenZipException("Error extracting item: " + result);
                    }
                }
            }
        }catch (SevenZipException e){
            //e.printStackTrace();
            if(e.getMessage().equalsIgnoreCase("Error extracting item: DATAERROR")){
                //throw new SevenZipException("please try another password!");
                throw new PasswordIncorrectZipException(String.format("entered password : %s is incorrect for %s zip file!",password,sourceZipFile),e);
            }else{
                throw e;
            }
        }
        finally
        {
            if (inArchive != null)
            {
                try
                {
                    inArchive.close();
                }
                catch (SevenZipException e)
                {
                    System.err.println("Error closing archive: " + e);
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null)
            {
                try
                {
                    randomAccessFile.close();
                }
                catch (IOException e)
                {
                    System.err.println("Error closing file: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

}

