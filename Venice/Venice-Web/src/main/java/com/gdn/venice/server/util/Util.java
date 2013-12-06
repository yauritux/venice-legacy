package com.gdn.venice.server.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Util {
	protected static final String VERSION_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/version.properties";
	
	public static String extractRequestBody(HttpServletRequest request) {
		StringBuilder stringBuilder = new StringBuilder();  
		BufferedReader bufferedReader = null;  
		try {  
			InputStream inputStream = request.getInputStream();  
			if (inputStream != null) {  
				bufferedReader = new BufferedReader(new InputStreamReader(  
						inputStream));  
				char[] charBuffer = new char[128];  
				int bytesRead = -1;  
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {  
					stringBuilder.append(charBuffer, 0, bytesRead);  
				}  
			} else {  
				stringBuilder.append("");  
			}  
		} catch (IOException ex) {  
			ex.printStackTrace(); 
		} finally {  
			if (bufferedReader != null) {  
				try {  
					bufferedReader.close();  
				} catch (IOException ex) {  
					ex.printStackTrace();  
				}  
			}  
		}  
		return stringBuilder.toString();
	}

	public static String readXMLData(String urlLocation) {
		String fileContent = "";
		
		try {
			URL url = new URL(urlLocation);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(
			                        new InputStreamReader(
			                        connection.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) 
				fileContent += inputLine;

			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return fileContent;
	}
	
	public static String getServerURL(String url) {
		return url.substring(0, url.lastIndexOf("/"));
	}
	
	public static String composeFetchData(String status, String startRow, String endRow, String totalRow, String data) {
		return "<response>\n" +
		    "<status>" + status + "</status>\n" +
		    "<startRow>" + startRow + "</startRow>\n" +
			"<endRow>" + endRow + "</endRow>\n" +
			"<totalRow>" + totalRow + "</totalRow>\n" +
			"<data>\n" + data + "</data>\n" +
			"</response>";
	}
	
	public static String getUserName(HttpServletRequest request) {
		//TODO: Change this in Geronimo Servlet, hardcoded here for development purpose in GWT
		//Note: Port 8889 is to be used as development, DO NOT USE IT FOR PRODUCTION
		if (request.getServerPort()==8889) {
//			return "emilia.s.herlambang";    
//			return "widy";
//			return "arif.p.gunawan";
//			return "alvina.e.p.subagyo";
//			return "lisa";    
			return "roland";
		} else {
			return request.getUserPrincipal().getName();
		}
	}
	
	public static Properties getVersionProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(VERSION_PROPERTIES_FILE));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}
	
	public static HashMap<String, String> formHashMapfromXML(String xml) {
		HashMap<String, String> retVal = new HashMap<String, String>();
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder
					.parse(new InputSource(new StringReader(xml)));
			NodeList nodeList = doc.getElementsByTagName("data");
			if (nodeList != null && nodeList.getLength() > 0) {
				Node dataNode = nodeList.item(0);
				NodeList childNodes = dataNode.getChildNodes();
				
				for (int i=0;i<childNodes.getLength();i++) {
					if (childNodes.item(i) instanceof Element) {
						Element dataNodeChild = (Element) childNodes.item(i);
						retVal.put(dataNodeChild.getNodeName(), StringEscapeUtils.unescapeXml(dataNodeChild.getTextContent()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static String formXMLfromHashMap(HashMap<String, String> map) {
		String xml = "<data>\n";
		
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			xml += "<" + key + ">" + escapeXML(map.get(key)) + "</" + key + ">\n";
		}
		
		xml += "</data>";
		
		return xml;
	}
	
	private static String escapeXML(String xml) {
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("\"", "&quot;");
		xml = xml.replaceAll("'", "&apos;");
		xml = xml.replaceAll("&", "&amp;");
		
		return xml;
	}
	
	public static Object isNull(Object object, Object replacement) {
		return object == null ? replacement : object;
	}
	
	/**
	 * Extracts the exception text from a typical server side EJBException
	 * by removing the prelude and replacing the tabs and carriage returns.
	 * Also the colons are removed so that the string is not split later
	 * by the code around the RPCManager in the client.
	 * @param exceptionText
	 * @return the presentable error message
	 */
	public static String extractMessageFromEJBExceptionText(String exceptionText){
		String retVal = exceptionText.substring(97);
		return retVal.replace('\n', ' ').replace('\t', ' ').replace(':', '-');
	}

}
