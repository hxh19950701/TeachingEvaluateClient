package com.hxh19950701.teachingevaluateclient.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxh19950701.teachingevaluateclient.Bean.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.SecondTargetAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseFragment;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/7/13.
 */
public class FirstTargetFragment extends BaseFragment {

    protected ExpandableListView elvFirstTarget;
    protected List<EvaluateThirdTarget> evaluateThirdTargetList;
    protected float[] score;

    public FirstTargetFragment(List<EvaluateThirdTarget> evaluateThirdTargetList, float[] score) {
        this.evaluateThirdTargetList = evaluateThirdTargetList;
        this.score = score;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_first_target, null);
        elvFirstTarget = (ExpandableListView) view.findViewById(R.id.elvFirstTarget);
        return view;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        elvFirstTarget.setAdapter(new SecondTargetAdapter(evaluateThirdTargetList, score));
    }

    @Override
    public void onClick(View v) {

    }
}