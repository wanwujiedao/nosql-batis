#NoSql Batis/Mongo Batis

[Mongo Batis](https://github.com/qinchuanbao/nosql-batis/tree/master/mongo-batis) 是一个mybatis风格的针对Mongodb的数据访问框架。我们只需要定义数据操作的接口，并进行相关配置，Mongo Batis即可通过动态代理方式生成相应的实现类，避免了大量的重复性代码。让我们的工作更有效率，代码也更简洁。


## 功能特性

* 提供对Mongodb进行**增删查改**四种操作的封装
* 基于注解的**映射器配置**方式
* 自动生成**接口实现类**
* 可实现**自定义的注解**及其解析器
* 非常方便地与**spring**进行集成

## 安装
```
$ git clone https://github.com/qinchuanbao/nosql-batis.git
$ cd nosql-batis
$ mvn clean install -Dmaven.test.skip
```

## 使用
1. 查询

	* 参数:

		参数        |             含义            |     类型   | 是否允许为空 | 默认值
		--------- | ------------------------- |-------------| :----------------: | ---------
		  criteria   |        查询条件         |  DBObject |          是         |
		   multi     | 是否查询多条文档  |  boolean  |          是         |  true
		    field     |         返回字段        |  DBObject |          是         |
		   sort       |         排序方式        | DBObject  |           是        |
		skip, limit |       分页条件          |      int      |           是        |


	* 示例:
		```java
		/**
		* 分页获取含有某标签的资源
		*/
		@Query(criteria = "{'tags':'#{tag}'}")
		@Order("{'lastModify': -1}")
		@Fields("{'tags' : true}")
		List<Resource> getResourceListByTag(@Param("tag") String tag, Page page);

		/**
		* 查询全部资源
		*/
		@Query
		public List<Resource> getAllResources();
		```
	* 说明:

	1. 需要使用分页功能时，请将`skip`和`limit`参数放入`Page`类实例中作为映射器接口的参数(位置任意)。
	2. `DBObject`类型的参数在配置中使用`JSON格式`的字符串，单引号或者双引号均可。
	3. 默认情况下会将接口的参数的序号(从0开始)作为key，参数的值作为value加入`参数上下文`中。同时也可以使用`@Param`注解为参数起一个名字，这样就会将这个名字作为key，值作为value加入到参数上下文中。
	4. 配置的字符串中允许使用`带命名根节点的json-path`表达式，解析器会根据根节点名称从`参数上下文`中获取相应的值。
	5. 如果使用注解进行配置，需要在方法或接口上使用@Collection注解来标识操作的目标collection。方法上的配置优先级更高。
	6. 上述说明不仅限于查询接口，全局有效。

2. 插入

	* 参数:  无

	* 示例:
		```java
		/**
		 * 批量添加资源数据
		 */
		@Insert
		public void addResources(Resource resource1, Resource resource2);

		/**
		 * 批量添加资源数据
		 */
		@Insert
		public void addResourceList(List<Resource> resources);
		```
3. 更新

	* 参数:

		参数     |               含义                 |     类型   |  是否允许为空 | 默认值
		---------- |------------------------------- | ----------- | :------------------: | --------
		 criteria |               查询条件         | DBObject |            是         |
		 update |               更新内容         | DBObject |            否         |
		 upsert  | 不存在时是否进行插入 |  boolean  |            是         |  false
		  multi   |    是否更新多条文档     |  boolean  |            是         |  true

	* 示例:
		```java
		/**
		 * 给资源添加新的标签
		 */
		@Update(
		        criteria = " {'rid': ' #{resource.rid} '} ",
		        update = "{ '$addToSet' : {'tags': {'$each': #{tags}}} }"
		)
		void addTags(@Param("resource") Resource resource, @Param("tags") List<String> tags);
		```
4. 删除

    * 参数:

		参数     |     含义    |    类型    | 是否允许为空 | 默认值
		--------- | ------------ | ----------- | :-----------------: |-----
		criteria | 查询条件 | DBObject |           否         |

    * 示例:

		```java
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
		```

## 示例代码

```java
//配置mongodb信息
MongoTemplate mongoTemplate = new MongoTemplate(new Mongo("127.0.0.1:27017"), "mongo_batis_test");

//添加注解处理器
Set<AnnotationMethodProcessor> processors = new LinkedHashSet<AnnotationMethodProcessor>(4);

//1. 查询注解处理器
processors.add(new QueryAnnotationMethodProcessor(mongoTemplate));
//2. 更新注解处理器
processors.add(new UpdateAnnotationMethodProcessor(
//3. 插入注解处理器
processors.add(new InsertAnnotationMethodProcessor(mongoTemplate));
//4. 删除注解处理器
processors.add(new DeleteAnnotationMethodProcessor(mongoTemplate));

//构造实例生成工厂
AnnotationMapperInstanceFactory factory = new AnnotationMapperInstanceFactory();
factory.setProcessors(processors);

//生成接口实例
ResourceDao resourceDao = (ResourceDao) factory.getInstance(ResourceDao.class);

//调用接口进行相关的操作
resourceDao.getAllResources()
```

更加完整的示例代码可以参考: https://github.com/qinchuanbao/nosql-batis/tree/master/mongo-batis/src/test/java/org/cbqin/batis/mongodb

##与spring集成

- 方法:

1. 通过`MapperScanner`接口可实现扫描指定包下所有的映射器接口(带有Mapper注解标记)，并生成其实例。
2.  通过`MapperInstanceRegister`可将所有MapperScanner生成的接口实例注入到spring的ioc容器中。

- 配置:

  ```xml
  <!-- 接口扫描器-->
  <bean id="mapperScanner" class="org.cbqin.batis.core.scanner.AnnotationMapperScanner">
          <property name="basePackage" value="org.cbqin.batis.mongodb.iface"/>
          <property name="mapperInstanceFactory" ref="mapperInstanceFactory"/>
  </bean>

  <!--注入实例到IOC中-->
  <bean class="org.cbqin.batis.core.spring.MapperInstanceRegister">
          <property name="mapperScanner" ref="mapperScanner"/>
  </bean>
  ```

    完整的配置文件请参考: https://github.com/qinchuanbao/nosql-batis/blob/master/mongo-batis/src/test/resources/applicationContext.xml


## 后续开发工作
* 支持使用XML文件进行映射器的配置
* 提供框架内缓存
* 提供数据库访问操作的前置与后置拦截器
* 提供xsd文件和自定义的xml标签，简化spring的集成配置
* 替换或重新开发部分组件, 减少对第三方组件的依赖
* 编写单元测试，完善集成测试
* 部分代码重构