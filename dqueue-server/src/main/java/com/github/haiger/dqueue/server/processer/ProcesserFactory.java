package com.github.haiger.dqueue.server.processer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.util.ClassScanner;

/**
 * @author haiger
 * @since 2017年2月14日 下午10:18:04
 */
public class ProcesserFactory {
    private static final String processerPackage = "com.github.haiger.dqueue.server.processer";
    private Map<RequestCode, RequestProcesser> processers = new HashMap<>();
    
    public RequestProcesser getProcesser(RequestCode code) {
        return processers.get(code);
    }
    
    public void init() throws Throwable {
        ClassScanner scanner = new ClassScanner(processerPackage);
        Set<Class<?>> classes = scanner.scanAnnotation(Processer.class);
        if (classes == null) {
            throw new Exception("no RequestProcesser implements");
        }
        
        for (Class<?> clazz : classes) {
            Processer processer = clazz.getAnnotation(Processer.class);
            RequestProcesser requestProcesser = (RequestProcesser) clazz.newInstance();
            processers.put(processer.code(), requestProcesser);
        }
    }
}
