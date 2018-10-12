package com.sucre.function;

import java.net.URLEncoder;

import com.sucre.mainUtil.MyUtil;
import com.sucre.mainUtil.SinaUtils;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;

public class mainFunction extends Thread4Net {

	protected mainFunction(int u, boolean isCircle) {
		super(u, isCircle);

	}

	@Override
	public int doWork(int index) {
		Nets net = new Nets();
		String ret = net.goPost("passport.weibo.com", 443, getHash());
		if (!MyUtil.isEmpty(ret)) {
			String tid = MyUtil.midWord("tid\":\"", "\",\"", ret); 
			String newId = MyUtil.midWord("new_tid\":", "}", ret);
			newId = newId.indexOf("true") != -1 ? "2" : "3";
			String con = MyUtil.midWord("confidence\":", "}", ret);
			con = con == null ? "100" : con;

			System.out.println(tid + "<>" + newId + "<>" + con);

			ret = net.goPost("passport.weibo.com", 443, getCookie(tid, newId, con));
			if (!MyUtil.isEmpty(ret)) {
				String cookie = MyUtil.getAllCookie(ret);
				System.out.println("cookie:" + cookie);
				if (!MyUtil.isEmpty(cookie)) {
					ret = net.goPost("www.weibo.com", 443, getUid(cookie));
					if (!MyUtil.isEmpty(ret)) {
						String uid = MyUtil.midWord("['vid']='", "';", ret);
						System.out.println( "uid:" + uid);
						if (!MyUtil.isEmpty(uid)) {
							MyUtil.outPutData("key.txt", "null|null|" + SinaUtils.CaculateS(uid) + "|null|null|" + uid
									+ "|" + MyUtil.midWord("SUB=", ";", cookie) + "|null");
						}
					}

				}
			}

		}

		return 0;
	}

	private byte[] getHash() {
		StringBuilder data = new StringBuilder(900);
		String temp = "cb=gen_callback&fp=%7B%22os%22%3A%221%22%2C%22browser%22%3A%22Chrome69%2C0%2C3497%2C92%22%2C%22fonts%22%3A%22undefined%22%2C%22screenInfo%22%3A%221920*1080*24%22%2C%22plugins%22%3A%22Portable%20Document%20Format%3A%3Ainternal-pdf-viewer%3A%3AChrome%20PDF%20Plugin%7C%3A%3A"
				+ MyUtil.makeNonce(32)
				+ "%3A%3AChrome%20PDF%20Viewer%7C%3A%3Ainternal-nacl-plugin%3A%3ANative%20Client%22%7D\r\n";
		data.append("POST https://passport.weibo.com/visitor/genvisitor HTTP/1.1\r\n");
		data.append("Host: passport.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: " + temp.length() + "\r\n");
		data.append("Cache-Control: max-age=0\r\n");
		data.append("Origin: https://passport.weibo.com\r\n");
		data.append("If-Modified-Since: 0\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https%3A%2F%2Fweibo.com%2F&domain=.weibo.com&ua=php-sso_sdk_client-0.6.28&_rand=1539225114.5623\r\n");

		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append(temp);
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	private byte[] getCookie(String tid, String newid, String Con) {
		StringBuilder data = new StringBuilder(900);
		data.append("GET https://passport.weibo.com/visitor/visitor?a=incarnate&t=" + URLEncoder.encode(tid) + "&w="
				+ newid + "&c=" + Con + "&gc=&cb=cross_domain&from=weibo&_rand=0." + MyUtil.makeNumber(17)
				+ " HTTP/1.1\r\n");
		data.append("Host: passport.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https%3A%2F%2Fweibo.com%2F&domain=.weibo.com&ua=php-sso_sdk_client-0.6.28&_rand=1539225114.5623\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("Cookie: tid=" + tid + "__" + Con + "\r\n");
		data.append("\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	private byte[] getUid(String cookie) {
		StringBuilder data = new StringBuilder(900);
		data.append("GET https://weibo.com/ HTTP/1.1\r\n");
		data.append("Host: weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Upgrade-Insecure-Requests: 1\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36\r\n");
		data.append(
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
		data.append(
				"Referer: https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https%3A%2F%2Fweibo.com%2F&domain=.weibo.com&ua=php-sso_sdk_client-0.6.28&_rand=1539225114.5623\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("Cookie: " + cookie + "\r\n");
		data.append("\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

}
