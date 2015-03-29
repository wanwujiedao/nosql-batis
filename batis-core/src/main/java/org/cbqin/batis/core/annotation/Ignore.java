package org.cbqin.batis.core.annotation;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/23
 * @version 0.1.0
 */

/**
 * 标示字段不需要被持久化
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore {
}
