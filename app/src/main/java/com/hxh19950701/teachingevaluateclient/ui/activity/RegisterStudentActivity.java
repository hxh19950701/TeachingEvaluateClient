package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
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
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Clazz;
import com.hxh19950701.teachingevaluateclient.bean.service.Student;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.internet.NetServer;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.DepartmentApi;
import com.hxh19950701.teachingevaluateclient.internet.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.utils.SnackBarUtils;

import java.util.HashSet;
import java.util.List;

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

    protected List<Clazz> data;

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

        rbSexMale = (RadioButton) findViewById(R.id.rbTerm1);
        rbSexFemale = (RadioButton) findViewById(R.id.rbTerm2);
        rgSex = (RadioGroup) findViewById(R.id.rgTerm);

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

        etStudentId.addTextChangedListener(new TextWatcherImpl() {
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

        etName.addTextChangedListener(new TextWatcherImpl() {
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
                if (!hasFocus && TextUtils.isEmpty(tilStudentId.getError())) {
                    checkStudentId();
                }
            }
        });

    }

    @Override
    protected void initData() {
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
        for (int i = 0; i < data.size(); ++i) {
            currentDepartment.add(data.get(i).getSubject().getDepartment().getName());
        }
        ArrayAdapter departmentAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentDepartment.toArray());
        departmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spDepartment.setAdapter(departmentAdapter);
    }

    protected void initSubject() {
        currentSubject.clear();
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).getSubject().getDepartment().getName().equals(spDepartment.getSelectedItem())) {
                currentSubject.add(data.get(i).getSubject().getName());
            }
        }
        ArrayAdapter subjectAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentSubject.toArray());
        subjectAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spSubject.setAdapter(subjectAdapter);
    }

    protected void initYear() {
        currentYear.clear();
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).getSubject().getName().equals(spSubject.getSelectedItem())) {
                currentYear.add(data.get(i).getYear());
            }
        }
        ArrayAdapter yearAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentYear.toArray());
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);
    }

    protected void initClazz() {
        currentClazz.clear();
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).getSubject().getName().equals(spSubject.getSelectedItem())
                    && ((Integer) spYear.getSelectedItem()).equals(data.get(i).getYear())) {
                currentClazz.add(data.get(i).getName());
            }
        }
        ArrayAdapter clazzAdapter = new ArrayAdapter(getApplication(), R.layout.spinner_item, currentClazz.toArray());
        clazzAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spClazz.setAdapter(clazzAdapter);
    }

    protected void initInfo() {
        srlRegister.setRefreshing(true);
        DepartmentApi.getClazzList(new SimpleServiceCallback<List<Clazz>>(clRegister) {

            @Override
            public void onAfter() {
                srlRegister.setRefreshing(false);
                refreshSaveButtonEnable();
            }

            @Override
            public void onGetDataSuccess(List<Clazz> clazz) {
                data = clazz;
                initDepartment();
            }

            @Override
            public void onException(String s) {
                SnackBarUtils.showLong(clRegister, "获取系部信息失败。");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                startSave();
        }
    }

    protected Clazz getClazz() {
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).getName().equals(spClazz.getSelectedItem())) {
                return data.get(i);
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
                + "\n性别：" + (rgSex.getCheckedRadioButtonId() == R.id.rbTerm1 ? "男" : "女")
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
        String studentId = etStudentId.getText().toString();
        String name = etName.getText().toString();
        int sex = rgSex.getCheckedRadioButtonId() == R.id.rbTerm1 ? Constant.SEX_MALE : Constant.SEX_FEMALE;
        int clazzId = getClazz().getId();

        StudentApi.register(studentId, name, sex, clazzId, new SimpleServiceCallback<Student>(clRegister) {
            @Override
            public void onAfter() {
                registerDialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(Student student) {
                startActivity(new Intent(getApplication(), StudentMainUiActivity.class));
                finish();
            }
        });
    }

    protected void checkStudentId() {
        String studentId = etStudentId.getText().toString();
        StudentApi.hasExist(studentId, new SimpleServiceCallback<Boolean>(clRegister) {

            @Override
            public void onGetDataSuccess(Boolean isExist) {
                if (isExist) {
                    tilStudentId.setError("该学号已被使用。");
                } else {
                    tilStudentId.setError("");
                    tilStudentId.setErrorEnabled(false);
                    refreshSaveButtonEnable();
                }
            }

            @Override
            public void onException(String s) {
                tilStudentId.setError("无法检测该学号是否已被使用。");
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
        if (tilStudentId.getEditText().isFocused() || !TextUtils.isEmpty(tilStudentId.getError())
                || TextUtils.isEmpty(etName.getText().toString()) || data == null) {
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }
    }
}