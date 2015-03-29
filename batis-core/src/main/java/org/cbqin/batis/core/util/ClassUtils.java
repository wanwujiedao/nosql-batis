package org.cbqin.batis.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/24
 */
public class ClassUtils {
    /**
     * 基本数据类型
     *
     * @see Class#isPrimitive()
     */
    public static final List<Class> BASIC_TYPES = Arrays.asList(new Class[]{
            byte.class,
            Byte.class,

            char.class,
            Character.class,

            short.class,
            Short.class,

            int.class,
            Integer.class,

            long.class,
            Long.class,

            float.class,
            Float.class,

            double.class,
            Double.class,

            boolean.class,
            Boolean.class,

            String.class,
    });

    public static boolean isBasicType(Class clazz) {
        return BASIC_TYPES.contains(clazz);
    }
}
