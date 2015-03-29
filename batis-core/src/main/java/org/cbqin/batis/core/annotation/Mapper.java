package org.cbqin.batis.core.annotation;

import java.lang.annotation.*;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/2/28
 * @version 0.1.0
 */

/**
 * 映射标记
 * <p>标示需要自动生成代理类的接口</>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
}
