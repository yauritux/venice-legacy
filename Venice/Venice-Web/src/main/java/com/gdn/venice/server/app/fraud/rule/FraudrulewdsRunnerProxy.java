package com.gdn.venice.server.app.fraud.rule;

import com.gdn.venice.server.app.fraud.rule.binding.FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator;

public class FraudrulewdsRunnerProxy implements FraudrulewdsRunner {
  private String _endpoint = null;
  private FraudrulewdsRunner fraudrulewdsRunner = null;
  
  public FraudrulewdsRunnerProxy() {
    _initFraudrulewdsRunnerProxy();
  }
  
  public FraudrulewdsRunnerProxy(String endpoint) {
    _endpoint = endpoint;
    _initFraudrulewdsRunnerProxy();
  }
  
  private void _initFraudrulewdsRunnerProxy() {
    try {
      fraudrulewdsRunner = (new FraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpServiceLocator()).getFraudrulewdsRunnerExport1_FraudrulewdsRunnerHttpPort();
      if (fraudrulewdsRunner != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)fraudrulewdsRunner)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)fraudrulewdsRunner)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (fraudrulewdsRunner != null)
      ((javax.xml.rpc.Stub)fraudrulewdsRunner)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public FraudrulewdsRunner getFraudrulewdsRunner() {
    if (fraudrulewdsRunner == null)
      _initFraudrulewdsRunnerProxy();
    return fraudrulewdsRunner;
  }
  
  public void fraudrule(javax.xml.rpc.holders.StringHolder executionId, javax.xml.rpc.holders.StringHolder fraudStatus, java.lang.Long orderId, javax.xml.rpc.holders.IntHolder riskPoint, javax.xml.rpc.holders.StringHolder outputString, javax.xml.rpc.holders.StringHolder userdata) throws java.rmi.RemoteException, RulesetExecutionException{
    if (fraudrulewdsRunner == null)
      _initFraudrulewdsRunnerProxy();
    fraudrulewdsRunner.fraudrule(executionId, fraudStatus, orderId, riskPoint, outputString, userdata);
  }
}