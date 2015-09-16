package org.jcommon.com.wechat.media;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.utils.MD5;

public class DefaultMediaFactory extends MediaFactory{
	private Logger logger = Logger.getLogger(getClass());
	private static final String MEDIA_STORE="wechat.media.store";
	private static final String MEDIA_URL  ="wechat.media.url";
	
	@Override
	public File createEmptyFile(Media media) {
		// TODO Auto-generated method stub
		String store = System.getProperty(MEDIA_STORE, System.getProperty("user.dir"));
	    String id    = media.getMedia_id();
	    File file    = new File(store,id);
		return file;
	}

	@Override
	public Media createUrl(Media media) {
		// TODO Auto-generated method stub
		String url = System.getProperty(MEDIA_URL, "/");
		File file  = media.getMedia();
		String content_type = media.getContent_type();
		if(file!=null && file.exists()){
			String file_name = file.getName();
			try {
				if(content_type==null){
					content_type = ContentType.html.type;
					logger.info("content_type is null, will use default content_type:"+content_type);
				}else{
					logger.info(content_type);
				}
				file_name        = file_name + "-" + MD5.getMD5(content_type.getBytes());
				File newfile     = new File(file.getAbsolutePath(),file_name);
				boolean rename   = file.renameTo(newfile);
				if(rename)
					logger.info("File Renamed");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
			url = url + file_name;
			media.setUrl(url);
		}
		
		return media;
	}

	@Override
	public Media getMediaFromUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}
}
