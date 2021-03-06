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
package org.jcommon.com.wechat.data;

public class InMessage extends XmlObject {
	private Media media;
	private User from;
	private String ToUserName;
	private String FromUserName;
	private long CreateTime;
	private String MsgType;
	private long MsgId;
	private String Content;
	private String PicUrl;
	private String MediaId;
	private String Format;
	private String ThumbMediaId;
	private float Location_X;
	private float Location_Y;
	private int Scale;
	private String Label;
	private String Title;
	private String Description;
	private String Url;

	private String signature;
	private String timestamp;
	private String nonce;

	public InMessage(String xml) {
		super(xml);
	}

	public InMessage(String xml, String signature, String timestamp,
			String nonce) {
		super(xml);
		this.signature = signature;
		this.timestamp = timestamp;
		this.nonce = nonce;
	}

	public String getToUserName() {
		return this.ToUserName;
	}

	public void setToUserName(String toUserName) {
		this.ToUserName = toUserName;
	}

	public String getFromUserName() {
		return this.FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return this.CreateTime;
	}

	public void setCreateTime(long createTime) {
		this.CreateTime = createTime;
	}

	public org.jcommon.com.wechat.utils.MsgType getMessageType() {
		return org.jcommon.com.wechat.utils.MsgType.getType(this.MsgType);
	}

	public String getMsgType() {
		return this.MsgType;
	}

	public void setMsgType(String msgType) {
		this.MsgType = msgType;
	}

	public long getMsgId() {
		return this.MsgId;
	}

	public void setMsgId(long msgId) {
		this.MsgId = msgId;
	}

	public String getContent() {
		return this.Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}

	public String getPicUrl() {
		return this.PicUrl;
	}

	public void setPicUrl(String picUrl) {
		this.PicUrl = picUrl;
	}

	public String getMediaId() {
		return this.MediaId;
	}

	public void setMediaId(String mediaId) {
		this.MediaId = mediaId;
	}

	public String getFormat() {
		return this.Format;
	}

	public void setFormat(String format) {
		this.Format = format;
	}

	public String getThumbMediaId() {
		return this.ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.ThumbMediaId = thumbMediaId;
	}

	public float getLocation_X() {
		return this.Location_X;
	}

	public void setLocation_X(float location_X) {
		this.Location_X = location_X;
	}

	public float getLocation_Y() {
		return this.Location_Y;
	}

	public void setLocation_Y(float location_Y) {
		this.Location_Y = location_Y;
	}

	public int getScale() {
		return this.Scale;
	}

	public void setScale(int scale) {
		this.Scale = scale;
	}

	public String getLabel() {
		return this.Label;
	}

	public void setLabel(String label) {
		this.Label = label;
	}

	public String getTitle() {
		return this.Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getUrl() {
		return this.Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public Media getMedia() {
		return this.media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}