package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxh19950701.teachingevaluateclient.Bean.CourseBean;
import com.hxh19950701.teachingevaluateclient.Bean.EvaluatedItemBean;
import com.hxh19950701.teachingevaluateclient.Bean.ItemBean;
import com.hxh19950701.teachingevaluateclient.Bean.SuccessBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.ItemAdapter;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestParams;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Arrays;

public class StudentEvaluateActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected TextView tvCourse;
    protected TextView tvFirstTarget;
    protected TextView tvSecondTarget;
    protected ViewPager vpItem;
    protected CoordinatorLayout clEvaluate;
    protected TextView tvLoading;
    protected TextView tvLoadFail;
    protected LinearLayout llEvaluate;
    protected Button btnNext;
    protected Button btnLast;
    protected RadioGroup rgAnswer;
    protected RadioButton rbYes;
    protected RadioButton rbNo;
    protected RadioButton rbUnclear;

    protected int lastItem = 0;
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
        tvCourse = (TextView) findViewById(R.id.tvCourse);
        tvFirstTarget = (TextView) findViewById(R.id.tvFirstTarget);
        tvSecondTarget = (TextView) findViewById(R.id.tvSecondTarget);
        vpItem = (ViewPager) findViewById(R.id.vpItem);
        clEvaluate = (CoordinatorLayout) findViewById(R.id.clEvaluate);
        tvLoadFail = (TextView) findViewById(R.id.tvLoadFail);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
        llEvaluate = (LinearLayout) findViewById(R.id.llEvaluate);
        btnLast = (Button) findViewById(R.id.btnLast);
        btnNext = (Button) findViewById(R.id.btnNext);
        rgAnswer = (RadioGroup) findViewById(R.id.rgAnswer);
        rbYes = (RadioButton) findViewById(R.id.rbYes);
        rbNo = (RadioButton) findViewById(R.id.rbNo);
        rbUnclear = (RadioButton) findViewById(R.id.rbUnclear);
    }

    @Override
    protected void initListener() {
        vpItem.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                saveData(lastItem, rgAnswer.getCheckedRadioButtonId());
                refreshData(position);
                lastItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        btnNext.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        tvLoadFail.setOnClickListener(this);

    }

    protected void saveData(final int itemId, int radioButtonId) {
        final float score;
        float totalScore = itemBean.getTargetList().get(itemId).getTotalScore();
        switch (radioButtonId) {
            case R.id.rbYes:
                score = totalScore;
                break;
            case R.id.rbNo:
                score = 0.0f;
                break;
            case -1:
                score = -1;
                break;
            default:
                score = totalScore * 0.5f;
                break;
        }
        if (scoreData[itemId] != score) {
            final BaseRequestParams requestParams = new BaseRequestParams();
            requestParams.addQueryStringParameter("action", "updateStudentCourseEvaluateItem");
            requestParams.addQueryStringParameter("courseId", courseId + "");
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
                                scoreData[itemId] = score;
                            } else {
                                SnackBarUtils.showLong(clEvaluate, "很抱歉，由于服务器故障等原因，您的评价没有保存成功。");
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            super.onFailure(e, s);
                            SnackBarUtils.showLong(clEvaluate, "很抱歉，由于网络故障等原因，您的评价没有保存成功。");
                        }
                    });
        }
    }

    protected void refreshData(int position) {
        //初始化UI
        tvFirstTarget.setText("第一指标：" + itemBean.getTargetList().get(position).getSecondTarget().getFirstTarget().getName());
        tvSecondTarget.setText("第二指标：" + itemBean.getTargetList().get(position).getSecondTarget().getName());
        if (position == 0) {
            btnLast.setVisibility(View.INVISIBLE);
        } else {
            btnLast.setVisibility(View.VISIBLE);
        }

        if (position == itemBean.getTargetList().size() - 1) {
            btnNext.setText("完成");
        } else {
            btnNext.setText("下一个");
        }

        //初始化数据
        float score = scoreData[position];
        float totalScore = itemBean.getTargetList().get(position).getTotalScore();
        if (score == totalScore) {
            rgAnswer.check(R.id.rbYes);
        } else if (score == 0) {
            rgAnswer.check(R.id.rbNo);
        } else if (score == -1) {
            rgAnswer.clearCheck();
        } else {
            rgAnswer.check(R.id.rbUnclear);
        }
    }

    protected void showResultDialog() {
        float score = 0;
        for (int i = 0; i < scoreData.length; ++i) {
            if (scoreData[i] < 0) {
                vpItem.setCurrentItem(i);
                SnackBarUtils.showLong(clEvaluate, "你还没评价该项。");
                return;
            }else {
                score += scoreData[i];
            }
        }
        new MaterialDialog.Builder(this)
                .title("完成评教")
                .content("课程\"" + courseBean.getCourse().getName() + "\"最终得分：" + score + "。\n在下面写上你的意见，帮助老师改善教学质量。")
                .cancelable(true)
                .alwaysCallInputCallback()
                .input("", PrefUtils.getString("serverURL", "http://"), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if ((TextUtils.isEmpty(input.toString()) || input.toString().startsWith("http://")) && input.length() > 10) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        }
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        String url = dialog.getInputEditText().getText().toString();
                        if (TextUtils.isEmpty(url)) {
                            PrefUtils.remove("serverURL");
                        } else {
                            PrefUtils.putString("serverURL", dialog.getInputEditText().getText().toString());
                        }
                        TeachingEvaluateClientApplication.initServerURL();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        PrefUtils.remove("serverURL");
                        TeachingEvaluateClientApplication.initServerURL();
                    }
                })
                .show();
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
                                tvCourse.setText(courseBean.getCourse().getName());
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
                            vpItem.setAdapter(new ItemAdapter(getSupportFragmentManager(), itemBean));
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
                            refreshData(0);
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
            case R.id.btnLast:
                vpItem.setCurrentItem(vpItem.getCurrentItem() - 1);
                break;
            case R.id.btnNext:
                if (vpItem.getCurrentItem() == itemBean.getTargetList().size() - 1) {
                    saveData(itemBean.getTargetList().size() - 1, rgAnswer.getCheckedRadioButtonId());
                    showResultDialog();
                } else {
                    vpItem.setCurrentItem(vpItem.getCurrentItem() + 1);
                }
                break;
            case R.id.tvLoadFail:
                initCourse();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentStatus == STATUS_LOAD_SUCCESS) {
            saveData(lastItem, rgAnswer.getCheckedRadioButtonId());
        }
    }
}