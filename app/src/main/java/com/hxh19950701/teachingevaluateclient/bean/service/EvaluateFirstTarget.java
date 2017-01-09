package com.hxh19950701.teachingevaluateclient.bean.service;

import java.util.List;

public class EvaluateFirstTarget extends TimeMakableRecord {

    private String name;
    private List<EvaluateSecondTarget> secondTargets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EvaluateSecondTarget> getSecondTargets() {
        return secondTargets;
    }

    public void setSecondTargets(List<EvaluateSecondTarget> secondTargets) {
        this.secondTargets = secondTargets;
    }
}
