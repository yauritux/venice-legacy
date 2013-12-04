/**
 * OutboundServiceSessionEJBLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.pws.integration.pathway;

public class OutboundServiceSessionEJBLocator extends org.apache.axis.client.Service implements com.pws.integration.pathway.OutboundServiceSessionEJB {

    public OutboundServiceSessionEJBLocator() {
    }


    public OutboundServiceSessionEJBLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    /**
     * Added this constructor to the generated stub code to change the port address
     */
	public OutboundServiceSessionEJBLocator(String publishOutboundPort_address ) {
		this.OutboundServiceSessionEJBBeanServicePort_address = publishOutboundPort_address;
    }


    public OutboundServiceSessionEJBLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OutboundServiceSessionEJBBeanServicePort
    private java.lang.String OutboundServiceSessionEJBBeanServicePort_address = "http://gdn-esd-01:9080/GDNMediationModuleWeb/sca/OutboundServiceSessionEJBServiceExport";
//    private java.lang.String OutboundServiceSessionEJBBeanServicePort_address = "http://localhost:8080/OutboundServiceSessionEJB/OutboundServiceSessionEJBPortType/";
    
    

    public java.lang.String getOutboundServiceSessionEJBBeanServicePortAddress() {
        return OutboundServiceSessionEJBBeanServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OutboundServiceSessionEJBBeanServicePortWSDDServiceName = "OutboundServiceSessionEJBBeanServicePort";

    public java.lang.String getOutboundServiceSessionEJBBeanServicePortWSDDServiceName() {
        return OutboundServiceSessionEJBBeanServicePortWSDDServiceName;
    }

    public void setOutboundServiceSessionEJBBeanServicePortWSDDServiceName(java.lang.String name) {
        OutboundServiceSessionEJBBeanServicePortWSDDServiceName = name;
    }

    public com.pws.integration.pathway.OutboundServiceSessionEJBPortType getOutboundServiceSessionEJBBeanServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OutboundServiceSessionEJBBeanServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOutboundServiceSessionEJBBeanServicePort(endpoint);
    }

    public com.pws.integration.pathway.OutboundServiceSessionEJBPortType getOutboundServiceSessionEJBBeanServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.pws.integration.pathway.OutboundServiceSessionEJBBeanServicePortBindingStub _stub = new com.pws.integration.pathway.OutboundServiceSessionEJBBeanServicePortBindingStub(portAddress, this);
            _stub.setPortName(getOutboundServiceSessionEJBBeanServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOutboundServiceSessionEJBBeanServicePortEndpointAddress(java.lang.String address) {
        OutboundServiceSessionEJBBeanServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.pws.integration.pathway.OutboundServiceSessionEJBPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.pws.integration.pathway.OutboundServiceSessionEJBBeanServicePortBindingStub _stub = new com.pws.integration.pathway.OutboundServiceSessionEJBBeanServicePortBindingStub(new java.net.URL(OutboundServiceSessionEJBBeanServicePort_address), this);
                _stub.setPortName(getOutboundServiceSessionEJBBeanServicePortWSDDServiceName());
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
        if ("OutboundServiceSessionEJBBeanServicePort".equals(inputPortName)) {
            return getOutboundServiceSessionEJBBeanServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pathway.integration.pws.com", "OutboundServiceSessionEJB");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pathway.integration.pws.com", "OutboundServiceSessionEJBBeanServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OutboundServiceSessionEJBBeanServicePort".equals(portName)) {
            setOutboundServiceSessionEJBBeanServicePortEndpointAddress(address);
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
