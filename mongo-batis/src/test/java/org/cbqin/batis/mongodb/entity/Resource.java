package org.cbqin.batis.mongodb.entity;


import java.util.Date;
import java.util.List;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/2/11
 * @version 0.1.0
 */

/**
 * 资源信息
 */
public class Resource {
    /**
     * 资源ID
     */
    private String rid;

    /**
     * 资源名称
     */
    private String title;

    /**
     * 最新修改时间
     */
    private Date lastModify;

    /**
     * 资源存储地址
     */
    private String path;

    /**
     * 标签
     */
    private List<String> tags;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
}
