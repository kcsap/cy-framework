package com.cy.framework.util.json;

import com.cy.framework.util.CommonUtils;
import com.cy.framework.util.FinalConfigParam;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.util.Date;


public class JSONDateProcess implements JsonValueProcessor {

	/**
	 * 功能描述：特定类型列表数据的处理
	 * 
	 * @see JsonValueProcessor#processArrayValue(Object,
	 *      JsonConfig)
	 */
	public Object processArrayValue(Object obj, JsonConfig arg1) {
		String str = null;
		try {
			if (obj != null) {
				if (obj instanceof Date) {
					Date date = (Date) obj;
					str = CommonUtils.date2string(date, FinalConfigParam.DATETIME_FORMAT_STYLE);
				} else if (obj instanceof Float || obj instanceof Double) {
					str = String.format("%.3f", obj);
				} else {
					str = obj.toString();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception

		}
		return str;
	}

	/**
	 * 功能描述：特定类型对象的处理
	 *
	 * @see JsonValueProcessor#processObjectValue(String,
	 *      Object, JsonConfig)
	 */
	public Object processObjectValue(String pro, Object obj, JsonConfig jc) {
		String str = null;
		try {
			if (obj != null) {
				if (obj instanceof Date) {
					Date date = (Date) obj;
					str = CommonUtils.date2string(date, FinalConfigParam.DATETIME_FORMAT_STYLE);
				} else if (obj instanceof Float || obj instanceof Double) {
					str = String.format("%.3f", obj);
				} else {
					str = obj.toString();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
