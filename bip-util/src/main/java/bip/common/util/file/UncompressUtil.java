package bip.common.util.file;

import com.github.junrar.extract.ExtractArchive;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by ramezani on 4/19/2018.
 */
public class UncompressUtil {

    /*
    Apache Commons Compress software defines an API for working with compression and archive formats. These include: bzip2, gzip, pack200, lzma, xz, Snappy, traditional Unix Compress, DEFLATE, DEFLATE64, LZ4, Brotli, Zstandard and ar, cpio, jar, tar, zip, dump, 7z, arj.
     */


    public static void unCompress(String zipFile, String outputFolder,String archiveName) throws IOException, ArchiveException {
        File folder = new File(outputFolder);
        File zip = new File(zipFile);
        checkCompression(zip,folder);

        FileInputStream zipIn = new FileInputStream(zip);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName,zipIn);
        ArchiveEntry entry;
        while ((entry = archiveInputStream.getNextEntry()) != null) {
            File newFile = new File(outputFolder,entry.getName());
            newFile.getParentFile().mkdir();
            FileOutputStream fos = new FileOutputStream(newFile);

            if(entry.isDirectory()){
                newFile.mkdir();
                continue;
            }

            IOUtils.copy(archiveInputStream,fos);
        }
        zipIn.close();
    }
    public static void unTarGZ(String zipFile,String outputFolder)throws IOException {
        File folder = new File(outputFolder);
        File zip = new File(zipFile);
        checkCompression(zip,folder);

        final File input = new File(zipFile);
        final InputStream is = new FileInputStream(input);
        final CompressorInputStream in = new GzipCompressorInputStream(is, true);
        TarArchiveInputStream tin = new TarArchiveInputStream(in);
        TarArchiveEntry entry = tin.getNextTarEntry();
        while (entry != null) {
            File archiveEntry = new File(outputFolder, entry.getName());
            archiveEntry.getParentFile().mkdirs();
            if (entry.isDirectory()) {
                archiveEntry.mkdir();
                entry = tin.getNextTarEntry();
                continue;
            }
            OutputStream out = new FileOutputStream(archiveEntry);
            IOUtils.copy(tin, out);
            out.close();
            entry = tin.getNextTarEntry();
        }
        in.close();
        tin.close();
    }
    public static void unZip(String zipFile, String outputFolder) throws IOException {
        File folder = new File(outputFolder);
        File zip = new File(zipFile);
        checkCompression(zip,folder);

        byte[] buffer = new byte[1024];

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder,fileName);
                newFile.getParentFile().mkdir();
                if(ze.isDirectory()){
                    newFile.mkdir();
                    ze = zis.getNextEntry();
                    continue;
                }

                //System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

    }

    public static void unRar(String rarFile, String outputFolder) throws IOException {
        File rar = new File(rarFile);
        File folder = new File(outputFolder);
        checkCompression(rar,folder);
        //System.out.println("tmpDir="+tmpDir.getAbsolutePath());
        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(rar, folder);
        //System.out.println("finished.");
    }
    private static void checkCompression(File compressFile, File outputFolder)throws IOException {
        if(!compressFile.exists()){
            throw new IOException("rarFile not exists: " + compressFile.getAbsolutePath());
        }
        if(!(outputFolder.mkdirs())){
            throw new IOException("Could not create temp directory: " + outputFolder.getAbsolutePath());
        }

    }

    public static void uncompress(String compressFile, String outputFolder) throws IOException, ArchiveException {
        compressFile=compressFile.toLowerCase();
        if(compressFile.endsWith(".rar")) {
            unRar(compressFile, outputFolder);
        }else if(compressFile.endsWith(".tar.gz")){
            unTarGZ(compressFile, outputFolder);
        }else if(compressFile.endsWith(".zip")){
            unZip(compressFile, outputFolder);
            //unCompress(compressFile, outputFolder,"zip");
        }else{
            int extPos = compressFile.lastIndexOf('.');
            String ext = compressFile.substring(extPos+1);
            unCompress(compressFile, outputFolder,ext);
        }
    }
    public static File uncompress(String compressFile) throws IOException, ArchiveException {
        File outputFolder = File.createTempFile("bip-","");
        //System.out.println("tmpDir="+tmpDir.getAbsolutePath());
        if(!(outputFolder.delete())){
            throw new IOException("Could not delete temp file: " + outputFolder.getAbsolutePath());
        }
        uncompress(compressFile,outputFolder.getAbsolutePath());
        return outputFolder;
    }

    public static void main(String[] args) throws IOException, ArchiveException {
        System.out.println(uncompress("C:\\Users\\ramezani\\Desktop\\uncompress\\uncompress.zip").getAbsolutePath());
        System.out.println(uncompress("C:\\Users\\ramezani\\Desktop\\uncompress\\uncompress.rar").getAbsolutePath());
        System.out.println(uncompress("C:\\Users\\ramezani\\Desktop\\uncompress\\uncompress.tar.gz").getAbsolutePath());
    }

}
