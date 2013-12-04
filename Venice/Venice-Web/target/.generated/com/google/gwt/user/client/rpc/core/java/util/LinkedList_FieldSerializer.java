package com.google.gwt.user.client.rpc.core.java.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class LinkedList_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer.deserialize(reader, (java.util.LinkedList)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.google.gwt.user.client.rpc.core.java.util.LinkedList_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.util.LinkedList_CustomFieldSerializer.serialize(writer, (java.util.LinkedList)object);
    }
  }
  public static Class<?> concreteType() {
    return java.util.LinkedList.class;
  }
  
  public static java.util.LinkedList instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new java.util.LinkedList();
  }
  
}
