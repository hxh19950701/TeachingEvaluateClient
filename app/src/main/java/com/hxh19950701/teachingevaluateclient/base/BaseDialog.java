package com.hxh19950701.teachingevaluateclient.base;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;

public class BaseDialog extends MaterialDialog {

    protected BaseDialog(Builder builder) {
        super(builder);
        ButterKnife.bind(this);
    }
}
