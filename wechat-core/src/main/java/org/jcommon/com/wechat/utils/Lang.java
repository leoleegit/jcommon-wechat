package org.jcommon.com.wechat.utils;

public enum Lang {
	zh_CN,
	zh_TW,
	en;
	
	public static Lang getLang(String str){
		if(zh_TW.toString().equals(str))
			return zh_TW;
		else if(en.toString().equals(str))
			return en;
		return zh_CN;
	}
}
