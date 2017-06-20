package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.FirstTargetViewPagerAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.CourseAndEvaluatedItem;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.response.TeacherCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.event.StudentEvaluateCourseCompleteEvent;
import com.hxh19950701.teachingevaluateclient.fragment.FirstTargetFragment;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class EvaluateActivity extends BaseActivity implements FirstTargetFragment.OnItemScoreUpdateListener {

    private static final String TAG = EvaluateActivity.class.getSimpleName();

    public static Intent newIntent(Context context, int courseId, int identity, boolean isReadOnly) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        } else if (identity != Constant.IDENTITY_STUDENT && identity != Constant.IDENTITY_TEACHER) {
            throw new IllegalArgumentException("Invalid identity : " + identity);
        }
        return new Intent(context, EvaluateActivity.class)
                .putExtra(Constant.KEY_COURSE_ID, courseId)
                .putExtra(Constant.KEY_IDENTITY, identity)
                .putExtra(Constant.KEY_READ_ONLY, isReadOnly);
    }

    private final int THIRD_TARGET_COUNT = EvaluateTargetManager.getThirdTargets().size();

    @BindView(R.id.clEvaluate)
    /*package*/ CoordinatorLayout clEvaluate;
    @BindView(R.id.tlFirstTarget)
    /*package*/ TabLayout tlFirstTarget;
    @BindView(R.id.vpFirstTarget)
    /*package*/ ViewPager vpFirstTarget;

    private Course course = null;
    private int identity = -1;
    private boolean isReadOnly = false;
    private float score[] = new float[THIRD_TARGET_COUNT];

    {
        Arrays.fill(score, -1.0f);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_evaluate;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initSetting();
        loadEvaluatedItem();
    }

    private void initSetting() {
        Intent intent = getIntent();
        identity = intent.getIntExtra(Constant.KEY_IDENTITY, -1);
        isReadOnly = intent.getBooleanExtra(Constant.KEY_READ_ONLY, false);
    }

    private void loadEvaluatedItem() {
        int courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId < 0) {
            SnackBarUtils.showLongPost(clEvaluate, "初始化失败，非法的的启动参数。");
            return;
        }
        switch (identity) {
            case Constant.IDENTITY_STUDENT:
                EvaluateApi.getStudentAllEvaluatedItemsByCourse(courseId,
                        new SimpleServiceCallback<CourseAndEvaluatedItem>(clEvaluate) {
                            @Override
                            public void onGetDataSuccessful(CourseAndEvaluatedItem data) {
                                initStudentEvaluatedItem(data);
                            }
                        });
                break;
            case Constant.IDENTITY_TEACHER:
                EvaluateApi.getTeacherAllEvaluatedItemsByCourse(courseId,
                        new SimpleServiceCallback<List<TeacherCourseEvaluate>>(clEvaluate) {
                            @Override
                            public void onGetDataSuccessful(List<TeacherCourseEvaluate> data) {
                                initTeacherEvaluatedItem(data);
                            }
                        });
                break;
            default:
                SnackBarUtils.showLongPost(clEvaluate, "初始化失败，非法的的启动参数。");
        }
    }

    private void initStudentEvaluatedItem(CourseAndEvaluatedItem data) {
        course = data.getCourse();
        setTitle("评价：" + course.getName());
        if (data.getItem() != null) {
            for (StudentCourseEvaluate item : data.getItem()) {
                int pos = item.getItem().getId();
                if (pos >= 0 && pos < score.length) {
                    score[pos] = item.getScore();
                }
            }
        }
        vpFirstTarget.setAdapter(new FirstTargetViewPagerAdapter(getSupportFragmentManager(), score, isReadOnly, this));
        vpFirstTarget.setOffscreenPageLimit(EvaluateTargetManager.getFirstTargets().size());
        tlFirstTarget.setupWithViewPager(vpFirstTarget);
        invalidateOptionsMenu();
    }

    private void initTeacherEvaluatedItem(List<TeacherCourseEvaluate> data) {
        if (data != null && data.size() != 0) {
            setTitle("评价：" + data.get(0).getCourse().getName());
            for (TeacherCourseEvaluate item : data) {
                score[item.getItem().getId()] = item.getScore();
            }
            vpFirstTarget.setAdapter(new FirstTargetViewPagerAdapter(getSupportFragmentManager(), score, isReadOnly, this));
            vpFirstTarget.setOffscreenPageLimit(EvaluateTargetManager.getFirstTargets().size());
            tlFirstTarget.setupWithViewPager(vpFirstTarget);
            invalidateOptionsMenu();
        }
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

    @SuppressWarnings("StringBufferReplaceableByString")
    private void showResultDialog() {
        float totalScore = getTotalScore();
        if (totalScore < 0.0f) {
            SnackBarUtils.showLong(clEvaluate, "存在未评价的项目。");
        } else {
            String content = new StringBuilder()
                    .append("课程 \"").append(course.getName())
                    .append("\" 的最终得分为 ").append(totalScore)
                    .toString();
            new MaterialDialog.Builder(this).title("确定提交吗").content(content)
                    .positiveText("提交").onPositive((dialog, which) -> commitScore())
                    .show();
        }
    }

    private void commitScore() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在提交").content("请稍后...")
                .progress(true, 0).cancelable(false).build();
        EvaluateApi.commitEvaluate(course.getId(), new SimpleServiceCallback<StudentCourseInfo>(clEvaluate, dialog) {
            @Override
            public void onGetDataSuccessful(StudentCourseInfo studentCourseInfo) {
                EventManager.postEvent(new StudentEvaluateCourseCompleteEvent(studentCourseInfo));
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
                    item.setVisible(getTotalScore() >= 0.0f && !isReadOnly);
                    break;
                case R.id.action_analysis:
                    item.setVisible(false);
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
                        public void onGetDataSuccessful(StudentCourseEvaluate data) {
                            score[itemId] = data.getScore();
                            textView.setText(getString(R.string.point, score[itemId]));
                            invalidateOptionsMenu();
                        }
                    });
        }
    }
}