package org.jcommon.com.wechat.jiaoka.utils;

public enum Presence {
	Available("NLN"),
	UnAvailable("BSY"),
	Busy("FLN");
	
	public String name;
	
	Presence(String name){
		this.name = name;
	}
}
