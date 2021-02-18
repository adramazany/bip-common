package aip.olap.util;

import aip.util.AIPException;
import aip.util.NVL;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AIPSoapUtil {

	public static String executeSOAP(String url,String xmlSoapText) throws AIPException {
		AIPSoapUtil soapUtil = getInstance();
		SOAPMessage soapMessage = soapUtil.executeSOAP(url, xmlSoapText, null, null,null);
		return soapUtil.soapMessageToString(soapMessage);
	}

	public static String executeSOAP(String url,String username,String password, String xmlSoapText) throws AIPException{
		AIPSoapUtil soapUtil = getInstance();
		String authenticationTicket = soapUtil.loginSOAP(url, username, password);
		SOAPMessage soapMessage = soapUtil.executeSOAP(url, xmlSoapText, authenticationTicket, null,null);
		return soapUtil.soapMessageToString(soapMessage);
	}

	public SOAPMessage executeSOAP(String url, String xmlSoapText, String authenticationTicket, String namespacePrefix,String[][] namespacePrefixUri)throws AIPException {

		SOAPConnection soapConnection = null;
		try {
			final SOAPMessage soapMessage = getMessageSoap(namespacePrefixUri);


			SOAPBody soapBody = soapMessage.getSOAPBody();
			
			// MimeHeaders mh = soapMessage.getMimeHeaders();
			// mh.setHeader("SOAPAction",
			// "\"urn:schemas-microsoft-com:xml-analysis:Discover\"");
			// mh.setHeader("encoding", "utf8");
			// // PCF : role
			// if (headers != null) {
			// for (int i = 0; i < headers.length; i++)
			// mh.setHeader(headers[i][0], headers[i][1]);
			// }

			// SOAPPart soapPart = soapMessage.getSOAPPart();
			// SOAPEnvelope envelope = soapPart.getEnvelope();
			// SOAPBody body = envelope.getBody();

			//SOAPElement content = stringToSOAPElement(xmlSoapText);
			addXmlStringToSOAPBody(soapBody,xmlSoapText);
			//System.out.println("content=");content.toString();
			//soapBody.addChildElement(content);

//			try{		System.out.println("AIPSoapUtil.executeSOAP():soapMessage=");
//			soapMessage.writeTo(System.out);}catch(Exception e){}
//			System.out.println("");
			
			
			if(!NVL.isEmpty(authenticationTicket)){
				addAuthenticationTicket(authenticationTicket, soapMessage, namespacePrefix);
			}
			
			
			soapMessage.saveChanges();

			soapConnection = getConnectionSOAP();

			SOAPMessage soapMessageReply = soapConnection.call(soapMessage, url);

			SOAPBody replyBody = soapMessageReply.getSOAPBody();

			soapConnection.close();

			return soapMessageReply;
			
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} catch (SOAPException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} finally {
			if (soapConnection != null)try {soapConnection.close();} catch (SOAPException e) {}
		}

	}

	private static String loginSOAP(String url, String username, String password) throws AIPException {
		return getInstance().loginSOAP(url, username, password, "",null);
	}

	private String loginSOAP(String url, String username, String password, String namespacePrefix,String[][] namespacePrefixUri) throws AIPException {
		SOAPConnection soapConnection = null;
		try {
			final SOAPMessage soapMessage = getMessageSoap(namespacePrefixUri);
			final SOAPBody soapBody = soapMessage.getSOAPBody();
			final SOAPElement loginElement = soapBody.addChildElement("Login", namespacePrefix);

			loginElement.addChildElement("Username", namespacePrefix).addTextNode(username);
			loginElement.addChildElement("Password", namespacePrefix).addTextNode(password);

			soapMessage.saveChanges();

			soapConnection = getConnectionSOAP();
			final SOAPMessage soapMessageReply = soapConnection.call(soapMessage, url);
			final String textContent = soapMessageReply.getSOAPHeader().getFirstChild().getTextContent();

			return textContent;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AIPException(e);
		} finally {
			if (soapConnection != null)
				try {
					soapConnection.close();
				} catch (SOAPException e) {
				}
		}
	}

	private static SOAPConnection getConnectionSOAP() throws UnsupportedOperationException, SOAPException {
		final SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		final SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		return soapConnection;
	}

	private static SOAPMessage getMessageSoap() throws SOAPException {
		return getInstance().getMessageSoap(null);
	}

	private SOAPMessage getMessageSoap(String[][] namespacePrefixUri) throws SOAPException {
		final MessageFactory messageFactory = MessageFactory.newInstance();
		final SOAPMessage soapMessage = messageFactory.createMessage();
		
		soapMessage.getSOAPPart().recycleNode();
		

		
		// Object for message parts
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
//		SOAPHeader soapHeader = envelope.getHeader();
//		SOAPBody soapBody = envelope.getBody(); 
		
//		envelope.setPrefix("");
//		envelope.removeNamespaceDeclaration("SOAP-ENV");
//		
//		soapHeader.setPrefix("");
//		soapHeader.removeNamespaceDeclaration("SOAP-ENV");
//		
//		soapBody.setPrefix("");
//		soapBody.removeNamespaceDeclaration("SOAP-ENV");
		

//		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
//		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
//		envelope.addNamespaceDeclaration("enc", "http://schemas.xmlsoap.org/soap/encoding/");
//		envelope.addNamespaceDeclaration("env", "http://schemas.xmlsoap.org/soap/envelop/");

		if (namespacePrefixUri != null) {
			for (int i = 0; i < namespacePrefixUri.length; i++) {
				envelope.addNamespaceDeclaration(namespacePrefixUri[i][0], namespacePrefixUri[i][1]);
			}
		}

		//envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

		
		return soapMessage;
	}

	private void addAuthenticationTicket(String authenticationTicket, SOAPMessage soapMessage, String namespacePrefix)
			throws SOAPException {
		final SOAPHeader header = soapMessage.getSOAPHeader();
		final SOAPElement authenticationTicketHeader = header.addChildElement("AuthenticationTicketHeader",
				namespacePrefix);
		authenticationTicketHeader.addChildElement("Ticket", namespacePrefix).addTextNode(authenticationTicket);
	}

	private static SOAPElement addXmlStringToSOAPBody(SOAPBody soapBody ,String xmlText) throws SOAPException, IOException,
			ParserConfigurationException, SAXException {
		// try {
		// Load the XML text into a DOM Document
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);
		InputStream stream = new ByteArrayInputStream(xmlText.getBytes());
		Document doc = builderFactory.newDocumentBuilder().parse(stream);

		// Use SAAJ to convert Document to SOAPElement
		// Create SoapMessage
		MessageFactory msgFactory = MessageFactory.newInstance();
		//SOAPMessage message = msgFactory.createMessage();
		//SOAPBody soapBody = message.getSOAPBody();

		// This returns the SOAPBodyElement
		// that contains ONLY the Payload
		return soapBody.addDocument(doc);

		// } catch (SOAPException e) {
		// System.out.println("SOAPException : " + e);
		// return null;
		//
		// } catch (IOException e) {
		// System.out.println("IOException : " + e);
		// return null;
		//
		// } catch (ParserConfigurationException e) {
		// System.out.println("ParserConfigurationException : " + e);
		// return null;
		//
		// } catch (SAXException e) {
		// System.out.println("SAXException : " + e);
		// return null;
		//
		// }
	}
	public String soapMessageToString(SOAPMessage soapMessage)throws AIPException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(baos);
		} catch (SOAPException e) {
			e.printStackTrace();
			throw new AIPException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AIPException(e);
		}
		return baos.toString();
	}

	public static AIPSoapUtil getInstance() {
		return new AIPSoapUtil();
	}
	
	
	private static void main1(String[] args) {
		String xmlSoapText="<Batch xmlns=\"http://schemas.microsoft.com/analysisservices/2003/engine\"> \n" +
				"<Parallel> \n" +
				"<Process xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ddl2=\"http://schemas.microsoft.com/analysisservices/2003/engine/2\" xmlns:ddl2_2=\"http://schemas.microsoft.com/analysisservices/2003/engine/2/2\" xmlns:ddl100_100=\"http://schemas.microsoft.com/analysisservices/2008/engine/100/100\"> \n" +
				"<Object> \n" +
				"<DatabaseID>TavalodFot</DatabaseID> \n" +
				"<CubeID>Aipsabtbidw</CubeID> \n" +
				"</Object> \n" +
				"<Type>ProcessFull</Type> \n" +
				"<WriteBackTableCreation>UseExisting</WriteBackTableCreation> \n" +
				"</Process> \n" +
				"</Parallel> \n" +
				"</Batch>";
		String result;
		try {
			
			
			result = executeSOAP("http://192.168.0.71:80/olap/msmdpump.dll", xmlSoapText);
			System.out.println("AIPSoapUtil.main():result="+result);
		} catch (AIPException e) {
			e.printStackTrace();
		}
	}
	private static void main3(String[] args) {
	/*
	<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/>
	<SOAP-ENV:Body>
	<Discover xmlns="urn:schemas-microsoft-com:xml-analysis">
	<RequestType>DBSCHEMA_CATALOGS</RequestType>
	<Restrictions>
	<RestrictionList/>
	</Restrictions>
	<Properties><PropertyList>
	<Content>SchemaData</Content>
	<DataSourceInfo>WIN2003R2-SSAS</DataSourceInfo>
	</PropertyList>
	</Properties>
	</Discover>
	</SOAP-ENV:Body>
	</SOAP-ENV:Envelope>

<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/"><Header/>
<Body><Discover xmlns="urn:schemas-microsoft-com:xml-analysis"><RequestType>DBSCHEMA_CATALOGS</RequestType><Restrictions><RestrictionList/></Restrictions><Properties><PropertyList><Content>SchemaData</Content><DataSourceInfo>WIN2003R2-SSAS</DataSourceInfo></PropertyList></Properties></Discover></Body></Envelope>

	*/
		String xmlSoapText="	<Discover xmlns=\"urn:schemas-microsoft-com:xml-analysis\">"+
	"<RequestType>DBSCHEMA_CATALOGS</RequestType>"+
	"<Restrictions>"+
	"<RestrictionList/>"+
	"</Restrictions>"+
	"<Properties><PropertyList>"+
	"<Content>SchemaData</Content>"+
	"<DataSourceInfo>WIN2003R2-SSAS</DataSourceInfo>"+
	"</PropertyList>"+
	"</Properties>"+
	"</Discover>";

		String result;
		try {
			result = executeSOAP("http://192.168.0.71:80/olap/msmdpump.dll", xmlSoapText);
			System.out.println("AIPSoapUtil.main():result="+result);
		} catch (AIPException e) {
			e.printStackTrace();
		}
	}

}
