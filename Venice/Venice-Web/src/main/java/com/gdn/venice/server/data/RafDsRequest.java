package com.gdn.venice.server.data;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
/**
 * This class provides a data source request container for general use
 * along with conversion utility methods to convert responses to and from
 * SmartClient XML responses.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011 
 *
 */
public class RafDsRequest {
	
	/*
	 * This is the data source element name extracted from the request XML
	 */
	private String dataSource;
	
	/*
	 * This is the operation type element name extracted from the request XML
	 * (fetch, update, add, remove)
	 */
	private String operationType;
	
	/*
	 * This is the start row that is requested
	 */
	private int startRow;

	/*
	 * This is the end row that is requested
	 */
	private int endRow;
	
	/*
	 * This is the column that will be used for sorting request rows
	 */
	private String sortBy;
	
	
	/*
	 * This is the text match style element name extracted from the request XML
	 */
	private String textMatchStyle;
	
	/*
	 * This is the component id element name extracted from the request XML
	 */
	private String componentId;
	
	/*
	 * This is a list of the request data (parameters) passed into the request
	 */
	private List<HashMap<String, String>> data;
	
	/*
	 * This is a list of the old values of the data for the request
	 * (original values that have changed or at times values appended to the request)
	 */
	private List<HashMap<String, String>> oldValues;

	/*
	 * This is a JPQL advanced criteria object used by the request
	 */
	private JPQLAdvancedQueryCriteria criteria;

	/*
	 * Extra parameters passed to the request 
	 */
	private HashMap<String, String> params;

	/**
	 * Gets the data (parameters) for the request
	 * @return the data (parameters)
	 */
	public List<HashMap<String, String>> getData() {
		return data;
	}

	/**
	 * Sets the data (parameters) for the request
	 * @param data
	 */
	public void setData(List<HashMap<String, String>> data) {
		this.data = data;
	}

	/**
	 * Returns the current JPQL advanced criteria used by the request
	 * @return the advanced criteria
	 */
	public JPQLAdvancedQueryCriteria getCriteria() {
		return criteria;
	}

	/**
	 * Sets the JPQL query criteria to be used by the request
	 * @param criteria
	 */
	public void setCriteria(JPQLAdvancedQueryCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * Get the extra parameters that have been passed to the request
	 * @return the parameters
	 */
	public HashMap<String, String> getParams() {
		return params;
	}

	/**
	 * Set the extra parameters to pass to the request
	 * @param params
	 */
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	/**
	 * Get the data source name of the request
	 * @return the data source name
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * Set the data source name of the request
	 * @param dataSource
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Get the operation type of the request (fetch, update, add, remove)
	 * @return the operation type
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * Set the operation type of the request (fetch, update, add, remove)
	 * @param operationType
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * Get the start row of the request
	 * @return start row
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * Set the start row of the request
	 * @param startRow
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * Get the end row of the request
	 * @return the end row
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * Set the end row of the request
	 * @param endRow
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * Get the sort by column of the request
	 * @return the sort by column
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * Set the sort by column of the request
	 * @param sortBy
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * Get the text match style of the request
	 * @return the text match style
	 */
	public String getTextMatchStyle() {
		return textMatchStyle;
	}

	/**
	 * Set the text match style of the request
	 * @param textMatchStyle
	 */
	public void setTextMatchStyle(String textMatchStyle) {
		this.textMatchStyle = textMatchStyle;
	}

	/**
	 * Get the component id of the request
	 * @return the component id
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Set the component id of the request
	 * @param componentId
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * Get the list of iold values from the request
	 * @return the list of old values
	 */
	public List<HashMap<String, String>> getOldValues() {
		return oldValues;
	}

	/**
	 * Set the list of old values in the request
	 * @param oldValues
	 */
	public void setOldValues(List<HashMap<String, String>> oldValues) {
		this.oldValues = oldValues;
	}

	/**
	 * Convert XML format supported by SmartClient to a RafDsRequest
	 * @param xmlContent
	 * @return the new RafDsRequest object
	 * @throws Exception
	 */
	public static RafDsRequest convertXmltoRafDsRequest(String xmlContent)
			throws Exception {

		RafDsRequest rafDsRequest = new RafDsRequest();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(new InputSource(new StringReader(
				xmlContent)));

		NodeList nodeList = doc.getElementsByTagName("dataSource");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setDataSource(nodeList.item(0).getTextContent());
		}

		nodeList = doc.getElementsByTagName("operationType");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setOperationType(nodeList.item(0).getTextContent());
		}

