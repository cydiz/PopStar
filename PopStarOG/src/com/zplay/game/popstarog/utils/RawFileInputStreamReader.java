package com.zplay.game.popstarog.utils;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;

/**
 * 读取apk包中某一个文件
 * 
 * @author glzlaohuai
 * @date 2013-11-27
 */
public class RawFileInputStreamReader {

	/**
	 * 读取apk包中的某一个文件
	 * 
	 * @param file
	 * @return
	 */
	public static InputStream readRawFile(Context context, String file) {
		InputStream inputStream = null;
		
		try {
			@SuppressWarnings("resource")
			ZipFile zipFile = new ZipFile(
					context.getApplicationInfo().publicSourceDir);
			ZipEntry zentry = zipFile.getEntry(file);
			inputStream = zipFile.getInputStream(zentry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}

}
