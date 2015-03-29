package org.cbqin.batis.core.util;


import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/28
 */
public class ReflectionUtils {
    /**
     * @param basePackage 包路径
     * @return 带有指定注解的接口的列表
     * @link https://github.com/ronmamo/reflections
     */
    public static Set<Class<?>> scanAnnotation(String basePackage, Class<? extends Annotation> clazz) {
        //TODO: http://stackoverflow.com/questions/435890/find-java-classes-implementing-an-interface
        //TODO: 换成性能更高的框架
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner(false /* don't exclude Object.class */),
                        new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(basePackage))));

        return reflections.getTypesAnnotatedWith(clazz);
    }

    /**
     * 从一个数组总查询指定类型的注解
     *
     * @param annotations 注解列表
     * @param clazz       要查找的注解类型
     * @return 目标注解
     */

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(Annotation[] annotations, Class<T> clazz) {
        if (annotations == null || clazz == null) return null;

        Annotation targetAnnotation = null;
        for (Annotation annotation : annotations) {
            if (clazz.isAssignableFrom(annotation.getClass())) {
                targetAnnotation = annotation;
                break;
            }
        }
        if (targetAnnotation == null) {
            return null;
        } else {
            return (T) targetAnnotation;
        }
    }

    /**
     * 获取方法的返回类型
     *
     * @param method 方法
     * @return 返回类型
     */
    public static Class<?> getReturnType(Method method) {

        Class<?> returnType = method.getReturnType();

        //集合类型
        if (Collection.class.isAssignableFrom(returnType)) {
            //获取泛型返回类型
            Type returnTypeParameter = method.getGenericReturnType();

            if (returnTypeParameter instanceof ParameterizedType) {
                //获取泛型参数的类型，也就是我们真正要的类型
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();

                if (actualTypeArguments != null && actualTypeArguments.length == 1) { //集合类型泛型参数最多只有一个
                    returnTypeParameter = actualTypeArguments[0];

                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            }
        }

        /*else if (Map.class.isAssignableFrom(returnType)) {
            Type returnTypeParameter = method.getGenericReturnType();
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    returnTypeParameter = actualTypeArguments[1];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }
                }
            }
        }*/

        return returnType;
    }
}
