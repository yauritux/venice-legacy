package com.gdn.venice.server.data;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TestJavaDom {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		String content = "<request>" +
   "<data>" +
   "     <isc_RafDataSource_0/>" +
   " </data>" +
   " <dataSource>isc_RafDataSource_0</dataSource>" +
   " <operationType>fetch</operationType>" +
   " <startRow>0</startRow>" +
   " <endRow>75</endRow>" +
   " <sortBy>orderid</sortBy>" +
   " <textMatchStyle>substring</textMatchStyle>" +
   " <componentId>isc_ActivityReportReconciliationView_6_0</componentId>" +
   " <oldValues></oldValues>" +
"</request>";
		
		Document doc = builder.parse(new InputSource(new StringReader(content)));
		System.out.println(doc);
		System.out.println(doc.getElementsByTagName("data").getLength());
	}
}
