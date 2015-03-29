package org.cbqin.batis.mongodb.delete;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.cbqin.batis.core.exception.ConfigException;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.core.util.JsonUtils;
import org.cbqin.batis.mongodb.common.MongoBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/4
 */
public class DeleteAnnotationMethodProcessor implements AnnotationMethodProcessor<DeleteContext, Void> {
    private static final Logger logger = LoggerFactory.getLogger(DeleteAnnotationMethodProcessor.class);

    private MongoTemplate mongoTemplate;

    @Override
    public Class<? extends Annotation> getTargetAnnotationType() {
        return Delete.class;
    }

    @Override
    public DeleteContext parseMethod(Method method, Object[] args) {

        //验证方法返回类型是否合法
        if (!Void.TYPE.equals(method.getReturnType())) {
            throw new ConfigException("非法的返回类型, 删除操作只能返回 void ");
        }

        //设置删除操作上下文
        DeleteContext deleteContext = new DeleteContext();


        Delete deleteAnnotation = method.getAnnotation(Delete.class);

        //目标collectionName
        deleteContext.setCollectionName(MongoBatisUtils.getCollectionName(method));

        //删除条件
        String deleteCriteriaString = deleteAnnotation.criteria();
        deleteCriteriaString = JsonUtils.parseAndReplaceJsonPathExpression(method, args, deleteCriteriaString);
        deleteContext.setCriteria((DBObject) JSON.parse(deleteCriteriaString));


        logger.debug(deleteContext.toString());
        return deleteContext;
    }

    @Override
    public Void executeMethod(DeleteContext context) {
        BasicQuery criteria = new BasicQuery(context.getCriteria());

        mongoTemplate.remove(criteria, context.getCollectionName());
        return null;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public DeleteAnnotationMethodProcessor() {
    }

    public DeleteAnnotationMethodProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}