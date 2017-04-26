package com.hxh19950701.teachingevaluateclient.bean.response;

public class TimeMakableRecord extends StringMakableRecord {

	protected String createTime;
	protected String updateTime;

	public final String getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public final String getUpdateTime() {
		return updateTime;
	}

	public final void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
