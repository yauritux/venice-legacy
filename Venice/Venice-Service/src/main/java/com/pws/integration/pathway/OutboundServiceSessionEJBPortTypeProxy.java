package com.pws.integration.pathway;

public class OutboundServiceSessionEJBPortTypeProxy implements
		com.pws.integration.pathway.OutboundServiceSessionEJBPortType {
	private String _endpoint = null;
	private com.pws.integration.pathway.OutboundServiceSessionEJBPortType outboundServiceSessionEJBPortType = null;
	/*
	 * Added to generated code
	 */
	private String _outboundServicePort_address = null;

	public OutboundServiceSessionEJBPortTypeProxy() {
		_initOutboundServiceSessionEJBPortTypeProxy();
	}

	public OutboundServiceSessionEJBPortTypeProxy(String endpoint) {
		_endpoint = endpoint;
		_initOutboundServiceSessionEJBPortTypeProxy();
	}

	/**
	 * Added this contructor to the generated stub code so that we can pass the
	 * endpoint address
	 */
	public OutboundServiceSessionEJBPortTypeProxy(String endpoint,
			String outboundServicePort_address) {
		_endpoint = endpoint;
		_outboundServicePort_address = outboundServicePort_address;
		_initOutboundServiceSessionEJBPortTypeProxy();
	}

	private void _initOutboundServiceSessionEJBPortTypeProxy() {
		try {

			// Added this if to set the port address based on the value passed
			// in the new constructor
			if (_outboundServicePort_address != null
					&& !_outboundServicePort_address.isEmpty()) {
				outboundServiceSessionEJBPortType = (new com.pws.integration.pathway.OutboundServiceSessionEJBLocator(
						_outboundServicePort_address))
						.getOutboundServiceSessionEJBBeanServicePort();
			} else {

				outboundServiceSessionEJBPortType = (new com.pws.integration.pathway.OutboundServiceSessionEJBLocator())
						.getOutboundServiceSessionEJBBeanServicePort();
			}
			if (outboundServiceSessionEJBPortType != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) outboundServiceSessionEJBPortType)
							._setProperty(
									"javax.xml.rpc.service.endpoint.address",
									_endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) outboundServiceSessionEJBPortType)
							._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (outboundServiceSessionEJBPortType != null)
			((javax.xml.rpc.Stub) outboundServiceSessionEJBPortType)
					._setProperty("javax.xml.rpc.service.endpoint.address",
							_endpoint);

	}

	public com.pws.integration.pathway.OutboundServiceSessionEJBPortType getOutboundServiceSessionEJBPortType() {
		if (outboundServiceSessionEJBPortType == null)
			_initOutboundServiceSessionEJBPortTypeProxy();
		return outboundServiceSessionEJBPortType;
	}

	public java.lang.Boolean publish(
			com.pws.integration.pathway.TextMessage outboundMessage)
			throws java.rmi.RemoteException {
		if (outboundServiceSessionEJBPortType == null)
			_initOutboundServiceSessionEJBPortTypeProxy();
		return outboundServiceSessionEJBPortType.publish(outboundMessage);
	}

}