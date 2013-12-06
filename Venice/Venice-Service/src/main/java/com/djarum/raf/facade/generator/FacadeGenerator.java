/**
 * 
 */
package com.djarum.raf.facade.generator;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * FacadeGenerator.java
 * 
 * This is used to generate all of the code for the session facade based on
 * templates and configuration... was easier than using Velocity
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FacadeGenerator {

	private String _localInterfaceTemplate;
	private String _remoteInterfaceTemplate;
	private String _beanTemplate;
	private String _localOutputDir;
	private String _remoteOutputDir;
	private String _beanOutputDir;

	private String _log4jOutputFile;
	private String _facadePackage;

	/**
	 * Generates a local EJB interface Java source file based on the input
	 * parameters
	 * 
	 * @param objectClassName
	 */
	public void generateLocalInterfaceFile(String objectClassName, String objectInstanceName) {
		try {
			FileInputStream fstream = new FileInputStream(this._localInterfaceTemplate);
			DataInputStream template = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(template));
			String strLine;
			/*
			 * Read File Line By Line and replace the $OBJECT_CLASS and
			 * $OBJECT_INSTANCE template placeholders and then write the output
			 * to the correctly named .java file
			 */
			FileWriter writer = new FileWriter(this._localOutputDir + "/"	+ objectClassName + "SessionEJBLocal.java", true);

			while ((strLine = br.readLine()) != null) {
				String strOutput = strLine.replaceAll("\\$OBJECT_CLASS", objectClassName);
				strOutput = strOutput.replaceAll("\\$OBJECT_INSTANCE", objectInstanceName);
				System.out.println(strOutput);
				writer.write(strOutput, 0, strOutput.length());
				writer.write("\n");
				writer.flush();
			}
			// Close the input and output streams
			template.close();
			writer.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Generates a remote EJB interface Java source file based on the input
	 * parameters
	 * 
	 * @param objectClassName
	 */
	public void generateRemoteInterfaceFile(String objectClassName, String objectInstanceName) {
		try {
			FileInputStream fstream = new FileInputStream(this._remoteInterfaceTemplate);
			DataInputStream template = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(template));
			String strLine;
			/*
			 * Read File Line By Line and replace the $OBJECT_CLASS and
			 * $OBJECT_INSTANCE template placeholders and then write the output
			 * to the correctly named .java file
			 */
			FileWriter writer = new FileWriter(this._remoteOutputDir + "/"	+ objectClassName + "SessionEJBRemote.java", true);

			while ((strLine = br.readLine()) != null) {
				String strOutput = strLine.replaceAll("\\$OBJECT_CLASS", objectClassName);
				strOutput = strOutput.replaceAll("\\$OBJECT_INSTANCE",  objectInstanceName);
				System.out.println(strOutput);
				writer.write(strOutput, 0, strOutput.length());
				writer.write("\n");
				writer.flush();
			}
			// Close the input and output streams
			template.close();
			writer.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Generates a EJB bean Java source file based on the input parameters
	 * 
	 * @param objectClassName
	 * @param objectInstanceName
	 * @param pkMethodName
	 */
	public void generateSessionBeanFile(String objectClassName, String objectInstanceName, String pkMethodName) {

		try {
			FileInputStream fstream = new FileInputStream(this._beanTemplate);
			DataInputStream template = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(template));
			String strLine;
			/*
			 * Read File Line By Line and replace the $OBJECT_CLASS and
			 * $OBJECT_INSTANCE template placeholders and then write the output
			 * to the correctly named .java file
			 */
			FileWriter writer = new FileWriter(this._beanOutputDir + "/" + objectClassName + "SessionEJBBean.java", true);

			while ((strLine = br.readLine()) != null) {
				String strOutput = strLine.replaceAll("\\$OBJECT_CLASS", objectClassName);
				strOutput = strOutput.replaceAll("\\$OBJECT_INSTANCE", objectInstanceName);
				strOutput = strOutput.replaceAll("\\$PK_METHOD", pkMethodName);
				System.out.println(strOutput);
				writer.write(strOutput, 0, strOutput.length());
				writer.write("\n");
				writer.flush();
			}
			// Close the input and output streams
			template.close();
			writer.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Generates the Log4j category entries for the log4j.properties file
	 * 
	 * @param objectClassName
	 */
	public void generateLog4jCategories(String objectClassName) {

		try {
			FileWriter writer = new FileWriter(this._log4jOutputFile, true);

			String log4jCategory = "log4j.category." + this._facadePackage + "." + objectClassName + "SessionEJBBean=DEBUG, STDOUT";
			System.out.println(log4jCategory);
			writer.write(log4jCategory, 0, log4jCategory.length());
			writer.write("\n");
			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the _localInterfaceTemplate
	 */
	public String getLocalInterfaceTemplate() {
		return _localInterfaceTemplate;
	}

	/**
	 * @param localInterfaceTemplate
	 *            the _localInterfaceTemplate to set
	 */
	public void setLocalInterfaceTemplate(String localInterfaceTemplate) {
		this._localInterfaceTemplate = localInterfaceTemplate;
	}

	/**
	 * @return the _remoteInterfaceTemplate
	 */
	public String getRemoteInterfaceTemplate() {
		return _remoteInterfaceTemplate;
	}

	/**
	 * @param remoteInterfaceTemplate
	 *            the _remoteInterfaceTemplate to set
	 */
	public void setRemoteInterfaceTemplate(String remoteInterfaceTemplate) {
		this._remoteInterfaceTemplate = remoteInterfaceTemplate;
	}

	/**
	 * @return the _beanTemplate
	 */
	public String getBeanTemplate() {
		return _beanTemplate;
	}

	/**
	 * @param beanTemplate
	 *            the _beanTemplate to set
	 */
	public void setBeanTemplate(String beanTemplate) {
		this._beanTemplate = beanTemplate;
	}

	/**
	 * @return the _localOutputDir
	 */
	public String getLocalOutputDir() {
		return _localOutputDir;
	}

	/**
	 * @param localOutputDir
	 *            the _localOutputDir to set
	 */
	public void setLocalOutputDir(String localOutputDir) {
		this._localOutputDir = localOutputDir;
	}

	/**
	 * @return the _remoteOutputDir
	 */
	public String getRemoteOutputDir() {
		return _remoteOutputDir;
	}

	/**
	 * @param remoteOutputDir
	 *            the _remoteOutputDir to set
	 */
	public void setRemoteOutputDir(String remoteOutputDir) {
		this._remoteOutputDir = remoteOutputDir;
	}

	/**
	 * @return the _beanOutputDir
	 */
	public String getBeanOutputDir() {
		return _beanOutputDir;
	}

	/**
	 * @param beanOutputDir
	 *            the _beanOutputDir to set
	 */
	public void setBeanOutputDir(String beanOutputDir) {
		this._beanOutputDir = beanOutputDir;
	}

	/**
	 * @return the _log4jOutputFile
	 */
	public String getLog4jOutputFile() {
		return _log4jOutputFile;
	}

	/**
	 * @param log4jOutputFile
	 *            the _log4jOutputFile to set
	 */
	public void setLog4jOutputFile(String log4jOutputFile) {
		this._log4jOutputFile = log4jOutputFile;
	}

	/**
	 * @return the _facadePackage
	 */
	public String getFacadePackage() {
		return _facadePackage;
	}

	/**
	 * @param facadePackage
	 *            the _facadePackage to set
	 */
	public void setFacadePackage(String facadePackage) {
		this._facadePackage = facadePackage;
	}

	/**
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {

		FacadeGenerator myGenerator = new FacadeGenerator();

		try {
			XMLConfiguration config = new XMLConfiguration("/home/software/venice_workspace/GDN-VENICE-Facade/ejbModule/com/djarum/raf/facade/generator/facadeConfig.xml");

			/*
			 * Get the index entry for the adapter configuration from the
			 * configuration file - there will be multiple adapter
			 * configurations
			 */
			myGenerator.setLocalInterfaceTemplate(config.getString("templates.localInterfaceTemplate"));
			System.out.println("Using local interface template:" + myGenerator.getLocalInterfaceTemplate());

			myGenerator.setRemoteInterfaceTemplate(config.getString("templates.remoteInterfaceTemplate"));
			System.out.println("Using remote interface template:" + myGenerator.getRemoteInterfaceTemplate());

			myGenerator.setBeanTemplate(config.getString("templates.beanTemplate"));
			System.out.println("Using bean template:" + myGenerator.getBeanTemplate());

			myGenerator.setLocalOutputDir(config.getString("output.localInterfaceDir"));
			System.out.println("Using local interface output directory:" + myGenerator.getLocalOutputDir());

			myGenerator.setRemoteOutputDir(config.getString("output.remoteInterfaceDir"));
			System.out.println("Using remote interface output directory:" + myGenerator.getRemoteOutputDir());

			myGenerator.setBeanOutputDir(config.getString("output.beanDir"));
			System.out.println("Using bean output directory:" + myGenerator.getBeanOutputDir());

			myGenerator.setLog4jOutputFile(config.getString("output.log4jCategoriesFile"));
			System.out.println("Using log4jCategoriesFile:" + myGenerator.getLog4jOutputFile());

			myGenerator.setFacadePackage(config.getString("output.facadePackage"));
			System.out.println("Using facadePackage:" + myGenerator.getFacadePackage());

			List entities = config.getList("facade.entity.[@name]");
			List pkMethods = config.getList("facade.entity.[@pkmethod]");
			Integer entityConfigIndex = new Integer(0);
			Iterator i = entities.iterator();
			while (i.hasNext()) {
				String objectClassName = (String) i.next();
				String objectInstanceName = objectClassName.substring(0, 1).toLowerCase() + objectClassName.substring(1, objectClassName.length());
				String pkMethodName = pkMethods.get(entityConfigIndex).toString();
				System.out.println("Processing facade generation for entity:" + objectClassName + "...");
				System.out.println("Substituting $OBJECT_CLASS for:" + objectClassName);
				System.out.println("Substituting $OBJECT_INSTANCE for:" + objectInstanceName);
				System.out.println("Substituting $PK_METHOD for:" + pkMethodName);

				System.out.println("Processing local interface...");
				myGenerator.generateLocalInterfaceFile(objectClassName, objectInstanceName);

				System.out.println("Processing remote interface...");
				myGenerator.generateRemoteInterfaceFile(objectClassName, objectInstanceName);

				System.out.println("Processing bean...");
				myGenerator.generateSessionBeanFile(objectClassName, objectInstanceName, pkMethodName);

				System.out.println("Processing Log4j Categories...");
				myGenerator.generateLog4jCategories(objectClassName);

				entityConfigIndex++;
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		System.out.println("FINISHED!");
		System.exit(0);
	}
}
