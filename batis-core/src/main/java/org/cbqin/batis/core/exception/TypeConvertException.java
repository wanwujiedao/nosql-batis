package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/24
 * @version 0.1.0
 */

/**
 * 类型转换异常
 */
public class TypeConvertException extends RuntimeException {
    public TypeConvertException(String message) {
        super(message);
    }

    public TypeConvertException(String message, Exception e) {
        super(message, e);
    }
}
