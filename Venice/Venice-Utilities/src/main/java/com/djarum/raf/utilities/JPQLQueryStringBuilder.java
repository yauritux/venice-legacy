package com.djarum.raf.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;


//import com.gdn.venice.persistence.VenOrder;

/**
 * JPQLQueryStringBuilder.java
 * 
 * A parameterized JPQL query builder class that can take any entity bean class
 * (assuming the naming convention of (entityName)Id || (entityName)sId ||
 * (entityName)ParentId for primary keys Note that reflection is used to
 * ascertain the getters for the private fields hence naming standards are
 * essential. Later we can revise this and use the annotations API
 * 
 * Additionally either simple or advanced criteria can be passed in a criteria
 * object and used to build a JPQL statement as well as the array of bind
 * variables necessary for execution of the statement.
 * 
 * The design of this needs to be refactored as it was hacked together rather 
 * quickly to get something in place.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 * @param <T>
 */
public class JPQLQueryStringBuilder<T> {
	/*
	 * The parameterized business object based on type T
	 */
	private T businessObject;
	protected static Logger _log = null;

	/**
	 * QueryBuilder - basic constructor
	 * 
	 * @param businessObject is the object to use for building the query. The internal state of the object is used to build the query parameters.
	 */
	public JPQLQueryStringBuilder(T businessObject) {
		super();
		this.businessObject = businessObject;
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.util.JPQLQueryStringBuilder");

		String hubEntity = businessObject
				.getClass()
				.getName()
				.substring(
						businessObject.getClass().getPackage().getName()
								.length() + 1);

		from = " from " + hubEntity + " o ";
		aliasMap.put(hubEntity, "o");
	}

	private String stmt = "";
	private String select = "select o";
	private String from = null;
	private String where = "where ";

	// Used to store the aliases for reference
	private HashMap<String, String> aliasMap = new HashMap<String, String>();
	private char nextAlias = 'o';

	private Integer bindingIndex = 0;

	private Object[] bindingArray = new Object[20];

	/**
	 * @return the bindingArray
	 */
	public Object[] getBindingArray() {
		return bindingArray;
	}

