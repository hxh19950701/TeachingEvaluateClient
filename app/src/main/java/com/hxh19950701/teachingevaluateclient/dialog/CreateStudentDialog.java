package com.hxh19950701.teachingevaluateclient.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.network.ServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.UserApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

public class CreateStudentDialog extends MaterialDialog {

    private final Context context;
    private final List<String> usernameList;
    private final List<String> successfulUsernameList;

    public CreateStudentDialog(Context context, @NonNull List<String> usernameList) {
        super(new Builder(context).content("正在准备...").progress(true, 0).cancelable(false));
        this.context = context;
        this.usernameList = usernameList;
        this.successfulUsernameList = new ArrayList<>(usernameList.size());
    }

    @Override
    public void show() {
        super.show();
        startCreate(0);
    }

    private void startCreate(int pos) {
        if (usernameList.size() == pos) {
            showResult();
            dismiss();
            return;
        }
        String username = usernameList.get(pos);
        String password = MD5Utils.encipher(username);
        setContent("正在创建：" + username + " ...");
        UserApi.register(username, password, Constant.IDENTITY_STUDENT, new ServiceCallback<User>() {

            @Override
            public void onAfter() {
                startCreate(pos + 1);
            }

            @Override
            public void onSuccess(ResponseData<User> data) {
                if (data.getCode() == Constant.CODE_SUCCESS) {
                    successfulUsernameList.add(username);
                }
            }
        });
    }

    private void showResult() {
        usernameList.removeAll(successfulUsernameList);
        if (usernameList.isEmpty()) {
            new MaterialDialog.Builder(context).content("用户已创建成功！")
                    .positiveText("好的").show();
        } else {
            StringBuilder sb = new StringBuilder();
            for (String username : usernameList) {
                sb.append(username + "\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            new MaterialDialog.Builder(context).title("下列" + usernameList.size() + "个用户创建失败")
                    .positiveText("好的").content(sb).show();
        }
    }
}
