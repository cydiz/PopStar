package com.zplay.game.popstarog;

import com.e7studio.android.e7appsdk.utils.E7Config;
import com.zplay.android.sdk.pay.ZplayApplication;

public class PopApplication extends ZplayApplication {
	public void onCreate() {
		super.onCreate();
		E7Config.disableAllLog();
		E7Config.setLogDir("/e7studio/popstar_og/log/");
		E7Config.setSPFileName("PopStar");
	}
}
