package com.hxh19950701.teachingevaluateclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.Bean.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.Bean.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;

import java.util.ArrayList;
import java.util.List;

public class SecondTargetAdapter extends BaseExpandableListAdapter {

    List<Second> second = new ArrayList<>();
    float[] score;

    public SecondTargetAdapter(List<EvaluateThirdTarget> evaluateThirdTargets, float[] score) {
        for (int i = 0; i < evaluateThirdTargets.size(); ++i) {
            int pos = find(evaluateThirdTargets.get(i).getSecondTarget().getId());
            if (pos == second.size()) {
                second.add(new Second(evaluateThirdTargets.get(i).getSecondTarget()));
            }
            second.get(pos).third.add(evaluateThirdTargets.get(i));
        }
        this.score = score;
    }

    protected int find(int id) {
        for (int i = 0; i < second.size(); ++i) {
            if (second.get(i).evaluateSecondTarget.getId() == id) {
                return i;
            }
        }
        return second.size();
    }

    @Override
    public int getGroupCount() {
        return second.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return second.get(groupPosition).third.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return second.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return second.get(groupPosition).third.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(TeachingEvaluateClientApplication.getApplication(), R.layout.item_second_target, null);
        }

        ((TextView) convertView.findViewById(R.id.tvSecondTargetName)).setText(second.get(groupPosition).evaluateSecondTarget.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(TeachingEvaluateClientApplication.getApplication(), R.layout.item_third_target, null);
        }
        ((TextView) convertView.findViewById(R.id.tvThirdTargetName)).setText(second.get(groupPosition).third.get(childPosition).getName());
        ((TextView) convertView.findViewById(R.id.tvScore)).setText(score[second.get(groupPosition).third.get(childPosition).getId()] == -1f ? "未评价" : score + "" );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class Second {
        public EvaluateSecondTarget evaluateSecondTarget;
        public List<EvaluateThirdTarget> third = new ArrayList<>();

        public Second(EvaluateSecondTarget evaluateSecondTarget) {
            this.evaluateSecondTarget = evaluateSecondTarget;
        }
    }


}

