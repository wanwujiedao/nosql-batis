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
public @interface Fields {
    /**
     * 要查询的字段列表
     *
     * @return 字段列表
     */
    String value();
}
