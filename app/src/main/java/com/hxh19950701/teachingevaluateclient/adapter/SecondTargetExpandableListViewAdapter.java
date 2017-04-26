package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateThirdTarget;

import butterknife.BindView;

public class SecondTargetExpandableListViewAdapter extends BaseExpandableListAdapter {

    private EvaluateFirstTarget firstTarget;
    private float[] score;
    private boolean isReadOnly;

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, float[] score) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.isReadOnly = false;
    }

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, float[] score, boolean isReadOnly) {
        this.firstTarget = firstTarget;
        this.score = score;
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
        holder.bindData(thirdTarget, score[thirdTarget.getId()], isReadOnly);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return !isReadOnly;
    }

    public static class GroupViewHolder extends BaseViewHolder {

        @BindView(R.id.tvSecondTargetName)
        /*package*/ TextView tvSecondTargetName;

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(EvaluateSecondTarget secondTarget) {
            tvSecondTargetName.setText(secondTarget.getName());
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {

        @BindView(R.id.tvThirdTargetName)
        /*package*/ TextView tvThirdTargetName;
        @BindView(R.id.tvScore)
        /*package*/ TextView tvScore;
        @BindView(R.id.tvTotalScore)
        /*package*/ TextView tvTotalScore;

        public ChildViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(EvaluateThirdTarget thirdTarget, float score, boolean isReadOnly) {
            Context context = itemView.getContext();
            tvThirdTargetName.setText(thirdTarget.getName());
            tvScore.setText(score < 0.0f ? "未评价" : context.getString(R.string.point, score));
            tvTotalScore.setText(context.getString(R.string.point, (float) thirdTarget.getTotalScore()));
        }
    }
}