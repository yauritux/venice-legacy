/**
 * InvokeBusinessProcessWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws;

public class InvokeBusinessProcessWebServiceLocator extends org.apache.axis.client.Service implements GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebService {

    public InvokeBusinessProcessWebServiceLocator() {
    }


    public InvokeBusinessProcessWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public InvokeBusinessProcessWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for InvokeBusinessProcessWebServiceSoap

    /**
     * SOAP Port
     */
    private java.lang.String InvokeBusinessProcessWebServiceSoap_address = "http://GDN-LMD-01:19086/teamworks/webservices/VEN/InvokeBusinessProcessWebService.tws";

    public java.lang.String getInvokeBusinessProcessWebServiceSoapAddress() {
        return InvokeBusinessProcessWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InvokeBusinessProcessWebServiceSoapWSDDServiceName = "InvokeBusinessProcessWebServiceSoap";

    public java.lang.String getInvokeBusinessProcessWebServiceSoapWSDDServiceName() {
        return InvokeBusinessProcessWebServiceSoapWSDDServiceName;
    }

    public void setInvokeBusinessProcessWebServiceSoapWSDDServiceName(java.lang.String name) {
        InvokeBusinessProcessWebServiceSoapWSDDServiceName = name;
    }

    public GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType getInvokeBusinessProcessWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InvokeBusinessProcessWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInvokeBusinessProcessWebServiceSoap(endpoint);
    }

    public GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType getInvokeBusinessProcessWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServiceSoapSoapBindingStub _stub = new GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServiceSoapSoapBindingStub(portAddress, this);
            _stub.setPortName(getInvokeBusinessProcessWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInvokeBusinessProcessWebServiceSoapEndpointAddress(java.lang.String address) {
        InvokeBusinessProcessWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServiceSoapSoapBindingStub _stub = new GDN_LMD_01.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServiceSoapSoapBindingStub(new java.net.URL(InvokeBusinessProcessWebServiceSoap_address), this);
                _stub.setPortName(getInvokeBusinessProcessWebServiceSoapWSDDServiceName());
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
        if ("InvokeBusinessProcessWebServiceSoap".equals(inputPortName)) {
            return getInvokeBusinessProcessWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://GDN-LMD-01:19086/teamworks/webservices/VEN/InvokeBusinessProcessWebService.tws", "InvokeBusinessProcessWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://GDN-LMD-01:19086/teamworks/webservices/VEN/InvokeBusinessProcessWebService.tws", "InvokeBusinessProcessWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("InvokeBusinessProcessWebServiceSoap".equals(portName)) {
            setInvokeBusinessProcessWebServiceSoapEndpointAddress(address);
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
