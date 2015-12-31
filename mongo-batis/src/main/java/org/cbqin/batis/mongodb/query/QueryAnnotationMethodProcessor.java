package org.cbqin.batis.mongodb.query;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.core.util.JsonUtils;
import org.cbqin.batis.core.util.ReflectionUtils;
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
public class QueryAnnotationMethodProcessor implements AnnotationMethodProcessor<QueryContext, Object> {
    private static final Logger logger = LoggerFactory.getLogger(QueryAnnotationMethodProcessor.class);

    private MongoTemplate mongoTemplate;

    @Override
    public QueryContext parseMethod(Method method, Object[] args) {
        //TODO: 把order, returnType, field等编译时就已经确定的参数都缓存起来，以提高效率

        //建立查询操作上下文
        QueryContext queryContext = new QueryContext();

        Query queryAnnotation = method.getAnnotation(Query.class);

        //是否查询列表, 后面的对判断返回类型的判定也依此为参照
        boolean multi = queryAnnotation.multi();
        queryContext.setMulti(multi);

        //获取，解析，替换条件字符串中的JSON-PATH表达式
        String criteriaString = queryAnnotation.criteria();
        if (StringUtils.isBlank(criteriaString)) {
            criteriaString = "{}";
        }
        criteriaString = JsonUtils.parseAndReplaceJsonPathExpression(method, args, criteriaString);
        DBObject criteria = (DBObject) JSON.parse(criteriaString);
        queryContext.setCriteriaObject(criteria);

        //查询顺序
        Order orderAnnotation = method.getAnnotation(Order.class);
        if (orderAnnotation != null) {
            String order = orderAnnotation.value();
            queryContext.setSortObject(((DBObject) JSON.parse(order)));
        }

        //获取collection名称
        queryContext.setCollectionName(MongoBatisUtils.getCollectionName(method));

        //获取需要返回的fields
        Fields fieldsAnnotation = method.getAnnotation(Fields.class);
        if (fieldsAnnotation != null) {
            String fieldsString = fieldsAnnotation.value();
            queryContext.setFieldObject(((DBObject) JSON.parse(fieldsString)));
        }

        //获取returnType
       /* final Class methodReturnType = method.getReturnType();
        Class returnType;

        if (Collection.class.isAssignableFrom(methodReturnType)) {
            if (!queryContext.isMulti()) {
                throw new ConfigConflictException("注解中的multi的配置为false，但返回类型却是集合类型");
            }

            //情况1: 如果返回一个列表，由于类型擦除机制，使用额外的注解来标识具体的类型
            //获取子类型
            ReturnType type = method.getAnnotation(ReturnType.class);
            if (type == null) {
                throw new ConfigException("返回List时必须配置ReturnType来表示具体的元素类型");
            }
            returnType = type.value();
        } else {
            //情况2: 返回普通对象
            returnType = methodReturnType;
        }*/
        Class<?> returnType = ReflectionUtils.getReturnType(method);
        queryContext.setReturnType(returnType);

        //分页
        //TODO: 考虑是否有更优雅的实现
        if (args != null) { //方法可能没有参数
            for (Object argument : args) {
                //扫描参数的类型，如果是Page类型，则将其作为分页条件
                if (argument instanceof Page) {
                    //作为分页参数
                    Page page = (Page) argument;
                    queryContext.setPage(page);
                }
            }
        }

        return queryContext;
    }


    @Override
    public Object executeMethod(QueryContext context) {

        //设置查询操作上下文

        // 1. 条件和返回字段
        BasicQuery query = new BasicQuery(context.getCriteriaObject(), context.getFieldObject());

        //2. 排序
        DBObject order = context.getSortObject();
        if (order != null) {
            query.setSortObject(order);
        }

        //3. 分页
        Page page = context.getPage();
        if (page != null && page.getSkip() != null) {
            query.skip(page.getSkip());
        }
        if (page != null && page.getLimit() != null) {
            query.limit(page.getLimit());
        }

        boolean multi = context.isMulti();
        Class returnType = context.getReturnType();
        String collectionName = context.getCollectionName();

        logger.trace(context.toString());

        if (multi) {
            /*DBCursor cursor = mongoTemplate.getCollection(collectionName).find(query.getQueryObject(), query.getFieldsObject()).sort(query.getQueryObject());
            if (page != null && page.getSkip() != null) {

                cursor = cursor.skip(page.getSkip());
            }
            if (page != null && page.getLimit() != null) {
                cursor = cursor.limit(page.getLimit());
            }
            while (cursor.hasNext()) {
                DBObject object = cursor.next();
                //将DBObject转换成return type对应的类型
                try {
                   Object returnObject =  returnType.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }*/

            return mongoTemplate.find(query, returnType, collectionName);
        } else {
            //查询单个对象
            return mongoTemplate.findOne(query, returnType, collectionName);
        }
    }

    @Override
    public Class<? extends Annotation> getTargetAnnotationType() {

        return Query.class;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public QueryAnnotationMethodProcessor() {
    }

    public QueryAnnotationMethodProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
