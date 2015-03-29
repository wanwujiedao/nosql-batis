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
public @interface Order {
    /**
     * 查询的排序方式
     *
     * @return 排序字段和方向的列表
     */
    String value();
}
