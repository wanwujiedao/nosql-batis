package org.cbqin.batis.core.mapper;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/3
 */
public interface MapperInstanceFactory<T> {
    /**
     * 获取接口的实例
     *
     * @param clazz 接口
     * @return 接口实现类
     */
    T getInstance(Class<? extends T> clazz);
}
