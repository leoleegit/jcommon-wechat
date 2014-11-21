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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jcommon.com.util.Json2Object;
import org.jcommon.com.util.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class News extends JsonObject
{
  private List<Articles> articles;
  public final static int max_size = 10;
  
  public News(String data)
  {
    super(data);

    JSONObject jsonO = JsonUtils.getJSONObject(data);
    if (jsonO != null)
      try {
        if (jsonO.has("articles")) {
          List<Object> list = Json2Object.json2Objects(Articles.class, jsonO.getString("articles"));
          resetArticles(list);
        }
      }
      catch (JSONException e) {
        Json2Object.logger.error("", e);
      }
  }
  
  public News(List<Articles> articles)
  {
    super();
    setArticles(articles);
  }

  private void resetArticles(List<Object> list)
  {
    if (list == null) return;
    if (this.articles == null) this.articles = new ArrayList<Articles>();
    for (Iterator<?> i$ = list.iterator(); i$.hasNext(); ) {
      Object o = i$.next();
      this.articles.add((Articles)o);
    }
    list.clear();
    list = null;
  }

  public List<Articles> getArticles() {
    return this.articles;
  }

  public void setArticles(List<Articles> articles) {
    this.articles = articles;
  }
}