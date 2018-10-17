package com.sucre.properties;

import com.sucre.mainUtil.Info;

public class accounts implements Info{
	static String ADSL=null;
	static String ADSLpassword=null;
	static String ADSLname=null;
	static Info acc=new accounts();
	private accounts() {
		
	}
	@Override
	public void setADSL(String adslID) {
		ADSL=adslID;
		
	}
	@Override
	public void setADSLName(String adslName) {
		ADSLname=adslName;
	}
	@Override
	public void setADSLPass(String adslPass) {
		
		ADSLpassword=adslPass;
	}
	@Override
	public String getADSL() {
		
		return ADSL;
	}
	@Override
	public String getADSLname() {
		
		return ADSLname;
	}
	@Override
	public String getADSLpass() {
		
		return ADSLpassword;
	}
	
	public static Info getInstance() {
		return acc;
	}
	
}