		nodeList = doc.getElementsByTagName("startRow");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setStartRow(new Integer(nodeList.item(0)
					.getTextContent()).intValue());
		}

		nodeList = doc.getElementsByTagName("endRow");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setEndRow(new Integer(nodeList.item(0)
					.getTextContent()).intValue());
		}

		nodeList = doc.getElementsByTagName("sortBy");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setSortBy(nodeList.item(0).getTextContent());
		}

		nodeList = doc.getElementsByTagName("textMatchStyle");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setTextMatchStyle(nodeList.item(0).getTextContent());
		}

		nodeList = doc.getElementsByTagName("componentId");
		if (nodeList != null && nodeList.getLength() > 0) {
			rafDsRequest.setComponentId(nodeList.item(0).getTextContent());
		}

		if (rafDsRequest.getOperationType().equals("fetch")) {
			// get Data Node
			nodeList = doc.getElementsByTagName("data");
			if (nodeList != null && nodeList.getLength() > 0) {
				JPQLAdvancedQueryCriteria queryCriteria = null;
				// assumption: there is only gonna be one data node (take first
				// one here)
				Node dataNode = nodeList.item(0);
				// children of data node would be something like
				// isc_RafDataSource_0
				NodeList dataNodeChildren = dataNode.getChildNodes();
				for (int i = 0; i < dataNodeChildren.getLength(); i++) {
					if (dataNodeChildren.item(i) instanceof Element) {
						Element dataNodeChild = (Element) dataNodeChildren
								.item(i);
						if (dataNodeChild.getAttribute("constructor") != null
								&& dataNodeChild.getAttribute("constructor")
										.equals("AdvancedCriteria")) {
							// if children of data node (isc_RafDataSource_0)
							// has "constructor" attribute and its value is
							// "AdvancedCriteria",
							// it's a list of criteria. Get the list of criteria
							// here.
							NodeList criteriaNodes = dataNodeChild
									.getChildNodes();
							for (int j = 0; j < criteriaNodes.getLength(); j++) {
								if (criteriaNodes.item(j) instanceof Element
										&& criteriaNodes.item(j).getNodeName()
												.equals("operator")) {
									queryCriteria = new JPQLAdvancedQueryCriteria();
									queryCriteria
											.setBooleanOperator(criteriaNodes
													.item(j).getTextContent());
								}
								if (criteriaNodes.item(j) instanceof Element
										&& criteriaNodes.item(j).getNodeName()
												.equals("criteria")) {
									// get the list of "criteria" children
									// within the current "criteria" node
									NodeList internalCriteriaNodes = ((Element) criteriaNodes
											.item(j))
											.getElementsByTagName("criteria");
									if (internalCriteriaNodes != null
											&& internalCriteriaNodes
													.getLength() > 0) {
										// if there is criteria within criteria,
										// this is advanced criteria.
										// assumption: there's only one internal
										// criteria within criteria, take the
										// first one
										JPQLAdvancedQueryCriteria advancedQueryCriteria = new JPQLAdvancedQueryCriteria();

										NodeList booleanOperatorNodes = ((Element) criteriaNodes
												.item(j))
												.getElementsByTagName("operator");
										if (booleanOperatorNodes != null
												&& booleanOperatorNodes
														.getLength() > 0) {
											for (int k = 0; k < booleanOperatorNodes
													.getLength(); k++) {
												if (booleanOperatorNodes
														.item(k)
														.getParentNode()
														.equals(criteriaNodes
																.item(j))) {
													advancedQueryCriteria
															.setBooleanOperator(booleanOperatorNodes
																	.item(k)
																	.getTextContent());
												}
											}
										}

										Element internalCriteriaNode = (Element) internalCriteriaNodes
												.item(0);
										NodeList internalCriteriaElemChildNodes = internalCriteriaNode
												.getElementsByTagName("elem");
										for (int k = 0; k < internalCriteriaElemChildNodes
												.getLength(); k++) {
											if (internalCriteriaElemChildNodes
													.item(k) instanceof Element) {
												JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();

												NodeList fieldNameNodes = ((Element) internalCriteriaElemChildNodes
														.item(k))
														.getElementsByTagName("fieldName");
												String fieldClass = null;

												if (fieldNameNodes != null
														&& fieldNameNodes
																.getLength() > 0) {
													simpleCriteria
															.setFieldName(fieldNameNodes
																	.item(0)
																	.getTextContent());
													fieldClass = DataNameTokens
															.getDataNameToken()
															.getFieldClass(
																	fieldNameNodes
																			.item(0)
																			.getTextContent());
													simpleCriteria
															.setFieldClass(fieldClass);
												}

												NodeList operatorNodes = ((Element) internalCriteriaElemChildNodes
														.item(k))
														.getElementsByTagName("operator");
												if (operatorNodes != null
														&& operatorNodes
																.getLength() > 0) {
													if (fieldClass != null
															&& useEquals(fieldClass)) {
														simpleCriteria
																.setOperator("equals");
													} else {
														simpleCriteria
																.setOperator(operatorNodes
																		.item(0)
																		.getTextContent());
													}
												}

												NodeList valueNodes = ((Element) internalCriteriaElemChildNodes
														.item(k))
														.getElementsByTagName("value");
												if (valueNodes != null
														&& valueNodes
																.getLength() > 0) {
													if (simpleCriteria
															.getFieldClass() != null
															&& simpleCriteria
																	.getFieldClass()
																	.equals("java.sql.Timestamp")) {
														DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

														simpleCriteria
																.setValue(new Long(
																		formatter
																				.parse(valueNodes
																						.item(0)
																						.getTextContent())
																				.getTime())
																		.toString());
													} else {
														simpleCriteria
																.setValue(valueNodes
																		.item(0)
																		.getTextContent());
													}
												}

												advancedQueryCriteria
														.add(simpleCriteria);
											}
										}

										queryCriteria
												.add(advancedQueryCriteria);
									} else {
										// if there is no criteria within
										// criteria, this is simple criteria.
										JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();

										// This is the case of Simple Criteria
										// (no criteria within criteria)
										NodeList fieldNameNodes = ((Element) criteriaNodes
												.item(j))
												.getElementsByTagName("fieldName");
										String fieldClass = null;

										if (fieldNameNodes != null
												&& fieldNameNodes.getLength() > 0) {
											simpleCriteria
													.setFieldName(fieldNameNodes
															.item(0)
															.getTextContent());
											fieldClass = DataNameTokens
													.getDataNameToken()
													.getFieldClass(
															fieldNameNodes
																	.item(0)
																	.getTextContent());
											simpleCriteria
													.setFieldClass(fieldClass);
										}

										NodeList operatorNodes = ((Element) criteriaNodes
												.item(j))
												.getElementsByTagName("operator");
										if (operatorNodes != null
												&& operatorNodes.getLength() > 0) {
											if (fieldClass != null
													&& useEquals(fieldClass)) {
												simpleCriteria
														.setOperator("equals");
											} else {
												simpleCriteria
														.setOperator(operatorNodes
																.item(0)
																.getTextContent());
											}
										}

										NodeList valueNodes = ((Element) criteriaNodes
												.item(j))
												.getElementsByTagName("value");
										if (valueNodes != null
												&& valueNodes.getLength() > 0) {
											if (simpleCriteria.getFieldClass() != null
													&& simpleCriteria
															.getFieldClass()
															.equals("java.sql.Timestamp")) {
												DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

												simpleCriteria
														.setValue(new Long(
																formatter
																		.parse(valueNodes
																				.item(0)
																				.getTextContent())
																		.getTime())
																.toString());
											} else {
												simpleCriteria
														.setValue(valueNodes
																.item(0)
																.getTextContent());
											}
										}

										queryCriteria.add(simpleCriteria);
									}

								}
							}
						} else {
							NodeList criteriaNodes = dataNodeChild
									.getChildNodes();
							for (int j = 0; j < criteriaNodes.getLength(); j++) {
								if (criteriaNodes.item(j) instanceof Element) {
									// if there is no criteria within criteria,
									// this is simple criteria.
									JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();

									simpleCriteria.setFieldName(criteriaNodes
											.item(j).getNodeName());

									String fieldClass = DataNameTokens
											.getDataNameToken().getFieldClass(
													criteriaNodes.item(j)
															.getNodeName());
									simpleCriteria.setFieldClass(fieldClass);

									if (fieldClass != null
											&& useEquals(fieldClass)) {
										simpleCriteria.setOperator("equals");
									} else {
										simpleCriteria.setOperator("iContains");
									}

									if (simpleCriteria.getFieldClass() != null
											&& simpleCriteria
													.getFieldClass()
													.equals("java.sql.Timestamp")) {
										DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

										simpleCriteria
												.setValue(new Long(
														formatter
																.parse(criteriaNodes
																		.item(j)
																		.getTextContent())
																.getTime())
														.toString());
									} else {
										simpleCriteria.setValue(criteriaNodes
												.item(j).getTextContent());
									}
									if (queryCriteria == null) {
										queryCriteria = new JPQLAdvancedQueryCriteria();
										queryCriteria.setBooleanOperator("and");
									}
									queryCriteria.add(simpleCriteria);
								}
							}
						}
					}
				}

				rafDsRequest.setCriteria(queryCriteria);
			}
		} else if (rafDsRequest.getOperationType().equals("update")
				|| rafDsRequest.getOperationType().equals("remove")
				|| rafDsRequest.getOperationType().equals("add")) {
			// get Data Node
			nodeList = doc.getElementsByTagName("data");
			if (nodeList != null && nodeList.getLength() > 0) {
				List<HashMap<String, String>> dataList = null;
				Node dataNode = nodeList.item(0);
				// children of data node would be something like
				// isc_RafDataSource_0
				NodeList dataNodeChildren = dataNode.getChildNodes();
				for (int i = 0; i < dataNodeChildren.getLength(); i++) {
					if (dataNodeChildren.item(i) instanceof Element) {
						Element dataNodeChild = (Element) dataNodeChildren
								.item(i);
						NodeList dataFieldNodes = dataNodeChild.getChildNodes();
						HashMap<String, String> data = new HashMap<String, String>();

						for (int j = 0; j < dataFieldNodes.getLength(); j++) {
							if (dataFieldNodes.item(j) instanceof Element) {

								String dataKey = dataFieldNodes.item(j)
										.getNodeName();
								String dataValue = "";
								// if Field Class is null, that means it is
								// excluded from filter or data query
								// e.g. commenthistory "extra" column in
								// LogActivityReconRecord
								if (DataNameTokens.getDataNameToken()
										.getFieldClass(dataKey) != null) {
									if (DataNameTokens.getDataNameToken()
											.getFieldClass(dataKey)
											.equals("java.sql.Timestamp")) {
										DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

										dataValue = new Long(formatter.parse(
												dataFieldNodes.item(j)
														.getTextContent())
												.getTime()).toString();
									} else {
										dataValue = dataFieldNodes.item(j)
												.getTextContent();
									}

									data.put(dataKey, dataValue);
								}

							}
						}

						if (dataList == null) {
							dataList = new ArrayList<HashMap<String, String>>();
						}

						dataList.add(data);
					}
				}
				rafDsRequest.setData(dataList);
			}

			/*
			 * Map the old values too because sometimes SmartClient uses them
			 * for update and we need access to them this is something Henry did
			 * not know about - DF
			 */
			NodeList oldValueNodeList = doc.getElementsByTagName("oldValues");
			if (nodeList != null && nodeList.getLength() > 0) {
				HashMap<String, String> oldList = new HashMap<String, String>();
				
				/*
				 * This is ther list of HashMaps that will be set to the oldValues member
				 * this is a mess because Henry wrote it that way - DF
				 */
				List<HashMap<String, String>> oldValuesList = null;
				for (int i = 0; i < oldValueNodeList.getLength(); i++) {
					Node n = oldValueNodeList.item(i);
					NodeList oldValueList = n.getChildNodes();

					for (int j = 0; j < oldValueList.getLength(); j++) {
						Node entry = oldValueList.item(j);
						String key = entry.getNodeName();
						String value = "";
						/*
						 * if Field Class is null, that means it is excluded
						 * from filter or data query e.g. commenthistory "extra"
						 * column in LogActivityReconRecord
						 */
						if (DataNameTokens.getDataNameToken()
								.getFieldClass(key) != null) {
							if (DataNameTokens.getDataNameToken()
									.getFieldClass(key)
									.equals("java.sql.Timestamp") && (!entry.getTextContent().equals("")  || !entry.getTextContent().isEmpty()) ) {
								DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

								value = new Long(formatter.parse(
										entry.getTextContent()).getTime())
										.toString();
							} else {
								value = entry.getTextContent();
							}

							oldList.put(key, value);
						}
					}
					if(oldValuesList == null){
						oldValuesList = new ArrayList<HashMap<String, String>>();
					}
					oldValuesList.add(oldList); 
				}
				rafDsRequest.setOldValues(oldValuesList);
			}

		}

		return rafDsRequest;
	}

	/**
	 * This inner class provides a container for the SmartCLient criteria
	 * for the request
	 */
	public static class Criteria {
		/*
		 * This is the name of the field in SmartClient 
		 */
		private String fieldName;
		
		/*
		 * This is the operator to be used (equals, notEquals, greater, less, iContains etc.)
		 */
		private String operator;
		
		/*
		 * This is the string representation of the value to be used as criteria
		 */
		private String value;

		/**
		 * Returns the field name of the criteria field
		 * @return the field name
		 */
		public String getFieldName() {
			return fieldName;
		}

		/**
		 * Sets the field name of the criteria field
		 * @param fieldName
		 */
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		/**
		 * Gets the operator to be used in the criteria
		 * @return the operator
		 */
		public String getOperator() {
			return operator;
		}

		/**
		 * Sets the operator to be used in the criteria
		 * @param operator
		 */
		public void setOperator(String operator) {
			this.operator = operator;
		}

		/**
		 * Gets the value in string form
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value in string form
		 * @param value
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * This method determines which types should use queals as the
	 * operator instaead of iContains
	 * @param fieldClass
	 * @return the java type in string form
	 */
	private static boolean useEquals(String fieldClass) {
		if (fieldClass.equals("java.lang.Long")
				|| fieldClass.equals("java.lang.Integer")
				|| fieldClass.equals("java.lang.Double")
				|| fieldClass.equals("java.lang.Boolean")
				|| fieldClass.equals("java.math.BigDecimal")) {
			return true;
		}
		return false;
	}
}
