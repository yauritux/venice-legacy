package com.gwtplatform.dispatch.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.rpc.impl.TypeHandler;
import java.util.HashMap;
import java.util.Map;

public class DispatchService_TypeSerializer extends com.google.gwt.user.client.rpc.impl.SerializerBase {
  private static final Map<String, TypeHandler> methodMapJava;
  private static final MethodMap methodMapNative;
  private static final Map<Class<?>, String> signatureMapJava;
  private static final JsArrayString signatureMapNative;
  
  static {
    if (GWT.isScript()) {
      methodMapJava = null;
      methodMapNative = loadMethodsNative();
      signatureMapJava = null;
      signatureMapNative = loadSignaturesNative();
    } else {
      methodMapJava = loadMethodsJava();
      methodMapNative = null;
      signatureMapJava = loadSignaturesJava();
      signatureMapNative = null;
    }
  }
  
  @SuppressWarnings("deprecation")
  private static Map<String, TypeHandler> loadMethodsJava() {
    Map<String, TypeHandler> result = new HashMap<String, TypeHandler>();
    result.put("com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533", new com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.ActionException/2451163915", new com.gwtplatform.dispatch.shared.ActionException_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.ActionImpl/2338109017", new com.gwtplatform.dispatch.shared.ActionImpl_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.BatchResult/3569380335", new com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer.Handler());
    result.put("[Lcom.gwtplatform.dispatch.shared.BatchResult;/569845401", new com.gwtplatform.dispatch.shared.BatchResult_Array_Rank_1_FieldSerializer.Handler());
    result.put("[Lcom.gwtplatform.dispatch.shared.Result;/3987005119", new com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.ServiceException/918872072", new com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.UnsecuredActionImpl/3290230922", new com.gwtplatform.dispatch.shared.UnsecuredActionImpl_FieldSerializer.Handler());
    result.put("com.gwtplatform.dispatch.shared.UnsupportedActionException/26432650", new com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer.Handler());
    result.put("java.lang.String/2004016611", new com.google.gwt.user.client.rpc.core.java.lang.String_FieldSerializer.Handler());
    result.put("java.util.ArrayList/3821976829", new com.google.gwt.user.client.rpc.core.java.util.ArrayList_FieldSerializer.Handler());
    result.put("java.util.Arrays$ArrayList/1243019747", new com.google.gwt.user.client.rpc.core.java.util.Arrays_ArrayList_FieldSerializer.Handler());
    result.put("java.util.Collections$EmptyList/3012082351", new com.google.gwt.user.client.rpc.core.java.util.Collections_EmptyList_FieldSerializer.Handler());
    result.put("java.util.Collections$SingletonList/833432284", new com.google.gwt.user.client.rpc.core.java.util.Collections_SingletonList_FieldSerializer.Handler());
    result.put("java.util.LinkedList/1060625595", new com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer.Handler());
    result.put("java.util.Stack/1031431137", new com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer.Handler());
    result.put("java.util.Vector/3125574444", new com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer.Handler());
    return result;
  }
  
  @SuppressWarnings("deprecation")
  private static native MethodMap loadMethodsNative() /*-{
    var result = {};
    result["com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533"] = [
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;),
        @com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/google/gwt/user/client/rpc/IncompatibleRemoteServiceException;)
      ];
    
    result["com.gwtplatform.dispatch.shared.ActionException/2451163915"] = [
        @com.gwtplatform.dispatch.shared.ActionException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.ActionException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/gwtplatform/dispatch/shared/ActionException;),
      ];
    
    result["com.gwtplatform.dispatch.shared.ActionImpl/2338109017"] = [
        ,
        ,
        @com.gwtplatform.dispatch.shared.ActionImpl_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/gwtplatform/dispatch/shared/ActionImpl;)
      ];
    
    result["com.gwtplatform.dispatch.shared.BatchResult/3569380335"] = [
        @com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/gwtplatform/dispatch/shared/BatchResult;),
        @com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/gwtplatform/dispatch/shared/BatchResult;)
      ];
    
    result["[Lcom.gwtplatform.dispatch.shared.BatchResult;/569845401"] = [
        @com.gwtplatform.dispatch.shared.BatchResult_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.BatchResult_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Lcom/gwtplatform/dispatch/shared/BatchResult;),
        @com.gwtplatform.dispatch.shared.BatchResult_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Lcom/gwtplatform/dispatch/shared/BatchResult;)
      ];
    
    result["[Lcom.gwtplatform.dispatch.shared.Result;/3987005119"] = [
        @com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;[Lcom/gwtplatform/dispatch/shared/Result;),
        @com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;[Lcom/gwtplatform/dispatch/shared/Result;)
      ];
    
    result["com.gwtplatform.dispatch.shared.ServiceException/918872072"] = [
        @com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/gwtplatform/dispatch/shared/ServiceException;),
      ];
    
    result["com.gwtplatform.dispatch.shared.UnsecuredActionImpl/3290230922"] = [
        ,
        ,
        @com.gwtplatform.dispatch.shared.UnsecuredActionImpl_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Lcom/gwtplatform/dispatch/shared/UnsecuredActionImpl;)
      ];
    
    result["com.gwtplatform.dispatch.shared.UnsupportedActionException/26432650"] = [
        @com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Lcom/gwtplatform/dispatch/shared/UnsupportedActionException;),
      ];
    
    result["java.lang.String/2004016611"] = [
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/lang/String;),
        @com.google.gwt.user.client.rpc.core.java.lang.String_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/lang/String;)
      ];
    
    result["java.util.ArrayList/3821976829"] = [
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/ArrayList;),
        @com.google.gwt.user.client.rpc.core.java.util.ArrayList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/ArrayList;)
      ];
    
    result["java.util.Arrays$ArrayList/1243019747"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.Collections$EmptyList/3012082351"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.Collections$SingletonList/833432284"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/List;),
        @com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/List;)
      ];
    
    result["java.util.LinkedList/1060625595"] = [
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/LinkedList;),
        @com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/LinkedList;)
      ];
    
    result["java.util.Stack/1031431137"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Stack;),
        @com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Stack;)
      ];
    
    result["java.util.Vector/3125574444"] = [
        @com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer::instantiate(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::deserialize(Lcom/google/gwt/user/client/rpc/SerializationStreamReader;Ljava/util/Vector;),
        @com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer::serialize(Lcom/google/gwt/user/client/rpc/SerializationStreamWriter;Ljava/util/Vector;)
      ];
    
    return result;
  }-*/;
  
  @SuppressWarnings("deprecation")
  private static Map<Class<?>, String> loadSignaturesJava() {
    Map<Class<?>, String> result = new HashMap<Class<?>, String>();
    result.put(com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException_FieldSerializer.concreteType(), "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533");
    result.put(com.gwtplatform.dispatch.shared.ActionException_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.ActionException/2451163915");
    result.put(com.gwtplatform.dispatch.shared.ActionImpl_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.ActionImpl/2338109017");
    result.put(com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.BatchResult/3569380335");
    result.put(com.gwtplatform.dispatch.shared.BatchResult_Array_Rank_1_FieldSerializer.concreteType(), "[Lcom.gwtplatform.dispatch.shared.BatchResult;/569845401");
    result.put(com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer.concreteType(), "[Lcom.gwtplatform.dispatch.shared.Result;/3987005119");
    result.put(com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.ServiceException/918872072");
    result.put(com.gwtplatform.dispatch.shared.UnsecuredActionImpl_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.UnsecuredActionImpl/3290230922");
    result.put(com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer.concreteType(), "com.gwtplatform.dispatch.shared.UnsupportedActionException/26432650");
    result.put(com.google.gwt.user.client.rpc.core.java.lang.String_FieldSerializer.concreteType(), "java.lang.String/2004016611");
    result.put(com.google.gwt.user.client.rpc.core.java.util.ArrayList_FieldSerializer.concreteType(), "java.util.ArrayList/3821976829");
    result.put(com.google.gwt.user.client.rpc.core.java.util.Arrays.ArrayList_CustomFieldSerializer.concreteType(), "java.util.Arrays$ArrayList/1243019747");
    result.put(com.google.gwt.user.client.rpc.core.java.util.Collections.EmptyList_CustomFieldSerializer.concreteType(), "java.util.Collections$EmptyList/3012082351");
    result.put(com.google.gwt.user.client.rpc.core.java.util.Collections.SingletonList_CustomFieldSerializer.concreteType(), "java.util.Collections$SingletonList/833432284");
    result.put(com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer.concreteType(), "java.util.LinkedList/1060625595");
    result.put(com.google.gwt.user.client.rpc.core.java.util.Stack_FieldSerializer.concreteType(), "java.util.Stack/1031431137");
    result.put(com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer.concreteType(), "java.util.Vector/3125574444");
    return result;
  }
  
  @SuppressWarnings("deprecation")
  private static native JsArrayString loadSignaturesNative() /*-{
    var result = [];
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException::class)] = "com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException/3936916533";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.ActionException::class)] = "com.gwtplatform.dispatch.shared.ActionException/2451163915";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.ActionImpl::class)] = "com.gwtplatform.dispatch.shared.ActionImpl/2338109017";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.BatchResult::class)] = "com.gwtplatform.dispatch.shared.BatchResult/3569380335";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.BatchResult[]::class)] = "[Lcom.gwtplatform.dispatch.shared.BatchResult;/569845401";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.Result[]::class)] = "[Lcom.gwtplatform.dispatch.shared.Result;/3987005119";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.ServiceException::class)] = "com.gwtplatform.dispatch.shared.ServiceException/918872072";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.UnsecuredActionImpl::class)] = "com.gwtplatform.dispatch.shared.UnsecuredActionImpl/3290230922";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@com.gwtplatform.dispatch.shared.UnsupportedActionException::class)] = "com.gwtplatform.dispatch.shared.UnsupportedActionException/26432650";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.lang.String::class)] = "java.lang.String/2004016611";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.ArrayList::class)] = "java.util.ArrayList/3821976829";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.Arrays$ArrayList::class)] = "java.util.Arrays$ArrayList/1243019747";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.Collections$EmptyList::class)] = "java.util.Collections$EmptyList/3012082351";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.Collections$SingletonList::class)] = "java.util.Collections$SingletonList/833432284";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.LinkedList::class)] = "java.util.LinkedList/1060625595";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.Stack::class)] = "java.util.Stack/1031431137";
    result[@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(@java.util.Vector::class)] = "java.util.Vector/3125574444";
    return result;
  }-*/;
  
  public DispatchService_TypeSerializer() {
    super(methodMapJava, methodMapNative, signatureMapJava, signatureMapNative);
  }
  
}
