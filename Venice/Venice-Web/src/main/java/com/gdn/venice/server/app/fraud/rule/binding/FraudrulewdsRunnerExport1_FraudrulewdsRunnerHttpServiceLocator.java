/**
 * FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gdn.venice.server.app.fraud.rule.binding;

import java.io.FileInputStream;
import java.util.Properties;

import com.gdn.venice.server.app.fraud.rule.FraudrulewdsRunner;

public class FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator extends org.apache.axis.client.Service implements FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpService {

    public FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator() {
    	try {
			setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    public FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
    	
		try {
			setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    	
		try {
			setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    //Read config from environment variable
    protected static final String FRAUDRULES_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/lib/FraudRulesConfig.properties";
    
    private Properties getFraudRulesProperties() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(FRAUDRULES_PROPERTIES_FILE));
		return properties;		
	}

    // Use to get a proxy class for FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort
    private java.lang.String FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address;

    public java.lang.String getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortAddress() {
        return FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address;
    }
    
    public void setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortAddress() throws Exception {
    	//Get Rules Server URL based on Connection Profile
		Properties connectionProperties = getFraudRulesProperties();
		
		String serverURL = connectionProperties.getProperty("fraudrules.server");
		String serverPort = connectionProperties.getProperty("fraudrules.port");
        FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address = "http://" + serverURL + ":" + serverPort  + "/Fraud-MediationWeb/sca/FraudrulewdsRunnerExport1";
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName = "FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort";

    public java.lang.String getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName() {
        return FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName;
    }

    public void setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName(java.lang.String name) {
        FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName = name;
    }

    public FraudrulewdsRunner getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort(endpoint);
    }

    public FraudrulewdsRunner getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpBindingStub _stub = new FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpBindingStub(portAddress, this);
            _stub.setPortName(getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortEndpointAddress(java.lang.String address) {
        FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (FraudrulewdsRunner.class.isAssignableFrom(serviceEndpointInterface)) {
                FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpBindingStub _stub = new FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpBindingStub(new java.net.URL(FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort_address), this);
                _stub.setPortName(getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort".equals(inputPortName)) {
            return getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://fraudruleApp/Binding", "FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://fraudruleApp/Binding", "FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort".equals(portName)) {
            setFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
