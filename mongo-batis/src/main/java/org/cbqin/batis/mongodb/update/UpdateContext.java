package org.cbqin.batis.mongodb.update;

import com.mongodb.DBObject;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class UpdateContext {
    private DBObject criteria;
    private DBObject update;
    private boolean multiUpdate;
    private String collectionName;

    private boolean upsert;


    public boolean isUpsert() {
        return upsert;
    }

    public void setUpsert(boolean upsert) {
        this.upsert = upsert;
    }

    public DBObject getCriteria() {
        return criteria;
    }

    public void setCriteria(DBObject criteria) {
        this.criteria = criteria;
    }

    public boolean isMultiUpdate() {
        return multiUpdate;
    }

    public void setMultiUpdate(boolean multiUpdate) {
        this.multiUpdate = multiUpdate;
    }

    public DBObject getUpdate() {
        return update;
    }

    public void setUpdate(DBObject update) {
        this.update = update;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public String toString() {
        return "UpdateContext{" +
                "collectionName='" + collectionName + '\'' +
                ", criteria=" + criteria +
                ", update=" + update +
                ", multiUpdate=" + multiUpdate +
                ", upsert=" + upsert +
                '}';
    }
}
