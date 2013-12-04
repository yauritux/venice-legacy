/**
 * Message.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.pws.integration.pathway;

public abstract class Message  implements java.io.Serializable {
    private java.lang.String msgPayload;

    private java.lang.String msgType;

    private java.lang.String sourceAdapter;

    public Message() {
    }

    public Message(
           java.lang.String msgPayload,
           java.lang.String msgType,
           java.lang.String sourceAdapter) {
           this.msgPayload = msgPayload;
           this.msgType = msgType;
           this.sourceAdapter = sourceAdapter;
    }


    /**
     * Gets the msgPayload value for this Message.
     * 
     * @return msgPayload
     */
    public java.lang.String getMsgPayload() {
        return msgPayload;
    }


    /**
     * Sets the msgPayload value for this Message.
     * 
     * @param msgPayload
     */
    public void setMsgPayload(java.lang.String msgPayload) {
        this.msgPayload = msgPayload;
    }


    /**
     * Gets the msgType value for this Message.
     * 
     * @return msgType
     */
    public java.lang.String getMsgType() {
        return msgType;
    }


    /**
     * Sets the msgType value for this Message.
     * 
     * @param msgType
     */
    public void setMsgType(java.lang.String msgType) {
        this.msgType = msgType;
    }


    /**
     * Gets the sourceAdapter value for this Message.
     * 
     * @return sourceAdapter
     */
    public java.lang.String getSourceAdapter() {
        return sourceAdapter;
    }


    /**
     * Sets the sourceAdapter value for this Message.
     * 
     * @param sourceAdapter
     */
    public void setSourceAdapter(java.lang.String sourceAdapter) {
        this.sourceAdapter = sourceAdapter;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Message)) return false;
        Message other = (Message) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.msgPayload==null && other.getMsgPayload()==null) || 
             (this.msgPayload!=null &&
              this.msgPayload.equals(other.getMsgPayload()))) &&
            ((this.msgType==null && other.getMsgType()==null) || 
             (this.msgType!=null &&
              this.msgType.equals(other.getMsgType()))) &&
            ((this.sourceAdapter==null && other.getSourceAdapter()==null) || 
             (this.sourceAdapter!=null &&
              this.sourceAdapter.equals(other.getSourceAdapter())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getMsgPayload() != null) {
            _hashCode += getMsgPayload().hashCode();
        }
        if (getMsgType() != null) {
            _hashCode += getMsgType().hashCode();
        }
        if (getSourceAdapter() != null) {
            _hashCode += getSourceAdapter().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Message.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pathway.integration.pws.com", "message"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msgPayload");
        elemField.setXmlName(new javax.xml.namespace.QName("", "msgPayload"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msgType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "msgType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceAdapter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceAdapter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
