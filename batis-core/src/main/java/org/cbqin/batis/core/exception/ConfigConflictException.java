package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/25
 */
public class ConfigConflictException extends ConfigException {
    public ConfigConflictException(String message) {
        super(message);
    }

    public ConfigConflictException(String message, Exception e) {
        super(message, e);
    }
}
