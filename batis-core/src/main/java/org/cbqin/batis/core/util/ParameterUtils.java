package org.cbqin.batis.core.util;

import org.cbqin.batis.core.annotation.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class ParameterUtils {

    /**
     * 获取方法参数的相关信息
     *
     * @param method 方法
     * @param args   方法参数
     * @return 方法参数信息
     */
    public static Map<String, Object> getParameterContext(Method method, Object[] args) {
        Map<String, Object> parameterContext = new HashMap<String, Object>();

        //各个参数的类型
        Class<?>[] parameterTypes = method.getParameterTypes();

        //各个参数上注解
        Annotation[][] annotationsList = method.getParameterAnnotations();

        //遍历参数
        for (int i = 0; i < parameterTypes.length; i++) {
            //参数类型
            //Class<?> type = parameterTypes[i];
            //参数值
            Object value = args[i];
            //参数注解
            Annotation[] annotations = annotationsList[i];

            //放入上下文
            Param paramAnnotation = ReflectionUtils.findAnnotation(annotations, Param.class);

            //情况1: 参数存在@Param注解，则将@Param的值作为key,参数值作为Value
            if (paramAnnotation != null) {
                String key = paramAnnotation.value();
                parameterContext.put(key.trim(), value);

            }
            //情况2: 参数没有@Param注解，则将参数的序号作为key
            parameterContext.put(Integer.toString(i), value);
        }
        //返回一个不可修改的集合
        return Collections.unmodifiableMap(parameterContext);
    }


}
