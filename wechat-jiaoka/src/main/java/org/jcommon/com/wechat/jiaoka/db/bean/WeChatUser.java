package org.jcommon.com.wechat.jiaoka.db.bean;

import java.sql.Timestamp;

import org.jcommon.com.wechat.data.User;

public class WeChatUser extends org.jcommon.com.wechat.data.JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int wechat_user_id;
	private int isdelete;
	
	private String subscribe;
	private String openid;
	private String nickname;
	private String sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private String headimgurl;
	private Timestamp subscribe_time;
	private String unionid;
	private String remark;
	private String groupid;
	private String phone_number;
	
	private Timestamp create_time;
	
	
	public WeChatUser(User user){
		this.subscribe = user.getSubscribe();
		this.openid    = user.getOpenid();
		this.nickname  = user.getNickname();
		this.sex       = user.getSex();
		this.language  = user.getLanguage();
		this.city      = user.getCity();
		this.province  = user.getProvince();
		this.country   = user.getCountry();
		this.headimgurl= user.getHeadimgurl();
		this.subscribe_time = new Timestamp(user.getSubscribe_time()*1000);
		this.unionid   = user.getUnionid();
		this.remark    = user.getRemark();
		this.groupid   = user.getGroupid();
		if(user.getCreate_time()!=0)
			this.create_time = new Timestamp(user.getCreate_time());
	}
	
	public WeChatUser(){
		
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Timestamp getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Timestamp subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public void setWechat_user_id(int wechat_user_id) {
		this.wechat_user_id = wechat_user_id;
	}

	public int getWechat_user_id() {
		return wechat_user_id;
	}

	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}

	public int getIsdelete() {
		return isdelete;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPhone_number() {
		return phone_number;
	}
}
