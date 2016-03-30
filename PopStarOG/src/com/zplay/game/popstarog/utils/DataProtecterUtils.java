package com.zplay.game.popstarog.utils;

import com.zplay.game.popstarog.others.GameConstants;

public class DataProtecterUtils {

	private final static String DES_KEY = GameConstants.DES_KEY;

	public static String intToDesString(int num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	public static String floatToDesString(float num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	public static String longToDesString(long num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	public static int desStringToInt(String desedString) {
		return Integer.parseInt(Encrypter.doDESDecode(desedString, DES_KEY));
	}

	public static float desStringToFloat(String desedString) {
		return Float.parseFloat(Encrypter.doDESDecode(desedString, DES_KEY));
	}

	public static long desStringToLong(String desedString) {
		return Long.parseLong(Encrypter.doDESDecode(desedString, DES_KEY));
	}
}
