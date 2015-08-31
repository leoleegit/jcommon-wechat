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

import java.lang.reflect.Field;
import org.apache.log4j.Logger;

public enum ErrorType
{
  error_3(-3, "多媒体 不能为空"),	
  error_2(-2, "多媒体上传错误"),	
  error_1(-1, "系统繁忙"), 
  error0(0, "请求成功"), 
  error40001(40001, "获取access_token时AppSecret错误，或者access_token无效"), 
  error40002(40002, "不合法的凭证类型"), 
  error40003(40003, "不合法的OpenID"), 
  error40004(40004, "不合法的媒体文件类型"), 
  error40005(40005, "不合法的文件类型"), 
  error40006(40006, "不合法的文件大小"), 
  error40007(40007, "不合法的媒体文件id"), 
  error40008(40008, "不合法的消息类型"), 
  error40009(40009, "不合法的图片文件大小"), 
  error40010(40010, "不合法的语音文件大小"), 
  error40011(40011, "不合法的视频文件大小"), 
  error40012(40012, "不合法的缩略图文件大小"), 
  error40013(40013, "不合法的APPID"), 
  error40014(40014, "不合法的access_token"), 
  error40015(40015, "不合法的菜单类型"), 
  error40016(40016, "不合法的按钮个数"), 
  error40017(40017, "不合法的按钮个数"), 
  error40018(40018, "不合法的按钮名字长度"), 
  error40019(40019, "不合法的按钮KEY长度"), 
  error40020(40020, "不合法的按钮URL长度"), 
  error40021(40021, "不合法的菜单版本号"), 
  error40022(40022, "不合法的子菜单级数"), 
  error40023(40023, "不合法的子菜单按钮个数"), 
  error40024(40024, "不合法的子菜单按钮类型"), 
  error40025(40025, "不合法的子菜单按钮名字长度"), 
  error40026(40026, "不合法的子菜单按钮KEY长度"), 
  error40027(40027, "不合法的子菜单按钮URL长度"), 
  error40028(40028, "不合法的自定义菜单使用用户"), 
  error40029(40029, "不合法的oauth_code"), 
  error40030(40030, "不合法的refresh_token"), 
  error40031(40031, "不合法的openid列表"), 
  error40032(40032, "不合法的openid列表长度"), 
  error40033(40033, "不合法的请求字符，不能包含\\uxxxx格式的字符"), 
  error40035(40035, "不合法的参数"), 
  error40038(40038, "不合法的请求格式"), 
  error40039(40039, "不合法的URL长度"), 
  error40050(40050, "不合法的分组id"), 
  error40051(40051, "分组名字不合法"), 
  error41001(40052, "缺少access_token参数"), 
  error41002(41002, "缺少appid参数"), 
  error41003(41003, "缺少refresh_token参数"), 
  error41004(41004, "缺少secret参数"), 
  error41005(41005, "缺少多媒体文件数据"), 
  error41006(41006, "缺少media_id参数"), 
  error41007(41007, "缺少子菜单数据"), 
  error41008(41008, "缺少oauth code"), 
  error41009(41009, "缺少openid"), 
  error42001(42001, "access_token超时"), 
  error42002(42002, "refresh_token超时"), 
  error42003(42003, "oauth_code超时"), 
  error43001(43001, "需要GET请求"), 
  error43002(43002, "需要POST请求"), 
  error43003(43003, "需要HTTPS请求"), 
  error43004(43004, "需要接收者关注"), 
  error43005(43005, "需要好友关系"), 
  error44001(44001, "多媒体文件为空"), 
  error44002(44002, "POST的数据包为空"), 
  error44003(44003, "图文消息内容为空"), 
  error44004(44004, "文本消息内容为空"), 
  error45001(45001, "多媒体文件大小超过限制"), 
  error45002(45002, "消息内容超过限制"), 
  error45003(45003, "标题字段超过限制"), 
  error45004(45004, "描述字段超过限制"), 
  error45005(45005, "链接字段超过限制"), 
  error45006(45006, "图片链接字段超过限制"), 
  error45007(45007, "语音播放时间超过限制"), 
  error45008(45008, "图文消息超过限制"), 
  error45009(45009, "接口调用超过限制"), 
  error45010(45010, "创建菜单个数超过限制"), 
  error45015(45015, "回复时间超过限制"), 
  error45016(45016, "系统分组，不允许修改"), 
  error45017(45017, "分组名字过长"), 
  error45018(45018, "分组数量超过上限"), 
  error46001(46001, "不存在媒体数据"), 
  error46002(46002, "不存在的菜单版本"), 
  error46003(46003, "不存在的菜单数据"), 
  error46004(46004, "不存在的用户"), 
  error47001(47001, "解析JSON/XML内容错误"), 
  error48001(48001, "api功能未授权"), 
  error50001(50001, "用户未授权该api;"), 
  errornull(-10000, "没有相应的错误");

  public int error;
  public String message;
  public static Logger logger = Logger.getLogger(Error.class);

  private ErrorType(int error, String message) {
    this.error = error;
    this.message = message;
  }

  public static ErrorType getError(int error) {
    Field[] fs = Error.class.getDeclaredFields();
    for (Field f : fs) {
      if (f.getType() == Error.class) {
        f.setAccessible(true);
        try {
          Object value = f.get(errornull);
          int e = ((ErrorType)value).error;
          if (e == error)
            return (ErrorType)value;
        }
        catch (IllegalArgumentException e) {
          logger.error("", e);
        }
        catch (IllegalAccessException e) {
          logger.error("", e);
        }
      }
    }
    return errornull;
  }
}