package bip.common.compress;

import bip.common.util.BIPUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by ramezani on 6/4/2019.
 */
public class ArchiveUtilTest {
    String[] passwords=new String[]{"1234","12345","123"};
    String basePath = "C:\\java\\workspace-maven\\bip-common\\bip-common-compress\\src\\test\\resources\\";
    String[] zipFiles=new String[]{
             "RFP-pass-123.zip"
            ,"test-pass-123.rar"
            ,"test-pass-123.zip"
            ,"tmp-pass-123.7z"
            ,"tmp-pass-123.rar"
            ,"tmp-pass-123.zip"
    };

    @org.junit.Test
    public void extractAndDelete() throws Exception {
        File srcZip = new File("e:\\fullshare\\sepehr\\error_file_inject\\moved\\saman.bip@gmail.com.zip");

        ArchiveUtil.extractToTemp(srcZip,new String[]{"123"});
        Assert.assertTrue("could not delete "+srcZip,srcZip.delete());
        System.out.println("succeed.");
    }

    @org.junit.Test
    public void isEncryptedFile() throws Exception {
    }

    @org.junit.Test
    public void isEncryptedContent() throws Exception {
    }

    @org.junit.Test
    public void verifyPasswords() throws Exception {
        for (int i = 0; i < zipFiles.length; i++) {
            String verifiedPass = ArchiveUtil.verifyPasswords(new File(basePath+zipFiles[i]) , passwords );
            System.out.println(String.format("%s zip file password = %s",zipFiles[i],verifiedPass));
            assertNotNull(String.format("verifiedPass should not to be null:%s",zipFiles[i]),verifiedPass);
        }
    }

    @org.junit.Test
    public void extract() throws Exception {
        String srcZipPath = "e:\\email\\adramazany@yahoo.com.rar";
        //String srcZipPath = "C:\\java\\workspace-maven\\bip-common\\bip-common-compress\\src\\test\\resources\\test-pass-123.rar";
        File srcZip = new File(srcZipPath);
        String filenameNoExt = BIPUtil.getFileNameNoPathNoExt(srcZipPath);
        File destFolder = new File(srcZip.getParent(),filenameNoExt);
        destFolder.mkdirs();

        ArchiveUtil.extract(srcZip,destFolder,passwords);
        System.out.println("succeed.");
    }

    @Test
    public void testPerformanceOfRandomAccessVSOutStream() throws Exception {

        OutputStream os;
        RandomAccessFile raf;
        int size = 10000;
        File file = new File("test.log");

        long a=System.currentTimeMillis();
        os =  new FileOutputStream(file);
        for(int i=0;i<size;i++){
            os.write(("1").getBytes());
        }
        os.close();
        long b=System.currentTimeMillis();
        System.out.println("writing direct "+(b-a));

        raf = new RandomAccessFile(file,"rws");
        for(int i=0;i<size;i++){
            raf.write(("1").getBytes());
        }
        raf.close();

        long c=System.currentTimeMillis();
        System.out.println("random access write "+(c-b));

        raf = new RandomAccessFile(file,"rw");
        for(int i=0;i<size;i++){
            raf.write(("1").getBytes());
        }
        raf.close();

        long d=System.currentTimeMillis();
        System.out.println("random access optimized write "+(d-c));

        os = new BufferedOutputStream( new FileOutputStream(file));
        for(int i=0;i<size;i++){
            os.write(("1").getBytes());
        }
        os.close();
        long e=System.currentTimeMillis();
        System.out.println("buffered writing direct "+(e-d));


    }


}