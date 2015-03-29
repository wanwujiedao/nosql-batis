package org.cbqin.batis.core.spring;

import org.apache.commons.lang3.StringUtils;
import org.cbqin.batis.core.scanner.AbstractMapperScanner;
import org.cbqin.batis.core.scanner.MapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Map;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/1/20
 * @version 0.1.0
 */

/**
 * 将指定接口的代理实例注入到spring ioc中
 */
public class MapperInstanceRegister implements BeanDefinitionRegistryPostProcessor {

    /**
     * 扫描器
     */
    private MapperScanner mapperScanner;

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory 当前应用上下文中的bean工厂
     * @throws org.springframework.beans.BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final Map<Class, Object> map = mapperScanner.getMapperInstances();

        for (Map.Entry<Class, Object> entry : map.entrySet()) {
            //把接口的名字作为bean名称
            //首字母变为小写
            StringUtils.uncapitalize(entry.getKey().getSimpleName());
            String beanName = entry.getKey().getSimpleName();
            if (beanFactory.containsBean(beanName)) {
                //重名处理
                beanName = entry.getKey().getName();
            }
            beanFactory.registerSingleton(beanName, entry.getValue());
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    public void setMapperScanner(AbstractMapperScanner mapperScanner) {
        this.mapperScanner = mapperScanner;
    }
}
