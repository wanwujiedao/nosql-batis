package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/24
 * @version 0.1.0
 */

/**
 * 默认的异常转换
 */
public class DefaultExceptionTranslator implements ExceptionTranslator {

    /**
     * 不作任何封装
     *
     * @param e 异常
     * @return 封装后的异常
     */
    @Override
    public RuntimeException translateException(RuntimeException e) {
        return e;
    }
}
