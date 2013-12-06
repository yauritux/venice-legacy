package com.gdn.venice.bpmenablement;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import teamworks.samples.client.WebAPIFactory;
import teamworks.samples.client.repository.ClientRepository;
import teamworks.samples.client.repository.ClientRepositorySynchronizer;
import teamworks.samples.client.repository.SimpleClientRepository;
import teamworks.samples.client.repository.SynchronizeCompletedEvent;
import teamworks.samples.client.repository.SynchronizeCompletedListener;

import com.lombardisoftware.webapi.SavedSearch;
import com.lombardisoftware.webapi.UserConfiguration;
import com.lombardisoftware.webapi.Variable;
import com.lombardisoftware.webapi.WebAPI;

public class BPMAdapter {
	public static final String WEBAPI_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/lib/WebAPIFactory.properties";
	public static final String LDAP_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/lib/LdapConfiguration.properties";
	
	private WebAPI webAPI = null;
	private ClientRepository clientRepository=null;
	private UserConfiguration cachedUserConfiguration=null;
	private static HashMap<String, BPMAdapter> bpmAdapters = new HashMap<String, BPMAdapter>();
	
	private int numberOfConnectionTries = 3;
	
	private boolean isSynchronizerCompleted = true;
	
	private String userName;
	private String password;
	
	
	public static BPMAdapter getBPMAdapter(String userName, String password) {
		BPMAdapter bpmAdapter = bpmAdapters.get(userName);
		if (bpmAdapter == null) {
			bpmAdapter = new BPMAdapter(userName, password);
			bpmAdapters.put(userName, bpmAdapter);
		} else {
			try {
				if (!bpmAdapter.getWebAPI().testConnection()) {
					bpmAdapter = new BPMAdapter(userName, password);
					bpmAdapters.put(userName, bpmAdapter);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return bpmAdapter;
	}

	public BPMAdapter(String userName, String password) {
		this.userName = userName;
		this.password = password;
		clientRepository = new SimpleClientRepository();
	}

	private Properties getConnectionProperties() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(WEBAPI_PROPERTIES_FILE));
		properties.put("javax.xml.rpc.security.auth.username", userName);
		properties.put("javax.xml.rpc.security.auth.password", password);
		return properties;		
	}
	
	private static Properties getLdapProperties() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(LDAP_PROPERTIES_FILE));
		return properties;		
	}

	public WebAPI getWebAPI() throws Exception {
		if (webAPI == null) {            
			Properties connectionProperties;
			int i = 0;
			while ((connectionProperties = getConnectionProperties()) != null) {
				try {
					// Create a factory from the connection properties
					WebAPIFactory factory = WebAPIFactory.newInstance(connectionProperties);

					// Create a new instance
					WebAPI webAPI = factory.newWebAPI();

					// Test the connection 
					webAPI.testConnection();

					// If the connection is valid, then save the instance and exit the loop
					this.webAPI = webAPI;
					break;
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				i++;
				if (i>=numberOfConnectionTries) {
					break;
				}
			}
		}        
		return webAPI;
	}
	
	private UserConfiguration getUserConfiguration() {
		if (cachedUserConfiguration == null) {
            try {
                WebAPI webAPI = getWebAPI();
                if (webAPI != null) {
                    cachedUserConfiguration = webAPI.getUserConfiguration();
                }
            }
            catch(Exception ex) {
            	ex.printStackTrace();
            }
        }
        return cachedUserConfiguration;
    }
	
	public ClientRepository getClientRepository() {
        return clientRepository;
    }

	public ClientRepositorySynchronizer getSynchronizer() {
		ClientRepositorySynchronizer synchronizer = null;
		
		UserConfiguration userConfiguration = getUserConfiguration();
		if (userConfiguration != null) {
			try {
				// TODO: Allow user to configure which saved searches they would like 
				// to synchronize from their list of saved searches.
				//
				// For now, include all saved searches except for the History
				// saved search.
				Set<Long> savedSearchIds = new TreeSet<Long>();
				for(SavedSearch savedSearch : userConfiguration.getSavedSearches()) {
					if (!savedSearch.getName().equals("History")) {
						savedSearchIds.add(savedSearch.getId());
					}
				}

				synchronizer = new ClientRepositorySynchronizer(getClientRepository(), getWebAPI(), savedSearchIds);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return synchronizer;
	}
	
	public void synchronize() {
		
		ClientRepositorySynchronizer synchronizer = getSynchronizer();
		
		synchronizer.addSynchronizeCompletedListener(new SynchronizeCompletedListener() {
				public void synchronizeCompleted(final SynchronizeCompletedEvent event) {
					isSynchronizerCompleted=true;
				}
			});
		
		isSynchronizerCompleted = false;
		synchronizer.run();
		
		while (!isSynchronizerCompleted) {
		}
		
	}
	
	public String getExternalDataVariableAsString(long taskId, String variableName) {
		try {
			Variable[]  variables = getClientRepository().loadTask(taskId).getAttachedExternalActivity().getData().getVariables();
			for (int i=0;i<variables.length;i++) {
				if (variables[i].getName().equals(variableName)) {
					return (String) variables[i].getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public HashMap<String, String> getExternalDataVariableAsHashMap(long taskId, String variableName) {
		HashMap<String,String> variableValueMap = null;
		
		try {
			Variable[]  variables = getClientRepository().loadTask(taskId).getAttachedExternalActivity().getData().getVariables();
			for (int i=0;i<variables.length;i++) {
				if (variables[i].getName().equals(variableName)) {
					String variableValue = (String) variables[i].getValue();
					variableValueMap = new HashMap<String,String>();

					Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
					String[] split = p.split(variableValue);
					
					for ( int j=1; j< split.length; j++ ) {
						variableValueMap.put("data"+j, split[j]);
					}
					return variableValueMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean startBusinessProcess(String businessProcessName, Map<String, String> inputValues) throws Exception{
		
		//Get BPM Server URL based on Connection Profile
		Properties connectionProperties = getConnectionProperties();
		
		String serverURL = connectionProperties.getProperty("javax.xml.rpc.service.endpoint.address");
		
		int beginIndex = serverURL.indexOf("//")+2;
		int endIndex = serverURL.indexOf(":", beginIndex);
		serverURL = serverURL.substring(beginIndex, endIndex);
		serverURL = serverURL.replaceAll("-", "_");
		serverURL = serverURL.toUpperCase();
		
		//ServerURL here shall be something like HENRYCHANDRA or GDN_LMD_01 or GDN_LMS_01 (matching the package name)
		
		//construct parameter and trigger the process via web service client using reflection
		
		System.out.println("Triggering " + businessProcessName + " in " + serverURL);
		
		Class portClass = Class.forName(serverURL + ".teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortTypeProxy");
		
		Object port = portClass.newInstance();
		
		Class processParamClass = Class.forName(serverURL + ".teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.ProcessParameter");
		
		Object[] processParams = (Object[]) Array.newInstance(processParamClass, inputValues.size());
		
		Iterator<String> iterator = inputValues.keySet().iterator();
		int i=0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = inputValues.get(key);
			
			Method setKeyMethod = processParamClass.getDeclaredMethod("setKey", java.lang.String.class);
			Method setValueMethod = processParamClass.getDeclaredMethod("setValue", java.lang.String.class);
			
			Object processParam = processParamClass.newInstance();
			
			setKeyMethod.invoke(processParam, key);
			setValueMethod.invoke(processParam, value);
			
			processParams[i] = processParam;
			i++;
		}
		
		try {
			Method invokeBusinessProcessMethod = portClass.getDeclaredMethod("invokeBusinessProcess", java.lang.String.class, Array.newInstance(processParamClass, inputValues.size()).getClass());
			
			invokeBusinessProcessMethod.invoke(port, businessProcessName, processParams);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
//		InvokeBusinessProcessWebServicePortType port = new InvokeBusinessProcessWebServicePortTypeProxy();
//		
//		ProcessParameter[] processParams = new ProcessParameter[inputValues.size()];
//		
//		Iterator<String> iterator = inputValues.keySet().iterator();
//		int i=0;
//		while (iterator.hasNext()) {
//			String key = iterator.next();
//			String value = inputValues.get(key);
//			ProcessParameter processParam = new ProcessParameter();
//			processParam.setKey(key);
//			processParam.setValue(value);
//			processParams[i] = processParam;
//			i++;
//		}
//		
//		try {
//			port.invokeBusinessProcess(businessProcessName, processParams);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
	}
	
	public static String getUserPasswordFromLDAP(String userName) {
		Properties ldapProperties;
		try {
			ldapProperties = getLdapProperties();
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		
		DirContext context = null;
		
		StringBuffer output = new StringBuffer();
		try {
			userName = "cn=" + userName;
			String url = ldapProperties.getProperty("ldap.url");
			String attribute = "userPassword";
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldapProperties.getProperty("ldap.principal"));
			env.put(Context.SECURITY_CREDENTIALS, ldapProperties.getProperty("ldap.credential"));
			
			context = new InitialDirContext(env);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> enumeration = context.search(ldapProperties.getProperty("ldap.userbasename"), userName, ctrl);
			while (enumeration.hasMore()) {
				SearchResult result = (SearchResult) enumeration.next();
				Attributes attribs = result.getAttributes();
				NamingEnumeration values = ((BasicAttribute) attribs.get(attribute)).getAll();
				while (values.hasMore()) {
					if (output.length() > 0) {
						output.append("|");
					}
					output.append(new String((byte[]) values.next()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (context!=null) {
			try {
				context.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return output.toString();
	}
}
