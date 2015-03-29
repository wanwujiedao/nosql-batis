package org.cbqin.batis.mongodb.common;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/21
 */

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Collection {
    /**
     * collection名称
     *
     * @return collection名称
     */
    String value();
}
