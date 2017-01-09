package com.hxh19950701.teachingevaluateclient.bean.service;


public class EvaluateThirdTarget extends TimeMakableRecord {

	private String name;
	private EvaluateSecondTarget secondTarget;
	private int totalScore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EvaluateSecondTarget getSecondTarget() {
		return secondTarget;
	}

	public void setSecondTarget(EvaluateSecondTarget secondTarget) {
		this.secondTarget = secondTarget;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
}
