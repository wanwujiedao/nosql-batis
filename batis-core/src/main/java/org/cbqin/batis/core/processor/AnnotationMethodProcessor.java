package org.cbqin.batis.core.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/4
 * @version 0.1.0
 */

/**
 * 注解配置方法处理器
 *
 * @param <C>
 * @param <R>
 */
public interface AnnotationMethodProcessor<C, R> extends MethodProcessor<C, R> {

    /**
     * 根据方法上的注解和参数解析配置,获取相关操作的上下文
     *
     * @param method 方法
     * @param args   参数
     * @return 相关操作的上下文
     */
    C parseMethod(Method method, Object[] args);

    /**
     * @param context 相关操作上下文
     * @return 方法返回值
     */
    R executeMethod(C context);

    /**
     * 获取此处理器可以处理的注解类型
     *
     * @return 可处理的注解类型
     */
    Class<? extends Annotation> getTargetAnnotationType();
}
