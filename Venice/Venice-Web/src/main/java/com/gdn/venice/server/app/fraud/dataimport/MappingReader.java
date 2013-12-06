package com.gdn.venice.server.app.fraud.dataimport;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@SuppressWarnings("rawtypes")
public class MappingReader {
	private ArrayList<HashMap<String, Class>> mappingResult;
	
	
	public MappingReader () {
		this.mappingResult = new ArrayList<HashMap<String, Class>>();
	}

	public ArrayList<HashMap<String, Class>> readMappingOrder(String fileName) {
		try {
			Document doc = getDocument(fileName);
			
			ClassLoader classLoader = MappingReader.class.getClassLoader();
			
			NodeList listOfElement = doc.getElementsByTagName("data");
			for (int i = 0; i < listOfElement.getLength(); i++) {
				Element data = (Element) listOfElement.item(i);
				String dataName = data.getElementsByTagName("name").item(0).getTextContent();
				Class dataType = classLoader.loadClass(data.getElementsByTagName("type").item(0).getTextContent());
				
				HashMap<String, Class> mappingElement = new HashMap<String, Class>();
				mappingElement.put(dataName, dataType);
				this.mappingResult.add(mappingElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.mappingResult.clear();
		}
		
		return this.mappingResult;
	}

	private static Document getDocument(String name) {
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(false);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<HashMap<String, Class>> getMappingResult() {
		return mappingResult;
	}
}
