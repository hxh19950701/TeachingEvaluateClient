package com.hxh19950701.teachingevaluateclient.bean.response;

public class Code extends IdRecord {

	private static long ALIVE_TIME = 10 * 60 * 1000;
	
	private String code;
	private long endTime;

	public Code() {
		super();
	}

	public Code(String code) {
		super();
		this.code = code;
		this.endTime = System.currentTimeMillis() + ALIVE_TIME;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isEnable(){
		return System.currentTimeMillis() < endTime;
	}
}
