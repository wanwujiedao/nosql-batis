package org.cbqin.batis.mongodb.update;

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
public @interface Update {
    /**
     * 更新条件
     */
    String criteria();

    /**
     * 更新内容
     */
    String update();

    /**
     * 是否更新多条记录
     */
    boolean multi() default true;

    /**
     * 不存在时是否进行插入
     */
    boolean upsert() default false;
}
