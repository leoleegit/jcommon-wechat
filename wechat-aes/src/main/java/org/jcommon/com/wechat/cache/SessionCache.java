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
package org.jcommon.com.wechat.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jcommon.com.util.collections.MapStore;
import org.jcommon.com.wechat.WechatSession;

public class SessionCache extends MapStore
{
  private static SessionCache instance = new SessionCache();

  public static SessionCache instance() { return instance; }

  public void addWechatSession(WechatSession session) {
    if (session == null) return;
    String key = session.getWechatID();
    if ((super.hasKey(key)) && (super.getOne(key) != session)) {
      ((WechatSession)super.getOne(key)).shutdown();
    }
    super.addOne(key, session);
  }

  public void removeWechatSession(WechatSession session) {
    if (session == null) return;
    String key = session.getWechatID();
    if (super.hasKey(key))
      super.removeOne(key);
  }
  
  public boolean updateWechatSession(WechatSession session){
	  if (session == null) return false;
	  String key = session.getWechatID();
	  return super.updateOne(key, session);
  }

  public WechatSession getWechatSession(String key)
  {
    if (super.hasKey(key))
      return (WechatSession)super.getOne(key);
    return null;
  }

  public List<WechatSession> getAllWechatSession() {
    List<WechatSession>  sessions = new ArrayList<WechatSession>();
    Map<Object,Object> all = super.getAll();
    for (Iterator<?> i$ = all.values().iterator(); i$.hasNext(); ) {
      Object o = i$.next();
      WechatSession session = (WechatSession)o;
      sessions.add(session);
    }
    return sessions;
  }
}