package com.sucre.function;

import java.net.URLEncoder;

import com.sucre.mainUtil.Info;
import com.sucre.mainUtil.MyUtil;
import com.sucre.mainUtil.SinaUtils;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.properties.accounts;

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
			if(!MyUtil.isEmpty(tid)) {tid=tid.replace("\\", "");}
			String newId = MyUtil.midWord("new_tid\":", "}", ret);
			if(!MyUtil.isEmpty(newId)) {newId = newId.indexOf("true") != -1 ? "3" : "2";}
			String con = MyUtil.midWord("confidence\":", "}", ret);
			con = con == null ? "100" : con;

			// System.out.println(tid + "<>" + newId + "<>" + con + Thread.currentThread().getName());
			// System.out.println(ret);
			ret = net.goPost("passport.weibo.com", 443, getCookie(tid, newId, con));
			if (!MyUtil.isEmpty(ret)) {
				String cookie = MyUtil.getAllCookie(ret);
			    //System.out.println("取到cookie"+ Thread.currentThread().getName());

				if (!MyUtil.isEmpty(cookie)) {
					ret = net.goPost("www.weibo.com", 443, getUid(cookie));
					if (!MyUtil.isEmpty(ret)) {
						String uid = MyUtil.midWord("['vid']='", "';", ret);
						

						if (!MyUtil.isEmpty(uid) && !uid.equals("null")) {
							System.out.println("uid:" + uid + Thread.currentThread().getName());
							MyUtil.outPutData("key.txt", "null|null|" + SinaUtils.CaculateS(uid) + "|null|null|" + uid
									+ "|" + MyUtil.midWord("SUB=", ";", cookie) + "|null");
						} else {
							
							if (Thread.currentThread().getName().equals("ip")) {
								System.out.println("要换ip了！");
								MyUtil.cutAdsl(accounts.getInstance().getADSL());
								MyUtil.sleeps(1000);
								MyUtil.connAdsl(accounts.getInstance().getADSL(), accounts.getInstance().getADSLname(),
										accounts.getInstance().getADSLpass());
								System.out.println("换完ip了！");
								MyUtil.sleeps(2000);
							} else {
								MyUtil.sleeps(5000);
							}

						}
					}

				}
			}

		}

		return 0;
	}

	private byte[] getHash() {
		StringBuilder data = new StringBuilder(900);
		String agent = "Chrome/" + MyUtil.getRand(90, 10) + ".0." + MyUtil.getRand(9999, 1000) + "."
				+ MyUtil.getRand(100, 10);
		String temp = "cb=gen_callback&fp=" + MyUtil.makeNonce(32);

		data.append("POST https://passport.weibo.com/visitor/genvisitor HTTP/1.1\r\n");
		data.append("Host: passport.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: " + temp.length() + "\r\n");
		data.append("Cache-Control: max-age=0\r\n");
		data.append("Origin: https://passport.weibo.com\r\n");
		data.append("If-Modified-Since: 0\r\n");
		data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
				+ agent + " Safari/537.36\r\n");
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
		tid=tid==null?"":tid;
		newid=newid==null?"":newid;
		Con=Con==null?"":Con;
		
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