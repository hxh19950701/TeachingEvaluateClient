package com.hxh19950701.teachingevaluateclient.dialog;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseDialog;
import com.hxh19950701.teachingevaluateclient.event.UserFilterChangedEvent;
import com.hxh19950701.teachingevaluateclient.manager.EventManager;

import butterknife.BindView;
import butterknife.OnTextChanged;

public class UserFilterDialog extends BaseDialog {

    public static UserFilterDialog newInstance(Context context) {
        Builder builder = new Builder(context).customView(R.layout.dialog_user_filter, false);
        return new UserFilterDialog(builder);
    }

    @BindView(R.id.etUsername)
    /*package*/ EditText etUsername;
    @BindView(R.id.rgIdentity)
    /*package*/ RadioGroup rgIdentity;
    @BindView(R.id.rgEnable)
    /*package*/ RadioGroup rgEnable;

    protected UserFilterDialog(Builder builder) {
        super(builder);
        //辣鸡ButterKnife，竟然不支持RadioGroup的setOnCheckedChangeListener
        RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> onSettingChanged();
        rgIdentity.setOnCheckedChangeListener(listener);
        rgEnable.setOnCheckedChangeListener(listener);
    }

    private UserFilterChangedEvent getCurrentUserFilterChangedEvent() {
        return new UserFilterChangedEvent(
                etUsername.getText().toString(),
                Integer.parseInt(findViewById(rgIdentity.getCheckedRadioButtonId()).getTag().toString()),
                Integer.parseInt(findViewById(rgEnable.getCheckedRadioButtonId()).getTag().toString()));
    }

    @Override
    public void show() {
        super.show();
        EventManager.postEvent(getCurrentUserFilterChangedEvent());
    }

    @OnTextChanged(R.id.etUsername)
    public void onSettingChanged() {
        EventManager.postEvent(getCurrentUserFilterChangedEvent());
    }
}
