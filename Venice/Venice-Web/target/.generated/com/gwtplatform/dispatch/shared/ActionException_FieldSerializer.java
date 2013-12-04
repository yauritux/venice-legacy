package com.gwtplatform.dispatch.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ActionException_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.ActionException_FieldSerializer.deserialize(reader, (com.gwtplatform.dispatch.shared.ActionException)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.gwtplatform.dispatch.shared.ActionException_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.ActionException_FieldSerializer.serialize(writer, (com.gwtplatform.dispatch.shared.ActionException)object);
    }
  }
  public static Class<?> concreteType() {
    return com.gwtplatform.dispatch.shared.ActionException.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, com.gwtplatform.dispatch.shared.ActionException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static com.gwtplatform.dispatch.shared.ActionException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new com.gwtplatform.dispatch.shared.ActionException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, com.gwtplatform.dispatch.shared.ActionException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.serialize(streamWriter, instance);
  }
  
}
