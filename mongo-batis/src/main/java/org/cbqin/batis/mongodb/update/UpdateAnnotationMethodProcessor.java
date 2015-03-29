package org.cbqin.batis.mongodb.update;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import org.cbqin.batis.core.exception.ConfigException;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.core.util.JsonUtils;
import org.cbqin.batis.core.util.ParameterUtils;
import org.cbqin.batis.mongodb.common.MongoBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.BasicUpdate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/4
 */
public class UpdateAnnotationMethodProcessor implements AnnotationMethodProcessor<UpdateContext, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAnnotationMethodProcessor.class);
    private MongoTemplate mongoTemplate;

    @Override
    public UpdateContext parseMethod(Method method, Object[] args) {
        //验证方法返回类型是否合法
        //
        Class returnType = method.getReturnType();
        if (!Integer.TYPE.equals(returnType) && !Void.TYPE.equals(returnType)) {
            throw new ConfigException("非法的返回类型, 更新操作只能返回整型或void");
        }

        //获取方法上下文
        Map<String, Object> parameterContext = ParameterUtils.getParameterContext(method, args);

        //设置更新操作上下文
        UpdateContext updateContext = new UpdateContext();
        Update updateAnnotation = method.getAnnotation(Update.class);

        //更新条件
        String criteriaString = updateAnnotation.criteria();
        criteriaString = JsonUtils.parseAndReplaceJsonPathExpression(parameterContext, criteriaString);
        DBObject criteria = (DBObject) JSON.parse(criteriaString);
        updateContext.setCriteria(criteria);

        //更新内容
        String updateString = updateAnnotation.update();
        updateString = JsonUtils.parseAndReplaceJsonPathExpression(parameterContext, updateString);
        DBObject content = (DBObject) JSON.parse(updateString);
        updateContext.setUpdate(content);

        //目标collection
        updateContext.setCollectionName(MongoBatisUtils.getCollectionName(method));

        //是否更新多条记录
        boolean multi = updateAnnotation.multi();
        updateContext.setMultiUpdate(multi);

        //目标document不存在时是否进行插入
        updateContext.setUpsert(updateAnnotation.upsert());


        logger.debug(updateContext.toString());
        return updateContext;
    }

    @Override
    public Integer executeMethod(UpdateContext context) {
        //构造更新条件
        BasicQuery query = new BasicQuery(context.getCriteria());

        //构造更新内容
        BasicUpdate update = new BasicUpdate(context.getUpdate());

        //执行更新
        WriteResult result;
        if (context.isUpsert()) {
            result = mongoTemplate.upsert(query, update, context.getCollectionName());
        } else {
            if (context.isMultiUpdate()) {
                result = mongoTemplate.updateMulti(query, update, context.getCollectionName());
            } else {
                result = mongoTemplate.updateFirst(query, update, context.getCollectionName());
            }
        }

        result.getLastError().throwOnError();
        return result.getN();
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Class<? extends Annotation> getTargetAnnotationType() {
        return Update.class;
    }

    public UpdateAnnotationMethodProcessor() {
    }

    public UpdateAnnotationMethodProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
