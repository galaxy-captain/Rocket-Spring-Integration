# Rocket-Spring-Integration
> 需要Maven依赖：<br> 
[org.apache.rocketmq:rocketmq-client:4.5.1](https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-client/4.5.1)<br>
[org.apache.rocketmq:rocketmq-acl:4.5.1](https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-acl/4.5.1)<br>
[org.springframework.retry:spring-retry:1.2.4.RELEASE](https://mvnrepository.com/artifact/org.springframework.retry/spring-retry/1.2.4.RELEASE)

## 项目简介 
* 提供@RocketListener注解，将RocketMQ与Spring集成，帮助开发人员提高开发效率。
* 可以快速从基于@RabbitListener注解的Spring项目迁移。

## 功能特性
#### 1. 自动注册
> 提供@RocketListener(consumerGroup,topic,tag)，在Spring启动过程中，自动注册DefaultMQPushConsumer。

1.1 注册MessageListenerConcurrently消息监听器 <br>
```java
@Service
public class MessageConsumer{
   
    // orderly = false 并发消费
   @RocketListener(orderly = false, consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
   public ConsumeConcurrentlyStatus service(Message msg, MessageExt msgx, ConsumeConcurrentlyContext context) {
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
   }
   
}
```
1.2 注册MessageListenerOrderly消息监听器 <br>
```java
@Service
public class MessageConsumer{
    
   // orderly = true 顺序消费
   @RocketListener(orderly = true, consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
   public ConsumeOrderlyStatus service(Message msg, MessageExt msgx, ConsumeOrderlyContext context) {
      return ConsumeOrderlyStatus.SUCCESS;
   }
   
}
```

#### 2. 延迟重试
2.1 并发消费 <br>
@RocketListener(delayTimeLevel)支持延迟重试时间级别的定义。真实延迟时间由RocketMQ配置文件中messageDelayLevel决定。
```java
@RocketListener(delayTimeLevel = 1, /* ...省略... */)
```
2.2 顺序消费<br>
@RocketListener(suspendTimeMillis)支持暂停时间的定义，时间单位为毫秒(millis)
```java
@RocketListener(suspendTimeMillis = 1000, /* ...省略... */)
```

#### 3. 异常忽略
3.1 根据Exception类型忽略异常 <br>
@RocketListener(ignoredExceptions)支持定义可以忽略的异常，这些异常将不进行重试。
```java
@RocketListener(ignoredExceptions = {NullpointerException.class, ArrayIndexOutOfBoundsException.class})
```
3.2 实现ExceptionIgnore接口忽略异常 <br>
@RocketListener(exceptionIgnores)支持定义可以忽略的异常，实现ExceptionIgnore接口，当ignorable(e)返回true时忽略异常。
```java
@RocketListener(exceptionIgnores = {MyIgnore1.class, MyIgnore2.class})
```
#### 4. 多级别配置
* RocketConfiguration - 全局级别
* @RocketConsumer - 类级别
* @RocketListener - 方法级别

优先级：方法级 > 类级 > 全局级，高优先级配置覆盖低优先级配置。
#### 5. Properties占位符配置
与@Value注解中，占位符使用一致，可以加载任意Properties键值
```java
@RocketListener(topic = "${rocketmq.topic}")
```
#### 6. 消费监听器多种返回值类型
6.1 ConsumeConcurrentlyStatus类型
```java
@RocketListener(consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
  public ConsumeConcurrentlyStatus service(Message msg, MessageExt msgx, ConsumeConcurrentlyContext context) {
     return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
}
```
6.2 Boolean类型
* true为消费成功
* false为消费失败
```java
@RocketListener(consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
public boolean service(Message msg, ConsumeConcurrentlyContext context) {
     return true;
}
```
6.1 Void类型
* 默认消费成功
* 抛出异常消费失败
```java
@RocketListener(consumerGroup = "myGroup", topic = "myTopic",tag = "myTag")
public void service(Message msg, MessageExt msgx) {
    
}
```
## 使用教程

1. 使用@EnableRocketMQ开启功能
   ```java
   @EnableRocketMQ
   @Configuration
   public class RocketMQConfiguration {
       // ...省略... 
   }
   ```
2. 配置NameServer
   * RocketConfiguration中使用setNameServer()方法
   * @RocketConsumer在类上nameServer定义
   * @RocketListener在方法上nameServer定义

3. @RocketListener在方法上声明消费监听器
    ```java
    @Service
    public class MessageConsumerListener {
    
       @RocketListener(
          orderly = false, // 是否顺序消费
          nameServer = "127.0.0.1:9876", // nameServer地址
          instance = "myInstance", // instance名称
          consumerGroup = "myGroup", // consumerGroup名称
          topic = "myTopic", // topic名称
          tag = "myTag", // tag名称
          delayTimeLevel = 3, // 并发消费的重试延迟级别，orderly=false时有效
          suspendTimeMillis = 1000, // 顺序消费时的暂停时间，orderly=true时有效
          ignoredExceptions = {MyException1.class, MyException2.class}, // 可以被忽略的Exception
          exceptionIgnores = {MyIgnore1.class, MyIgnore2.class}, // 根据Exception内容忽略
          hook = MyRPCHook.class // RPCHook
       )
       public ConsumeConcurrentlyStatus service(YourMessageClass msg, MessageExt msgx, ConsumeConcurrentlyContext ctx) {
          return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
       }
 
    }
    ```

## 结束语
> 功能不断完善中......

#### 您的 ★Star 是对我工作的肯定。
#### 有任何事情，请留言或邮箱联系我。
邮箱：goskyer@163.com
