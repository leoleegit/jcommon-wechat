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
package org.jcommon.com.wechat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WechatUtils {
	public static String getDate(){
		Date date = new Date();
		DateFormat ft= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = ft.format(date);
		return dt;
	}
	
	public static String createSignature(String token, String timestamp, String nonce){
		if(token!=null && timestamp!=null && nonce!=null){
			  List<String> arl = new ArrayList<String>(); 
			  arl.add(token);
			  arl.add(timestamp);
			  arl.add(nonce);
			  
			  Collections.sort(arl,new Realize_Comparator());
			  
			  StringBuilder sb = new StringBuilder();
			  for(String s : arl){
				  sb.append(s);
			  }
			  
			  return encryptToSHA(sb.toString());
		}
		return null;
	}
	
	public static String createJsSignature(String noncestr, String jsapi_ticket, String timestamp, String url){
		if(noncestr!=null && jsapi_ticket!=null && timestamp!=null&& url!=null){
			  //jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
 
			  StringBuilder sb = new StringBuilder();
			  sb.append("jsapi_ticket=").append(jsapi_ticket).append("&")
			    .append("noncestr=").append(noncestr).append("&")
			    .append("timestamp=").append(timestamp).append("&")
			    .append("url=").append(url);
			  
			  return encryptToSHA(sb.toString());
		}
		return null;
	}	
	  
    public static String encryptToSHA(String info) {  
      byte[] digesta = null;  
      try {  
          MessageDigest alga = MessageDigest.getInstance("SHA-1");  
          alga.update(info.getBytes());  
          digesta = alga.digest();  
      } catch (NoSuchAlgorithmException e) {  
          e.printStackTrace();  
      }  
      String rs = byte2hex(digesta);  
      return rs;  
    }  

    public static String byte2hex(byte[] b) {  
      String hs = "";  
	  String stmp = "";  
	  for (int n = 0; n < b.length; n++) {  
	      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
	      if (stmp.length() == 1) {  
	          hs = hs + "0" + stmp;  
	          } else {  
	              hs = hs + stmp;  
	          }  
	      }  
	      return hs.toLowerCase();  
	  }
   }
