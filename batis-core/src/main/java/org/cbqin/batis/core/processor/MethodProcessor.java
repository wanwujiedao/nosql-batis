package org.cbqin.batis.core.processor;

import java.lang.reflect.Method;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/4
 */
public interface MethodProcessor<C, R> {

    public C parseMethod(final Method method, final Object[] args);

    public R executeMethod(final C context);
}
