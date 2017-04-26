package com.hxh19950701.teachingevaluateclient.fragment;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.SecondTargetExpandableListViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseFragment;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.response.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;

import butterknife.BindView;

public class FirstTargetFragment extends BaseFragment implements ExpandableListView.OnChildClickListener {

    @BindView(R.id.elvFirstTarget)
    /*package*/ ExpandableListView elvFirstTarget;

    private EvaluateFirstTarget firstTarget;
    private float[] score;
    private boolean isReadOnly;
    private OnItemScoreUpdateListener listener;

    public FirstTargetFragment(EvaluateFirstTarget firstTarget, float[] score, boolean isReadOnly, @Nullable OnItemScoreUpdateListener listener) {
        this.firstTarget = firstTarget;
        this.score = score;
        this.listener = listener;
        this.isReadOnly = isReadOnly;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_first_target;
    }

    @Override
    public void initListener() {
        elvFirstTarget.setOnChildClickListener(this);
    }

    @Override
    public void initData() {
        elvFirstTarget.setAdapter(new SecondTargetExpandableListViewAdapter(firstTarget, score, isReadOnly));
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
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, final long id) {
        SecondTargetExpandableListViewAdapter adapter = (SecondTargetExpandableListViewAdapter) parent.getExpandableListAdapter();
        EvaluateThirdTarget thirdTarget = adapter.getChild(groupPosition, childPosition);
        final int itemId = thirdTarget.getId();
        int checkedId = getCheckedId(itemId);
        new MaterialDialog.Builder(getContext()).title("评价该项").items(R.array.checkItem)
                .itemsCallbackSingleChoice(checkedId, (dialog, itemView, which, text) -> {
                    float newScore = getCheckedScore(itemId, which);
                    TextView tvScore = (TextView) v.findViewById(R.id.tvScore);
                    if (listener != null) {
                        listener.onItemScoreUpdate(tvScore, itemId, newScore);
                    }
                    return true;
                }).show();
        return false;
    }

    public interface OnItemScoreUpdateListener {
        void onItemScoreUpdate(TextView textView, int itemId, float newScore);
    }
}