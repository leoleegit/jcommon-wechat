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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class TempFileCache
{
  public static Logger logger = Logger.getLogger(TempFileCache.class);

  public static String root = System.getProperty("java.io.tmpdir");

  public static void saveCache(String file_path, Map<Object, Object> data, String prefix, String suffix, long length) {
    try { 
      String file_name = prefix + suffix;
      file_path = file_path!=null?file_path:root;
      File temp = new File(file_path, file_name);
      logger.info("cache path:"+temp.getAbsolutePath());
      if (!temp.exists())
        temp.createNewFile();
      PrintWriter is = new PrintWriter(temp);
      List<String> temp_list = new ArrayList<String>();
      for (Iterator<?> i$ = data.keySet().iterator(); i$.hasNext(); ) { 
    	Object name = i$.next();
    	temp_list.add(name + "\t:\t" + data.get(name));
      }
      long end = (length==-1 || length>=temp_list.size())?0:temp_list.size()-length;
      for(int i=temp_list.size()-1; i>=end; i--){
    	  is.println(temp_list.get(i));
      }
      temp_list.clear();
      is.close();
    } catch (IOException e)
    {
      logger.error("", e);
    }
  }

  public static void loadCache(String file_path, Map<Object, Object> data, String prefix, String suffix) {
    try {
      file_path = file_path!=null?file_path:root;
      String file_name = prefix + suffix;
      File temp = new File(file_path, file_name);
      logger.info("cache path:"+temp.getAbsolutePath());
      if (!temp.exists())
        return;
      @SuppressWarnings("resource")
	  BufferedReader dr = new BufferedReader(new InputStreamReader(new FileInputStream(temp)));

      for (String line = dr.readLine(); line != null; line = dr.readLine())
        if (line.indexOf("\t:\t") != -1) {
          String name = line.substring(0, line.indexOf("\t:\t"));
          String value = line.substring(line.indexOf("\t:\t") + 3, line.length());
          if ((notNull(name)) && (notNull(value)))
            data.put(name.trim(), value.trim());
        }
    }
    catch (IOException e)
    {
      logger.error("", e);
    }
  }

  private static boolean notNull(String str) {
    return (str != null) && (!"".equals(str));
  }
}