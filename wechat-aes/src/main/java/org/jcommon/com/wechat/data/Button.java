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
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.wechat.utils.ButtonType;
import org.json.JSONException;
import org.json.JSONObject;

public class Button extends JsonObject
{
  private String type;
  private String name;
  private String key;
  private String url;
  private List<Button> sub_button;

  public Button(String json)
  {
    super(json);
    JSONObject jsonO = JsonUtils.getJSONObject(json);
    if (jsonO != null)
      try {
        if (jsonO.has("sub_button")) {
          List<Object> list = json2Objects(Articles.class, jsonO.getString("sub_button"));
          resetButton(list);
        }
      }
      catch (JSONException e) {
        logger.error("", e);
      }
  }

  public Button(String name, List<Button> list) {
    this.name = name;
    setSub_button(list);
  }

  private void resetButton(List<Object> list)
  {
    if (list == null) return;
    if (this.sub_button == null) this.sub_button = new ArrayList<Button>();
    for (Iterator<?> i$ = list.iterator(); i$.hasNext(); ) {
      Object o = i$.next();
      this.sub_button.add((Button)o);
    }
    list.clear();
    list = null;
  }

  public Button(String name, ButtonType type, String urlorkey)
  {
    this.name = name;
    this.type = type.toString();
    if (type == ButtonType.click)
      this.key = urlorkey;
    else if (type == ButtonType.view)
      this.url = urlorkey;
  }

  public void addSubButton(Button button) {
    if (this.sub_button == null) this.sub_button = new ArrayList<Button>();
    this.sub_button.add(button);
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKey() {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<Button> getSub_button() {
    return this.sub_button;
  }

  public void setSub_button(List<Button> sub_button) {
    this.sub_button = sub_button;
  }
}