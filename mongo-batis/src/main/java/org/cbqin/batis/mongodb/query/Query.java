package org.cbqin.batis.mongodb.query;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/14
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Query {
    /**
     * 查询条件
     */
    String criteria() default "{}";

    /**
     * 是否查询多个，默认为true
     */
    boolean multi() default true;
}
