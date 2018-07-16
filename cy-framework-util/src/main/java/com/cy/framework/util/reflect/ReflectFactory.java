package com.cy.framework.util.reflect;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectFactory {

	public Object invoke(Object bean, String method, List<Object> list, Class<?>... params) {
		Logger logger = Logger.getLogger(ReflectFactory.class);
		Object obj = null;
		try {
			Method methods = bean.getClass().getMethod(method, params);
			if (method != null) {
				obj = methods.invoke(bean, list.toArray());
				logger.warn("ThreadFactory run invoke result is :" + obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("ThreadFactory run Method Exception:" + e.toString());
		}
		return obj;
	}
}
