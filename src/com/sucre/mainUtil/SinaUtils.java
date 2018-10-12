package com.sucre.mainUtil;

public class SinaUtils {
	/**
	 * 新浪的s算法
	 * @param uid
	 * @return
	 */
	public static String CaculateS(String uid) {
		String md5=MyUtil.MD5(uid+"5l0WXnhiY4pJ794KIJ7Rw5F45VXg9sjo");
		//System.out.println(md5);
		String temp=md5.substring(1, 2);
		temp+=md5.substring(5, 6);
		temp+=md5.substring(2, 3);
		temp+=md5.substring(10, 11);
		temp+=md5.substring(17, 18);
		temp+=md5.substring(9, 10);
		temp+=md5.substring(25, 26);
		temp+=md5.substring(27, 28);
		return temp; 
	}
}
