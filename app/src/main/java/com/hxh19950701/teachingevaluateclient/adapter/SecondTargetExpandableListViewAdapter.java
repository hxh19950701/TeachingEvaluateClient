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

import java.util.Map;

public class SecondTargetExpandableListViewAdapter extends BaseExpandableListAdapter {

    private EvaluateFirstTarget firstTarget;
    private Map<Integer, Float> score;

    public SecondTargetExpandableListViewAdapter(EvaluateFirstTarget firstTarget, Map<Integer, Float> score) {
        this.firstTarget = firstTarget;
        this.score = score;
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
        Float currentScore = score.get(thirdTarget.getId());
        holder.bindData(thirdTarget, currentScore == null ? -1 : currentScore);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
        private TextView tvScore;

        public ChildViewHolder(View itemView) {
            TextView tvThirdTargetName = (TextView) itemView.findViewById(R.id.tvThirdTargetName);
            TextView tvScore = (TextView) itemView.findViewById(R.id.tvScore);
        }

        public void bindData(EvaluateThirdTarget thirdTarget, float score) {
            tvThirdTargetName.setText(thirdTarget.getName());
            tvScore.setText(score == -1 ? "未评价" : score + "分");
        }
    }
}