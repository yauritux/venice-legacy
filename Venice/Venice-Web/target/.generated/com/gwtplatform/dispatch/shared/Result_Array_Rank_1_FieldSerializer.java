package com.gwtplatform.dispatch.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class Result_Array_Rank_1_FieldSerializer {
  public static class Handler implements com.google.gwt.user.client.rpc.impl.TypeHandler {
    public void deserialize(SerializationStreamReader reader, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer.deserialize(reader, (com.gwtplatform.dispatch.shared.Result[])object);
    }
    public Object instantiate(SerializationStreamReader reader) throws SerializationException {
      return com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer.instantiate(reader);
    }
    public void serialize(SerializationStreamWriter writer, Object object) throws SerializationException {
      com.gwtplatform.dispatch.shared.Result_Array_Rank_1_FieldSerializer.serialize(writer, (com.gwtplatform.dispatch.shared.Result[])object);
    }
  }
  public static Class<?> concreteType() {
    return com.gwtplatform.dispatch.shared.Result[].class;
  }
  
  public static void deserialize(SerializationStreamReader streamReader, com.gwtplatform.dispatch.shared.Result[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static com.gwtplatform.dispatch.shared.Result[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new com.gwtplatform.dispatch.shared.Result[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, com.gwtplatform.dispatch.shared.Result[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
}
