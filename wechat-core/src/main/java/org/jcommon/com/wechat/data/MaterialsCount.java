package org.jcommon.com.wechat.data;

public class MaterialsCount extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int voice_count;
	private int video_count;
	private int image_count;
	private int news_count;

	public MaterialsCount(String json) {
		super(json);
	}

	public int getVoice_count() {
		return voice_count;
	}

	public void setVoice_count(int voice_count) {
		this.voice_count = voice_count;
	}

	public int getVideo_count() {
		return video_count;
	}

	public void setVideo_count(int video_count) {
		this.video_count = video_count;
	}

	public int getImage_count() {
		return image_count;
	}

	public void setImage_count(int image_count) {
		this.image_count = image_count;
	}

	public int getNews_count() {
		return news_count;
	}

	public void setNews_count(int news_count) {
		this.news_count = news_count;
	}

}
