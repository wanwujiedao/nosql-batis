package org.cbqin.batis.mongodb.delete;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Delete {
    String criteria();
}
