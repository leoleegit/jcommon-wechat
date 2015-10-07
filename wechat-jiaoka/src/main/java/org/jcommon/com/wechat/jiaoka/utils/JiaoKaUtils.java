package org.jcommon.com.wechat.jiaoka.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiaoKaUtils {
	public static boolean isNumeric(String str){ 
		if(str==null)return false;
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false; 
	    } 
	    return true; 
	}
	
	public static boolean isInteger(String str){
		if(isNumeric(str)){
			int length = String.valueOf(Integer.MAX_VALUE).length();
			if(str.length()<=length)
				return true;
		}
		return false;
	}
}
