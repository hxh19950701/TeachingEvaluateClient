package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hxh19950701.teachingevaluateclient.Bean.ClazzBean;
import com.hxh19950701.teachingevaluateclient.Bean.HasExistBean;
import com.hxh19950701.teachingevaluateclient.Bean.RegisterUserBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseRequestCallBack;
import com.hxh19950701.teachingevaluateclient.internet.NetServer;
import com.hxh19950701.teachingevaluateclient.utils.PrefUtils;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashSet;

public class RegisterStudentActivity extends BaseActivity {

    protected Toolbar toolbar;
    protected Button btnSave;
    protected CoordinatorLayout clRegister;
    protected TextInputLayout tilStudentId;
    protected SwipeRefreshLayout srlRegister;

    protected EditText etStudentId;
    protected EditText etName;

    protected RadioButton rbSexMale;
    protected RadioButton rbSexFemale;
    protected RadioGroup rgSex;

    protected Spinner spDepartment;
    protected Spinner spSubject;
    protected Spinner spYear;
    protected Spinner spClazz;

    protected ClazzBean data;

    protected HashSet<String> currentSubject = new HashSet<>();
    protected HashSet<String> currentDepartment = new HashSet<>();
    protected HashSet<String> currentClazz = new HashSet<>();
    protected HashSet<Integer> currentYear = new HashSet<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_student);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etStudentId = (EditText) findViewById(R.id.etStudentId);
        etName = (EditText) findViewById(R.id.etName);

        rbSexMale = (RadioButton) findViewById(R.id.rbSexMale);
        rbSexFemale = (RadioButton) findViewById(R.id.rbSexFemale);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);

        spDepartment = (Spinner) findViewById(R.id.spDepartment);
        spSubject = (Spinner) findViewById(R.id.spSubject);
        spYear = (Spinner) findViewById(R.id.spYear);
        spClazz = (Spinner) findViewById(R.id.spClazz);

        btnSave = (Button) findViewById(R.id.btnSave);
        clRegister = (CoordinatorLayout) findViewById(R.id.clRegister);
        srlRegister = (SwipeRefreshLayout) findViewById(R.id.srlRegister);
        tilStudentId = (TextInputLayout) findViewById(R.id.tilStudentId);
    }

    @Override
    protected void initListener() {
        btnSave.setOnClickListener(this);

        etStudentId.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6 || s.length() > 16) {
                    tilStudentId.setError("学号由6~16个数字和字母组成。");
                } else {
                    tilStudentId.setError("");
                    tilStudentId.setErrorEnabled(false);
                }
                refreshSaveButtonEnable();
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshSaveButtonEnable();
            }
        });

        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initSubject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                initSubject();
            }
        });

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initYear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initClazz();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        srlRegister.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initInfo();
            }
        });

        etStudentId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkStudentId();
                }
            }
        });
    }

    @Override
    protected void initDate() {
        initInfo();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("完善信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSave.setEnabled(false);
        srlRegister.setColorSchemeResources(R.color.colorAccent);
        btnSave.setEnabled(false);
    }

    protected void initDepartment() {
        currentDepartment.clear();
        for (int i = 0; i < data.getClazzList().size(); ++i) {
            currentDepartment.add(data.getClazzList().get(i).getSubject().getDepartment().getName());
        }
        ArrayAdapter departmentAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentDepartment.toArray());
        departmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spDepartment.setAdapter(departmentAdapter);
    }

    protected void initSubject() {
        currentSubject.clear();
        for (int i = 0; i < data.getClazzList().size(); ++i) {
            if (data.getClazzList().get(i).getSubject().getDepartment().getName().equals(spDepartment.getSelectedItem())) {
                currentSubject.add(data.getClazzList().get(i).getSubject().getName());
            }
        }
        ArrayAdapter subjectAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentSubject.toArray());
        subjectAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spSubject.setAdapter(subjectAdapter);
    }

    protected void initYear() {
        currentYear.clear();
        for (int i = 0; i < data.getClazzList().size(); ++i) {
            if (data.getClazzList().get(i).getSubject().getName().equals(spSubject.getSelectedItem())) {
                currentYear.add(data.getClazzList().get(i).getYear());
            }
        }
        ArrayAdapter yearAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentYear.toArray());
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);
    }

    protected void initClazz() {
        currentClazz.clear();
        for (int i = 0; i < data.getClazzList().size(); ++i) {
            if (data.getClazzList().get(i).getSubject().getName().equals(spSubject.getSelectedItem())
                    && ((Integer) spYear.getSelectedItem()).equals(data.getClazzList().get(i).getYear())) {
                currentClazz.add(data.getClazzList().get(i).getName());
            }
        }
        ArrayAdapter clazzAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentClazz.toArray());
        clazzAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spClazz.setAdapter(clazzAdapter);
    }

    protected void initInfo() {
        if (srlRegister != null) {
            srlRegister.setRefreshing(true);
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("cookie", PrefUtils.getString("cookie", ""));
        requestParams.addQueryStringParameter("action", "getClazzList");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, TeachingEvaluateClientApplication.getDepartmentManagerURL(),
                requestParams, new BaseRequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        System.out.println(responseInfo.result);
                        Gson gson = new Gson();
                        data = gson.fromJson(responseInfo.result, ClazzBean.class);
                        if (data.isSuccess()) {
                            initDepartment();
                            if (srlRegister != null) {
                                srlRegister.setRefreshing(false);
                            }
                        } else {
                            NetServer.requireLoginAgain(RegisterStudentActivity.this, "登录身份已过期。");
                        }
                        refreshSaveButtonEnable();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        if (srlRegister != null) {
                            srlRegister.setRefreshing(false);
                        }
                        SnackBarUtils.showLong(clRegister, "获取系部信息失败。\n错误代码：" + String.format("0x%06x", e.getExceptionCode()));
                        refreshSaveButtonEnable();
                    }
                }

        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                startSave();
        }
    }

    protected ClazzBean.ClazzListBean getClazz() {
        for (int i = 0; i < data.getClazzList().size(); ++i) {
            if (data.getClazzList().get(i).getName().equals(spClazz.getSelectedItem())) {
                return data.getClazzList().get(i);
            }
        }
        return null;
    }

    protected void startSave() {
        if (etStudentId.getText().toString().isEmpty() || etName.getText().toString().isEmpty()) {
            SnackBarUtils.showLong(clRegister, "信息填写不完整。");
            return;
        }
        String msg = "\n学号：" + etStudentId.getText().toString()
                + "\n姓名：" + etName.getText().toString()
                + "\n性别：" + (rgSex.getCheckedRadioButtonId() == R.id.rbSexMale ? "男" : "女")
                + "\n系部：" + spDepartment.getSelectedItem()
                + "\n专业：" + spSubject.getSelectedItem()
                + "\n入学年份：" + spYear.getSelectedItem()
                + "\n班级：" + spClazz.getSelectedItem();
        new MaterialDialog.Builder(this)
                .title("确认提交吗？")
                .content("一旦提交，某些信息将不能再次修改，请核对信息是否准确无误。" + msg)
                .positiveText("提交")
                .negativeText("修改")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveStudentInfo();
                    }
                })
                .show();
    }

    protected void saveStudentInfo() {
        final MaterialDialog registerDialog = new MaterialDialog.Builder(this)
                .title("正在保存信息").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).show();
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("cookie", PrefUtils.getString("cookie", ""));
        requestParams.addBodyParameter("action", "register");
        requestParams.addBodyParameter("studentId", etStudentId.getText().toString());
        requestParams.addBodyParameter("name", etName.getText().toString());
        requestParams.addBodyParameter("sex", rgSex.getCheckedRadioButtonId() == R.id.rbSexMale ? "0" : "1");
        requestParams.addBodyParameter("clazz", getClazz().getId() + "");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, TeachingEvaluateClientApplication.getStudentManagerURL(),
                requestParams, new BaseRequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        RegisterUserBean registerUserBean = gson.fromJson(responseInfo.result, RegisterUserBean.class);
                        if (registerUserBean.isSuccess()) {
                            registerDialog.dismiss();
                            startActivity(new Intent(getApplication(), StudentMainUiActivity.class));
                            finish();
                        } else {
                            SnackBarUtils.showLong(clRegister, "保存信息失败。");
                            registerDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        super.onFailure(e, s);
                        SnackBarUtils.showLong(clRegister, "连接服务器失败。\n错误代码：" + String.format("0x%06x", e.getExceptionCode()));
                        registerDialog.dismiss();
                    }
                });
    }

    protected void checkStudentId() {
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("cookie", PrefUtils.getString("cookie", ""));
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET,
                TeachingEvaluateClientApplication.getStudentManagerURL() + "?action=hasExist&studentId=" + etStudentId.getText().toString(),
                requestParams, new BaseRequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        super.onSuccess(responseInfo);
                        Gson gson = new Gson();
                        HasExistBean hasExistBean = gson.fromJson(responseInfo.result, HasExistBean.class);
                        if (hasExistBean.isExist()) {
                            tilStudentId.setError("该学号已被使用。");
                            SnackBarUtils.showLong(clRegister, "该学号已被使用。");
                        } else {
                            tilStudentId.setError("");
                            tilStudentId.setErrorEnabled(false);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title("真的要退出吗？")
                .content("在你完善信息之前，你将无法使用本系统。")
                .positiveText("退出")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NetServer.requireLoginAgain(RegisterStudentActivity.this, null);
                        finish();
                    }
                })
                .show();
    }

    private void refreshSaveButtonEnable() {
        if (tilStudentId.isFocused() || !TextUtils.isEmpty(tilStudentId.getError())
                || TextUtils.isEmpty(etName.getText().toString()) || data == null) {
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }
    }
}