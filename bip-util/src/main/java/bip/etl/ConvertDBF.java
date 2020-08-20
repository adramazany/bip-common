package bip.etl;

/**
 * Created by ramezani on 4/5/2018.
 */
import java.io.*;
import com.sun.jna.Native;


import com.linuxense.javadbf.*;
import com.sun.jna.Library;

/**
 * Created by ramezani on 4/5/2018.
 */
public class ConvertDBF {


 /*   static {
        try {
            System.load("c:\\Users\\ramezani\\Desktop\\sabt\\AMAR\\Dos2MsCnv.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }
*/
    public interface IClsDos2Ms extends Library {
        String CnvDos2Ms(String strdos );
    }


    public static void main(String[] args) {
        IClsDos2Ms cnv =(IClsDos2Ms)Native.loadLibrary("Dos2MsCnv", IClsDos2Ms.class);
        //IClsDos2Ms cnv =(IClsDos2Ms)Native.loadLibrary("c:\\Users\\ramezani\\Desktop\\sabt\\AMAR\\Dos2MsCnv.dll", IClsDos2Ms.class);
        //IClsDos2Ms cnv =(IClsDos2Ms)Native.loadLibrary("Dos2MsCnv.dll", IClsDos2Ms.class);
        //

        DBFReader reader = null;
        try {

            // create a DBFReader object
            reader = new DBFReader(new FileInputStream("c:\\Users\\ramezani\\Desktop\\sabt\\AMAR\\S_ELAT.DBF "));

            // get the field count if you want for some reasons like the following

            int numberOfFields = reader.getFieldCount();

            // use this count to fetch all field information
            // if required

            for (int i = 0; i < numberOfFields; i++) {

                DBFField field = reader.getField(i);

                // do something with it if you want
                // refer the JavaDoc API reference for more details
                //
                System.out.println(field.getName());
            }

            // Now, lets us start reading the rows

            Object[] rowObjects;

            while ((rowObjects = reader.nextRecord()) != null) {

                for (int i = 0; i < rowObjects.length; i++) {
                    String st = new String(rowObjects[i].toString().getBytes("Cp1256"));

                    System.out.println(st);
                }
            }

            // By now, we have iterated through all of the rows

        } catch (DBFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            DBFUtils.close(reader);
        }

        try {
            //System.out.println( new String("ÂáÑŽí æ ÈíãÇÑíåÇí ÊÛÐíåÇí".getBytes("Cp1250"),"Cp1256"));
            //System.out.println( new String(new String("ÂáÑŽí æ ÈíãÇÑíåÇí ÊÛÐíåÇí".getBytes("Cp1250"),"Cp1256").getBytes(),"UTF-8"));
            //System.out.println( new String("ÂáÑŽí æ ÈíãÇÑíåÇí ÊÛÐíåÇí".getBytes("Cp1252"),"Utf-8"));
            System.out.println( new String("ÂáÑŽí æ ÈíãÇÑíåÇí ÊÛÐíåÇí".getBytes("Cp1252"),"Cp1256"));//okay

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
