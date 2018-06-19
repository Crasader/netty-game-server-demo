package org.xialei.game.annotation;

import java.lang.annotation.*;

/**
 * 协议定义
 * 4字节消息长度+2字节模块号+2字节子类型号
 * 消息注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Protocol {
    /**
     * 消息所属模块号
     */
    short module();

    /**
     * 消息所属子类型
     */
    short cmd();
}
