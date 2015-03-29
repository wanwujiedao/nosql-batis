package org.cbqin.batis.mongodb;

import org.cbqin.batis.mongodb.entity.Resource;
import org.cbqin.batis.mongodb.iface.ResourceDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/2/27
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class SpringIntegrationTest {
    @Autowired
    private ResourceDao resourceDao;

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Test
    public void testIntegrateWithSpring() {
        //准备数据
        Resource resource = new Resource();
        resource.setRid(UUID.randomUUID().toString());
        resource.setTitle("测试资源");
        resource.setLastModify(new Date());
        resource.setTags(Arrays.asList("spring", "batis", "mongodb"));
        resource.setPath("http://download.cbqin.org/test/resource.doc");

        //验证resourceDao实例是否注入成功
        Assert.assertNotNull(resourceDao);

        //添加资源
        String rid = testAddResource(resource);

        //查询资源
        Resource resourceFromQuery = testGetResourceTags(rid);

        //验证资源是否成功添加
        Assert.assertNotNull(resourceFromQuery);
        Assert.assertTrue(resourceFromQuery.getTags().contains("spring"));

        //更新资源标签列表
        String newTag = "java";
        testUpdateResourceTags(rid, Arrays.asList(newTag));

        //验证是否更新成功
        Assert.assertTrue(testGetResourceTags(rid).getTags().contains(newTag));

        //删除资源
        testDeleteResource(rid);

        //验证是否删除成功
        Assert.assertNull(testGetResourceTags(rid));
    }

    private String testAddResource(Resource resource) {
        resourceDao.addResource(resource);
        return resource.getRid();
    }

    private void testUpdateResourceTags(String rid, List<String> tags) {
        resourceDao.upsertTagsAndDate(rid, tags, new Date());
    }

    private Resource testGetResourceTags(String rid) {
        return resourceDao.getResourceTags(rid);
    }

    private void testDeleteResource(String rid) {
        resourceDao.deleteResourceById(rid);
    }
}
