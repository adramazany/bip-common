package aip.ssl;


import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/** Establish a SSL connection to a host and port, writes a byte and
 * prints the response. See
 * http://confluence.atlassian.com/display/JIRA/Connecting+to+SSL+services
 */
public class SSLPoke {
    public static void main(String[] args) {
    	//args=new String[2];//{""};
    	//args[0]="bmiutility4.bmi.ir";
    	//args[0]="bmi.ir";
    	//args[1]="443";
    	

    	//System.setProperty("javax.net.ssl.trustStore", "e:/aip.jks");
    	//System.setProperty("javax.net.ssl.trustStore", "e:\\java/jdk1.6.0_02\\jre\\lib\\security\\cacerts"); 
    	//System.setProperty("javax.net.ssl.trustStore", "myTrustStore");
    	//System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	//System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
//    	System.setProperty("javax.net.ssl.keyStore", "new_cert.p12");
//    	System.setProperty("javax.net.ssl.keyStorePassword", "newpass");
    	//System.setProperty("javax.net.ssl.keyStore", ".keystore");
    	//System.setProperty("javax.net.ssl.keyStorePassword", "aippia");
/*    	System.out.println("SSLPoke.main():");
    	System.out.println("javax.net.ssl.trustStore="+System.getProperty("javax.net.ssl.trustStore"));
    	System.out.println("javax.net.ssl.trustStorePassword="+System.getProperty("javax.net.ssl.trustStorePassword"));
    	System.out.println("javax.net.ssl.keyStoreType="+System.getProperty("javax.net.ssl.keyStoreType"));
    	System.out.println("javax.net.ssl.keyStore="+System.getProperty("javax.net.ssl.keyStore"));
    	System.out.println("javax.net.ssl.keyStorePassword="+System.getProperty("javax.net.ssl.keyStorePassword"));
*/
    	
    	
    	if (args.length != 2) {
			System.out.println("Usage: "+SSLPoke.class.getName()+" <host> <port>");
			System.out.println("Example: "+SSLPoke.class.getName()+" bmiutility4.bmi.ir 443 \n or pgws.bpm.bankmellat.ir 443");
			System.exit(1);
		}
		try {
			System.out.println(""+SSLPoke.class.getName()+" "+args[0]+" "+args[1]);
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(args[0], Integer.parseInt(args[1]));

			InputStream in = sslsocket.getInputStream();
			OutputStream out = sslsocket.getOutputStream();

			// Write a test byte to get a reaction :)
			out.write(1);

			while (in.available() > 0) {
				System.out.print(in.read());
			}
			System.out.println("Successfully connected");

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}

