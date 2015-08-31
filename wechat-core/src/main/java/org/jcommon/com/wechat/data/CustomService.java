package org.jcommon.com.wechat.data;

import java.security.NoSuchAlgorithmException;

import org.jcommon.com.wechat.utils.MD5;

public class CustomService extends JsonObject{
	private String kfaccount;
	private String nickname;
	private String passwd;
	
	private String kf_nick;
	private String kf_id;
	private String kf_headimg;

	public String getKf_nick() {
		return kf_nick;
	}

	public void setKf_nick(String kf_nick) {
		this.kf_nick = kf_nick;
		setNickname(kf_nick);
	}

	public String getKf_id() {
		return kf_id;
	}

	public void setKf_id(String kf_id) {
		this.kf_id = kf_id;
	}

	public String getKf_headimg() {
		return kf_headimg;
	}

	public void setKf_headimg(String kf_headimg) {
		this.kf_headimg = kf_headimg;
	}

	public CustomService(String kfaccount){
		this.kfaccount = kfaccount;
	}
	
	public CustomService(String kfaccount, String nickname, String passwd){
		setNickname(nickname);
		setKfaccount(kfaccount);
		setPasswd(passwd);
	}
	
	public void setKfaccount(String kfaccount) {
		this.kfaccount = kfaccount;
	}

	public String getKfaccount() {
		return kfaccount;
	}

	public void setPasswd(String passwd) {
		if(passwd!=null)
			try {
				passwd = MD5.getMD5(passwd.getBytes());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		this.passwd = passwd;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
}
