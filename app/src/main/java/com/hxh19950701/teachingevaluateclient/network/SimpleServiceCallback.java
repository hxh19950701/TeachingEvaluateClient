package com.hxh19950701.teachingevaluateclient.network;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.exception.HttpException;

public abstract class SimpleServiceCallback<Data> extends ServiceCallback<Data> {

    private View container;
    private Dialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SimpleServiceCallback(@NonNull View container) {
        this.container = container;
    }

    public SimpleServiceCallback(@NonNull View container, @Nullable Dialog dialog) {
        this.container = container;
        this.dialog = dialog;
    }

    public SimpleServiceCallback(@NonNull View container, @Nullable CharSequence dialogText) {
        this.container = container;
        this.dialog = new MaterialDialog.Builder(container.getContext()).content(dialogText)
                .progressIndeterminateStyle(false).cancelable(false).build();
    }

    public SimpleServiceCallback(@NonNull View container, @Nullable SwipeRefreshLayout swipeRefreshLayout) {
        this.container = container;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onStart() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onSuccess(ResponseData<Data> data) {
        if (data.getCode() == Constant.CODE_SUCCESS) {
            onGetDataSuccessful(data.getData());
        } else {
            onGetDataFailed(data.getCode(), data.getMsg());
        }
    }

    public abstract void onGetDataSuccessful(Data data);

    public void onGetDataFailed(int code, String msg) {
        SnackBarUtils.showLong(container, msg);
    }

    @Override
    public void onFailure(HttpException e, String s) {
        SnackBarUtils.showConnectFailure(container, e.getExceptionCode());
    }

    @Override
    public void onJsonSyntaxException(String s) {
        SnackBarUtils.showSystemError(container);
    }

    @Override
    public void onAfter() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
