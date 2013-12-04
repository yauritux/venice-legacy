package com.gwtplatform.dispatch.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class BatchResult_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer.deserialize(reader, (com.gwtplatform.dispatch.shared.BatchResult)object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.BatchResult_FieldSerializer.serialize(writer, (com.gwtplatform.dispatch.shared.BatchResult)object);
    }
  }
  private static native java.util.List getResults(com.gwtplatform.dispatch.shared.BatchResult instance) /*-{
    return instance.@com.gwtplatform.dispatch.shared.BatchResult::results;
  }-*/;
  
  private static native void  setResults(com.gwtplatform.dispatch.shared.BatchResult instance, java.util.List value) /*-{
    instance.@com.gwtplatform.dispatch.shared.BatchResult::results = value;
  }-*/;
  
  public static Class<?> concreteType() {
    return com.gwtplatform.dispatch.shared.BatchResult.class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, com.gwtplatform.dispatch.shared.BatchResult instance) throws SerializationException {
    setResults(instance, (java.util.List) streamReader.readObject());
    
  }
  
  public static com.gwtplatform.dispatch.shared.BatchResult instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new com.gwtplatform.dispatch.shared.BatchResult();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, com.gwtplatform.dispatch.shared.BatchResult instance) throws SerializationException {
    streamWriter.writeObject(getResults(instance));
    
  }
  
}
