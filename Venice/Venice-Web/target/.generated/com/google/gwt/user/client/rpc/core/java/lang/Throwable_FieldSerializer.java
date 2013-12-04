package com.google.gwt.user.client.rpc.core.java.lang;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class Throwable_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.lang.Throwable_FieldSerializer.deserialize(reader, (java.lang.Throwable)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.google.gwt.user.client.rpc.core.java.lang.Throwable_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.google.gwt.user.client.rpc.core.java.lang.Throwable_FieldSerializer.serialize(writer, (java.lang.Throwable)object);
    }
  }
  private static native java.lang.String getDetailMessage(java.lang.Throwable instance) /*-{
    return instance.@java.lang.Throwable::detailMessage;
  }-*/;
  
  private static native void  setDetailMessage(java.lang.Throwable instance, java.lang.String value) /*-{
    instance.@java.lang.Throwable::detailMessage = value;
  }-*/;
  
  public static Class<?> concreteType() {
    return java.lang.Throwable.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, java.lang.Throwable instance) throws SerializationException {
    setDetailMessage(instance, streamReader.readString());
    
  }
  
  public static java.lang.Throwable instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new java.lang.Throwable();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, java.lang.Throwable instance) throws SerializationException {
    streamWriter.writeString(getDetailMessage(instance));
    
  }
  
}
