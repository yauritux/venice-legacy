package com.gdn.venice.hssf;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class MappingReader {

	/*
	 * Melakukan mapping order Properties
	 */
	private ArrayList<String> mapResult=new ArrayList<String>();
	
	/*
	 * Keeps track of the column being processed
	 */
	private Integer resultColumn = 1;
	
	public void readMapOrder(String fileName) throws Exception {
		
		try {
			Document doc = getDocument(fileName);
			Element root = doc.getDocumentElement();
			Node element = root.getFirstChild();
			resultColumn = 1;
			while (element != null) {
				if (element.getNodeName().toString().equalsIgnoreCase("mapping-element")) {
					this.mapResult.add("set" +element.getTextContent());	
				}
				element = element.getNextSibling();
				resultColumn++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Column mapping failed at column:" + resultColumn);
		}
	}
	private static Document getDocument(String name) throws Exception {
		try {
			
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setIgnoringComments(true);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setValidating(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				return builder.parse(new InputSource(name));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getDocument failed. Original exception was:" + e.getMessage());
		}
	}
	
	public ArrayList<String> getMapResult() {
		return mapResult;
	}

	public void setMapResult(ArrayList<String> mapResult) {
		
		this.mapResult = mapResult;
	}
	
	/**
	 * @return the result column being processed
	 */
	public Integer getResultColumn() {
		return resultColumn;
	}

}
