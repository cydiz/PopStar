package com.zplay.game.popstarog.utils.sp;

/**
 * 
 * 常量保持者
 * 
 * @author laohuai
 */

public class ConstantsHolder {
	// 2013-11-27 12:24:35自己定义的sdk版本，每次升级之后需要做修改
	public final static String SDK_VERSION = "1.0.0";
	// 作为launcher的activity
	public final static String LAUNCHER_ACTIVITY = "com.zplay.android.sdk.pay.ZplayActivity";
	public final static String IS_OPEN_GOSHOP = "http://popstar.zplay.cn/ten/open_close_zwb0605.php?gameid=zplay029003&ver=2.3.2";
	// 服务器地址
	public final static String DEFAULT_URL = "http://interface.zplay.cn/smspay/";
	// log信息保存目录
	public final static String DIR_LOG = "/.zplay/paySdk/log/";
	// 请求接口时候单条信息字段分隔符
	public final static String COMMA = ",";
	// 请求接口时候多条信息信息分隔符
	public final static String SEPERACTOR = ";";

	// 三大运营商的缩写
	public final static String CMCC = "CMCC";
	public final static String CHINA_UNICOME = "CU";
	public final static String CHINA_TELECOM = "CT";

	// 计费文件名
	public final static String ZPLAY_CONSUME_FILE = "ZPlayConsumeInfo.xml";
	public final static String ALIPAY_PLUGIN_APK = "alipay_plugin.apk";

	// 保存着启动的activity的类名的文件
	public final static String ZPLAY_LAUNCH_CONFIG_FILE = "ZPlayLaunchConfig";

	/**
	 * 获取channel以及gameID需要用到的信息
	 */
	// @decrept
	// channel以及gameID所在的文件名(刚开始时候设计是在manifest文件中的，但是因为涉及到孟宪国部分的批量打包，他们以前是放在asssets文件夹下的，所以改成了这种方式)
	// 现在mm、斯凯、gameid、channelid等配置都写到了该文件中，所以改名称为如下所写，更加顾名思义，你觉得呢…？
	public final static String FILE_PAY_CONFIG = "ZplayConfig.xml";
	// channel标示
	public final static String KEY_CHANNEL = "ChannelID";
	// gameID标示
	public final static String KEY_GAMEID = "GameID";
	// channel以及gameID的所处的xml文件的根节点
	public final static String NODE_CHANNEL_GAMEID = "infos";

	// mm支付的配置
	public final static String KEY_MM_APP_KEY = "MM_APPKEY";
	public final static String KEY_MM_APP_ID = "MM_APPID";

	// 斯凯支付配置(MS意指MobileSky，MT意指merchat)
	public final static String KEY_MS_MERCHANTID = "MS_MT_ID";
	public final static String KEY_MS_MERCHANTKEY = "MS_MT_KEY";
	public final static String KEY_MS_APPID = "MS_APPID";
	public final static String KEY_MS_APPNAME = "MS_APPNAME";
	public final static String KEY_MS_SYS_ID = "MS_SYS_ID";

	// derive标示
	public final static String KEY_DERIVE = "derive";

	// 使用mm渠道还是使用zplay定义的渠道
	public final static String KEY_USE_MM_CHANNEL = "USE_MM_CHANNEL";

	// 密钥(对于所有的配置信息都使用des方法进行加密，但是程序可能被反编译，所以后期要重新设计新的流程保证收保护的信息不会被非法获取到)
	public final static String DES_KEY = "12345678";

