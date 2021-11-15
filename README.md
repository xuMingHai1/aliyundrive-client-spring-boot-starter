# aliyundrive-client-spring-boot-starter

#### 介绍

这是基于webflux.WebClient开发的阿里云盘客户端。提供简单好用的方法来使用阿里云盘，内部使用了缓存来提升性能，还可以自定义缓存实例，使用SpringBoot的自动装配，无需复杂的配置，开箱即用。大家觉得不错，欢迎一起参与开发！！！

#### 软件架构
QQ交流群：776339385

整体使用从里到外的扩展架构，最底层封装阿里云盘接口的访问，再由中间的执行器对访问进一步地增强，最后模板类进行方法的拓展。
经过精心制作，追求快速，低内存，高并发。结构清晰，相互之间的耦合度很低，使用了很多设计模式，如装饰器、模板、组合。。。


#### 安装教程

在pom文件中引入这个依赖。

只需要这一个依赖就可以是springboot项目，因为其内部引入了springboot-webflux


```xml
<dependency>
    <groupId>xyz.xuminghai</groupId>
    <artifactId>aliyundrive-client-spring-boot-starter</artifactId>
    <version>0.0.5-alpha</version>
</dependency>
```

#### 使用说明


refresh_token获取说明
![refresh_token获取说明](https://images.gitee.com/uploads/images/2021/1010/175912_b1196636_8492227.png "屏幕截图.png")

一般只需要在配置文件里加上refresh_token就可以了
![输入图片说明](https://images.gitee.com/uploads/images/2021/1112/024254_d5bcfa85_8492227.png "屏幕截图.png")

目前还添加了这些配置，有访问请求的webClient配置，和缓存实例装饰器配置，还有自定义缓存实例
![输入图片说明](https://images.gitee.com/uploads/images/2021/1115/212551_80f15050_8492227.png "屏幕截图.png")
配置简单方便，十分人性化！
![输入图片说明](https://images.gitee.com/uploads/images/2021/1115/212659_92cdcd53_8492227.png "屏幕截图.png")
注入ReactiveClientTemplate或ClientTemplate就可以使用了
![输入图片说明](https://images.gitee.com/uploads/images/2021/1115/212801_cd894c10_8492227.png "屏幕截图.png")

测试用例：
![输入图片说明](https://images.gitee.com/uploads/images/2021/1115/212945_a1e707b9_8492227.png "屏幕截图.png")
自定义缓存实例说明：

默认使用的是jvm缓存，这也是最快的，比方说使用redis，需要经过一系列的序列化，反序列化。

自定义缓存实例需要继承AbstractCacheInstance，创建缓存实例的bean，可以使用自带的kryo序列化器。详情请参考test包下的代码
![输入图片说明](https://images.gitee.com/uploads/images/2021/1115/213042_21515cc2_8492227.png "屏幕截图.png")
这个类中存放这令牌信息，每次刷新令牌时内容会自动更新
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/111603_3776d1e8_8492227.png "屏幕截图.png")

#### 参与贡献

[xuMingHai](https://gitee.com/xuminghai123)
