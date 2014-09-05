package com.banker.framework.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class BaseBean implements Serializable {
	
	private static final long serialVersionUID = 5083968312258727725L;

	/**
	 * 用于调试使用，toString 可以得到所有 public 成员变量信息
	 * 
	 * @Override
	 */
	public String toString() {
		Class<?> c = this.getClass();
		Field[] fields = c.getFields();
		StringBuffer buf = new StringBuffer();
		String className = c.getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		buf.append(" Class : {" + className);
		int len = fields.length;
		for (int i = 0; i < len; i++) {
			StringBuffer itemBuf = new StringBuffer();
			String typeName = fields[i].getType().getName();
			typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
			String name = fields[i].getName();
			itemBuf.append(typeName + " : ");
			itemBuf.append("\"" + name + "\" = ");
			try {
				boolean accessFlag = fields[i].isAccessible();
				fields[i].setAccessible(true);
				itemBuf.append(c.getField(name).get(this));
				fields[i].setAccessible(accessFlag);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (i != len - 1) {
				itemBuf.append(",");
			}
			buf.append(itemBuf.toString());
		}
		buf.append("}");
		return buf.toString();
	}

}
