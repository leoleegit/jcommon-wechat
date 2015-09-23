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

public class Event extends XmlObject{

  private String ToUserName;
  private String FromUserName;
  private long CreateTime;
  private String MsgType;
  private String Event;
  private String EventKey;
  private String Ticket;
  private float Latitude;
  private float Longitude;
  private float Precision;

  private String signature;
  private String timestamp;
  private String nonce;
  
  private String Status;
  private String MsgID;
  
  public Event(String xml){
	    super(xml);
  }
  
  public Event(String xml,String signature, String timestamp, String nonce){
	    super(xml);
	    this.signature  = signature;
	    this.timestamp  = timestamp;
	    this.nonce      = nonce;
  }


  public String getToUserName(){
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
  public String getMsgType() {
    return this.MsgType;
  }
  public void setMsgType(String msgType) {
    this.MsgType = msgType;
  }
  public String getEvent() {
    return this.Event;
  }
  public void setEvent(String event) {
    this.Event = event;
  }
  public String getEventKey() {
    return this.EventKey;
  }
  public void setEventKey(String eventKey) {
    this.EventKey = eventKey;
  }
  public String getTicket() {
    return this.Ticket;
  }
  public void setTicket(String ticket) {
    this.Ticket = ticket;
  }
  public float getLatitude() {
    return this.Latitude;
  }
  public void setLatitude(float latitude) {
    this.Latitude = latitude;
  }
  public float getLongitude() {
    return this.Longitude;
  }
  public void setLongitude(float longitude) {
    this.Longitude = longitude;
  }
  public float getPrecision() {
    return this.Precision;
  }
  public void setPrecision(float precision) {
    this.Precision = precision;
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

	public void setStatus(String status) {
		Status = status;
	}

	public String getStatus() {
		return Status;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	}

	public String getMsgID() {
		return MsgID;
	}
}