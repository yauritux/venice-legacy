package com.gwtplatform.dispatch.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class UnsupportedActionException_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer.deserialize(reader, (com.gwtplatform.dispatch.shared.UnsupportedActionException)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.UnsupportedActionException_FieldSerializer.serialize(writer, (com.gwtplatform.dispatch.shared.UnsupportedActionException)object);
    }
  }
  public static Class<?> concreteType() {
    return com.gwtplatform.dispatch.shared.UnsupportedActionException.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, com.gwtplatform.dispatch.shared.UnsupportedActionException instance) throws SerializationException {
    
    com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static com.gwtplatform.dispatch.shared.UnsupportedActionException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new com.gwtplatform.dispatch.shared.UnsupportedActionException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, com.gwtplatform.dispatch.shared.UnsupportedActionException instance) throws SerializationException {
    
    com.gwtplatform.dispatch.shared.ServiceException_FieldSerializer.serialize(streamWriter, instance);
  }
  
}
