package org.cbqin.batis.core.annotation;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/5
 */


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    /**
     * 参数名称
     */
    String value();
}
