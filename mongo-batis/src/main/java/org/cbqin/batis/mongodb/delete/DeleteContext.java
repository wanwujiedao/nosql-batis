package org.cbqin.batis.mongodb.delete;

import com.mongodb.DBObject;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class DeleteContext {
    private String collectionName;
    private DBObject criteria;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public DBObject getCriteria() {
        return criteria;
    }

    public void setCriteria(DBObject criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "DeleteContext{" +
                "collectionName='" + collectionName + '\'' +
                ", criteria=" + criteria +
                '}';
    }
}
