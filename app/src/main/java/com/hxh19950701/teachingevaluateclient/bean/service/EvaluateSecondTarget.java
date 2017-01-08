package com.hxh19950701.teachingevaluateclient.bean.service;

import com.hxh19950701.teachingevaluateclient.base.TimeMakableRecord;

public class EvaluateSecondTarget extends TimeMakableRecord {
	private int id;
	private String name;
	private EvaluateFirstTarget firstTarget;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EvaluateFirstTarget getFirstTarget() {
		return firstTarget;
	}

	public void setFirstTarget(EvaluateFirstTarget firstTarget) {
		this.firstTarget = firstTarget;
	}
}
