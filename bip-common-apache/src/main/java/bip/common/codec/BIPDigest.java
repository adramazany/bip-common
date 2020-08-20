package bip.common.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ramezani on 3/18/2019.
 */
public class BIPDigest extends DigestUtils{

    public static String digestHex(String input, String algorithm)throws NoSuchAlgorithmException, UnsupportedEncodingException    {
        return digestHex(input,algorithm,"UTF-8");
    }
    public static String digestHex(String input, String algorithm,String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] resultByte = digest(input.getBytes(charset),algorithm);
        return new String(Hex.encodeHex(resultByte));
    }

    public static String digestBase64(String input, String algorithm)throws NoSuchAlgorithmException, UnsupportedEncodingException    {
        return digestBase64(input,algorithm,"UTF-8");
    }
    public static String digestBase64(byte[] input, String algorithm)throws NoSuchAlgorithmException, UnsupportedEncodingException    {
        byte[] resultByte = digest(input,algorithm);
        return new String(Base64.encodeBase64String(resultByte));
    }
    public static String digestBase64(String input, String algorithm,String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] resultByte = digest(input.getBytes(charset),algorithm);
        return new String(Base64.encodeBase64String(resultByte));
    }

    public static byte[] digest(byte[] input, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(input);
        byte[] resultByte = messageDigest.digest();
        return resultByte;
    }

}
