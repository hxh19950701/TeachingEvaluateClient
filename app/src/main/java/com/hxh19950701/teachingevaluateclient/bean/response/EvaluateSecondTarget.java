package com.hxh19950701.teachingevaluateclient.bean.response;

import java.util.ArrayList;
import java.util.List;

public class EvaluateSecondTarget extends TimeMakableRecord {

    private String name;
    private EvaluateFirstTarget firstTarget;
    private List<EvaluateThirdTarget> thirdTargets = new ArrayList<>();

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

    public List<EvaluateThirdTarget> getThirdTargets() {
        return thirdTargets;
    }

    public void setThirdTargets(List<EvaluateThirdTarget> thirdTargets) {
        this.thirdTargets = thirdTargets;
    }
}
