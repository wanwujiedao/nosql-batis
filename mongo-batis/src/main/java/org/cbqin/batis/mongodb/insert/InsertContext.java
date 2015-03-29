package org.cbqin.batis.mongodb.insert;

import java.util.List;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class InsertContext {
    private List<Object> insertObjectList;
    private String collectionName;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<Object> getInsertObjectList() {
        return insertObjectList;
    }

    public void setInsertObjectList(List<Object> insertObjectList) {
        this.insertObjectList = insertObjectList;
    }

    @Override
    public String toString() {
        return "InsertContext{" +
                "collectionName='" + collectionName + '\'' +
                ", insertObjectList=" + insertObjectList +
                '}';
    }
}
