package com.hxh19950701.teachingevaluateclient.Bean;

public class EvaluateThirdTarget {
	
	private int id;		
	private String name;//����ָ������
	private EvaluateSecondTarget secondTarget;//�����ĵڶ�ָ��
	private int totalScore;//����
	private String mark;

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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
