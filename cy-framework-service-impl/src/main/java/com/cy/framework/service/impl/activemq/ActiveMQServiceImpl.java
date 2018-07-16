package com.cy.framework.service.impl.activemq;

import com.alibaba.fastjson.JSONArray;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.mybaties.dao.ActiveMQQueueMapper;
import com.cy.framework.service.dao.ActiveMQService;
import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.SerializeUtil;
import com.cy.framework.util.json.JsonUtilFastjson;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActiveMQServiceImpl extends DataException implements ActiveMQService {
    @Resource
    public RedisService redisService;
    @Resource
    public ActiveMQQueueMapper queueMapper;
    @Resource
    public ApplicationContext context;
    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private JmsMessagingTemplate messagingTemplate;

    @Override
    public void sendMessage(Destination destination, Serializable request) throws DataException {
        try {
            convertSendReceive(request, destination);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
    }

    @Override
    public void sendMessageSingleThread(Destination destination, Serializable request) {
        try {
            BaseFactoryParam param = (BaseFactoryParam) request;
            Object object = context.getBean(param.getControllerName() + "Controller");
            Class<?> classs = object.getClass().getSuperclass();
            Method method = classs.getMethod("invoke", BaseFactoryParam.class);
            List<Object> list = new ArrayList<>();
            list.add(request);
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object resultObjes = method.invoke(object, list.toArray());
                        logger.debug("invoke result is:" + JsonUtilFastjson.toJSONString(resultObjes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Date());
            logger.debug("contains bean:" + object.toString());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T sendMessage(Destination destination, Serializable request, Class<T> result) throws DataException {
        T t = null;
        try {
            t = convertSendAndReceive(request, result, destination);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
        return t;
    }

    @Override
    public <T> T sendMessage(Destination destination, Serializable request, Class<T> result, MessagePostProcessor postProcessor) throws DataException {
        T t = null;
        try {
            t = convertSendAndReceive(request, result, destination);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
        return t;
    }

    private <T> T convertSendAndReceive(Serializable request, Class<T> result, Destination destination) throws Exception {
        ActiveMQObjectMessage activeMQObjectMessage = new ActiveMQObjectMessage();
        activeMQObjectMessage.setObject(request);
        return messagingTemplate.convertSendAndReceive(destination, activeMQObjectMessage, result);

    }

    private void convertSendReceive(Serializable request, Destination destination) throws Exception {
        ActiveMQObjectMessage activeMQObjectMessage = new ActiveMQObjectMessage();
        activeMQObjectMessage.setObject(request);
        messagingTemplate.convertAndSend(destination, activeMQObjectMessage);

    }

    @Override
    public <T> T sendMessage(Destination destination, Serializable request, Map<String, Object> headers, Class<T> result) throws DataException {
        T t = null;
        try {
            t = convertSendAndReceive(request, result, destination);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
        return t;
    }

    @Override
    public void sendMessageQueue(String queueName, Serializable request) throws DataException {
        sendMessage(getQueue(queueName), request);
    }

    @Override
    public void sendMessageTopic(String topicName, Serializable request) throws DataException {
        sendMessage(getTopic(topicName), request);
    }

    @Override
    public <T> T sendMessageQueue(String queueName, Serializable request, Class<T> result) throws DataException {
        return sendMessage(getQueue(queueName), request, result);
    }

    @Override
    public <T> T sendMessageTopic(String topicNmae, Serializable request, Class<T> result) throws DataException {
        return sendMessage(getTopic(topicNmae), request, result);
    }

    @Override
    public <T> T sendMessageQueue(String queueName, Serializable request, Class<T> result, MessagePostProcessor postProcessor) throws DataException {
        return sendMessage(getQueue(queueName), request, result, postProcessor);
    }

    @Override
    public <T> T sendMessageTopic(String topicName, Serializable request, Class<T> result, MessagePostProcessor postProcessor) throws DataException {
        return sendMessage(getTopic(topicName), request, result, postProcessor);
    }

    @Override
    public <T> T sendMessageQueue(String queueName, Serializable request, Map<String, Object> headers, Class<T> result) throws DataException {
        return sendMessage(getQueue(queueName), request, headers, result);
    }

    @Override
    public <T> T sendMessageTopic(String topciName, Serializable request, Map<String, Object> headers, Class<T> result) throws DataException {
        return sendMessage(getTopic(topciName), request, headers, result);
    }

    private Serializable getSerializable(String controllerName, String actionName, String token, Object... data) {
        BaseFactoryParam param = new BaseFactoryParam();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(Arrays.asList(data));
        param.setData(jsonArray);
        param.setActionName(actionName);
        param.setControllerName(controllerName);
        param.setToken(token);
        return param;
    }

    private Serializable getSerializable(String queueName, String controllerName, String actionName, String token, Object... data) {
        BaseFactoryParam param = new BaseFactoryParam();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(Arrays.asList(data));
        param.setData(jsonArray);
        param.setQueue_name(queueName);
        param.setActionName(actionName);
        param.setControllerName(controllerName);
        param.setToken(token);
        return param;
    }

    @Override
    public String sendMessage(Destination destination, String controllerName, String actionName, String token, Object... data) throws DataException {
        sendMessage(destination, getSerializable(controllerName, actionName, token, data));
        return controllerName;
    }

    @Override
    public void sendMessageSingleThread(Destination destination, String controllerName, String actionName, String token, Object... data) {
        sendMessageSingleThread(destination, getSerializable(controllerName, actionName, token, data));
    }

    @Override
    public <T> T sendMessage(Destination destination, String controllerName, String actionName, String token, Class<T> result, MessagePostProcessor postProcessor, Object... data) throws DataException {
        return sendMessage(destination, getSerializable(controllerName, actionName, token, data), result, postProcessor);
    }

    @Override
    public <T> T sendMessage(Destination destination, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessage(destination, getSerializable(controllerName, actionName, token, data), result);
    }

    @Override
    public void sendMessageQueue(String queueName, String controllerName, String token, String actionName, Object... data) throws DataException {
        sendMessageQueue(queueName, getSerializable(queueName, controllerName, token, actionName, data));
    }

    @Override
    public void sendMessageTopic(String topicName, String controllerName, String token, String actionName, Object... data) throws DataException {
        sendMessageTopic(topicName, getSerializable(topicName, controllerName, token, actionName, data));
    }

    @Override
    public <T> T sendMessageQueue(String queueName, String controllerName, String token, String actionName, Class<T> result, Object... data) throws DataException {
        return sendMessageQueue(queueName, getSerializable(queueName, controllerName, actionName, token, data), result);
    }

    @Override
    public <T> T sendMessageTopic(String topicNmae, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessageTopic(topicNmae, getSerializable(topicNmae, controllerName, token, actionName, data), result);
    }

    @Override
    public <T> T sendMessageQueue(String queueName, MessagePostProcessor postProcessor, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessageQueue(queueName, getSerializable(queueName, controllerName, actionName, token, data), result, postProcessor);
    }

    @Override
    public <T> T sendMessageTopic(String topicName, MessagePostProcessor postProcessor, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessageTopic(topicName, getSerializable(topicName, controllerName, actionName, token, data), result, postProcessor);
    }

    @Override
    public <T> T sendMessageQueue(String queueName, Map<String, Object> headers, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessageQueue(queueName, getSerializable(queueName, controllerName, actionName, token, data), headers, result);
    }

    @Override
    public <T> T sendMessageTopic(String topciName, Map<String, Object> headers, String controllerName, String actionName, String token, Class<T> result, Object... data) throws DataException {
        return sendMessageTopic(topciName, getSerializable(topciName, controllerName, actionName, token, data), headers, result);
    }

    @Override
    public Queue getQueue(String name) {
        Queue queue = null;
        try {
            if (name == null || name.isEmpty()) {
                throw new Exception("name is null");
            }
            byte[] b = redisService.getObjBytes(name);
            if (b != null && b.length > 0) {
                queue = (Queue) SerializeUtil.unserialize(b);
            }
            if (queue == null) {
                logger.warn("get redis queue result is null");
                queue = new ActiveMQQueue(name);
                redisService.set(name.getBytes(), SerializeUtil.serialize(queue));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return queue;
    }

    @Override
    public Topic getTopic(String name) {
        Topic topic = null;
        try {
            if (name == null || name.isEmpty()) {
                throw new Exception("name is null");
            }
            topic = (Topic) redisService.getObj(name);
            if (topic == null) {
                logger.warn("get redis topic result is null");
                topic = new ActiveMQTopic(name);
                redisService.set(name.getBytes(), SerializeUtil.serialize(topic));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return topic;
    }
}
