package org.jcommon.com.wechat.jiaoka;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.Button;
import org.jcommon.com.wechat.utils.ButtonType;

public class DefaultMenus extends Menus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CalculatorKey = "CalculatorKey_001";

	public DefaultMenus(){
		List<Button> button = new ArrayList<Button>();
		
	    Button button1 = new Button("计算器",ButtonType.click,CalculatorKey);
//	    button1.addSubButton(new Button("赞一下",ButtonType.click,"V1001_GOOD"));
//	    button1.addSubButton(new Button("视频",ButtonType.view,"http://v.qq.com/"));
//	    button1.addSubButton(new Button("扫码带提示",ButtonType.scancode_push,"rselfmenu_0_0"));
//	    button1.addSubButton(new Button("扫码推事件",ButtonType.scancode_waitmsg,"rselfmenu_0_1"));
//	    
//	    Button button2 = new Button("Button2",null);
//	    button2.addSubButton(new Button("系统拍照发图",ButtonType.pic_sysphoto,"rselfmenu_1_0"));
//	    button2.addSubButton(new Button("拍照或者相册发图",ButtonType.pic_photo_or_album,"rselfmenu_1_1"));
//	    button2.addSubButton(new Button("微信相册发图",ButtonType.pic_weixin,"rselfmenu_1_2"));
//	    
//	    Button button3 = new Button("Button3",null);
//	    button3.addSubButton(new Button("发送位置",ButtonType.location_select,"rselfmenu_2_0"));
//	    button3.addSubButton(new Button("图片",ButtonType.media_id,"fOE5ZBSeEpfRGQcGJjbocObGlHdPTsaC5fL76jiAtDM"));
//	    button3.addSubButton(new Button("图文消息",ButtonType.view_limited,"ksZGzGa8qmgIMzd34UlE3D2nRm8wh7ztKk-Us-E6JEw"));
		
	    button.add(button1);
//	    button.add(button2);
//	    button.add(button3);
		
		super.setButton(button);
	}
}
