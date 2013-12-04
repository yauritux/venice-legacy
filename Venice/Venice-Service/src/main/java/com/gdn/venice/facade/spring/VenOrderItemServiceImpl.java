package com.gdn.venice.facade.spring;

import org.springframework.stereotype.Service;

import com.gdn.venice.facade.processor.MergeProcessor;
import com.gdn.venice.facade.processor.VenOrderItemMergeProcessor;
import com.gdn.venice.persistence.VenOrderItem;

@Service
public class VenOrderItemServiceImpl implements VenOrderItemService{
	
	@Override
	public VenOrderItem mergeVenOrderItem(VenOrderItem venOrderItem) {
		
		MergeProcessor processor = new VenOrderItemMergeProcessor();
		
		venOrderItem = (VenOrderItem) processor.doMerge(venOrderItem);
		
		return venOrderItem;
	}

}
