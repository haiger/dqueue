package com.github.haiger.dqueue.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author haiger
 * @since 2017年1月5日 上午7:45:52
 */
public class NamedThreadFactory implements ThreadFactory {
	private final String baseName;
    private final AtomicInteger threadNum = new AtomicInteger(0);
    
    public NamedThreadFactory(String baseName){
    	this.baseName = baseName;
    }
	
	public Thread newThread(Runnable r) {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setName(baseName + "-" + threadNum.getAndIncrement());
		return t;
	}

}
