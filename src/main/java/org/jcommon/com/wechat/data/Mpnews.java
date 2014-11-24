package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.utils.MediaType;

public class Mpnews extends Media{
  public Mpnews(String data)
  {
    super(data);
    setType(MediaType.mpnews.toString());
  }
  
  public Mpnews()
  {
    super();
  }
}
