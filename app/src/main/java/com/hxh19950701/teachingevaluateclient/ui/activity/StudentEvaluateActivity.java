package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.FirstTargetAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.internet.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.exception.HttpException;

import java.util.Arrays;
import java.util.List;

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

    protected Course course;
    protected List<EvaluateThirdTarget> item;
    protected List<StudentCourseEvaluate> evaluatedItem;

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
        vpFirstTarget = (ViewPager) findViewById(R.id.vpFirstTarget);
    }

    @Override
    protected void initListener() {
        tvLoadFail.setOnClickListener(this);
    }

    protected void showResultDialog() {
        if (currentStatus == STATUS_LOAD_SUCCESS) {
            float totalScore = 0.0f;
            for (int i = 0; i < scoreData.length; ++i) {
                if (scoreData[i] < 0) {
                    SnackBarUtils.showLong(clEvaluate, "存在未评价的项目。");
                    return;
                }
                totalScore += scoreData[i];
            }
            StringBuilder content = new StringBuilder();
            content.append("课程：").append(course.getName()).append("\n");
            content.append("得分：").append(totalScore);
            new MaterialDialog.Builder(this)
                    .title("结果").content(content)
                    .positiveText("提交").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    commitScore();
                }
            }).show();
        }
    }

    protected void commitScore() {
        final MaterialDialog commitDialog = new MaterialDialog.Builder(this)
                .title("正在提交").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).show();
        EvaluateApi.commitEvaluate(courseId, new SimpleServiceCallback<StudentCourseInfo>(clEvaluate) {

            @Override
            public void onAfter() {
                commitDialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(StudentCourseInfo studentCourseInfo) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        courseId = getIntent().getIntExtra("course", 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("教学评价");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCourse();
    }

    private void initCourse() {
        setStatus(STATUS_LOADING);
        if (courseId > 0) {
            CourseApi.getCourse(courseId, new SimpleServiceCallback<Course>(clEvaluate) {

                @Override
                public void onGetDataSuccess(Course course) {
                    initItem();
                }

                @Override
                public void onGetDataFailure(int code, String msg) {
                    super.onGetDataFailure(code, msg);
                    setStatus(STATUS_LOAD_FAIL);
                }

                @Override
                public void onException(String s) {
                    setStatus(STATUS_LOAD_FAIL);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    setStatus(STATUS_LOAD_FAIL);
                }
            });
        }
    }

    protected void initItem() {
        setStatus(STATUS_LOADING);
        EvaluateApi.getAllTargets(new SimpleServiceCallback<List<EvaluateThirdTarget>>(clEvaluate) {

            @Override
            public void onGetDataSuccess(List<EvaluateThirdTarget> data) {
                item = data;
                scoreData = new float[item.size()];
                initEvaluatedItem();
            }

            @Override
            public void onGetDataFailure(int code, String msg) {
                super.onGetDataFailure(code, msg);
                setStatus(STATUS_LOAD_FAIL);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                setStatus(STATUS_LOAD_FAIL);
            }

            @Override
            public void onException(String s) {
                setStatus(STATUS_LOAD_FAIL);
            }
        });
    }

    protected void initEvaluatedItem() {
        setStatus(STATUS_LOADING);
        EvaluateApi.getStudentAllEvaluatedItemsByCourse(courseId, new SimpleServiceCallback<List<StudentCourseEvaluate>>(clEvaluate) {
            @Override
            public void onGetDataSuccess(List<StudentCourseEvaluate> data) {
                evaluatedItem = data;
                Arrays.fill(scoreData, -1);
                if (evaluatedItem != null && evaluatedItem.size() != 0) {
                    for (int i = 0; i < evaluatedItem.size(); ++i) {
                        try {
                            scoreData[evaluatedItem.get(i).getItem().getId()] = evaluatedItem.get(i).getScore();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
                vpFirstTarget.setAdapter(new FirstTargetAdapter(getSupportFragmentManager(), item, scoreData));
                tlFirstTarget.setupWithViewPager(vpFirstTarget);
                setStatus(STATUS_LOAD_SUCCESS);
            }

            @Override
            public void onGetDataFailure(int code, String msg) {
                super.onGetDataFailure(code, msg);
                setStatus(STATUS_LOAD_FAIL);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                setStatus(STATUS_LOAD_FAIL);
            }

            @Override
            public void onException(String s) {
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

    public void saveItemScore(final View view, final long itemId, final float newScore) {
        final int pos = (int) itemId;
        if (scoreData[pos] != newScore) {
            EvaluateApi.updateItemScore(courseId, (int) itemId, newScore, new SimpleServiceCallback<StudentCourseEvaluate>(clEvaluate) {
                @Override
                public void onGetDataSuccess(StudentCourseEvaluate data) {
                    scoreData[pos] = newScore;
                    TextView tv = (TextView) view.findViewById(R.id.tvScore);
                    tv.setText(newScore + "分");
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    SnackBarUtils.showLong(clEvaluate, "很抱歉，由于服务器故障等原因，您的评价没有保存成功。");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_commit) {
            showResultDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}