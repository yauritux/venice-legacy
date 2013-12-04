package com.djarum.raf.utilities;

import java.io.Serializable;

/**
 * JPQLSimpleQueryCriteria.java
 * 
 * A container for simple query criteria. The values for operator are as per the
 * values supported by SmartClient and as follows: o equals o notEquals o
 * greater o greaterOrEqual o less o lessOrEqual o iContains (which is the
 * equivalent of the "like" operator in SQL)
 * 
 * The supported types are as follows: o java.lang.Integer o java.lang.Long o
 * java.lang.Double o java.lang.Boolean o java.math.BigDecimal o java.sql.Date o
 * java.util.Date o java.sql.Time o java.sql.Timestamp
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 * 
 */
public class JPQLSimpleQueryCriteria implements JPQLQueryCriteria, Serializable {
	static final long serialVersionUID = -4803432953781191806L;

	String fieldName; // the name of the field (dotted form referenced to the
						// JPA model)
	String fieldClass; // the java class type for example java.lang.String
	String operator; // the operator (inline with SmartClient)
	String value; // the value of the field represented as a string
	
	boolean caseSensitive;

	/**
	 * The equals operation. Returns true if the operator fieldName, fieldClass
	 * operator and value are equal
	 * 
	 * @param obj
	 *            the object for the equals comparison
	 */
	public boolean equals(Object obj) {
		if (obj instanceof JPQLSimpleQueryCriteria) {
			if (fieldName.equals(((JPQLSimpleQueryCriteria) obj).fieldName)) {
				if (fieldClass
						.equals(((JPQLSimpleQueryCriteria) obj).fieldClass)) {
					if (operator
							.equals(((JPQLSimpleQueryCriteria) obj).operator)) {
						if (value.equals(((JPQLSimpleQueryCriteria) obj).value)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldClass
	 */
	public String getFieldClass() {
		return fieldClass;
	}

	/**
	 * @param fieldClass
	 *            the fieldClass to set
	 */
	public void setFieldClass(String fieldClass) {
		this.fieldClass = fieldClass;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
