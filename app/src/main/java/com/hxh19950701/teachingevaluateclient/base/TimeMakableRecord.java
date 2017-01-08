package com.hxh19950701.teachingevaluateclient.base;

import java.sql.Timestamp;

public class TimeMakableRecord extends StringMakableRecord {

	private Timestamp createTime;
	private Timestamp updateTime;

	public final Timestamp getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public final Timestamp getUpdateTime() {
		return updateTime;
	}

	public final void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public final void notifyCreated() {
		if (createTime == null) {
			createTime = new Timestamp(System.currentTimeMillis());
			notifyUpdated();
		}
		else {
			throw new RuntimeException("This data has been created!");
		}
	}

	public final void notifyUpdated() {
		if(createTime == null){
			createTime = new Timestamp(System.currentTimeMillis());
		}
		updateTime = new Timestamp(System.currentTimeMillis());
	}
}
