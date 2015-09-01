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
import org.json.JSONException;
import org.json.JSONObject;

public class Menus extends JsonObject
{
  private List<Button> button;

  public Menus(String json)
  {
    super(json);
    JSONObject jsonO = JsonUtils.getJSONObject(json);
    if (jsonO != null)
      try {
        if (jsonO.has("button")) {
          List<Object> list = json2Objects(Articles.class, jsonO.getString("button"));
          resetButton(list);
        }
      }
      catch (JSONException e) {
        logger.error("", e);
      }
  }

  public Menus()
  {
  }

  public Menus(List<Button> button)
  {
    this.button = button;
  }

  public void addButton(Button button) {
    if (this.button == null) this.button = new ArrayList<Button>();
    this.button.add(button);
  }

  private void resetButton(List<Object> list)
  {
    if (list == null) return;
    if (this.button == null) this.button = new ArrayList<Button>();
    for (Iterator<?> i$ = list.iterator(); i$.hasNext(); ) {
      Object o = i$.next();
      this.button.add((Button)o);
    }
    list.clear();
    list = null;
  }

  public List<Button> getButton() {
    return this.button;
  }

  public void setButton(List<Button> button) {
    this.button = button;
  }
}