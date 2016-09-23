package com.hxh19950701.teachingevaluateclient.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.afollestad.materialdialogs.util.DialogUtils;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;

/**
 * Created by hxh19950701 on 2016/5/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, ColorChooserDialog.ColorCallback {

    // color chooser dialog
    protected static int primaryPreselect = PrefUtils.getInt("accentColor", 0);
    protected static int accentPreselect = PrefUtils.getInt("primaryColor", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initDate();
        initTheme();
    }

    protected void initTheme() {
        int accentColor = PrefUtils.getInt("accentColor", 0);;
        int primaryColor = PrefUtils.getInt("primaryColor", 0);
        if (accentColor != 0) {
            setAccentColor(accentColor);
        }
        if (primaryColor != 0) {
            setPrimaryColor(primaryColor);
        }
    }

    protected void setAccentColor(int color) {
        accentPreselect = color;
        ThemeSingleton.get().positiveColor = DialogUtils.getActionTextStateList(this, color);
        ThemeSingleton.get().neutralColor = DialogUtils.getActionTextStateList(this, color);
        ThemeSingleton.get().negativeColor = DialogUtils.getActionTextStateList(this, color);
        ThemeSingleton.get().widgetColor = color;
    }

    protected void setPrimaryColor(int color) {
        primaryPreselect = color;
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            getWindow().setNavigationBarColor(color);
        }
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initDate();

    public abstract void onClick(View view);

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Receives callback from color chooser dialog
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {
        if (dialog.isAccentMode()) {
            PrefUtils.putInt("accentColor", color);
            setAccentColor(color);
        } else {
            PrefUtils.putInt("primaryColor", color);
            setPrimaryColor(color);
        }
    }

    public void showColorChooserPrimary() {
        new ColorChooserDialog.Builder(this, R.string.chooseColor)
                .titleSub(R.string.chooseColor)
                .preselect(primaryPreselect)
                .show();
    }

    public void showColorChooserAccent() {
        new ColorChooserDialog.Builder(this, R.string.chooseColor)
                .titleSub(R.string.chooseColor)
                .accentMode(true)
                .preselect(accentPreselect)
                .show();
    }

    public void showColorChooserCustomColors() {
        int[][] subColors = new int[][]{
                new int[]{Color.parseColor("#EF5350"), Color.parseColor("#F44336"), Color.parseColor("#E53935")},
                new int[]{Color.parseColor("#EC407A"), Color.parseColor("#E91E63"), Color.parseColor("#D81B60")},
                new int[]{Color.parseColor("#AB47BC"), Color.parseColor("#9C27B0"), Color.parseColor("#8E24AA")},
                new int[]{Color.parseColor("#7E57C2"), Color.parseColor("#673AB7"), Color.parseColor("#5E35B1")},
                new int[]{Color.parseColor("#5C6BC0"), Color.parseColor("#3F51B5"), Color.parseColor("#3949AB")},
                new int[]{Color.parseColor("#42A5F5"), Color.parseColor("#2196F3"), Color.parseColor("#1E88E5")}
        };

        new ColorChooserDialog.Builder(this, R.string.chooseColor)
                .titleSub(R.string.chooseColor)
                .preselect(primaryPreselect)
                .customColors(R.array.custom_colors, subColors)
                .show();
    }

    public void showColorChooserCustomColorsNoSub() {
        new ColorChooserDialog.Builder(this, R.string.chooseColor)
                .titleSub(R.string.chooseColor)
                .preselect(primaryPreselect)
                .customColors(R.array.custom_colors, null)
                .show();
    }
}