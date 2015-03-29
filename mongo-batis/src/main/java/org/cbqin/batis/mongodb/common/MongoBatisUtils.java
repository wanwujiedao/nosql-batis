package org.cbqin.batis.mongodb.common;

import org.cbqin.batis.core.exception.ConfigException;

import java.lang.reflect.Method;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class MongoBatisUtils {

    /**
     * 获取collection名称
     *
     * @param method 方法
     * @return collection名称
     */
    public static String getCollectionName(Method method) {
        //优先获取方法上的@Collection注解
        Collection collectionName = method.getAnnotation(Collection.class);

        //如果方法上未定义，则从类上寻找
        collectionName = collectionName != null ? collectionName : method.getDeclaringClass().getAnnotation(Collection.class);

        //还是没有，报错
        if (collectionName == null) {
            throw new ConfigException("未定义此方法" + method.getName() + "的目标Collection名称");
        }

        return collectionName.value();
    }

}
