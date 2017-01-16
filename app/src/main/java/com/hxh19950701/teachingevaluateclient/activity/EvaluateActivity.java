package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.FirstTargetViewPagerAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.CourseAndEvaluatedItem;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.service.TeacherCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.fragment.FirstTargetFragment;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.Arrays;
import java.util.List;

public class EvaluateActivity extends BaseActivity implements FirstTargetFragment.OnItemScoreUpdateListener {

    private static final String TAG = EvaluateActivity.class.getSimpleName();

    private final int THIRD_TARGET_COUNT = EvaluateTargetManager.getThirdTargets().size();

    private CoordinatorLayout clEvaluate;
    private TabLayout tlFirstTarget;
    private ViewPager vpFirstTarget;

    private Course course = null;
    private int identity = -1;
    private boolean isReadOnly = false;
    private float score[] = new float[THIRD_TARGET_COUNT];

    {
        Arrays.fill(score, -1.0f);
    }

    public static Intent newIntent(Context context, int courseId, int identity, boolean isReadOnly) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        } else if (identity != Constant.IDENTITY_STUDENT && identity != Constant.IDENTITY_TEACHER) {
            throw new IllegalArgumentException("Invalid identity : " + identity);
        }
        Intent intent = new Intent(context, EvaluateActivity.class);
        intent.putExtra(Constant.KEY_COURSE_ID, courseId);
        intent.putExtra(Constant.KEY_IDENTITY, identity);
        intent.putExtra(Constant.KEY_READ_ONLY, isReadOnly);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_evaluate);
        clEvaluate = (CoordinatorLayout) findViewById(R.id.clEvaluate);
        tlFirstTarget = (TabLayout) findViewById(R.id.tlFirstTarget);
        vpFirstTarget = (ViewPager) findViewById(R.id.vpFirstTarget);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        loadEvaluatedItem();
    }

    private void loadEvaluatedItem() {
        Intent intent = getIntent();
        identity = intent.getIntExtra(Constant.KEY_IDENTITY, -1);
        isReadOnly = intent.getBooleanExtra(Constant.KEY_READ_ONLY, false);
        invalidateOptionsMenu();
        int courseId = intent.getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            switch (identity) {
                case Constant.IDENTITY_STUDENT:
                    EvaluateApi.getStudentAllEvaluatedItemsByCourse(courseId,
                            new SimpleServiceCallback<CourseAndEvaluatedItem>(clEvaluate) {
                                @Override
                                public void onGetDataSuccess(CourseAndEvaluatedItem data) {
                                    initStudentEvaluatedItem(data);
                                }
                            });
                    break;
                case Constant.IDENTITY_TEACHER:
                    EvaluateApi.getTeacherAllEvaluatedItemsByCourse(courseId,
                            new SimpleServiceCallback<List<TeacherCourseEvaluate>>(clEvaluate) {
                                @Override
                                public void onGetDataSuccess(List<TeacherCourseEvaluate> teacherCourseEvaluates) {

                                }
                            });
                    break;
                default:
                    SnackBarUtils.showLongPost(clEvaluate, "初始化失败，非法的的启动参数。");
            }
        } else {
            SnackBarUtils.showLongPost(clEvaluate, "初始化失败，非法的的启动参数。");
        }
    }

    private void initStudentEvaluatedItem(CourseAndEvaluatedItem data) {
        course = data.getCourse();
        setTitle("评价：" + course.getName());
        if (data.getItem() != null) {
            for (StudentCourseEvaluate item : data.getItem()) {
                score[item.getItem().getId()] = item.getScore();
            }
        }
        vpFirstTarget.setAdapter(new FirstTargetViewPagerAdapter(getSupportFragmentManager(), score, isReadOnly, this));
        vpFirstTarget.setOffscreenPageLimit(EvaluateTargetManager.getFirstTargets().size());
        tlFirstTarget.setupWithViewPager(vpFirstTarget);
    }

    @Override
    public void onClick(View view) {
    }

    private float getTotalScore() {
        if (score.length > 0) {
            float totalScore = 0.0f;
            for (float itemScore : score) {
                if (itemScore < 0.0f) {
                    return -1.0f;
                }
                totalScore += itemScore;
            }
            return totalScore;
        } else {
            return -1.0f;
        }
    }

    private void showResultDialog() {
        float totalScore = getTotalScore();
        if (totalScore < 0.0f) {
            SnackBarUtils.showLong(clEvaluate, "存在未评价的项目。");
        } else {
            StringBuilder content = new StringBuilder();
            content.append("课程：").append(course.getName()).append("\n");
            content.append("得分：").append(totalScore);
            new MaterialDialog.Builder(this).title("结果").content(content)
                    .positiveText("提交").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    commitScore();
                }
            }).show();
        }
    }

    private void commitScore() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .progressIndeterminateStyle(false).title("正在提交").content("请稍后...").cancelable(false).build();
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
                startActivity(StudentCommentActivity.newIntent(EvaluateActivity.this, course.getId()));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluate, menu);
        for (int index = 0; index < menu.size(); ++index) {
            MenuItem item = menu.getItem(index);
            switch (item.getItemId()) {
                case R.id.action_commit:
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    item.setVisible(getTotalScore() >= 0.0f && !isReadOnly);
                    break;
                case R.id.action_analysis:
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    item.setVisible(identity == Constant.IDENTITY_TEACHER);
                    break;
                default:
            }
        }
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

    @Override
    public void onItemScoreUpdate(final TextView textView, final int itemId, final float newScore) {
        float currentScore = score[itemId];
        if (currentScore != newScore) {
            EvaluateApi.updateItemScore(course.getId(), itemId, newScore,
                    new SimpleServiceCallback<StudentCourseEvaluate>(clEvaluate) {
                        @Override
                        public void onGetDataSuccess(StudentCourseEvaluate data) {
                            score[itemId] = data.getScore();
                            textView.setText(data.getScore() + "分");
                            invalidateOptionsMenu();
                        }
                    });
        }
    }
}