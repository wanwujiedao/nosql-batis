package org.cbqin.batis.core.mapper;


import org.cbqin.batis.core.exception.ClassFieldNotInitializedException;
import org.cbqin.batis.core.exception.ConfigException;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.core.processor.MethodProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/2/27
 * @version 0.1.0
 */

/**
 * 基于注解方式的接口实例生成器工厂
 */
public class AnnotationMapperInstanceFactory implements MapperInstanceFactory, InvocationHandler {

    /**
     * 注解处理器
     * <p>可动态修改，从而可以方便地添加自定义注解处理器</p>
     */
    private Set<AnnotationMethodProcessor> processors;

    /**
     * (注解-->处理器)映射列表
     * <p>从processors中解析生成，主要是为了提高效率</p>
     */
    private Map<Class<? extends Annotation>, AnnotationMethodProcessor> _annotationProcessorMap;

    /**
     * 初始化
     * <p>主要用来初始化<b>可识别的注解列表</b></p>
     */
    @SuppressWarnings("unchecked")
    public void init() {
        if (_annotationProcessorMap == null) {
            _annotationProcessorMap = new HashMap<Class<? extends Annotation>, AnnotationMethodProcessor>(12);
        }

        if (processors == null) {
            throw new ClassFieldNotInitializedException("processors未被正确初始化");
        }

        //缓存可识别注解列表，提高效率
        for (AnnotationMethodProcessor processor : processors) {
            _annotationProcessorMap.put(processor.getTargetAnnotationType(), processor);
        }
    }

    /**
     * 生成实现类
     *
     * @param iface 被代理的接口
     * @return 代理实现类
     */
    public Object getInstance(final Class iface) {
        //必须是接口
        assert iface.isInterface();
        //使用jdk生成代理类
        //TODO: 调研java assist 等其他工具通过拼接字符串的形式生成字节码，优化性能
        return Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, this);
    }

    /**
     * 动态代理方法
     * <p>主要流程为 解析注解 --> 获取数据操作的类型和参数 --> 执行操作 --> 返回
     * <p>当方法上有多个不同操作类型(增删查改)的注解时，根据<code>processors</code>中处理器定义时的顺序只处理第一个
     */

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (_annotationProcessorMap == null) {
            init();
        }

        //处理Object的方法，如equals, toString()
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        //遍历方法上所有注解，如果发现可以处理的注解则交给相应的注解处理器
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType(); //注解实例的类型只能通过annotationType方法获取

            if (_annotationProcessorMap.containsKey(annotationType)) { //当前注解可被处理
                MethodProcessor mapperProcessor = _annotationProcessorMap.get(annotationType);
                //任何实现了MapperProcessor接口的实现类，parseConfig的返回类型和processConfig的输入类型必然一致
                Object config = mapperProcessor.parseMethod(method, args);
                return mapperProcessor.executeMethod(config);
            }
        }

        throw new ConfigException("没有可识别的注解，无法生成此方法" + method.getName() + "的实现类，请检查您的注解配置是否有误");
    }


    /*********Getters and Setters*************/

    /**
     * Getter
     */
    public Set<AnnotationMethodProcessor> getProcessors() {
        return processors;
    }

    /**
     * Setter
     */
    public void setProcessors(Set<AnnotationMethodProcessor> processors) {
        this.processors = processors;
    }
}
