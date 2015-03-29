package org.cbqin.batis.mongodb;

import com.google.common.collect.ImmutableList;
import com.mongodb.Mongo;
import org.cbqin.batis.core.mapper.AnnotationMapperInstanceFactory;
import org.cbqin.batis.core.processor.AnnotationMethodProcessor;
import org.cbqin.batis.mongodb.delete.DeleteAnnotationMethodProcessor;
import org.cbqin.batis.mongodb.entity.Resource;
import org.cbqin.batis.mongodb.iface.ResourceDao;
import org.cbqin.batis.mongodb.insert.InsertAnnotationMethodProcessor;
import org.cbqin.batis.mongodb.query.Page;
import org.cbqin.batis.mongodb.query.QueryAnnotationMethodProcessor;
import org.cbqin.batis.mongodb.update.UpdateAnnotationMethodProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.net.UnknownHostException;
import java.util.*;

import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/26
 */
@RunWith(Theories.class)
public class MongoBatisAllTest {

    private ResourceDao resourceDao;

    private MongoTemplate mongoTemplate;

    /**
     * 初始化 mapperInstanceFactory
     */
    @Before
    @SuppressWarnings("deprecation")
    public void init() throws UnknownHostException {
        //配置mongodb信息
        mongoTemplate = new MongoTemplate(new Mongo("172.16.81.43:27017"), "mongo_test");

        //配置注解处理器
        Set<AnnotationMethodProcessor> processors = new LinkedHashSet<AnnotationMethodProcessor>(4);
        //1. 查询注解处理器
        processors.add(new QueryAnnotationMethodProcessor(mongoTemplate));
        //2. 更新注解处理器
        processors.add(new UpdateAnnotationMethodProcessor(mongoTemplate));
        //3. 插入注解处理器
        processors.add(new InsertAnnotationMethodProcessor(mongoTemplate));
        //4. 删除注解处理器
        processors.add(new DeleteAnnotationMethodProcessor(mongoTemplate));

        //构造实例生成工厂
        AnnotationMapperInstanceFactory factory = new AnnotationMapperInstanceFactory();
        factory.setProcessors(processors);

        //生成接口实例
        resourceDao = (ResourceDao) factory.getInstance(ResourceDao.class);

        //测试开始前清空数据库
        clean();
    }

    @After
    public void clean() {
        //测试完成后清空数据库
        String collectionName = ResourceDao.class.getAnnotation(org.cbqin.batis.mongodb.common.Collection.class).value();
        mongoTemplate.dropCollection(collectionName);
        System.out.println("数据库已经清空");
    }

    public static final int RESOURCE_COUNT_FOR_TEST = 20;

    @DataPoint
    public static List<Resource> getResourcesForTest() {
        List<Resource> resources = new ArrayList<Resource>(RESOURCE_COUNT_FOR_TEST);
        for (int i = 0; i < RESOURCE_COUNT_FOR_TEST; i++) {
            Resource resource = new Resource();
            resource.setRid(UUID.randomUUID().toString().replace("-", ""));
            resource.setTitle("测试资源 " + i);
            resource.setPath("http://download.cbqin.org/test/resource" + i + ".doc");
            resource.setLastModify(new Date());

            resources.add(resource);
        }
        return resources;
    }

    @Theory
    public void testMongoBatis(List<Resource> resources) {
        assumeNotNull(resources);
        assumeTrue(resources.size() >= 20);

        //单个插入
        resourceDao.addResource(resources.get(0));

        //批量插入
        //形式1: 多参数形式
        resourceDao.addResources(resources.get(1), resources.get(2));

        //形式2: 列表形式
        List<Resource> resourceListForInsert = resources.subList(3, 8);
        resourceDao.addResourceList(resourceListForInsert);

        //形式3: 数组形式
        Resource[] resourceArrayForInsert = _getResourceArray(resources.subList(8, 10));
        resourceDao.addResourceArray(resourceArrayForInsert);

        //查询全部
        List<Resource> allResources = resourceDao.getAllResources();
        Assert.assertEquals(allResources.size(), 10); //验证之前的插入结果

        //更新已存在的
        List<String> newTags = ImmutableList.of("java", "mongodb", "batis");
        Resource existedResource = resources.get(0);
        resourceDao.addTags(existedResource, newTags);

        //根据条件进行单个查询
        Resource resourceFromQuery = resourceDao.getResourceTags(existedResource.getRid());
        Assert.assertNotNull(resourceFromQuery);
        Assert.assertTrue(resourceFromQuery.getTags().containsAll(newTags)); //验证上一步的更新操作

        //更新不存在的(设置不存在则插入)
        String upsertResourceId = UUID.randomUUID().toString();
        resourceDao.upsertTagsAndDate(upsertResourceId, newTags, new Date());

        //根据条件进行分页查询
        Resource latestResource = resources.get(12);
        latestResource.setTags(newTags);
        latestResource.setLastModify(new Date());
        resourceDao.addResource(latestResource); //再插入一个，测试分页用

        List<Resource> resourceListWithTag = resourceDao.getRecentlyResourceListByTag(newTags.get(0), new Page(1, 1));
        Assert.assertNotNull(resourceListWithTag);
        Assert.assertTrue(resourceListWithTag.size() == 1);
        Assert.assertTrue(resourceListWithTag.get(0).getRid().equals(upsertResourceId)); //验证上一步的upsert操作

        //删除单个
        resourceDao.deleteResourceById(upsertResourceId);
        Assert.assertNull(resourceDao.getResourceTags(upsertResourceId)); //验证是否被删除

        //删除多个
        resourceDao.deleteOldResources(latestResource.getLastModify());

        //验证上一步的删除
        List<Resource> finalResourceList = resourceDao.getAllResources();
        Assert.assertTrue(finalResourceList.size() == 1 && finalResourceList.get(0).getLastModify().equals(latestResource.getLastModify()));
    }

    private static Resource[] _getResourceArray(List<Resource> resourceList) {
        Resource[] resourceArray = new Resource[resourceList.size()];
        for (int i = 0; i < resourceArray.length; i++) {
            resourceArray[i] = resourceList.get(i);
        }
        return resourceArray;
    }
}
