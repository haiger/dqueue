package com.github.haiger.dqueue.server.processer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.haiger.dqueue.common.protocol.RequestCode;

/**
 * @author haiger
 * @since 2017年2月14日 下午10:31:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Processer {
    RequestCode code();
}
