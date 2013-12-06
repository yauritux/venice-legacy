package com.gdn.venice.facade.callback;

/**
 * Session callback interface for implementing business logic pre/post events
 * 	- Classes that implement the SessionCallback interface must implement
 *    methods that perform validation or other business functions on the 
 *    data. If an operation fails then the implementer MUST return 
 *    Boolean.FALSE for the method that fails. This will create an EJBException
 *    and the whole container managed transaction will be rolled back.
 *    
 *  - SessionCallback implementation classes must be registered in module-config.xml
 *    (see the example VenCountrySessionEJBCallback and the configuration file)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public interface SessionCallback {
	
	/**
	 * Called by the framework prior to persisting the object
	 * @param businessObject is the specific business object that is being persisted
	 * @return true if the operation succeeds else false
	 */
	Boolean onPrePersist(Object businessObject);

	/**
	 * Called by the framework after persisting the object
	 * 
	 * @param businessObjectbusinessObject is the specific business object that is being persisted
	 * @return true if the operation succeeds else false
	 */
	Boolean onPostPersist(Object businessObject);

	/**
	 * Called by the framework prior to merging the object
	 * @param businessObject is the specific business object that is being merged
	 * @return true if the operation succeeds else false
	 */
	Boolean onPreMerge(Object businessObject);

	/**
	 * Called by the framework after merging the object
	 * 
	 * @param businessObjectbusinessObject is the specific business object that is being merged
	 * @return true if the operation succeeds else false
	 */
	Boolean onPostMerge(Object businessObject);

	/**
	 * Called by the framework prior to removing the object
	 * @param businessObject is the specific business object that is being removed
	 * @return true if the operation succeeds else false
	 */
	Boolean onPreRemove(Object businessObject);

	/**
	 * Called by the framework after removing the object
	 * 
	 * @param businessObjectbusinessObject is the specific business object that is being removed
	 * @return true if the operation succeeds else false
	 */
	Boolean onPostRemove(Object businessObject);
}
