package org.cbqin.batis.mongodb.insert;

import org.apache.commons.lang3.Validate;
import org.cbqin.batis.core.exception.ConfigException;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.mongodb.common.MongoBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/4
 */
public class InsertAnnotationMethodProcessor implements AnnotationMethodProcessor<InsertContext, Void> {

    private static final Logger logger = LoggerFactory.getLogger(InsertAnnotationMethodProcessor.class);

    private MongoTemplate mongoTemplate;

    @Override
    public InsertContext parseMethod(Method method, Object[] args) {
        //insert方法参数不能为空
        Validate.notEmpty(args);

        //验证方法返回类型是否合法
        if (!Void.TYPE.equals(method.getReturnType())) {
            throw new ConfigException("非法的返回类型, 插入操作只能返回 void ");
        }

        //设置更新信息

        InsertContext insertContext = new InsertContext();
        //Insert insertAnnotation = method.getAnnotation(Insert.class);

        //要插入的对象列表
        List<Object> objectList = new ArrayList<Object>();
        for (Object argument : args) {
            if (argument instanceof Collection) {
                //集合类型
                objectList.addAll((Collection) argument);
            } else if (argument.getClass().isArray()) {
                //数组类型
                objectList.addAll(Arrays.asList((Object[]) argument));
            } else {
                objectList.add(argument);
            }
        }
        insertContext.setInsertObjectList(objectList);

        //目标collection
        insertContext.setCollectionName(MongoBatisUtils.getCollectionName(method));


        logger.debug(insertContext.toString());

        return insertContext;
    }

    @Override
    public Void executeMethod(final InsertContext context) {
        //insert支持批量插入，减少PRC调用
        mongoTemplate.insert(context.getInsertObjectList(), context.getCollectionName());
        return null;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Class<? extends Annotation> getTargetAnnotationType() {
        return Insert.class;
    }

    public InsertAnnotationMethodProcessor() {
    }

    public InsertAnnotationMethodProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}