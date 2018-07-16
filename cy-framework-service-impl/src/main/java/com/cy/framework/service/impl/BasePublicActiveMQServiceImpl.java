package com.cy.framework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.PageInfo;
import com.cy.framework.mybaties.dao.BasePublicMapper;
import com.cy.framework.service.dao.ActiveMQService;
import com.cy.framework.service.dao.BasePublicActiveMQService;
import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.UserTokenService;
import com.cy.framework.util.FinalConfigParam;
import com.cy.framework.util.SerializeUtil;
import com.cy.framework.util.SystemCode;
import com.cy.framework.util.json.JSONUtils;
import com.cy.framework.util.json.JsonUtilFastjson;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public abstract class BasePublicActiveMQServiceImpl extends DataException implements BasePublicActiveMQService {
    private BasePublicMapper baseMapper;
    @Resource
    public ActiveMQService activeMQService;
    @Resource
    public UserTokenService userTokenService;
    public BaseFactoryParam baseFactoryParam;
    private Class<?> aClass;
    @Resource
    public RedisService redisService;
    public ActiveMQService getActiveMQService() {
        return activeMQService;
    }

    public void setMapper(BasePublicMapper mapper, Class<?> cl) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
        aClass = cl;
        setClass(cl);
    }

    public void setMapper(BasePublicMapper mapper) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Map<String, Object> insert(Object object, BaseFactoryParam param) {
        init();
        return JSONUtils.generatorMap("添加", baseMapper.insertSelective(object) > 0, object);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Map<String, Object> delete(Object id, BaseFactoryParam param) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteByPrimaryKey(id) > 0, id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Map<String, Object> update(Object object, BaseFactoryParam param) {
        init();
        return JSONUtils.generatorMap("更新", baseMapper.updateByPrimaryKeySelective(object) > 0, object);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Map<String, Object> deleteObject(Object object, BaseFactoryParam param) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteObject(object) > 0, object);

    }

    @Override
    public Object findById(Object id, BaseFactoryParam param) {
        init();
        return baseMapper.selectByPrimaryKey(id);
    }

    public Object query(Object t, BaseFactoryParam param) {
        init();
        return baseMapper.query(t);
    }

    @Override
    public Object queryListPage(Object t, Integer page, Integer pageSize, BaseFactoryParam param) {
        init();
        return baseMapper.queryListPage(t, getPageInfo(page,pageSize));
    }

    @Override
    public Integer queryListPageCount(Object t, BaseFactoryParam param) {
        init();
        return baseMapper.queryListPageCount(t);
    }

    @Override
    public Map<String, Object> queryManagerListPage(Object t, Integer page, Integer pageSize, BaseFactoryParam param) {
        Map<String, Object> map = new HashMap<>();
        map.put("rows", queryListPage(t, page, pageSize, param));
        map.put("total", queryListPageCount(t, param));
        return map;
    }

    @Override
    public <T> T sendQueue(BaseFactoryParam param, Class<T> t) {
        T object = null;
        try {
//            if (param.getQueue_name() == null || param.getQueue_name().isEmpty()) {
//                throw new Exception("队列名称未传");
//            }
            object = activeMQService.sendMessage(activeMQService.getQueue(param.getQueue_name()), param, t);
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-9999");
            jsonObject.put("message", e.getMessage());
            jsonObject.put("data", null);
            object = (T) jsonObject;
        }
        return object;
    }

    @Override
    public Object sendTopic(BaseFactoryParam param) {
        Object object = null;
        try {
            if (param.getQueue_name() == null || param.getQueue_name().isEmpty()) {
                throw new Exception("队列名称未传");
            }
            object = activeMQService.sendMessage(activeMQService.getTopic(param.getQueue_name()), param, null);
            System.out.println(object);
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-9999");
            jsonObject.put("message", e.getMessage());
            jsonObject.put("data", null);
            object = jsonObject.toString();
        }
        return object;
    }

    /**
     * 发送消息
     *
     * @param data
     * @param token
     */
    @Override
    public void sendUserIdMessage(Map<String, Object> data, String token) {
        activeMQService.sendMessage(activeMQService.getQueue("game.queue.server-not"), "session", "sendUserIdMessage", token, data);
    }
    /**
     * 根据用户id发送消息
     *
     * @param code
     * @param list
     * @param callBackMethod
     * @param token
     * @param <T>
     */
    @Override
    public <T> void sendUserIdMessage(String code, List<T> list, String callBackMethod, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", SystemCode.getByCode(code).getDesc());
        map.put("callbackMethod", callBackMethod);
        map.put("target_users", list);
        sendUserIdMessage(map, token);
    }

    @Override
    public <T> void sendUserIdMessage(SystemCode code, List<String> targetUsers, String callBackMethod, BaseFactoryParam param, Object... objects) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code.getCode());
        map.put("message", code.getDesc());
        map.put("callbackMethod", callBackMethod);
        map.put("data", objects);
        map.put("send_user", param.getUserInfo());
        if (targetUsers != null && targetUsers.size() > 0) {
            map.put("targetUser", targetUsers);
        }
        sendUserIdMessage(map, param.getToken());
    }

    @Override
    public <T> void sendUserIdMessage(SystemCode code, List<String> targetUsers, String callBackMethod, String token, String sendUser, Object... objects) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code.getCode());
        map.put("message", code.getDesc());
        map.put("callbackMethod", callBackMethod);
        map.put("data", objects);
        map.put("send_user", sendUser);
        if (targetUsers != null && targetUsers.size() > 0) {
            map.put("targetUser", targetUsers);
        }
        sendUserIdMessage(map, token);
    }

    @Override
    public <T> void sendUserIdMessage(SystemCode code, String callBackMethod, BaseFactoryParam param, List<String> targetUsers, List<T> object) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code.getCode());
        map.put("message", code.getDesc());
        map.put("callbackMethod", callBackMethod);
        map.put("data", object);
        if (targetUsers != null && targetUsers.size() > 0) {
            map.put("targetUser", targetUsers);
        }
        sendUserIdMessage(map, param.getToken());
    }
    /**
     * 根据actionid 获取controller queue
     *
     * @param actionId
     * @return
     */
    public Map<String, Object> actionGet(String actionId) {
        Map<String, Object> action = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes(FinalConfigParam.action));
        Map<String, Object> actionMap = (Map<String, Object>) action.get(actionId);
        if (actionMap == null || actionMap.isEmpty()) {
            throw new DataException("get redis actionMap result is null");
        }
        if (!actionMap.containsKey("actionName")) {
            throw new DataException("get redis controllerMap result is not null,but contains key actionName is not exites?");
        }
        Map<String, Object> controller = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes(FinalConfigParam.controller));
        Map<String, Object> controllerMap = (Map<String, Object>) controller.get(actionMap.get("controllerId").toString());
        if (controllerMap == null || controllerMap.isEmpty()) {
            throw new DataException("get redis controllerMap result is null");
        }
        if (!controllerMap.containsKey("controllerName")) {
            throw new DataException("get redis controllerMap result is not null,but contains key controllerName is not exites?");
        }
        Map<String, Object> queue = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes(FinalConfigParam.queue));
        Map<String, Object> queueMap = (Map<String, Object>) queue.get(actionMap.get("queueId").toString());
        if (queueMap == null || queueMap.isEmpty()) {
            throw new DataException("get redis queueMap result is null");
        }
        if (!queueMap.containsKey("queueName")) {
            throw new DataException("get redis controllerMap result is not null,but contains key  queueName is not exites?");
        }
        actionMap.put("action", actionMap.get("actionName"));
        actionMap.put("queue", queueMap.get("queueName"));
        actionMap.put("controller", controllerMap.get("controllerName"));
        return actionMap;
    }

    /**
     * 根据语音id获取语音
     *
     * @param speechId
     * @return
     */
    public Map<String, Object> getSpeech(Integer speechId) {
        Map<String, Object> map = null;
        try {
            Map<String, Object> map1 = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("speech"));
            map = (Map<String, Object>) map1.get(speechId.toString());
            if (map == null) {
                throw new DataException("找不到语言记录:id is :" + speechId + ",redis get speech map result is :" + JsonUtilFastjson.toJSONString(map1));
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }

    public Map<String, Object> getSpeech(String speechId) {
        Map<String, Object> map = null;
        try {
            Map<String, Object> map1 = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("speech"));
            map = (Map<String, Object>) map1.get(speechId);
            if (map == null) {
                throw new DataException("找不到语言记录:id is :" + speechId + ",redis get speech map result is :" + JsonUtilFastjson.toJSONString(map1));
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }

    public Map<String, Object> getBrand(Integer brandTypeFkId) {
        Map<String, Object> map = null;
        try {
            Map<String, Object> map1 = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("brands"));
            map = (Map<String, Object>) map1.get(brandTypeFkId.toString());
            if (map == null) {
                throw new DataException("找不到牌记录:id is :" + brandTypeFkId + ",redis get brands map result is :" + JsonUtilFastjson.toJSONString(map1));
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }

    public Map<String, Object> getBrand(String brandTypeFkId) {
        Map<String, Object> map = null;
        try {
            Map<String, Object> map1 = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("brands"));
            map = (Map<String, Object>) map1.get(brandTypeFkId);
            if (map == null) {
                throw new DataException("找不到牌记录:id is :" + brandTypeFkId + ",redis get brands map result is :" + JsonUtilFastjson.toJSONString(map1));
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }

    public Map<String, Object> getBrand() {
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("brands"));
            if (map == null) {
                throw new DataException("找不到牌记录,redis get brands map result is :" + JsonUtilFastjson.toJSONString(map));
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }
    /**
     * 根据游戏id，获取游戏对象
     *
     * @param parentId
     * @return
     */
    public Map<String, Object> getParent(Integer parentId) {
        Map<String, Object> map = null;
        try {
            Map<String, Object> map1 = (Map<String, Object>) SerializeUtil.unserialize(redisService.getObjBytes("parent"));
            map = (Map<String, Object>) map1.get(parentId.toString());
            if (map == null) {
                throw new DataException("查不到主游戏，parentId is:" + parentId + ",get redis parent result map is:" + JsonUtilFastjson.toJSONString(map1) + ",but reuslt map key not contains:" + parentId);
            }
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }
    /**
     * 根据游戏id获取当前游戏的出牌类型
     *
     * @param parentId
     * @param playType
     * @return
     */
    public Map<String, Object> getPlayType(Integer parentId, Object playType) {
        Map<String, Object> map = null;
        try {
            map = getParent(parentId);
            if (!map.containsKey("playType")) {
                throw new DataException("getParent result contains platType is not key");
            }
            return (Map<String, Object>) ((Map<String, Object>) map.get("playType")).get(playType.toString());
        } catch (DataException e) {
            map = exceptionResult(e.getMessage());
        }
        return map;
    }
}
