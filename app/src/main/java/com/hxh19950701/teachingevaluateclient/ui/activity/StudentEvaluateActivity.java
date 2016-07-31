package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxh19950701.teachingevaluateclient.Bean.CourseBean;
import com.hxh19950701.teachingevaluateclient.Bean.EvaluatedItemBean;
import com.hxh19950701.teachingevaluateclient.Bean.ItemBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.FirstTargetAdapter;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Arrays;

public class StudentEvaluateActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected CoordinatorLayout clEvaluate;
    protected TextView tvLoading;
    protected TextView tvLoadFail;
    protected LinearLayout llEvaluate;
    protected TabLayout tlFirstTarget;
    protected ViewPager vpFirstTarget;

    protected int courseId;
    protected float[] scoreData;
    protected int currentStatus = -1;

    protected CourseBean courseBean;
    protected ItemBean itemBean;
    protected EvaluatedItemBean evaluatedItemBean;

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_LOAD_FAIL = 1;
    private static final int STATUS_LOAD_SUCCESS = 2;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_evaluate);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        clEvaluate = (CoordinatorLayout) findViewById(R.id.clEvaluate);
        tvLoadFail = (TextView) findViewById(R.id.tvLoadFail);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
        llEvaluate = (LinearLayout) findViewById(R.id.llEvaluate);
        tlFirstTarget = (TabLayout) findViewById(R.id.tlFirstTarget);
        vpFirstTarget= (ViewPager) findViewById(R.id.vpFirstTarget);
    }

    @Override
    protected void initListener() {
        tvLoadFail.setOnClickListener(this);
    }



    protected void showResultDialog() {
    }

    @Override
    protected void initDate() {
        courseId = getIntent().getIntExtra("course", 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("教学评价");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCourse();
    }

    private void initCourse() {
        setStatus(STATUS_LOADING);
        if (courseId > 0) {
            final BaseRequestParams requestParams = new BaseRequestParams();
            requestParams.addQueryStringParameter("action", "getCourse");
            requestParams.addQueryStringParameter("courseId", courseId + "");
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configCurrentHttpCacheExpiry(0);
            httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getCourseManager(),
                    requestParams, new BaseRequestCallBack<String>() {
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            super.onSuccess(responseInfo);
                            Gson gson = new Gson();
                            courseBean = gson.fromJson(responseInfo.result, CourseBean.class);
                            if (courseBean.isSuccess()) {
                                initItem();
                            } else {
                                setStatus(STATUS_LOAD_FAIL);
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            super.onFailure(e, s);
                            setStatus(STATUS_LOAD_FAIL);
                        }
                    });
        }
    }

    protected void initItem() {
        setStatus(STATUS_LOADING);
        final BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "getAllTargets");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getEvaluateManager(),
                requestParams, new BaseRequestCallBack<String>() {
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        itemBean = gson.fromJson(responseInfo.result, ItemBean.class);
                        if (itemBean.isSuccess()) {
                            scoreData = new float[itemBean.getTargetList().size()];
                            initEvaluatedItem();
                        } else {
                            setStatus(STATUS_LOAD_FAIL);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        setStatus(STATUS_LOAD_FAIL);
                    }
                });
    }

    protected void initEvaluatedItem() {
        setStatus(STATUS_LOADING);
        final BaseRequestParams requestParams = new BaseRequestParams();
        requestParams.addQueryStringParameter("action", "getStudentAllEvaluatedItemsByCourse");
        requestParams.addQueryStringParameter("courseId", courseBean.getCourse().getId() + "");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getEvaluateManager(),
                requestParams, new BaseRequestCallBack<String>() {
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        evaluatedItemBean = gson.fromJson(responseInfo.result, EvaluatedItemBean.class);
                        if (evaluatedItemBean.isSuccess()) {
                            Arrays.fill(scoreData, -1);
                            if (evaluatedItemBean.getItem() != null && evaluatedItemBean.getItem().size() != 0) {
                                for (int i = 0; i < evaluatedItemBean.getItem().size(); ++i) {
                                    try {
                                        scoreData[evaluatedItemBean.getItem().get(i).getItem().getId()]
                                                = evaluatedItemBean.getItem().get(i).getScore();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            setStatus(STATUS_LOAD_SUCCESS);
                            vpFirstTarget.setAdapter(
                                    new FirstTargetAdapter(getSupportFragmentManager(), itemBean.getTargetList(), scoreData));
                            tlFirstTarget.setupWithViewPager(vpFirstTarget);
                        } else {
                            setStatus(STATUS_LOAD_FAIL);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        setStatus(STATUS_LOAD_FAIL);
                    }
                });
    }

    private void setStatus(int status) {
        if (currentStatus != status) {
            currentStatus = status;
            tvLoadFail.setVisibility(currentStatus == STATUS_LOAD_FAIL ? View.VISIBLE : View.GONE);
            tvLoading.setVisibility(currentStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
            llEvaluate.setVisibility(currentStatus == STATUS_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoadFail:
                initCourse();
                break;
        }
    }

    public int getCourseId() {
        return courseId;
    }

    public CoordinatorLayout getClEvaluate() {
        return clEvaluate;
    }
}