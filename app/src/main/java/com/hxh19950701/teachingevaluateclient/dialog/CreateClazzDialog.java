package com.hxh19950701.teachingevaluateclient.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateClazzDialog extends MaterialDialog {

    @BindView(R.id.etClazzName)
    EditText etClazzName;
    @BindView(R.id.etClazzYear)
    EditText etClazzYear;

    public CreateClazzDialog(Context context, OnPositiveListener listener) {
        super(new Builder(context).title("创建班级").customView(R.layout.dialog_create_clazz, false)
                .positiveText("创建").onPositive(listener));
        ButterKnife.bind(this);
    }

    public abstract static class OnPositiveListener implements SingleButtonCallback {

        public abstract void onPositive(String clazzName, int year);

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            CreateClazzDialog createClazzDialog = (CreateClazzDialog) dialog;
            onPositive(createClazzDialog.etClazzName.getText().toString(),
                    Integer.parseInt(createClazzDialog.etClazzYear.getText().toString())
            );
        }
    }
}