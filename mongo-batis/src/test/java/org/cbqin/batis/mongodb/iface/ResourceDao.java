package org.cbqin.batis.mongodb.iface;


import org.cbqin.batis.core.annotation.Mapper;
import org.cbqin.batis.core.annotation.Param;
import org.cbqin.batis.mongodb.common.Collection;
import org.cbqin.batis.mongodb.delete.Delete;
import org.cbqin.batis.mongodb.entity.Resource;
import org.cbqin.batis.mongodb.insert.Insert;
import org.cbqin.batis.mongodb.query.Fields;
import org.cbqin.batis.mongodb.query.Order;
import org.cbqin.batis.mongodb.query.Page;
import org.cbqin.batis.mongodb.query.Query;
import org.cbqin.batis.mongodb.update.Update;

import java.util.Date;
import java.util.List;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/14
 */

@Mapper
@Collection("resources")
public interface ResourceDao {

    /**
     * 添加资源数据
     */
    @Insert
    void addResource(Resource resource);

    /**
     * 批量添加资源数据
     */
    @Insert
    void addResources(Resource resource1, Resource resource2);

    /**
     * 批量添加资源数据
     */
    @Insert
    void addResourceList(List<Resource> resources);

    /**
     * 批量添加资源数据
     */
    @Insert
    void addResourceArray(Resource[] resources);

    /**
     * 查询全部资源
     */
    @Query
    List<Resource> getAllResources();

    /**
     * 给资源添加新的标签
     */
    @Update(
            criteria = " {'rid': ' #{resource.rid} '} ",
            update = "{ '$addToSet' : {'tags': {'$each': #{tags}}} }"
    )
    void addTags(@Param("resource") Resource resource, @Param("tags") List<String> tags);

    /**
     * 给资源添加新的标签
     */
    @Update(
            criteria = " {'rid': ' #{0}'} ",
            update = "{ '$addToSet' : {'tags': {'$each': #{tags}}}, '$set':{'lastModify': { '$date' : '#{2}' }}}",
            upsert = true
    )
    void upsertTagsAndDate(String rid, @Param("tags") List<String> tags, Date modifyTime);

    /**
     * 根据资源ID获取单个资源标签
     */
    @Query(criteria = " { 'rid': '#{0}' }", multi = false)
    @Fields("{'tags' : true}")
    Resource getResourceTags(String rid);

    /**
     * 分页获取含有某标签的资源
     */
    @Query(criteria = "{'tags':'#{0}'}")
    @Order("{'lastModify': -1}")
    List<Resource> getRecentlyResourceListByTag(String tag, Page page);

    /**
     * 删除指定ID的资源
     */
    @Delete(criteria = "{'rid':'#{0}'}")
    void deleteResourceById(String rid);

    /**
     * 删除指定日期之前的所有资源
     */
    @Delete(criteria = "{'lastModify':{'$lt': {'$date':'#{date}'}}}")
    void deleteOldResources(@Param("date") Date date);
}