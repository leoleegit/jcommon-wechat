package org.jcommon.com.wechat.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.data.BroadcastMessage;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.filter.UserFilter;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;

public class TestBaseObject extends TestBase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		System.out.println(new OpenID("1312312").toJson());
//		List<OpenID> open_ids = new ArrayList<OpenID>();
//		open_ids.add(new OpenID("1312312"));
//		open_ids.add(new OpenID("1312313"));
//		BroadcastMessage msg = new BroadcastMessage(MsgType.text,new UserFilter(open_ids));
//		Text text = new Text("hello");
//	    msg.setText(text);
//	    
//	    System.out.println(msg.toJson());
		
		 long timestamp = new Timestamp(System.currentTimeMillis()).getTime(); 
		 System.out.println(timestamp);
		 
	}

}
