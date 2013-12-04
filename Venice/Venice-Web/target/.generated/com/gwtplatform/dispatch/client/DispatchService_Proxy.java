package com.gwtplatform.dispatch.client;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class DispatchService_Proxy extends RemoteServiceProxy implements com.gwtplatform.dispatch.client.DispatchServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "com.gwtplatform.dispatch.client.DispatchService";
  private static final String SERIALIZATION_POLICY ="26E7A1967C9F2CFD79F4DB523818EE4B";
  private static final com.gwtplatform.dispatch.client.DispatchService_TypeSerializer SERIALIZER = new com.gwtplatform.dispatch.client.DispatchService_TypeSerializer();
  
  public DispatchService_Proxy() {
    super(GWT.getModuleBaseURL(),
      null, 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public com.google.gwt.http.client.Request execute(java.lang.String cookieSentByRPC, com.gwtplatform.dispatch.shared.Action action, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("DispatchService_Proxy.execute", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("execute");
      streamWriter.writeInt(2);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("com.gwtplatform.dispatch.shared.Action");
      streamWriter.writeString(cookieSentByRPC);
      streamWriter.writeObject(action);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("DispatchService_Proxy.execute",  "requestSerialized"));
      return doInvoke(ResponseReader.OBJECT, "DispatchService_Proxy.execute", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
      return new com.google.gwt.user.client.rpc.impl.FailedRequest();
    }
  }
  
  public com.google.gwt.http.client.Request undo(java.lang.String cookieSentByRPC, com.gwtplatform.dispatch.shared.Action action, com.gwtplatform.dispatch.shared.Result result, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("DispatchService_Proxy.undo", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("undo");
      streamWriter.writeInt(3);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString("com.gwtplatform.dispatch.shared.Action");
      streamWriter.writeString("com.gwtplatform.dispatch.shared.Result");
      streamWriter.writeString(cookieSentByRPC);
      streamWriter.writeObject(action);
      streamWriter.writeObject(result);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("DispatchService_Proxy.undo",  "requestSerialized"));
      return doInvoke(ResponseReader.VOID, "DispatchService_Proxy.undo", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
      return new com.google.gwt.user.client.rpc.impl.FailedRequest();
    }
  }
}
