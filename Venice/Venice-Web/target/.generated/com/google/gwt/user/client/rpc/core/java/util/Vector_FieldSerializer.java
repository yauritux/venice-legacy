package com.google.gwt.user.client.rpc.core.java.util;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class Vector_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer.deserialize(reader, (java.util.Vector)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.google.gwt.user.client.rpc.core.java.util.Vector_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.util.Vector_CustomFieldSerializer.serialize(writer, (java.util.Vector)object);
    }
  }
  public static Class<?> concreteType() {
    return java.util.Vector.class;
  }
  
  public static java.util.Vector instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new java.util.Vector();
  }
  
}
