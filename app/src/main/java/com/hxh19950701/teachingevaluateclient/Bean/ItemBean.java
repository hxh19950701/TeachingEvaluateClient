package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/7/13.
 */
public class ItemBean {
    protected boolean success;
    protected List<EvaluateThirdTarget> targetList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EvaluateThirdTarget> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<EvaluateThirdTarget> targetList) {
        this.targetList = targetList;
    }
}
