package org.jcommon.com.wechat.media;

import java.io.File;

import org.jcommon.com.wechat.data.Media;

public abstract class MediaFactory {
	public abstract Media getMediaFromUrl(String url);
	public abstract File createEmptyFile(Media media);
	public abstract Media createUrl(Media media);
}
