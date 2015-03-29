package org.cbqin.batis.core.scanner;

import org.cbqin.batis.core.exception.ClassFieldNotInitializedException;
import org.cbqin.batis.core.mapper.MapperInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/27
 */
public abstract class AbstractMapperScanner implements MapperScanner {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMapperScanner.class);
    /**
     * 要扫描的包路径
     */
    private String basePackage;

    /**
     * 映射接口实例化工厂
     */
    private MapperInstanceFactory<Object> mapperInstanceFactory;


    public String getBasePackage() {
        return basePackage;
    }

    /**
     * 扫描指定的包，根据具体的逻辑获取目标接口
     *
     * @return 接口列表
     */
    public abstract Set<Class<?>> scanPackage();

    @Override
    public Map<Class, Object> getMapperInstances() {
        if (mapperInstanceFactory == null) {
            throw new ClassFieldNotInitializedException("mapperInstanceFactory未正确初始化");
        }

        Map<Class, Object> mappers = new HashMap<Class, Object>();

        //获取basePackage下所有条件的接口
        Set<Class<?>> ifaceList = scanPackage();

        //逐个生成代理类实例
        for (Class<?> iface : ifaceList) {
            Object instance = mapperInstanceFactory.getInstance(iface);
            if (instance == null) {
                logger.warn("failed to generate mapper instance for {0}", iface.getName());
            } else {
                mappers.put(iface, mapperInstanceFactory.getInstance(iface));
            }
        }

        //返回一个不可修改的mapper集合
        return Collections.unmodifiableMap(mappers);
    }


    //getters and setters

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setMapperInstanceFactory(MapperInstanceFactory<Object> mapperInstanceFactory) {
        this.mapperInstanceFactory = mapperInstanceFactory;
    }
}
