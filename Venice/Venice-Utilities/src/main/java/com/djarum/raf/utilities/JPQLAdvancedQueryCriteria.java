package com.djarum.raf.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * JPQLAdvancedQueryCriteria.java
 * 
 * A container for both advanced and simple query criteria. This class acts as a
 * container for a list of criteria that may be either simple
 * (JPQLSimpleQueryCriteria) or advanced (JPQLAdvancedQueryCriteria).
 * 
 * The list of query criteria is vbuond to one boolean operator (for example
 * and, or). Note that the boolean operators must comply with the JPQL 2.0
 * language specification
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
public class JPQLAdvancedQueryCriteria implements JPQLQueryCriteria,
		Serializable {

	static final long serialVersionUID = 4382575176044912856L;

	private ArrayList<JPQLQueryCriteria> _criteriaList = null;

	private String _booleanOperator;

	/**
	 * Basic constructor
	 */
	public JPQLAdvancedQueryCriteria() {
		super();
		_criteriaList = new ArrayList<JPQLQueryCriteria>();
	}

	/**
	 * Constructor with field params
	 * 
	 * @param booleanOperator
	 *            the operator to use for the where clause join
	 */
	public JPQLAdvancedQueryCriteria(String booleanOperator) {
		super();
		this._booleanOperator = booleanOperator;
		_criteriaList = new ArrayList<JPQLQueryCriteria>(100);
	}

	/**
	 * Adds a new query criteria object (advanced or simple)
	 * 
	 * @param criteria
	 *            the new query criteria to add
	 * @return true if the operation succeeds else false
	 */
	public Boolean add(JPQLQueryCriteria criteria) {
		if (_criteriaList.add(criteria)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Removes a query criteria object from the list of criteria
	 * 
	 * @param criteria
	 *            the query criteria to remove (based on equals)
	 * @return true if the operation succeeds else false
	 */
	public Boolean remove(JPQLQueryCriteria criteria) {
		for (JPQLQueryCriteria next : _criteriaList) {
			if (next.equals(criteria)) {
				_criteriaList.remove(next);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Removes a query criteria object at the specified position
	 * 
	 * @param index
	 *            the position of query criteria to remove
	 */
	public void remove(int index) {
		_criteriaList.remove(index);
	}
	
	/**
	 * Returns a list iterator for a list of the query criteria objects
	 * 
	 * @return true if the operation succeeds else false
	 */
	public Iterator<JPQLQueryCriteria> getListIterator() {
		return _criteriaList.iterator();
	}

	/**
	 * Returns a list of JPQLSimpleQueryCriteria that have been added to the
	 * object **** UNTESTED****
	 * 
	 * @return an ArrayList of objects of class JPQLSimpleQueryCriteria
	 */
	public ArrayList<JPQLSimpleQueryCriteria> getSimpleCriteria() {

		ArrayList<JPQLSimpleQueryCriteria> simpleCriteriaList = new ArrayList<JPQLSimpleQueryCriteria>();
		Iterator<JPQLQueryCriteria> i = this._criteriaList.iterator();

		while (i.hasNext()) {
			JPQLQueryCriteria next = i.next();
			if (next instanceof JPQLSimpleQueryCriteria) {
				simpleCriteriaList.add((JPQLSimpleQueryCriteria) next);
			} else {
				JPQLAdvancedQueryCriteria advanced = (JPQLAdvancedQueryCriteria) next;
				simpleCriteriaList.addAll(advanced.getSimpleCriteria());
			}
		}
		return simpleCriteriaList;
	}

	/**
	 * The equals operation. Returns true if the operator and the criteria list
	 * are equal
	 * 
	 * @param obj
	 *            the object to compare
	 */
	public boolean equals(Object obj) {
		if (obj instanceof JPQLAdvancedQueryCriteria) {
			if (_booleanOperator
					.equals(((JPQLAdvancedQueryCriteria) obj)._booleanOperator)) {
				if (_criteriaList
						.equals(((JPQLAdvancedQueryCriteria) obj)._criteriaList)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the _booleanOperator
	 */
	public String getBooleanOperator() {
		return _booleanOperator;
	}

	/**
	 * @param booleanOperator
	 *            the booleanOperator to set
	 */
	public void setBooleanOperator(String booleanOperator) {
		this._booleanOperator = booleanOperator;
	}

}
