package com.hxh19950701.teachingevaluateclient.Bean;

public class EvaluateSecondTarget {
	private int id;
	private String name;//�ڶ�ָ������
	private EvaluateFirstTarget firstTarget;//�����ĵ�һָ������
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

	public EvaluateFirstTarget getFirstTarget() {
		return firstTarget;
	}

	public void setFirstTarget(EvaluateFirstTarget firstTarget) {
		this.firstTarget = firstTarget;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
