package com.djarum.raf.utilities;

/**
 * 
 * JPQL string escape utilities.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:henry@pwsindonesia.com">Henry Chandra</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 * 
 */
public class JPQLStringEscapeUtility {
	/**
	 * Escapes string data that is to be passed to a JPQL query statically. Note
	 * that you should only use an escape character if you are planning to bind
	 * the data to a LIKE predicate in the WHERE clause
	 * 
	 * @param stringData
	 *            the string data to escape
	 * @return the escaped string
	 */
	public static String escapeJPQLStringData(String stringData, String escape) {
		stringData = stringData.replaceAll("'", "''");
		stringData = stringData.replaceAll("%", escape + "%");
		stringData = stringData.replaceAll("_", escape + "_");
		return stringData;
	}
}
