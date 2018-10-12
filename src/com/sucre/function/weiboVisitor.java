package com.sucre.function;

import com.sucre.mainUtil.SinaUtils;

public class weiboVisitor {
	public static void main(String[] args) {
		int threadNum = Integer.parseInt(args.length != 0 && !"".equals(args[0]) ? args[0] : "0");
		System.out.println("开始任务。");
		mainFunction m = new mainFunction(3, false);
		//for (int i = 0; i < threadNum; i++) {
			Thread thread = new Thread(m);
			thread.start();
		//}
		//System.out.println(SinaUtils.CaculateS("1007523439724"));
		
	}
}
