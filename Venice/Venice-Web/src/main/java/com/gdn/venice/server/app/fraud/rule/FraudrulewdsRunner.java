/**
 * FraudrulewdsRunner.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gdn.venice.server.app.fraud.rule;

public interface FraudrulewdsRunner extends java.rmi.Remote {
    public void fraudrule(javax.xml.rpc.holders.StringHolder executionId, javax.xml.rpc.holders.StringHolder fraudStatus, java.lang.Long orderId, javax.xml.rpc.holders.IntHolder riskPoint, javax.xml.rpc.holders.StringHolder outputString, javax.xml.rpc.holders.StringHolder userdata) throws java.rmi.RemoteException, com.gdn.venice.server.app.fraud.rule.RulesetExecutionException;
}
