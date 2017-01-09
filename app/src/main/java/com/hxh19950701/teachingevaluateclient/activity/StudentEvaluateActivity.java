package com.hxh19950701.teachingevaluateclient.activity;

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
import com.hxh19950701.teachingevaluateclient.adapter.FirstTargetViewPagerAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.internet.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StudentEvaluateActivity extends BaseActivity {

    private static final String TAG = StudentEvaluateActivity.class.getSimpleName();
    private static final int STATUS_LOADING = 0;
    private static final int STATUS_LOAD_FAIL = 1;
    private static final int STATUS_LOAD_SUCCESS = 2;

    private CoordinatorLayout clEvaluate;
    private TextView tvLoading;
    private TextView tvLoadFail;
    private LinearLayout llEvaluate;
    private TabLayout tlFirstTarget;
    private ViewPager vpFirstTarget;

    private Map<Integer, Float> score;
    private int currentStatus = -1;

    protected Course course;
    protected List<EvaluateThirdTarget> item;
    protected List<StudentCourseEvaluate> evaluatedItem;

    private HttpHandler<String> loadCourseHandler;
    private HttpHandler<String> loadEvaluatedItemHandler;

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

    @Override
    protected void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadCourse();
        loadEvaluatedItem();
    }


    private void loadCourse() {
        final int courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            loadCourseHandler = CourseApi.getCourse(
                    courseId, new SimpleServiceCallback<Course>(clEvaluate) {

                        @Override
                        public void onAfter() {
                            refreshStatus();
                        }

                        @Override
                        public void onGetDataSuccess(Course data) {
                            course = data;
                            setTitle("正在评价：" + course.getName());
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
                //initEvaluatedItem();
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

    private void loadEvaluatedItem() {
        final int courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            loadEvaluatedItemHandler = EvaluateApi.getStudentAllEvaluatedItemsByCourse(
                    courseId, new SimpleServiceCallback<List<StudentCourseEvaluate>>(clEvaluate) {

                        @Override
                        public void onAfter() {
                            refreshStatus();
                        }

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
                            vpFirstTarget.setAdapter(new FirstTargetViewPagerAdapter(getSupportFragmentManager(), scoreData));
                            tlFirstTarget.setupWithViewPager(vpFirstTarget);
                        }
                    });
        } else {
            refreshStatus();
        }
    }

    private void refreshStatus() {
        if (loadCourseHandler == null && loadEvaluatedItemHandler == null) {
            setStatus(STATUS_LOAD_FAIL);
        } else if (loadCourseHandler == null || loadEvaluatedItemHandler == null) {
            setStatus(STATUS_LOADING);
        } else if (loadCourseHandler.getState() == HttpHandler.State.FAILURE
                || loadEvaluatedItemHandler.getState() == HttpHandler.State.FAILURE) {
            setStatus(STATUS_LOAD_FAIL);
        } else if (loadCourseHandler.getState() == HttpHandler.State.SUCCESS
                && loadEvaluatedItemHandler.getState() == HttpHandler.State.SUCCESS) {
            setStatus(STATUS_LOAD_SUCCESS);
        } else {
            setStatus(STATUS_LOADING);
        }
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
                loadCourse();
                loadEvaluatedItem();
                break;
        }
    }

    public int getCourseId() {
        return course == null ? -1 : course.getId();
    }

    public CoordinatorLayout getClEvaluate() {
        return clEvaluate;
    }

    public void updateItemScore(final View view, final int itemId, final float newScore) {
        if (scoreData[itemId] != newScore) {
            EvaluateApi.updateItemScore(course.getId(), itemId, newScore,
                    new SimpleServiceCallback<StudentCourseEvaluate>(clEvaluate) {
                        @Override
                        public void onGetDataSuccess(StudentCourseEvaluate data) {
                            scoreData[itemId] = newScore;
                            TextView tv = (TextView) view.findViewById(R.id.tvScore);
                            tv.setText(newScore + "分");
                        }
                    });
        }
    }

    private float getTotalScore() {
        float totalScore = 0.0f;
        for (float score : scoreData) {
            if (score < 0.0f) {
                return Float.MIN_VALUE;
            }
            totalScore += score;
        }
        return totalScore;
    }

    protected void showResultDialog() {
        if (currentStatus == STATUS_LOAD_SUCCESS) {
            float totalScore = getTotalScore();
            if (totalScore >= 0.0f) {
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
            } else {
                SnackBarUtils.showLong(clEvaluate, "存在未评价的项目。");
            }
        }
    }

    protected void commitScore() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在提交").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();
        EvaluateApi.commitEvaluate(course.getId(), new SimpleServiceCallback<StudentCourseInfo>(clEvaluate) {

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(StudentCourseInfo studentCourseInfo) {
                finish();
            }
        });
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