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
	private static final String DEFAULT_STORE = System.getProperty("user.dir")+File.separator+"mediastroe";
	
	@Override
	public File createEmptyFile(Media media) {
		// TODO Auto-generated method stub
		String store = System.getProperty(MEDIA_STORE, DEFAULT_STORE);
		checkDir(store);
	    String id    = media.getMedia_id();
	    File file    = new File(store,id);
		return file;
	}
	
	private void checkDir(String dir){
		File file = new File(dir);
		if(!file.exists())
			file.mkdirs();
	}

	/**
	 * return url : MEDIA_URL + / + MD5.getMD5(content_type.getBytes())+ / + filename
	 */
	@Override
	public Media createUrl(Media media) {
		// TODO Auto-generated method stub
		String url = System.getProperty(MEDIA_URL, "/");
		File file  = media.getMedia();
		String content_type = media.getContent_type();
		if(file!=null && file.exists()){
			String file_name = media.getMedia_name()==null?file.getName():media.getMedia_name();
			try {
				if(content_type==null){
					content_type = ContentType.html.type;
					logger.info("content_type is null, will use default content_type:"+content_type);
				}else{
					logger.info(content_type);
				}
				content_type     = MD5.getMD5(content_type.getBytes());
				File type_dir    = new File(file.getParent(),content_type);
				if(!type_dir.exists())
					type_dir.mkdirs();
				File newfile     = new File(type_dir.getAbsolutePath(),file_name);
				boolean rename   = file.renameTo(newfile);
				if(rename)
					logger.info("File Renamed");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
			url = url + content_type + "/" +file_name;
			media.setUrl(url);
		}
		
		return media;
	}

	@Override
	public Media getMediaFromUrl(String url) {
		// TODO Auto-generated method stub
		Media media = new Media();
		if(url!=null){
			String store = System.getProperty(MEDIA_STORE, DEFAULT_STORE);
			String url_start = System.getProperty(MEDIA_URL, "/");
			url              = url.substring(url.indexOf(url_start)+url_start.length());
			
			String content_type = url.substring(0,url.indexOf("/"));
			String file_name    = url.substring(url.indexOf(content_type)+content_type.length()+1);
			
			File type_dir    = new File(store,content_type);
			content_type = ContentType.getContentByType(content_type, true).type;
			logger.info(String.format("store:%s;file_name:%s;content_type:%s", store,file_name,content_type));
			
			if(!type_dir.exists())
				return media;
			File file        = new File(type_dir.getAbsolutePath(),file_name);
			media.setMedia(file);
			media.setMedia_name(file_name);
			media.setContent_type(content_type);
		}
		
		return media;
	}
}
