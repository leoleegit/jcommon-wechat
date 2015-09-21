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
import java.util.List;
import org.jcommon.com.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Menus extends JsonObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private List<Button> button;

  public Menus(String json)
  {
    super(json);
    JSONObject jsonO = JsonUtils.getJSONObject(json);
    if (jsonO != null)
      try {
        if (jsonO.has("menu")) {
        	jsonO = JsonUtils.getJSONObject(jsonO.getString("menu"));
        	if (jsonO.has("button")) {
        		JSONArray arr = JsonUtils.getJSONArray(jsonO.getString("button"));
        		button      = new ArrayList<Button>();
	        	for (int index = 0; index < arr.length(); index++) {
	        		button.add(new Button(arr.getString(index)));
	      	    }
	        }
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

  public List<Button> getButton() {
    return this.button;
  }

  public void setButton(List<Button> button) {
    this.button = button;
  }
}