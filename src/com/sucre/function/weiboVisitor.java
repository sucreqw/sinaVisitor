package com.sucre.function;

import com.sucre.mainUtil.Info;
import com.sucre.mainUtil.MyUtil;
import com.sucre.mainUtil.SinaUtils;
import com.sucre.properties.accounts;

public class weiboVisitor {
	
	
		
	
	public static void main(String[] args) {
		int threadNum = Integer.parseInt(args.length != 0 && !"".equals(args[0]) ? args[0] : "1");
	    Info info=accounts.getInstance();
		MyUtil.loadADSL("adsl.properties",accounts.getInstance());
		System.out.println("开始任务。线程数量："+ threadNum);
		System.out.println(info.getADSL() +"<>" + info.getADSLname() +"<>" + info.getADSLpass());
		mainFunction m = new mainFunction(1, true);
		for (int i = 0; i < threadNum; i++) {
			
			Thread thread = new Thread(m);
			if(i==0) {thread.setName("ip");}
			thread.start();
		}
		//System.out.println(SinaUtils.CaculateS("1007523439724"));
		
	}
	
}
