package com.gdn.venice.client.util;

/**
 * A permissions utility used to determine bitwise positional
 * read, write and execute permissions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2012
 * 
 */
public class PermissionsUtil {

	/**
	 * Returns teu if the permission contain read permission
	 * @param permissions the permissions to process
	 * @return true if the permission is set else false
	 */
	public static Boolean hasRead(Integer permissions) {
		if ((permissions.intValue() & 1) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Returns teu if the permission contain write permission
	 * @param permissions the permissions to process
	 * @return true if the permission is set else false
	 */
	public static Boolean hasWrite(Integer permissions) {
		if ((permissions.intValue() & 2) == 2) {
			return true;
		}
		return false;
	}

	/**
	 * Returns teu if the permission contain execute permission
	 * @param permissions the permissions to process
	 * @return true if the permission is set else false
	 */

	public static Boolean hasExecute(Integer permissions) {
		if ((permissions.intValue() & 4) == 4) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines of there are any permissions granted
	 * @param permissions the permissions object
	 * @return true if there is any permission else false
	 */
	public static Boolean hasAny(Integer permissions){
		return permissions > 0;
	}

	/**
	 * A test main
	 * @param args
	 */
	public static void main(String[] args) {		
		for(Integer test = 0; test <= 7; ++test){
			if (PermissionsUtil.hasRead(test)) {
				System.out.println("Has read");
			}else{
				System.out.println("No read");
			}

			if (PermissionsUtil.hasWrite(test)) {
				System.out.println("Has write");
			}else{
				System.out.println("No write");
			}

			if (PermissionsUtil.hasExecute(test)) {
				System.out.println("Has execute");
			}else{
				System.out.println("No execute");
			}
		}
	}
}
