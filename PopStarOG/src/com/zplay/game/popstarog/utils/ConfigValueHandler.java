package com.zplay.game.popstarog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Map;

import android.content.Context;

import com.e7studio.android.e7appsdk.utils.LogUtils;

/**
 * 读取application标签下的配置信息，对{@linkplain MetaValueGetter}类中的方法的wrap
 * 
 * @author glzlaohuai
 * @date 2013-4-18
 */
public class ConfigValueHandler {
	private final static String FILE_PAY_CONFIG = "ZplayConfig.xml";
	// channel标示
	private final static String KEY_CHANNEL = "ChannelID";
	// gameID标示
	private final static String KEY_GAMEID = "GameID";
	// channel以及gameID的所处的xml文件的根节点
	private final static String NODE_CHANNEL_GAMEID = "infos";

	// 使用mm渠道还是使用zplay定义的渠道
	private final static String KEY_USE_MM_CHANNEL = "USE_MM_CHANNEL";

	// 保存着channel以及gameID文件的解析结果的缓存
	private static SoftReference<Map<String, String>> XMLCache;
	private final static String TAG = "config_value_getter";

	/**
	 * 构造{@linkplain #XMLCache}
	 * 
	 * @param context
	 */
	private static void buildCache(Context context) {
		if (XMLCache == null || XMLCache.get() == null) {
			LogUtils.v(TAG, "没有channel以及gameID的信息缓存，或者缓存已释放，重新构建缓存");
			Map<String, Object> data = null;
			try {
				data = XMLParser.paraserXML(context.getAssets().open(
						FILE_PAY_CONFIG));
				LogUtils.v(TAG, data.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 保存着channel以及gameID的map结构
			@SuppressWarnings("unchecked")
			Map<String, String> infosMap = (Map<String, String>) data
					.get(NODE_CHANNEL_GAMEID);
			XMLCache = new SoftReference<Map<String, String>>(infosMap);
		}
	}

	/**
	 * 获取渠道（2013-4-19 11:41:55修改）
	 * 开始设计是从manifest中的meta处获取，但是因为设计到孟宪国的批量打包工具，所以此处按照以前的设计来获取
	 * 
	 * 2013-11-27 12:03:38
	 * 修改，因为可恶的mm改变了策略，任何修改以后的包都不能再使用mm进行正常的计费，所以，要想分渠道来统计计费数据
	 * ，只能使用mm的渠道来进行统计，所以
	 * ，这里将channelid改变为可以配置，如果要使用mm计费，那么在配置文件中将字段USE_MM_CHANNEl设置为1 否则就就标注为0
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getChannel(Context context) {
		String channel = null;
		if (useMMChannel(context)) {
			LogUtils.v(TAG, "使用mm渠道");
			InputStream inputStream = RawFileInputStreamReader.readRawFile(
					context, "mmiap.xml");
			if (inputStream == null) {
				channel = "000000";
				LogUtils.v(TAG, "没有mmiap.xml文件，使用默认渠道000000");
			} else {
				Map<String, Object> data = XMLParser.paraserXML(inputStream);
				channel = (String) ((Map<String, Object>) (data.get("data")))
						.get("channel");
			}
		} else {
			buildCache(context);
			return XMLCache.get().get(KEY_CHANNEL).trim();
		}
		return channel;
	}

	/**
	 * 获取gameID(2013-4-19 11:53:36修改)
	 * 开始设计是从manifest中的meta处获取，但是因为设计到孟宪国的批量打包工具，所以此处按照以前的设计来获取
	 * 
	 * @param context
	 * @return
	 */
	public static String getGameID(Context context) {
		buildCache(context);
		return XMLCache.get().get(KEY_GAMEID).trim();
	}

	/**
	 * 是否使用mm配置的渠道
	 * 
	 * @param context
	 * @return
	 */
	public static boolean useMMChannel(Context context) {
		buildCache(context);
		return XMLCache.get().get(KEY_USE_MM_CHANNEL).trim().equals("1") ? true
				: false;
	}

}
