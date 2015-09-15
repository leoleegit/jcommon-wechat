package org.jcommon.com.wechat.media;

import java.io.File;

import org.jcommon.com.wechat.data.Media;

public abstract class MediaFactory {
	public abstract File createEmptyFile(Media media);
	public abstract String createUrl(Media media);
}
