package org.cbqin.batis.mongodb.query;

import com.mongodb.DBObject;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/4
 * @version 0.1.0
 */

/**
 * 查询操作上下文信息
 */
public class QueryContext {
    /**
     * 查询条件
     */
    private DBObject criteriaObject;

    /**
     * 查询多个/单个
     */
    private boolean multi;

    /**
     * 返回字段
     */
    private DBObject fieldObject;

    /**
     * 排序字段
     */
    private DBObject sortObject;

    /**
     * 分页信息
     */
    private Page page;

    /**
     * 返回值类型
     */
    private Class returnType;


    /**
     * 目标collection名称
     */
    private String collectionName;

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public DBObject getCriteriaObject() {
        return criteriaObject;
    }

    public void setCriteriaObject(DBObject criteriaObject) {
        this.criteriaObject = criteriaObject;
    }

    public DBObject getFieldObject() {
        return fieldObject;
    }

    public void setFieldObject(DBObject fieldObject) {
        this.fieldObject = fieldObject;
    }

    public DBObject getSortObject() {
        return sortObject;
    }

    public void setSortObject(DBObject sortObject) {
        this.sortObject = sortObject;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public String toString() {
        return "QueryContext{" +
                "collectionName='" + collectionName + '\'' +
                ", criteriaObject=" + criteriaObject +
                ", multi=" + multi +
                ", fieldObject=" + fieldObject +
                ", sortObject=" + sortObject +
                ", page=" + page +
                ", returnType=" + returnType +
                '}';
    }
}
