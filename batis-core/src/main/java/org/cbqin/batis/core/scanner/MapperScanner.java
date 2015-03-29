package org.cbqin.batis.core.scanner;

import java.util.Map;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/24
 */
public interface MapperScanner {
    Map<Class, Object> getMapperInstances();
}
