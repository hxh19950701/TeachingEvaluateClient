package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;

import java.util.ArrayList;
import java.util.List;

public class SecondTargetAdapter extends BaseExpandableListAdapter {

    private List<Second> second = new ArrayList<>();
    private float[] score;

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
        return second.get(groupPosition).third.get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_second_target, parent, false);
        }
        TextView tvSecondTargetName = (TextView) convertView.findViewById(R.id.tvSecondTargetName);
        tvSecondTargetName.setText(second.get(groupPosition).evaluateSecondTarget.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_third_target, parent, false);
        }
        float currentScore = score[second.get(groupPosition).third.get(childPosition).getId()];
        TextView tvThirdTargetName = (TextView) convertView.findViewById(R.id.tvThirdTargetName);
        TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);
        tvThirdTargetName.setText(second.get(groupPosition).third.get(childPosition).getName());
        tvScore.setText(currentScore == -1 ? "未评价" : currentScore + "分");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class Second {
        public EvaluateSecondTarget evaluateSecondTarget;
        public List<EvaluateThirdTarget> third = new ArrayList<>();

        public Second(EvaluateSecondTarget evaluateSecondTarget) {
            this.evaluateSecondTarget = evaluateSecondTarget;
        }
    }


}

