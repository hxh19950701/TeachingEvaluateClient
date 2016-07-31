package com.hxh19950701.teachingevaluateclient.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.Bean.SuccessBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.SecondTargetAdapter;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseFragment;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.ui.activity.StudentEvaluateActivity;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

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
        elvFirstTarget.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, final View v, int groupPosition, int childPosition, final long id) {
                int checkId = -1;
                final int totalScore = ((EvaluateThirdTarget) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).getTotalScore();
                if (score[(int) id] == totalScore) {
                    checkId = 0;
                } else if (score[(int) id] == 0) {
                    checkId = 1;
                } else if (score[(int) id] > 0) {
                    checkId = 2;
                }
                final int finalCheckId = checkId;
                new MaterialDialog.Builder(getActivity())
                        .title("评价")
                        .items(R.array.checkItem)
                        .itemsCallbackSingleChoice(checkId, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                float newScore = 0;
                                switch (which) {
                                    case 0:
                                        newScore = totalScore;
                                        break;
                                    case 1:
                                        newScore = 0;
                                        break;
                                    case 2:
                                        newScore = totalScore / 2.0f;
                                        break;
                                }
                                System.out.println(newScore);
                                saveData(v, id, newScore);
                                return true;
                            }
                        })
                        .positiveText("提交")
                        .show();
                return true;
            }
        });
    }

    protected void saveData(final View v, final long itemId, final float newScore) {
        if (score[(int) itemId] != newScore) {
            final BaseRequestParams requestParams = new BaseRequestParams();
            requestParams.addQueryStringParameter("action", "updateStudentCourseEvaluateItem");
            requestParams.addQueryStringParameter("courseId", ((StudentEvaluateActivity) getActivity()).getCourseId() + "");
            requestParams.addQueryStringParameter("itemId", itemId + "");
            requestParams.addQueryStringParameter("score", score + "");
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configCurrentHttpCacheExpiry(0);
            httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getEvaluateManager(),
                    requestParams, new BaseRequestCallBack<String>() {
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            super.onSuccess(responseInfo);
                            Gson gson = new Gson();
                            SuccessBean successBean = gson.fromJson(responseInfo.result, SuccessBean.class);
                            if (successBean.isSuccess()) {
                                score[(int) itemId] = newScore;
                                ((TextView) v.findViewById(R.id.tvScore)).setText(newScore + "分");
                            } else {
                                SnackBarUtils.showLong(((StudentEvaluateActivity) getActivity()).getClEvaluate(), "很抱歉，由于服务器故障等原因，您的评价没有保存成功。");
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            super.onFailure(e, s);
                            SnackBarUtils.showLong(((StudentEvaluateActivity) getActivity()).getClEvaluate(), "很抱歉，由于网络故障等原因，您的评价没有保存成功。");
                        }
                    });
        }
    }

    @Override
    public void initData() {
        elvFirstTarget.setAdapter(new SecondTargetAdapter(evaluateThirdTargetList, score));
    }

    @Override
    public void onClick(View v) {

    }
}