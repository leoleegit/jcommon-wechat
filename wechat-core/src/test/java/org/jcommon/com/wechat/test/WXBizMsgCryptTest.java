package org.jcommon.com.wechat.test;

import org.jcommon.com.wechat.data.Encrypt;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class WXBizMsgCryptTest extends TestBase {

	/**
	 * @param args
	 * @throws AesException 
	 */
	public static void main(String[] args) throws AesException {
		// TODO Auto-generated method stub
		String token = "spotlightWechat";
		String appId = "wxff34bb799fa2e85c";
		String encodingAesKey = "26351XLOrapjfJHoLh11PNk9rmTcCcltSWzGfE832AQ";
		WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
		
		//String signature = "c11d5334bac67db5b33ebb0da1bee99cac1a9760";
		String msgSignature = "a1404b592f0f5d4a25957202ccfdd70524212ffc";
		String timeStamp    = "1443080286";
		String nonce        = "1457065636";
		String postData     = "<xml>    <ToUserName><![CDATA[gh_a0b44f377a19]]></ToUserName>    <Encrypt><![CDATA[8jPk4MZhIlxj9EEAqJ9GD1VLQO7XYenzi8xAgaaOXFFk+dvrhqsd1R7Mo4CBYJHWLJgR3LqokXXMhtg72x0wMximJy+C0HSPwqVghPKpV9/BVFcgyE3+E5cXQwxLulCsq7D2wqOa8LxTPnm6AHsNhlZdJ4lpWvUO9Zosc6q8CJ3qcOSc8xW2wTQ/QwnLVJMYaHynuryYdvSUsDkTinNKSCyY5is2V2bjtguZBFyy8TYqy4ohScP2H/Q/UdL0WHfzpDWiD14rGr077N7eLumUdGOz0H9m6/8TOSafSZK2FroQlAo6jHQX6lunP6WoTAACZfKWW9sml2brrpAdBIfZ23lJpvwOjlyeMeb//kAe1djTVe6zfFSUlxWAt26udja35inugcG7mB1Sxkws8jwp4Szu2PlMb+6zL3LM7J4Ho8Q=]]></Encrypt></xml>";
		//Encrypt crypt       = new Encrypt(postData);
//		System.out.println(crypt.getToUserName());
		//System.out.println(crypt.getEncrypt());
//		
		//String signature = SHA1.getSHA1(token, timeStamp, nonce, crypt.getEncrypt());
		//System.out.println(signature);
		String msg = pc.decryptMsg(msgSignature, timeStamp, nonce, postData); 
		System.out.println(msg);
		
//		String result = pc.decrypt(crypt.getEncrypt());
//		System.out.println(result);
	}

}
