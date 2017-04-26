package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.UserInfoRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.LoginRecord;
import com.hxh19950701.teachingevaluateclient.bean.response.Student;
import com.hxh19950701.teachingevaluateclient.bean.response.Teacher;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.network.api.TeacherApi;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.List;

import butterknife.BindView;

public class UserInfoActivity extends BaseActivity {

    public static Intent newIntent(Context context, int uid) {
        if (uid <= 0) {
            throw new IllegalArgumentException("uid is negative : " + uid);
        }
        return new Intent(context, UserInfoActivity.class)
                .putExtra(Constant.KEY_USER_ID, uid);
    }

    @BindView(R.id.clUserInfo)
    CoordinatorLayout clUserInfo;
    @BindView(R.id.rvUserInfo)
    RecyclerView rvInfo;

    private UserInfoRecyclerViewAdapter adapter = new UserInfoRecyclerViewAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        rvInfo.setLayoutManager(new LinearLayoutManager(this));
        initUserInfo();
    }

    private void initUserInfo() {
        int uid = getIntent().getIntExtra(Constant.KEY_USER_ID, -1);
        if (uid <= 0) {
            SnackBarUtils.showLongPost(clUserInfo, "非法的启用参数");
            return;
        }
        UserApi.getUser(uid, new SimpleServiceCallback<User>(clUserInfo) {
            @Override
            public void onGetDataSuccessful(User user) {
                adapter.setUser(user);
                setTitle(user.getUsername());
                if (user.getIdentity() == Constant.IDENTITY_STUDENT) {
                    initStudentInfo(uid);
                } else if (user.getIdentity() == Constant.IDENTITY_TEACHER) {
                    initTeacherInfo(uid);
                } else if (user.getIdentity() == Constant.IDENTITY_ADMINISTRATOR) {
                    initLoginRecords(uid);
                }
            }
        });
    }

    private void initTeacherInfo(int uid) {
        TeacherApi.get(uid, new SimpleServiceCallback<Teacher>(clUserInfo) {
            @Override
            public void onGetDataSuccessful(Teacher teacher) {
                adapter.setTeacher(teacher);
                initLoginRecords(uid);
            }
        });
    }

    private void initStudentInfo(int uid) {
        StudentApi.get(uid, new SimpleServiceCallback<Student>(clUserInfo) {
            @Override
            public void onGetDataSuccessful(Student student) {
                adapter.setStudent(student);
                initLoginRecords(uid);
            }
        });
    }

    private void initLoginRecords(int uid) {
        UserApi.getRecords(uid, new SimpleServiceCallback<List<LoginRecord>>(clUserInfo) {
            @Override
            public void onGetDataSuccessful(List<LoginRecord> loginRecords) {
                adapter.setRecords(loginRecords);
                rvInfo.setAdapter(adapter);
            }
        });
    }
}