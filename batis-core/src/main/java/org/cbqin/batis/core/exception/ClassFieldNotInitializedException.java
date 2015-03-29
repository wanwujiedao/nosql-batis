package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/24
 * @version 0.1.0
 */

/**
 * 类属性未被初始化异常
 */
public class ClassFieldNotInitializedException extends RuntimeException {

    public ClassFieldNotInitializedException() {
        super();
    }


    public ClassFieldNotInitializedException(String message) {
        super(message);
    }

    public ClassFieldNotInitializedException(String message, Exception e) {
        super(message, e);
    }
}
