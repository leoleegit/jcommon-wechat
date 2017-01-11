// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.config.ConfigLoader;

public abstract class XmlObject extends org.jcommon.com.util.JsonObject{
	private Logger logger = Logger.getLogger(getClass());
	private String xml;

	public XmlObject(String xml) {
		this.xml = xml;
		xml2Object();
	}

	private void xml2Object() {
		if (this.xml == null)
			return;
		for (Class<?> clazz = getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			Field[] fs = clazz.getDeclaredFields();
			Class<?> type = null;
			for (Field f : fs) {
				String name = f.getName();
				Method m = ConfigLoader.getMethod(getClass(), "set" + name);
				if (m == null) {
					m = ConfigLoader.getMethod(getClass(), "is" + name);
				}
				Document document = null;
				Element root = null;
				try {
					document = DocumentHelper.parseText(this.xml);
					root = document.getRootElement();
				} catch (DocumentException e) {
					this.logger.error(this.xml, e);
				}
				if (root == null)
					return;

				if ((m != null) && (root.element(name) != null))
					try {
						String value = root.element(name).getTextTrim();
						if (notNull(value)) {
							type = f.getType();
							Object args = null;
							if (String.class == type)
								args = value;
							else if ((Integer.class == type)
									|| (Integer.TYPE == type))
								args = Integer.valueOf(value);
							else if ((Boolean.class == type)
									|| (Boolean.TYPE == type))
								args = Boolean.valueOf(Boolean
										.parseBoolean(value));
							else if ((Long.class == type)
									|| (Long.TYPE == type))
								args = Long.valueOf(value);
							else if ((Float.class == type)
									|| (Float.TYPE == type))
								args = Float.valueOf(value);
							try {
								if (args != null)
									m.invoke(this, new Object[] { args });
							} catch (Exception e) {
								this.logger.warn(e);
							}
						}
					} catch (Exception e) {
						this.logger.error("", e);
					}
			}
		}
	}

	public String toXml() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement(this.getClass().getSimpleName());
		try {
			Field[] fs = getClass().getDeclaredFields();
			Class<?> type = null;
			for (Field f : fs) {
				String value = null;
				String name = f.getName();
				type = f.getType();
				Method m = JsonUtils.getMethod(getClass(), "get" + name);
				if (m == null)
					m = JsonUtils.getMethod(getClass(), "is" + name);
				if (m != null) {
					if (String.class == type) {
						value = (String) m.invoke(this, new Object[0]);
						if (value != null)
							value = "<![CDATA[" + value + "]]>";
					} else if ((Integer.class == type)
							|| (Integer.TYPE == type)) {
						int re = ((Integer) m.invoke(this, new Object[0]))
								.intValue();
						if (re != 0)
							value = String.valueOf(re);
					} else if ((Boolean.class == type)
							|| (Boolean.TYPE == type)) {
						value = String.valueOf((Boolean) m.invoke(this,
								new Object[0]));
					} else if ((Long.class == type) || (Long.TYPE == type)) {
						long re = ((Long) m.invoke(this, new Object[0]))
								.longValue();
						if (re != 0L)
							value = String.valueOf(re);
					} else if ((Float.class == type) || (Float.TYPE == type)) {
						float re = ((Float) m.invoke(this, new Object[0]))
								.floatValue();
						if (re != 0.0F)
							value = String.valueOf(re);
					}
				}
				if (value != null) {
					Element e = root.addElement(name);
					e.setText(value);
				}
			}
		} catch (Throwable t) {
			this.logger.error("", t);
		}
		return doc.asXML();
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	private static boolean notNull(String str) {
		return (str != null) && (!"".equals(str));
	}
}