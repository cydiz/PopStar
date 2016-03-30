package com.zplay.game.popstarog.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.e7studio.android.e7appsdk.utils.SDHandler;

public class ViewCapturer {

	/**
	 * 截屏
	 * 
	 * @param view
	 * @param filename
	 */
	public static void captureView(View view, String filename) {
		Bitmap image = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.RGB_565);
		view.draw(new Canvas(image));
		try {
			File dir = new File(SDHandler.getSDRootDIR() + "/popstarSina/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File myFile = new File(dir, filename);
			FileOutputStream out = new FileOutputStream(myFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, out); // Output
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
