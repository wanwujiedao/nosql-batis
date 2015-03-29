package org.cbqin.batis.core.scanner;


import org.cbqin.batis.core.annotation.Mapper;
import org.cbqin.batis.core.util.ReflectionUtils;

import java.util.Set;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/27
 */
public class AnnotationMapperScanner extends AbstractMapperScanner {
    /**
     * 扫描指定包下面所有带有Mapper注解的接口
     *
     * @return 接口列表
     */
    @Override
    public Set<Class<?>> scanPackage() {
        return ReflectionUtils.scanAnnotation(super.getBasePackage(), Mapper.class);
    }
}
