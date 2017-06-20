package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.CourseEvaluateInfoAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.MaxAndMin;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.response.TeacherCourseEvaluate;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.List;

import butterknife.BindView;

public class CourseEvaluateInfoActivity extends BaseActivity implements CourseEvaluateInfoAdapter.ItemClickListener {

    public static Intent newIntent(Context context, int courseId) {
        if (courseId <= 0) {
            throw new IllegalArgumentException("negative course id: " + courseId);
        }
        return new Intent(context, CourseEvaluateInfoActivity.class)
                .putExtra(Constant.KEY_COURSE_ID, courseId);
    }

    @BindView(R.id.rvCourseEvaluateInfo)
    /*package*/ RecyclerView rvCourseEvaluateInfo;
    @BindView(R.id.clCourseEvaluateInfo)
    /*package*/ CoordinatorLayout clCourseEvaluateInfo;

    private int courseId;
    private TeacherCourseEvaluate max;
    private TeacherCourseEvaluate min;
    private List<StudentCourseInfo> info;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_evaluate_info;
    }

    @Override
    protected void initView() {
        displayHomeAsUp();
        rvCourseEvaluateInfo.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        courseId = getIntent().getIntExtra(Constant.KEY_COURSE_ID, -1);
        if (courseId > 0) {
            initMaxAndMin(courseId);
            initCompletedEvaluate(courseId);
        } else {
            SnackBarUtils.showLongPost(clCourseEvaluateInfo, "非法的启动参数");
        }
    }

    private void initMaxAndMin(int courseId) {
        EvaluateApi.getMaxAndMin(courseId, new SimpleServiceCallback<MaxAndMin>(clCourseEvaluateInfo) {
            @Override
            public void onGetDataSuccessful(MaxAndMin maxAndMin) {
                max = maxAndMin.getMax();
                min = maxAndMin.getMin();
                setupRecyclerView();
            }
        });
    }

    private void initCompletedEvaluate(int courseId) {
        EvaluateApi.getCompletedEvaluate(courseId, new SimpleServiceCallback<List<StudentCourseInfo>>(clCourseEvaluateInfo) {
            @Override
            public void onGetDataSuccessful(List<StudentCourseInfo> studentCourseInfo) {
                info = studentCourseInfo;
                setupRecyclerView();
            }
        });
    }

    private synchronized void setupRecyclerView() {
        if (max != null && min != null && info != null) {
            rvCourseEvaluateInfo.setAdapter(new CourseEvaluateInfoAdapter(max, min, info, this));
        }
    }

    @Override
    public void onItemClick(StudentCourseInfo item) {
        if (TextUtils.isEmpty(item.getReplyTime())) {
            new MaterialDialog.Builder(this)
                    .title("回复该同学")
                    .content(item.getComment())
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .positiveText("回复")
                    .alwaysCallInputCallback()
                    .input(null, "感谢你的评价！",
                            (dialog, input) -> {
                                boolean enable = input.length() >= 5 && input.length() <= 120;
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(enable);
                            })
                    .onPositive((dialog, which) -> {
                        String reply = dialog.getInputEditText().getText().toString();
                        submitReply(item.getId(), reply);
                    })
                    .show();
        }
    }

    private void submitReply(int id, String reply) {
        EvaluateApi.reply(id, reply, new SimpleServiceCallback<StudentCourseInfo>(clCourseEvaluateInfo) {
            @Override
            public void onGetDataSuccessful(StudentCourseInfo studentCourseInfo) {
                SnackBarUtils.showLong(clCourseEvaluateInfo, "提交成功");
                initData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail:
                startActivity(EvaluateActivity.newIntent(this, courseId, Constant.IDENTITY_TEACHER, true));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
