package bip.common.compress.test;

import bip.common.compress.ArchiveUtil;
import net.sf.sevenzipjbinding.SevenZipException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by ramezani on 7/6/2019.
 */
public class TestArchiveUtil {

    public static void main(String[] args) throws FileNotFoundException, SevenZipException {
        if(args.length!=3){
            System.err.println("sytax: java ArchiveUtil <sourceZipFile> <destFolder> <password1,password2>");
            System.exit(0);
        }
        File sourceZipFile=new File(args[0]);
        File destFolder=new File(args[1]);
        String[] passwords=args[2].split(",");

        ArchiveUtil.extract(sourceZipFile,destFolder,passwords);
    }


}
