package org.jcommon.com.wechat.router;

public enum RouterType {
	Callback,
	Token;
	
	public static RouterType getType(String str){
		if(Callback.toString().equalsIgnoreCase(str))
			return Callback;
		else if(Token.toString().equalsIgnoreCase(str))
			return Token;
		return null;
	}
}
