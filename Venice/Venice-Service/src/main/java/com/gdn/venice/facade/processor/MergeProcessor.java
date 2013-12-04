package com.gdn.venice.facade.processor;

public abstract class MergeProcessor {
	public abstract boolean preMerge(Object obj);
	public abstract boolean merge(Object obj);
	public abstract boolean postMerge(Object obj);
	
	public final Object doMerge(Object obj){
		
		preMerge(obj);
		merge(obj);
		postMerge(obj);
		
		return obj;
	}
}
