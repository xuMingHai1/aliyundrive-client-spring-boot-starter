# aliyundrive-client-spring-boot-starter

#### 介绍

使用webflux的webClient访问阿里云盘。提供程序员简单好用的api来使用阿里云盘，添加了默认的缓存实现，和开放的接口可以自定义缓存实现，使用SpringBoot的自动装配，可以很方便的进行配置。使用起来简单快捷，相当于拥有1T或者更多的OSS对象存储；各位觉得可以不妨一起来开发！！！

#### 软件架构

采用了从下到上的架构，最底层封装阿里云盘接口的访问，再由中间的执行器对数据进行处理，最后到达模板类，扩展使用方法。使用springboot的自动装配，使用的Reactive很适合高并发的访问，参照了mybatis的缓存

#### 安装教程

QQ交流群：776339385

现在已经完成了基本实现，可以很方便地上手使用，目前有两个Template，只需要自动注入其中一个就能调用方法来使用阿里云盘了。代码清晰易懂，说明文档充分。

自动装配需要这个依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
```

#### 使用说明

旁边那个发行版，将其中的三个jar包导入就可以了（分别是源代码，文档，编译后的）

refresh_token获取说明
![refresh_token获取说明](https://images.gitee.com/uploads/images/2021/1010/175912_b1196636_8492227.png "屏幕截图.png")
依赖配置，只需要这一个依赖就可以运行了，推荐使用高版本的SpringBoot
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/110440_94727d84_8492227.png "屏幕截图.png")
普通情况下，只需要在配置文件里加上refresh_token就可以了
![普通情况下，只需要在配置文件里加上refresh_token就可以了](https://images.gitee.com/uploads/images/2021/1027/005251_3f6085ee_8492227.png "屏幕截图.png")
目前还添加了这些配置，有访问请求的webClient配置，和缓存实例装饰器配置，还有自定义缓存实例
![输入图片说明](https://images.gitee.com/uploads/images/2021/1027/005601_3874f264_8492227.png "屏幕截图.png")
配置简单方便，十分人性化！
![输入图片说明](https://images.gitee.com/uploads/images/2021/1027/005750_3e516986_8492227.png "屏幕截图.png")
注入ReactiveClientTemplate或ClientTemplate就可以使用了

测试用例：
![输入图片说明](https://images.gitee.com/uploads/images/2021/1102/175010_9fd8b0c1_8492227.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/1102/175025_fdfdbb5c_8492227.png "屏幕截图.png")
自定义缓存实例说明，需要继承AbstractCacheInstance和实现Cache接口
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/111034_e1fb5b4d_8492227.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/110946_046fe4e3_8492227.png "屏幕截图.png")
这个类中存放这令牌信息，每次刷新令牌时内容会自动更新
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/111603_3776d1e8_8492227.png "屏幕截图.png")
丰富的自定义配置
![输入图片说明](https://images.gitee.com/uploads/images/2021/1028/112351_2f73cefb_8492227.png "屏幕截图.png")

#### 参与贡献

[xuMingHai](https://gitee.com/xuminghai123)

#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)