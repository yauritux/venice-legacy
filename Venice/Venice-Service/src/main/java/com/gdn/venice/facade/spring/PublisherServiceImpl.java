package com.gdn.venice.facade.spring;

import org.springframework.stereotype.Service;

import com.gdn.venice.integration.outbound.Publisher;
import com.gdn.venice.persistence.VenOrderItem;

@Service
public class PublisherServiceImpl implements PublisherService {
	
	@Override
	public void publishUpdateOrderItemStatus(VenOrderItem newVenOrderItem){
		Publisher publisher = new Publisher();
        publisher.publishUpdateOrderItemStatus(newVenOrderItem);
	}
}
