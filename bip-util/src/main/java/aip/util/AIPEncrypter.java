package aip.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class AIPEncrypter {

	synchronized public static String encode(String inputStr) {
		byte[] defaultBytes = inputStr.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');

				hexString.append(hex);
			}
			inputStr = hexString + "";
		} catch (NoSuchAlgorithmException nsae) {
		}
		return inputStr;
	}
	
	public static void main(String[] argsV) {
	}
}