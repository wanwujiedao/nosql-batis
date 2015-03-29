package org.cbqin.batis.core.annotation;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/23
 * @version 0.1.0
 */

import java.lang.annotation.*;

/**
 * 标示实体类字段在数据中存储时使用的名称
 * <p>主要用于序列化和反序列化时的字段名称映射</p>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Field {
    String value();
}
