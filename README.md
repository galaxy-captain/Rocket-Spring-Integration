# Spring RocketMQ
> 需要Maven依赖：<br> 
[org.apache.rocketmq:rocketmq-client:4.5.1](https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-client/4.5.1)<br>
[org.apache.rocketmq:rocketmq-acl:4.5.1](https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-acl/4.5.1)<br>
[org.springframework.retry:spring-retry:1.2.4.RELEASE](https://mvnrepository.com/artifact/org.springframework.retry/spring-retry/1.2.4.RELEASE)

## 项目简介 
* 提供@RocketListener注解，将RocketMQ与Spring集成，帮助开发人员提高开发效率。
* 可以快速从基于@RabbitListener注解的Spring项目迁移。

## 功能特性
#### 1. 自动注册
提供@RocketListener(consumerGroup,topic,tag)，在Spring启动过程中，自动注册PushConsumer消息监听器。
```java
@Component
public class MessageConsumer{
    
   @RocketListener(consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
   public ConsumeConcurrentlyStatus service(Message msg, MessageExt msgx, ConsumeConcurrentlyContext context) {
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
   }
}
```
#### 2. 延迟重试
@RocketListener(delayTimeLevel)支持延迟重试时间级别的定义。<br>
真实延迟时间由RocketMQ配置文件中messageDelayLevel决定。
```java
@RocketListener(delayTimeLevel = 1, /* ...省略... */)
```

#### 3. 异常忽略
@RocketListener(ignoredExceptions)支持定义可以忽略的异常，这些异常将不进行重试。
```java
@RocketListener(ignoredExceptions = {NullpointerException.class,ArrayIndexOutOfBoundsException.class})
```

#### 4. 多级别配置
* RocketConfigurationBean - 全局级别
* @RocketConfiguration - 类级别
* @RocketListener - 方法级别

优先级：方法级 > 类级 > 全局级，高优先级配置覆盖低优先级配置。

## 使用教程
1. 使用@EnableRocket开启功能
   ```java
   @EnableRocket
   @Configuration
   public class RocketMQConfiguration {
       // ...省略... 
   }
   ```
2. 配置NameServer
   * RocketConfigurationBean中使用setNameServer()方法
   * @RocketNameServer在类上定义

3. @RocketListener在方法上声明消费监听器
    ```java
    @Service
    public class MessageConsumerListener {
    
       @RocketListener(
          intance = "myInstance",
          consumerGroup = "myGroup",
          topic = "myTopic",
          tag = "myTag",
          delayTimeLevel = 3,
          ignoredExceptions = {MyException1.class, MyException2.class}
       )
       public ConsumeConcurrentlyStatus service(SimpleMessage msg, MessageExt msgx, ConsumeConcurrentlyContext ctx) {
          return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
       }
 
    }
    ```

## 结束语
#### 有任何事情，请留言或邮箱联系我。
邮箱：goskyer@163.com