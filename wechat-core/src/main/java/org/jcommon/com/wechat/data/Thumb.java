package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.utils.MediaType;

public class Thumb extends Media{
	 public Thumb(String data){
	    super(data);
	    setType(MediaType.thumb.toString());
	 }
	  
	 public Thumb(){
	    super();
	    setType(MediaType.thumb.toString());
	 }
}
