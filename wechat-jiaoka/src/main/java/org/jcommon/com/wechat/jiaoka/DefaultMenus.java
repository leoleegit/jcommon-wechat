package org.jcommon.com.wechat.jiaoka;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.Button;
import org.jcommon.com.wechat.jiaoka.handlers.Agent;
import org.jcommon.com.wechat.jiaoka.handlers.Calculator;
import org.jcommon.com.wechat.jiaoka.handlers.JiaoKa;
import org.jcommon.com.wechat.utils.ButtonType;

public class DefaultMenus extends Menus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public DefaultMenus(){
		List<Button> button = new ArrayList<Button>();
		
	    Button button1 = new Button("计算器",ButtonType.click,Calculator.CalculatorKey);
//	    button1.addSubButton(new Button("赞一下",ButtonType.click,"V1001_GOOD"));
//	    button1.addSubButton(new Button("视频",ButtonType.view,"http://v.qq.com/"));
//	    button1.addSubButton(new Button("扫码带提示",ButtonType.scancode_push,"rselfmenu_0_0"));
//	    button1.addSubButton(new Button("扫码推事件",ButtonType.scancode_waitmsg,"rselfmenu_0_1"));
//	    
//		Button button1 = new Button("介绍信息",null);
//	    button1.addSubButton(new Button("业务介绍",ButtonType.view,"http://dwz6.cn/jiaoka4"));
//	    button1.addSubButton(new Button("预定指引",ButtonType.view,"http://dwz6.cn/jiaoka42"));
//	    button1.addSubButton(new Button("活动规则",ButtonType.view,"http://dwz6.cn/jiaoka43"));
//	    button1.addSubButton(new Button("定点列表",ButtonType.view,"http://dwz6.cn/jiaoka5"));
//	    button1.addSubButton(new Button("推广安排",ButtonType.view,"http://dwz6.cn/jiaoka53"));
//		
//	    Button button2 = new Button("马上预定",null);
//	    button2.addSubButton(new Button("刷卡",ButtonType.view,"http://dwz.cn/jiaoka"));
//	    button2.addSubButton(new Button("交卡",ButtonType.view,"http://form.mikecrm.com/f.php?t=ZXN7Do"));
//	    button2.addSubButton(new Button("买卡",ButtonType.view,"http://form.mikecrm.com/f.php?t=NPYVg3"));
	    
	    Button button2 = new Button("人工客服",ButtonType.click,Agent.AgentKey1);
	    
	    Button button3 = new Button("马上预定",null);
	    button3.addSubButton(new Button("刷卡",ButtonType.click,JiaoKa.JiaoKaKey1));
	    button3.addSubButton(new Button("交卡",ButtonType.click,JiaoKa.JiaoKaKey2));
	    button3.addSubButton(new Button("买卡",ButtonType.click,JiaoKa.JiaoKaKey3));
		
	    button.add(button1);
	    button.add(button2);
	    button.add(button3);
		
		super.setButton(button);
	}
}
