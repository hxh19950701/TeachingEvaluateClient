package com.hxh19950701.teachingevaluateclient.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.adapter.UserRecyclerViewAdapter;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.dialog.UserFilterDialog;
import com.hxh19950701.teachingevaluateclient.event.UserFilterChangedEvent;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.FilterUserUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserManageActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, UserRecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.clUserManage)
    /*package*/ CoordinatorLayout clUserManage;
    @BindView(R.id.srlUser)
    /*package*/ SwipeRefreshLayout srlUser;
    @BindView(R.id.rvUser)
    /*package*/ RecyclerView rvUser;

    private final List<User> fullUser = new ArrayList<>(50);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_manage;
    }

    @Override
    protected void initView() {
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        srlUser.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void initListener() {
        srlUser.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        rvUser.setAdapter(new UserRecyclerViewAdapter(new ArrayList<User>(50), UserManageActivity.this));
        initUserList();
        startReceiveEvent();
    }

    @Override
    public void onRefresh() {
        initUserList();
    }

    public void initUserList() {
        UserApi.getAllUser(new SimpleServiceCallback<List<User>>(clUserManage, srlUser) {
            @Override
            public void onGetDataSuccessful(List<User> users) {
                fullUser.clear();
                fullUser.addAll(users);
                filterUser(new UserFilterChangedEvent("", -1, -1));
            }
        });
    }

    @Override
    public void onItemClick(int position, User user) {
        startActivity(UserInfoActivity.newIntent(this, user.getId()));
    }

    @Override
    public void onItemLongClick(int position, User user) {
        if (user.getIdentity() == Constant.IDENTITY_ADMINISTRATOR) {
            return;
        }
        new MaterialDialog.Builder(this).title(user.getUsername())
                .items(user.isEnable() ? "停用该用户" : "启用该用户", "恢复默认密码")
                .itemsCallback((dialog, itemView, which, text) -> {
                    if (which == 0) {
                        showSwitchEnableDialog(position, user);
                    } else {
                        showResetPasswordDialog(user);
                    }
                }).show();
    }

    private void showSwitchEnableDialog(int position, User user) {
        boolean enable = !user.isEnable();
        new MaterialDialog.Builder(this).content("确认" + (enable ? "启用" : "停用") + "用户“" + user.getUsername() + "”吗？")
                .positiveText(enable ? "启用" : "停用").onPositive((dialog, which) -> switchEnable(position, user, enable))
                .negativeText("取消").show();
    }

    private void switchEnable(int position, User user, boolean enable) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在" + (enable ? "启用" : "停用") + "...")
                .cancelable(false).progress(true, 0).build();
        UserApi.modifyEnable(user.getId(), enable, new SimpleServiceCallback<User>(clUserManage, dialog) {
            @Override
            public void onGetDataSuccessful(User newUser) {
                user.setEnable(enable);
                rvUser.getAdapter().notifyItemChanged(position);
                SnackBarUtils.showLong(clUserManage, (enable ? "启用" : "停用") + "成功");
            }
        });
    }

    private void showResetPasswordDialog(User user) {
        new MaterialDialog.Builder(this).content("确认重置用户“" + user.getUsername() + "”的密码吗？")
                .positiveText("重置").onPositive((dialog, which) -> resetPassword(user))
                .negativeText("取消").show();
    }

    private void resetPassword(User user) {
        MaterialDialog dialog = new MaterialDialog.Builder(this).content("正在重置密码...")
                .cancelable(false).progress(true, 0).build();
        UserApi.resetPassword(user.getId(), new SimpleServiceCallback<User>(clUserManage, dialog) {
            @Override
            public void onGetDataSuccessful(User newUser) {
                SnackBarUtils.showLong(clUserManage, "重置密码成功");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                UserFilterDialog.newInstance(this).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void filterUser(UserFilterChangedEvent event) {
        List<User> filterUsers = new ArrayList<>(50);
        for (User user : fullUser) {
            if (FilterUserUtils.isQualifiedUsername(user.getUsername(), event.username)
                    && FilterUserUtils.isQualifiedIdentity(user.getIdentity(), event.identity)
                    && FilterUserUtils.isQualifiedEnable(user.isEnable(), event.enable)) {
                filterUsers.add(user);
            }
        }
        UserRecyclerViewAdapter adapter = (UserRecyclerViewAdapter) rvUser.getAdapter();
        adapter.getData().clear();
        adapter.getData().addAll(filterUsers);
        adapter.notifyDataSetChanged();
    }
}