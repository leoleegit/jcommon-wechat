package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.utils.MediaType;

public class Mpvideo extends Video {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mpvideo(String json) {
		super(json);
	}

	public Mpvideo(String title, String description) {
		super(title, description);
	}

	public Mpvideo(Video video) {
		super(video.getTitle(), video.getDescription());
	}

	public String getType() {
		return MediaType.mpvideo.name();
	}
}
