package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;

import java.util.Arrays;

public class SecondTargetExpandableListViewAdapter extends BaseExpandableListAdapter {

    private EvaluateFirstTarget firstTarget;
    private float[] score;
    private float[] averageScore;
    private boolean isReadOnly;

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, float[] score) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.averageScore = new float[score.length];
        this.isReadOnly = false;
        Arrays.fill(averageScore, -1.0f);
    }

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, float[] score, boolean isReadOnly) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.averageScore = new float[score.length];
        this.isReadOnly = isReadOnly;
        Arrays.fill(averageScore, -1.0f);
    }

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, float[] score, float[] averageScore, boolean isReadOnly) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.averageScore = averageScore;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public int getGroupCount() {
        return firstTarget.getSecondTargets().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return firstTarget.getSecondTargets().get(groupPosition).getThirdTargets().size();
    }

    @Override
    public EvaluateSecondTarget getGroup(int groupPosition) {
        return firstTarget.getSecondTargets().get(groupPosition);
    }

    @Override
    public EvaluateThirdTarget getChild(int groupPosition, int childPosition) {
        return firstTarget.getSecondTargets().get(groupPosition).getThirdTargets().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return firstTarget.getSecondTargets().get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return firstTarget.getSecondTargets().get(groupPosition).getThirdTargets().get(childPosition).getId();
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
            convertView.setTag(new GroupViewHolder(convertView));
        }
        GroupViewHolder holder = (GroupViewHolder) convertView.getTag();
        holder.bindData(firstTarget.getSecondTargets().get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_third_target, parent, false);
            convertView.setTag(new ChildViewHolder(convertView));
        }
        ChildViewHolder holder = (ChildViewHolder) convertView.getTag();
        EvaluateThirdTarget thirdTarget = firstTarget.getSecondTargets().get(groupPosition).getThirdTargets().get(childPosition);
        holder.bindData(thirdTarget, score[thirdTarget.getId()], averageScore[thirdTarget.getId()], isReadOnly);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return !isReadOnly;
    }

    private static class GroupViewHolder {

        private TextView tvSecondTargetName;

        public GroupViewHolder(View itemView) {
            tvSecondTargetName = (TextView) itemView.findViewById(R.id.tvSecondTargetName);
        }

        public void bindData(EvaluateSecondTarget secondTarget) {
            tvSecondTargetName.setText(secondTarget.getName());
        }
    }

    private static class ChildViewHolder {

        private TextView tvThirdTargetName;
        private TextView tvAverageScore;
        private TextView tvScore;

        public ChildViewHolder(View itemView) {
            tvThirdTargetName = (TextView) itemView.findViewById(R.id.tvThirdTargetName);
            tvAverageScore = (TextView) itemView.findViewById(R.id.tvAverageScore);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
        }

        public void bindData(EvaluateThirdTarget thirdTarget, float score, float averageScore, boolean isReadOnly) {
            tvThirdTargetName.setText(thirdTarget.getName());
            tvScore.setText(score < 0.0f ? "未评价" : score + "分");
            tvAverageScore.setVisibility(averageScore < 0 ? View.GONE : View.VISIBLE);
            tvAverageScore.setText("平均：" + averageScore + "分");
        }
    }
}