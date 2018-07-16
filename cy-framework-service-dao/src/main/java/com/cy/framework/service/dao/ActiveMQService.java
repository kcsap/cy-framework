package com.cy.framework.service.dao;

import org.springframework.messaging.core.MessagePostProcessor;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;
import java.io.Serializable;
import java.util.Map;

public interface ActiveMQService {
    /**
     * 发送消息，不带返回值
     *
     * @param destination 发送消息的队列-对象
     * @param request     发送的消息内容
     */
    void sendMessage(Destination destination, Serializable request) ;

    /**
     * 单线程
     * @param destination
     * @param request
     */
    void sendMessageSingleThread(Destination destination, Serializable request) ;

    /**
     * 发送消息，异步，指定方法名，类名，和参数
     *  @param destination
     * @param controllerName
     * @param actionName
     * @param token
     * @param data
     */
    String sendMessage(Destination destination, String controllerName, String actionName, String token, Object... data) ;

    /**
     * 同步消息
     * @param destination
     * @param controllerName
     * @param actionName
     * @param token
     * @param data
     * @return
     */
    void sendMessageSingleThread(Destination destination, String controllerName, String actionName, String token, Object... data) ;

    /**
     * 发送消息，有返回值
     *
     * @param destination 发送消息的队列-对象
     * @param request     发送的消息内容
     * @param result      返回的对象类型
     * @param <T>         返回值
     * @return
     */
    <T> T sendMessage(Destination destination, Serializable request, Class<T> result) ;

    /**
     * 发送消息，有返回值
     *
     * @param destination   发送消息的队列-对象
     * @param request       发送的消息内容
     * @param postProcessor
     * @param result        返回的对象类型
     * @param <T>           返回值
     * @return
     */
    <T> T sendMessage(Destination destination, Serializable request, Class<T> result, MessagePostProcessor postProcessor) ;

    /**
     * 发送消息，有返回值
     *
     * @param destination    队列
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param postProcessor  提交的进度
     * @param data           数据
     * @
     */
    <T> T sendMessage(Destination destination, String controllerName, String actionName, String token, Class<T> result, MessagePostProcessor postProcessor, Object... data) ;

    /**
     * 发送消息，有返回值
     *
     * @param destination    队列
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           数据
     * @
     */
    <T> T sendMessage(Destination destination, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 发送消息，有返回值
     *
     * @param destination 发送消息的队列-对象
     * @param request     发送的消息内容
     * @param headers     头部文件
     * @param result      返回的对象类型
     * @param <T>         返回值
     * @return
     */
    <T> T sendMessage(Destination destination, Serializable request, Map<String, Object> headers, Class<T> result) ;

    /**
     * 发送消息，不带返回值  一对一发/异步
     *
     * @param queueName      队列的名称
     * @param controllerName 类名
     * @param actionName     方法名
     * @param data           数据
     * @
     */
    void sendMessageQueue(String queueName, String controllerName, String token, String actionName, Object... data) ;

    /**
     * 发送 一对一消息-异步
     *
     * @param queueName 队列名称
     * @param request   请求的数据
     * @
     */
    void sendMessageQueue(String queueName, Serializable request) ;

    /**
     * 发送消息，不带返回值 群发/异步
     *
     * @param topicName      队列的名称
     * @param controllerName 类名
     * @param actionName     方法名
     * @param data           数据
     * @
     */
    void sendMessageTopic(String topicName, String controllerName, String token, String actionName, Object... data) ;

    /**
     * 发送消息，有返回值 一对一
     *
     * @param queueName 发送消息的队列
     * @param request   发送的消息内容
     * @param result    返回的对象类型
     * @param <T>       返回值
     * @return
     */
    <T> T sendMessageQueue(String queueName, Serializable request, Class<T> result) ;

    /**
     * 发送消息-同步-一对一
     *
     * @param queueName      队列名称 字符串
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageQueue(String queueName, String controllerName, String token, String actionName, Class<T> result, Object... data) ;

    /**
     * 发送消息，有返回值 群发
     *
     * @param topicNmae 发送消息的队列-对象
     * @param request   发送的消息内容
     * @param result    返回的对象类型
     * @param <T>       返回值
     * @return
     */
    <T> T sendMessageTopic(String topicNmae, Serializable request, Class<T> result) ;

    void sendMessageTopic(String topicNmae, Serializable request) ;

    /**
     * 发送消息，有返回值 群发
     *
     * @param topicNmae      队列的名称
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           提交的数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageTopic(String topicNmae, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 发送消息，有返回值 一对一
     *
     * @param queueName     发送消息的队列-对象
     * @param request       发送的消息内容
     * @param postProcessor
     * @param result        返回的对象类型
     * @param <T>           返回值
     * @return
     */
    <T> T sendMessageQueue(String queueName, Serializable request, Class<T> result, MessagePostProcessor postProcessor) ;

    /**
     * 一对一发-同步 有返回值
     *
     * @param queueName      队列的名称
     * @param postProcessor  发送的进度
     * @param controllerName 类名
     * @param actionName     方法
     * @param result         返回值的类型
     * @param data           提交的数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageQueue(String queueName, MessagePostProcessor postProcessor, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 发送消息，有返回值 群发
     *
     * @param topicName     发送消息的队列-对象
     * @param request       发送的消息内容
     * @param postProcessor
     * @param result        返回的对象类型
     * @param <T>           返回值
     * @return
     */
    <T> T sendMessageTopic(String topicName, Serializable request, Class<T> result, MessagePostProcessor postProcessor) ;

    /**
     * 群发消息-同步
     *
     * @param topicName      队列的名称
     * @param postProcessor  发送的进度
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           提交的数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageTopic(String topicName, MessagePostProcessor postProcessor, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 发送消息，有返回值
     *
     * @param queueName 发送消息的队列-对象
     * @param request   发送的消息内容
     * @param headers   头部文件
     * @param result    返回的对象类型
     * @param <T>       返回值
     * @return
     */
    <T> T sendMessageQueue(String queueName, Serializable request, Map<String, Object> headers, Class<T> result) ;

    /**
     * 一对一发送消息-有返回值
     *
     * @param queueName      队列的名称
     * @param headers        头部文件
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           提交的数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageQueue(String queueName, Map<String, Object> headers, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 群送消息，有返回值
     *
     * @param topciName 发送消息的队列-对象
     * @param request   发送的消息内容
     * @param headers   头部文件
     * @param result    返回的对象类型
     * @param <T>       返回值
     * @return
     */
    <T> T sendMessageTopic(String topciName, Serializable request, Map<String, Object> headers, Class<T> result) ;

    /**
     * 群送消息，有返回值 同步
     *
     * @param topciName      发送消息的队列-对象
     * @param headers        头部文件
     * @param controllerName 类名
     * @param actionName     方法名
     * @param result         返回值的类型
     * @param data           提交的数据
     * @param <T>
     * @return
     * @
     */
    <T> T sendMessageTopic(String topciName, Map<String, Object> headers, String controllerName, String actionName, String token, Class<T> result, Object... data) ;

    /**
     * 获取发布队列 对象
     *
     * @param name
     * @return
     */
    Queue getQueue(String name) ;

    /**
     * 获取消费者订阅对象
     *
     * @param name
     * @return
     */
    Topic getTopic(String name) ;
}
