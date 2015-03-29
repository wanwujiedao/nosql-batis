package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/24
 */


public interface ExceptionTranslator {
    /**
     * 将一种异常转换为另一种异常
     * <p>用于异常的包装，比如在解析配置时将xml格式错误重新包装为配置错误异常，从而达到更高的可识别性</p>
     */
    RuntimeException translateException(RuntimeException e);
}
