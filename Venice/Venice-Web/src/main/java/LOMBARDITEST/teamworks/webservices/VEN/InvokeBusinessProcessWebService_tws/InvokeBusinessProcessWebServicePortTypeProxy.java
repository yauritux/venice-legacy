package LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws;

public class InvokeBusinessProcessWebServicePortTypeProxy implements LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType {
  private String _endpoint = null;
  private LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType invokeBusinessProcessWebServicePortType = null;
  
  public InvokeBusinessProcessWebServicePortTypeProxy() {
    _initInvokeBusinessProcessWebServicePortTypeProxy();
  }
  
  public InvokeBusinessProcessWebServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initInvokeBusinessProcessWebServicePortTypeProxy();
  }
  
  private void _initInvokeBusinessProcessWebServicePortTypeProxy() {
    try {
      invokeBusinessProcessWebServicePortType = (new LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServiceLocator()).getInvokeBusinessProcessWebServiceSoap();
      if (invokeBusinessProcessWebServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)invokeBusinessProcessWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)invokeBusinessProcessWebServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (invokeBusinessProcessWebServicePortType != null)
      ((javax.xml.rpc.Stub)invokeBusinessProcessWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.InvokeBusinessProcessWebServicePortType getInvokeBusinessProcessWebServicePortType() {
    if (invokeBusinessProcessWebServicePortType == null)
      _initInvokeBusinessProcessWebServicePortTypeProxy();
    return invokeBusinessProcessWebServicePortType;
  }
  
  public void invokeBusinessProcess(java.lang.String businessProcessName, LOMBARDITEST.teamworks.webservices.VEN.InvokeBusinessProcessWebService_tws.ProcessParameter[] processParams) throws java.rmi.RemoteException{
    if (invokeBusinessProcessWebServicePortType == null)
      _initInvokeBusinessProcessWebServicePortTypeProxy();
    invokeBusinessProcessWebServicePortType.invokeBusinessProcess(businessProcessName, processParams);
  }
  
  
}