	/**
	 * Builds a where clause QBE style using the business object T for binding
	 * 
	 * @param complexTypeBindings is a Map of the complex type bindings to be applied to the query. 
	 */
	private void buildWhereQBE(
			@SuppressWarnings("rawtypes") Map complexTypeBindings) {
		Method[] methods = this.businessObject.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			try {
				_log.debug("methods[i].getName():" + methods[i].getName());
				if (methods[i].getName().startsWith("get")) {
					Object retval = methods[i].invoke(this.businessObject);
					_log.debug("retval:" + retval);

					// Only build if the field is not null or a question mark
					// passed by SOAPUI or some other stupid client
					if (retval != "" && retval != null && !retval.equals("?")) {
						if (methods[i].getReturnType().toString()
								.contains("String")) { // It is a string so use
														// like
							if (where.contains("o")) {
								where = where + " and o.";
							} else {
								where = where + "o.";
							}
							where = where
									+ methods[i].getName().substring(3, 4)
											.toLowerCase()
									+ methods[i].getName().substring(4);
							where = where + " like ";
							where = where + "\'";
							where = where
									+ methods[i].invoke(this.businessObject);
							where = where + "\'";
						} else if (!methods[i].getReturnType().toString()
								.contains("java.lang.")) {
							// Assume it is a complex type or a date and bind to
							// the caller
							// then determine if the complex type has a PK set
							// or not
							Object queryColumn = methods[i]
									.invoke(this.businessObject);
							_log.debug("queryColumn.getClass():"
									+ queryColumn.getClass().getName());
							Method[] columnMethods = queryColumn.getClass()
									.getDeclaredMethods();
							Object columnValue = null;
							for (int j = 0; j < columnMethods.length; j++) {
								String queryColumnClassName = queryColumn
										.getClass()
										.getName()
										.substring(
												queryColumn.getClass()
														.getPackage().getName()
														.length() + 1);
								/*
								 * If queryColumnClass holds the signature of
								 * the getter method for the primary key of the
								 * column then invoke it to see if it returns
								 * null or some other stuff.
								 * 
								 * Note that we chop 3 characters from the front
								 * of the queryColumnClass to cater for the
								 * standard table domain prefix (LOG, VEN, FIN
								 * etc.)
								 * 
								 * Note that the 2nd test on the if is for the
								 * entities that are named plural due to
								 * reserved SQL words (the field names may not
								 * match the getter name).
								 * 
								 * Note that the 3rd test is for parent entities
								 * having the PK named the same as the child
								 * entity PK (normal case).. i.e. lop "Parent"
								 */

								_log.debug("columnMethods[j].getName().substring(0, columnMethods[j].getName().length():"
										+ (columnMethods[j].getName()
												.substring(0, columnMethods[j]
														.getName().length())));
								_log.debug("Column class name with get prepended and Id appended:"
										+ "get"
										+ queryColumnClassName.substring(3)
										+ "Id");

								if (columnMethods[j].getName()
										.equalsIgnoreCase(
												"get"
														+ queryColumnClassName
																.substring(3)
														+ "Id")
										|| columnMethods[j]
												.getName()
												.equalsIgnoreCase(
														"get"
																+ queryColumnClassName
																		.substring(
																				3,
																				queryColumnClassName
																						.length() - 1)
																+ "Id")
										|| columnMethods[j]
												.getName()
												.equalsIgnoreCase(
														"get"
																+ queryColumnClassName
																		.substring(
																				3)
																		.replaceAll(
																				"Parent",
																				"")
																+ "Id")) {
									// Get the column value
									columnValue = columnMethods[j]
											.invoke(queryColumn);
									// set it to the return value
									j = columnMethods.length;
									// terminate the loop early for at least
									// some efficiency
								}
							}

							if (methods[i].getReturnType().toString()
									.contains("Date")) // pick up the dates here
								columnValue = methods[i]
										.invoke(this.businessObject);

							if (columnValue != null && columnValue != ""
									&& !columnValue.equals("?")) {
								// if the FK of the column is not null or a ?
								// then build this part of the clause
								if (where.contains("o")) {
									where = where + " and o.";
								} else {
									where = where + "o.";
								}
								where = where
										+ methods[i].getName().substring(3, 4)
												.toLowerCase()
										+ methods[i].getName().substring(4);
								where = where + " = ";
								where = where
										+ ":"
										+ complexTypeBindings.get(methods[i]
												.getName().substring(3, 4)
												.toLowerCase()
												+ methods[i].getName()
														.substring(4));
							}
						} else {
							// Assume it must be any other wrapper type so apply
							// equals
							if (where.contains("o")) {
								where = where + " and o.";
							} else {
								where = where + "o.";
							}
							where = where
									+ methods[i].getName().substring(3, 4)
											.toLowerCase()
									+ methods[i].getName().substring(4);
							where = where + " = ";
							where = where
									+ methods[i].invoke(this.businessObject);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns a JPQL query string
	 * 
	 * @param complexTypeBindings
	 *            - a Map with the caller side bindings for complex types and
	 *            java.util.Date specified as name/value pairs
	 * @param criteria
	 *            an advanced JPQL criteria object
	 * @return - a String containing the JPQL query to be executed in the
	 *         binding context of the caller.
	 */
	public String buildQueryString(
			@SuppressWarnings("rawtypes") Map complexTypeBindings,
			JPQLQueryCriteria criteria) {
		_log.debug("buildQueryString()");

		/*
		 * If the criteria object is provided then ignore the QBE fields and use
		 * the criteria to build the query string.
		 */
		if (criteria != null) {
			this.buildWhereCriteria(criteria);
		} else {
			this.buildWhereQBE(complexTypeBindings);
		}

		stmt = select + from;
		// If the where clause is empty don't append it
		if (!where.equals("where "))
			stmt = stmt + where;

		_log.debug("Statement:" + stmt);
		return stmt;

	}

	/**
	 * Builds a where clause using the criteria object
	 * 
	 * @param criteria
	 */
	private void buildWhereCriteria(JPQLQueryCriteria criteria) {
		bindingIndex = 0;
		where = " where " + this.buildFromWhereClause(criteria);
	}

	/**
	 * Returns a string based on the JPQL query criteria object and adds the
	 * various entities that are referenced to the from part of the statement
	 * 
	 * @param criteria the query criteria to use for building the where clause
	 * @return the query string
	 */
	private String buildFromWhereClause(JPQLQueryCriteria criteria) {
		_log.debug("buildFromWhereClause()");
		String localWhere = "";
		if (criteria instanceof JPQLAdvancedQueryCriteria) {
			_log.debug("buildFromWhereClause():processing JPQLAdvancedQueryCriteria");
			Iterator<JPQLQueryCriteria> criteriaIterator = ((JPQLAdvancedQueryCriteria) criteria)
					.getListIterator();
//			localWhere = localWhere + "(";
			while (criteriaIterator.hasNext()) {
				JPQLQueryCriteria next = criteriaIterator.next();
				localWhere = localWhere + this.buildFromWhereClause(next);
				if (criteriaIterator.hasNext()) {
					localWhere = localWhere
							+ " "
							+ ((JPQLAdvancedQueryCriteria) criteria)
									.getBooleanOperator() + " ";
					_log.debug("localWhere:" + localWhere);
				}
			}
//			localWhere = localWhere + ")";

			_log.debug("buildFromWhereClause().localWhere built:" + localWhere);
			return localWhere;
		} else if (criteria instanceof JPQLSimpleQueryCriteria) {
			_log.debug("buildFromWhereClause():processing JPQLSimpleQueryCriteria");

			String fieldName = ((JPQLSimpleQueryCriteria) criteria)
					.getFieldName();
			String fieldClass = ((JPQLSimpleQueryCriteria) criteria)
					.getFieldClass();
			String operator = null;
			if (((JPQLSimpleQueryCriteria) criteria).getOperator().equals(
					"equals")) {
				operator = "=";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("notEquals")) {
				operator = "<>";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("greater")) {
				operator = ">";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("greaterOrEqual")) {
				operator = ">=";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("less")) {
				operator = "<";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("lessOrEqual")) {
				operator = "<=";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equalsIgnoreCase("IN")) {
				operator = "IN";
			} else if (((JPQLSimpleQueryCriteria) criteria).getOperator()
					.equals("iContains")) {
				operator = "like";
			}else{
				String errMsg = "Instantiation of:" + fieldName
				+ " class:" + fieldClass + " unsupported operator - only equals, notEquals, greater, greaterOrEqual, less, lessOrEqual, IN and iContains are supported";
				_log.error(errMsg);
				throw new EJBException(errMsg);

			}
			_log.debug("buildFromWhereClause().operator:" + operator);
			
			String value = ((JPQLSimpleQueryCriteria) criteria).getValue();

			java.util.StringTokenizer st = new java.util.StringTokenizer(
					fieldName, ".");

			// Get the entity name from the criteria and add it to the from list
			String next = st.nextToken();
			if (!from.toLowerCase().contains(next.toLowerCase())) {
				from = from + "," + next;
				System.out.println("from:" + from);
				_log.debug("buildFromWhereClause()... adding alias:" + next);
				aliasMap.put(next, "" + ++nextAlias);
			}

			if (localWhere.isEmpty()) {
				if (fieldClass.equals("java.lang.String")) {
					_log.debug("field class is: "+fieldClass);
					if(operator.equalsIgnoreCase("IN")){
						if(((JPQLSimpleQueryCriteria) criteria).isCaseSensitive())
							localWhere = ("("+ fieldName + ") " + operator + " (?" + (1 + bindingIndex) + ")");
						else
							localWhere = (" LOWER( "+ fieldName + " ) " + operator + " (LOWER(?" + (1 + bindingIndex) + ") ) ");
					}
					else
						localWhere = (" LOWER( "+fieldName + " ) " + operator + " LOWER( ?" + (1 + bindingIndex) + " ) ");
				}else{
					_log.debug("field class is: "+fieldClass);
					if(operator.equalsIgnoreCase("IN"))
						localWhere = (fieldName + " " + operator + " (?" + (1 + bindingIndex) + ")");
					else
						localWhere = (fieldName + " " + operator + " ?" + (1 + bindingIndex));
				}
				_log.debug("buildFromWhereClause().localWhere:" + localWhere + " ... replacing entity names with aliases");
				
				// Replace all of the entity names with aliases
				Iterator<String> aliasListIterator = aliasMap.keySet()
						.iterator();
				while (aliasListIterator.hasNext()) {
					String nextEntity = (String) aliasListIterator.next();
					String alias = aliasMap.get(nextEntity);
					localWhere = localWhere.replaceAll(nextEntity, alias);

				}
				_log.debug("buildFromWhereClause().localWhere:" + localWhere + " ... entity names replaced with aliases");

				Object bindingObject = null;
				try {
					_log.debug("buildFromWhereClause()... checking validity of field class using Class.forName");
					// Just call this to check that the class is valid
					@SuppressWarnings("rawtypes")
					Class c = Class.forName(fieldClass);
					
					/*
					 * If value contains a hyphen then assume it is the string representation of a date
					 * type and convert it to long accordingly (same for both Date types as well as
					 * sql Time and Timestamp)
					 */
					if (fieldClass.contains("Date")
							|| fieldClass.contains("Time")) {
						if (value.contains("-")) {
							DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
							try {
								value = new Long(formatter.parse(value)
										.getTime()).toString();
							} catch (ParseException e) {
								_log.error("Date parsing exception:"
										+ e.getMessage());
								e.printStackTrace();
							}
						}
					}

					if (fieldClass.equals("java.lang.String")) {
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							ValueCollectionConverter<String> vcv = new ValueCollectionConverter<String>();
							bindingObject = vcv.valueToCollection(value, String.class);
						} else {
							bindingObject = new String(value);
							// Concatenate the percentage sign for like queries
							// using string
							if (operator.equalsIgnoreCase("like")) {
								bindingObject ="%".concat((String) bindingObject);
								bindingObject = ((String) bindingObject).concat("%");
							}
						}
					} else if (fieldClass.equals("java.lang.Integer")) {
						_log.debug("buildFromWhereClause()... processing java.lang.Integer:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							ValueCollectionConverter<Integer> vcv = new ValueCollectionConverter<Integer>();
							bindingObject = vcv.valueToCollection(value, Integer.class);
						}else{
							bindingObject = new Integer(value);
						}
					} else if (fieldClass.equals("java.lang.Long")) {
						_log.debug("buildFromWhereClause()... processing java.lang.Long:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							ValueCollectionConverter<Long> vcv = new ValueCollectionConverter<Long>();
							bindingObject = vcv.valueToCollection(value, Long.class);
						}else{
							bindingObject = new Long(value);
						}
					} else if (fieldClass.equals("java.lang.Double")) {
						_log.debug("buildFromWhereClause()... processing java.lang.Double:");
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							ValueCollectionConverter<Double> vcv = new ValueCollectionConverter<Double>();
							bindingObject = vcv.valueToCollection(value, Double.class);
						}else{
							bindingObject = new Double(value);
						}
					} else if (fieldClass.equals("java.lang.Boolean")) {
						_log.debug("buildFromWhereClause()... processing java.lang.Boolean:" + value);
						/*
						 * Note that it is pointless to support IN and a collection for Boolean
						 */
						bindingObject = new Boolean(value);
					} else if (fieldClass.equals("java.math.BigDecimal")) {
						_log.debug("buildFromWhereClause()... processing java.math.BigDecimal:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							ValueCollectionConverter<BigDecimal> vcv = new ValueCollectionConverter<BigDecimal>();
							bindingObject = vcv.valueToCollection(value, BigDecimal.class);
						}else{
							bindingObject = new java.math.BigDecimal(value);
						}
					} else if (fieldClass.equals("java.util.Date")) {
						_log.debug("buildFromWhereClause()... processing java.util.Date:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							bindingObject = this.valueToUtilDateArray(value);
						}else{
							bindingObject = new java.util.Date(new Long(value));
						}
					} else if (fieldClass.equals("java.sql.Date")) {
						_log.debug("buildFromWhereClause()... processing java.sql.Date:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							bindingObject = this.valueToSqlDateArray(value);
						}else{
							bindingObject = new java.sql.Date(new Long(value));
						}
					} else if (fieldClass.equals("java.sql.Time")) {
						_log.debug("buildFromWhereClause()... processing java.sql.Time:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							bindingObject = this.valueToSqlTimeArray(value);
						}else{
							bindingObject = new java.sql.Time(new Long(value));
						}
					} else if (fieldClass.equals("java.sql.Timestamp")) {
						_log.debug("buildFromWhereClause()... processing java.sql.Timestamp:" + value);
						if(((JPQLSimpleQueryCriteria) criteria).getOperator()
								.equalsIgnoreCase("IN")){
							bindingObject = this.valueToSqlTimestampArray(value);
						}else{
							bindingObject = new java.sql.Timestamp(new Long(value));
						}
					} else {
						String errMsg = "Instantiation of:" + fieldName
								+ " class:" + fieldClass + " unsupported type";
						_log.error(errMsg);
						throw new EJBException(errMsg);
					}
				} catch (ClassNotFoundException e) {
					String errMsg = "Instantiation of:" + fieldName + " class:"
							+ fieldClass
							+ " resulted in ClassNotFoundException:";
					_log.error(errMsg + e.getMessage());
					e.printStackTrace();
					throw new EJBException(errMsg);
				}
				this.bindingArray[this.bindingIndex++] = bindingObject;
			}
		}
		_log.debug("buildFromWhereClause()... returning localWhere:" + localWhere);
		return localWhere;
	}

	/**
	 * Generic inner class to provide a converter for comma delimited values
	 * of any supported type (types must have a constructor with a single
	 * java.lang.String parameter) to a List object to be used for
	 * binding the values of an IN clause in JPQL.
	 * 
	 * Note that since the framework passes Date/Time types as Long values
	 * we cannot use this mechanism and need type-specific methods 
	 * 
	 * <p>
	 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
	 * <p>
	 * <b>version:</b> 1.0
	 * <p>
	 * <b>since:</b> 2011
	 *
	 * @param <Z> is the parameterized type
	 */
	private class ValueCollectionConverter<Z> {
		
		/**
		 * Returns a List of class clazz containing the comma delimited values 
		 * @param value
		 * @param clazz
		 * @return the list of values
		 */
		@SuppressWarnings("unchecked")
		private List<Z> valueToCollection(String value, Class<Z> clazz) {
			ArrayList<Z> retVal = new ArrayList<Z>();
			String[] valueArray = value.split(",");
			for (String token : valueArray) {
				@SuppressWarnings("rawtypes")
				Constructor newValueConstructor = null;
				try {
					newValueConstructor = clazz.getConstructor(
							new Class[] { String.class });
				} catch (Exception e) {
					_log.error("An Exception occured when instantiating the String constructor for a type:"
							+ e.getMessage());
					e.printStackTrace();
				}
				try {
					retVal.add((Z) newValueConstructor
							.newInstance(new Object[] { token }));
				} catch (Exception e) {
					_log.error("An Exception occured when instantiating an object within the collection for binding:"
							+ e.getMessage());
					e.printStackTrace();
				}
			}
			return retVal;
		}
	}

	/**
	 * Convert a comma delimited string of java.util.Date long values into an array
	 * @param value the comma delimited list of java.util.Date values in long millisecond representation as a string
	 * @return the new array
	 */
	private List<java.util.Date> valueToUtilDateArray(String value){
			ArrayList<java.util.Date> retVal = new ArrayList<java.util.Date>();
			String[] valueArray = value.split(",");
			for(String token: valueArray){
				retVal.add(new java.util.Date(new Long(token)));
			}
			return retVal;
		}

	/**
	 * Convert a comma delimited string of java.sql.Date long values into an array
	 * @param value the comma delimited list of java.sql.Date values in long millisecond representation as a string
	 * @return the new array
	 */
	private List<java.sql.Date> valueToSqlDateArray(String value){
		ArrayList<java.sql.Date> retVal = new ArrayList<java.sql.Date>();
		String[] valueArray = value.split(",");
		for(String token: valueArray){
			retVal.add(new java.sql.Date(new Long(token)));
		}
		return retVal;
	}


	/**
	 * Convert a comma delimited string of java.sql.Time long values into an array
	 * @param value the comma delimited list of java.sql.Time values in long millisecond representation as a string
	 * @return the new array
	 */
	private List<java.sql.Time> valueToSqlTimeArray(String value){
		ArrayList<java.sql.Time> retVal = new ArrayList<java.sql.Time>();
		String[] valueArray = value.split(",");
		for(String token: valueArray){
			retVal.add(new java.sql.Time(new Long(token)));
		}
		return retVal;
	}

	/**
	 * Convert a comma delimited string of java.sql.Timestamp long values into an array
	 * @param value the comma delimited list of java.sql.Timestamp values in long millisecond representation as a string
	 * @return the new array
	 */
	private List<java.sql.Timestamp> valueToSqlTimestampArray(String value){
		ArrayList<java.sql.Timestamp> retVal = new ArrayList<java.sql.Timestamp>();
		String[] valueArray = value.split(",");
		for(String token: valueArray){
			retVal.add(new java.sql.Timestamp(new Long(token)));
		}
		return retVal;
	}

//	 /**
//	 * For the sake of testing the scenarios for this component
//	 *
//	 * @param args
//	 */
//	 public static void main(String[] args) {
//	
//		// VenOrder order = new VenOrder();
//		// VenOrderStatus orderStatus = new VenOrderStatus();
//		// orderStatus.setOrderStatusId(new Long(1));
//		// order.setVenOrderStatus(orderStatus);
//		 
//		 FrdCustomerWhitelistBlacklist customerBlacklist = new FrdCustomerWhitelistBlacklist();
//		 customerBlacklist.setCustomerFullName("Ng Jenny");
//		 customerBlacklist.setTimestamp(new Timestamp(System.currentTimeMillis()));
//		 
//		JPQLQueryStringBuilder<FrdCustomerWhitelistBlacklist> q = new JPQLQueryStringBuilder<FrdCustomerWhitelistBlacklist>(customerBlacklist);
//
//		JPQLSimpleQueryCriteria criteria1 = new JPQLSimpleQueryCriteria();
//		criteria1.setFieldName("FrdCustomerWhitelistBlacklist.timestamp");
//		criteria1.setFieldClass("java.sql.Timestamp");
//		criteria1.setOperator("equals");
//		criteria1.setValue("2013-01-01T00:00:00");
//
////		JPQLSimpleQueryCriteria criteria2 = new JPQLSimpleQueryCriteria();
////		criteria2.setFieldName("VenOrder.fulfillmentStatus");
////		criteria2.setFieldClass("java.lang.Long");
////		criteria2.setValue("2");
////		criteria2.setOperator("notEquals");
//
//		JPQLAdvancedQueryCriteria advancedQueryCriteria = new JPQLAdvancedQueryCriteria();
//		advancedQueryCriteria.add(criteria1);
////		advancedQueryCriteria.add(criteria2);
////		advancedQueryCriteria.setBooleanOperator("and");
//
//		System.out.println(q.buildQueryString(null, advancedQueryCriteria));
//	
////		try {
////			@SuppressWarnings("rawtypes")
////			Locator locator = new Locator<Object>();
////			@SuppressWarnings("unchecked")
////			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
////
////			List<VenOrder> orderList = orderHome.queryByRange(q.buildQueryString(null, advancedQueryCriteria), 0, 0);
////
////			Iterator<VenOrder> orderListIterator = orderList.iterator();
////			while (orderListIterator.hasNext()) {
////				VenOrder nextOrder = orderListIterator.next();
////				System.out.println("Order wcsOrderId:"	+ nextOrder.getWcsOrderId());
////			}
////
////		} catch (Exception e) {
////			e.printStackTrace();
////		}		
//	 }
}