	// 支付宝支付的配置信息
	public final static String ALIPAY_PARTNER = "yqzcuznjcsLPtsRF2qYwPC9Oxmzup9yK";
	public final static String ALIPAY_SELLER = "yqzcuznjcsLPtsRF2qYwPC9Oxmzup9yK";
	public final static String ALIPAY_RSA_PRIVATE = "tPPL+GtUERxVl7AEDqKk9o8wNArHf0icflRCT40q9N8ed4TGjIKmawL4yAMg 0qc9Ip6aAd9LxK66p+zOYkavXrtVAgth+e9wU0TmSgS3PZ9Kl53f5GS7JW/v 50uZzLXq0FY/rGcQqS2eRCAl80YZcODPBsUU87XZ3Wxs6HHeybhQi/quVQWM sdGN7AodKTNbSMD4dyTFzh29LgtTbeP/sFCHzGKVOS7wDRS7ZRweqBng1gXq R6dxy/qmzN5FDBggEjP2XYqaoZY4WF10IeQkSN06hMo44QW3T5PkVTbgBKDt oZ18RD3n+vf72++eU8DVWvYE861QVMUrJ4son4ZzM2o6eksKltGJkpudbZ8Z O/UoKF2hzf1mFbVp7te0FnB1V5SJORqNPblyndtj3A0nXDXlYA+c76SspR4m LjVD3FCRZomINqrEJIz3Adj07lYK9RvxBjL5uQP0ka/oItGDjH4ayAU3xCe9 XDB7sDgedHH6jEKNyQdRzZ7Mb/3W++YRjSwPf/lcvl/U4ixNyeEOk+VkErE7 au5K7P0uYkSSXjh4GnT66QebJ2Ex4RM9wIUN7Na5DU/b1Ta20rlBspVAI+Qp EV5rjZtINpA3A+rHEpEOcP6kOYwbcF5cG36U2QUVtiCZ1kiLgaapSzTCY1Hw 86f6Hwj0fhZ+b9qA5P+hPbGYYGuvEmACipIn3OfKEvg2GcHOs1aZ2gcRX+iI mhj0PIJBNd9fkOFBt+FSnf02OHKTaDbuq2DmvnOLHXXi/hxZDIGkuK/OcLEJ JRmcgGfZ1CqTxyCEAEYpJrW9FTJFD1JvVIiibb1/Q3mLNYqBeleuk7WvQxBf bKwE69SyXxLUfmUO546OkrxWFkIlI5nfReSAPli7etZebcE/0UaHyKIMUDeQ yZOu52LJKkCwsRHOQSZFx4r39UNIEtMGO1t2Bg7SEFlCo0ZdsNfdz/YCeA6h tUyWtdhyzqcIO/BJmC7JlvyRur2XMuOHXzitv9runTPyAU4NV9i9AxrjwD9s mGLkaTIEGlJMsjBXOb4KVFIDgm2kPttnFYr6UnzIVNEMRsZIEKbGPRzBK7Ay S5leZzyqZRZ3eWCawF628MQ0wapg6AkXCOuiS8XMprNqlWuyr3M=";
	public final static String ALIPAY_RSA_PUBLIC = "me6sz1cJZcXnPjRXvJFRevd3Iymezq5GUjiwn/0oLk5tSHjllWHbivJs2HXz XHjiJCqvKXTIy0ZYTPOdvQCBobZHCuThBYQOdix/QvkslUTDJKbQ/3I6cw0A sJlNAht1g5IDg/GOe+o=";

	// 易宝支付的配置信息
	public final static String YEEP_CUSTOMER = "cnyudpdd9HsqrIZIk37Qjg==";
	public final static String YEEP_KEY = "C1LGTj3NWi4dRAogDiSZrco5IX7VVkbiBsv6nFyGHGt8ra0QYLOa5QxIKKLL vTtkbtFw0cEb6+1U5D2H6vT1/Q==";

	// 两次调用支付方法之间最小的间隔(ms)
	public final static long PAY_METHOD_REQUEST_INTERVAL = 1000;

	// 订单上报失败之后最多重试次数
	public final static int ORDER_REPORT_RETRY_THRESHOLD = 5;

	// 联通支付sdk softkey
	public final static String KEY_CU_SOFTKEY = "CU_SOFT_KEY";
	// 联通支付sdk softcode
	public final static String KEY_CU_SOFTCODE = "CU_SOFT_CODE";
	// 联通沃商店sdk cpID
	public final static String CU_WO_ID = "CU_WO_ID";
	// 联通沃商店sdk cpCode
	public final static String CU_WO_CP_CODE = "CU_WO_CP_CODE";
	// 联通沃商店sdk AppId
	public final static String CU_WO_APP_ID = "CU_WO_APP_ID";
	// 联通沃商店sdk APPKey
	public final static String CU_WO_APP_KEY = "CU_WO_APP_KEY";
	// 联通沃商店sdk 获取手机号码
	public final static String CU_WO_PHONE = "CU_WO_PHONE";
	// 联通沃商店sdk 获取公司名称
	public final static String CU_WO_COMPANY = "CU_WO_COMPANY";
	// 是否支持电信支付方式
	public final static String TELE_ON_OFF = "TELE_ON_OFF";// 1为打开电信支付方式
	// 是否支持联通支付方式:1-支持|0-不支持
	public final static String UNICOM_ON_OFF = "UNICOM_ON_OFF";
	// 是否支持移动支付方式：1-支持|0-不支持
	public final static String CMCC_ON_OFF = "CMCC_ON_OFF";

	// 移动短代 目标号码
	public static final String CMCC_SEND_SMS_DES = "10658077016622";
}
