package com.hxh19950701.teachingevaluateclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.SecondTargetExpandableListViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseFragment;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;

public class FirstTargetFragment extends BaseFragment implements ExpandableListView.OnChildClickListener {

    public interface OnItemScoreUpdateListener {
        void onItemScoreUpdate(TextView textView, int itemId, float newScore);
    }

    private ExpandableListView elvFirstTarget;

    private EvaluateFirstTarget firstTarget;
    private float[] score;
    private OnItemScoreUpdateListener listener;
    private boolean isReadOnly;

    public FirstTargetFragment(EvaluateFirstTarget firstTarget, float[] score, boolean isReadOnly, @Nullable OnItemScoreUpdateListener listener) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.listener = listener;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_first_target, container, false);
        elvFirstTarget = (ExpandableListView) view.findViewById(R.id.elvFirstTarget);
        return view;
    }

    @Override
    public void initListener() {
        elvFirstTarget.setOnChildClickListener(this);
    }

    @Override
    public void initData() {
        elvFirstTarget.setAdapter(new SecondTargetExpandableListViewAdapter(firstTarget, score, isReadOnly));
    }

    @Override
    public void onClick(View v) {

    }

    public int getCheckedId(int itemId) {
        float itemScore = score[itemId];
        float totalScore = EvaluateTargetManager.getThirdTargetById(itemId).getTotalScore();
        if (itemScore == totalScore) {
            return 0;
        } else if (itemScore == 0.0) {
            return 1;
        } else if (itemScore > 0) {
            return 2;
        } else {
            return -1;
        }
    }

    public float getCheckedScore(int itemId, int which) {
        float totalScore = EvaluateTargetManager.getThirdTargetById(itemId).getTotalScore();
        switch (which) {
            case 0:
                return totalScore;
            case 1:
                return 0.0f;
            case 2:
                return totalScore / 2.0f;
            default:
                throw new IllegalArgumentException("unexpected checked id : " + which);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, final View v, int groupPosition, int childPosition, final long id) {
        SecondTargetExpandableListViewAdapter adapter = (SecondTargetExpandableListViewAdapter) parent.getExpandableListAdapter();
        EvaluateThirdTarget thirdTarget = adapter.getChild(groupPosition, childPosition);
        final int itemId = thirdTarget.getId();
        int checkedId = getCheckedId(itemId);
        new MaterialDialog.Builder(getContext()).title("评价该项").items(R.array.checkItem)
                .itemsCallbackSingleChoice(checkedId, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        float newScore = getCheckedScore(itemId, which);
                        TextView tvScore = (TextView) v.findViewById(R.id.tvScore);
                        if (listener != null) {
                            listener.onItemScoreUpdate(tvScore, itemId, newScore);
                        }
                        return true;
                    }
                }).show();
        return false;
    }
}