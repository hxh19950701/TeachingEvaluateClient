package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Clazz;
import com.hxh19950701.teachingevaluateclient.bean.service.Department;
import com.hxh19950701.teachingevaluateclient.bean.service.Student;
import com.hxh19950701.teachingevaluateclient.bean.service.Subject;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.impl.TextWatcherImpl;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudentActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private CoordinatorLayout clRegister;
    private TextInputLayout tilStudentId;
    private TextInputLayout tilStudentName;
    private RadioGroup rgSex;
    private Spinner spDepartment;
    private Spinner spSubject;
    private Spinner spYear;
    private Spinner spClazz;
    private Button btnSave;

    private HttpHandler<String> httpHandler = null;
    private final Map<String, Boolean> existence = new HashMap<>(20);

    private final TextWatcher STUDENT_ID_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            String studentId = s.toString();
            if (httpHandler != null) {
                httpHandler.cancel();
            }
            if (studentId.isEmpty()) {
                TextInputLayoutUtils.setErrorEnabled(tilStudentId, false);
                refreshOperationEnable();
            } else if (studentId.length() < 6) {
                tilStudentId.setError("学号由6~16个数字和字母组成。");
                refreshOperationEnable();
            } else {
                Boolean exist = existence.get(studentId);
                if (exist == null) {
                    tilStudentId.setError("正在检测该学号是否可用，请稍后……");
                    checkStudentIdExistence();
                } else {
                    setupStudentIdExistence(exist);
                    refreshOperationEnable();
                }
            }
        }
    };

    private final TextWatcher STUDENT_NAME_WATCHER = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            refreshOperationEnable();
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_student);
        clRegister = (CoordinatorLayout) findViewById(R.id.clRegister);
        tilStudentId = (TextInputLayout) findViewById(R.id.tilStudentId);
        tilStudentName = (TextInputLayout) findViewById(R.id.tilStudentName);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);
        spDepartment = (Spinner) findViewById(R.id.spDepartment);
        spSubject = (Spinner) findViewById(R.id.spSubject);
        spYear = (Spinner) findViewById(R.id.spYear);
        spClazz = (Spinner) findViewById(R.id.spClazz);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    @Override
    protected void initListener() {
        btnSave.setOnClickListener(this);
        spDepartment.setOnItemSelectedListener(this);
        spSubject.setOnItemSelectedListener(this);
        spYear.setOnItemSelectedListener(this);
        tilStudentId.getEditText().addTextChangedListener(STUDENT_ID_WATCHER);
        tilStudentName.getEditText().addTextChangedListener(STUDENT_NAME_WATCHER);
    }

    @Override
    protected void initData() {
        displayHomeAsUp();
        initAdapter();
        initDepartment();
        refreshOperationEnable();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        initDataFrom(parent.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        initDataFrom(parent.getId());
    }

    private void initDataFrom(int id) {
        switch (id) {
            case R.id.spDepartment:
                initSubject();
                break;
            case R.id.spSubject:
                initYear();
                break;
            case R.id.spYear:
                initClazz();
                break;
        }
    }

    private void initAdapter() {
        ArrayAdapter<Department> departmentAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        departmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spDepartment.setAdapter(departmentAdapter);

        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        subjectAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spSubject.setAdapter(subjectAdapter);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        ArrayAdapter<Clazz> clazzAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        clazzAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spClazz.setAdapter(clazzAdapter);
    }

    protected void initDepartment() {
        ArrayAdapter<Department> adapter = (ArrayAdapter<Department>) spDepartment.getAdapter();
        adapter.clear();
        adapter.addAll(DepartmentInfoManager.getDepartments());
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) {
            if (spDepartment.getSelectedItemPosition() == 0) {
                initSubject();
            } else {
                spDepartment.setSelection(0);
            }
        }
    }

    protected void initSubject() {
        Department department = (Department) spDepartment.getSelectedItem();
        if (department != null) {
            ArrayAdapter<Subject> adapter = (ArrayAdapter<Subject>) spSubject.getAdapter();
            adapter.clear();
            adapter.addAll(department.getSubjects());
            adapter.notifyDataSetChanged();
            if (adapter.getCount() > 0) {
                if (spSubject.getSelectedItemPosition() == 0) {
                    initYear();
                } else {
                    spSubject.setSelection(0);
                }
            }
        }
    }

    protected void initYear() {
        Subject subject = (Subject) spSubject.getSelectedItem();
        if (subject != null) {
            ArrayAdapter<Integer> adapter = (ArrayAdapter<Integer>) spYear.getAdapter();
            adapter.clear();
            adapter.addAll(subject.getYears());
            adapter.notifyDataSetChanged();
            if (adapter.getCount() > 0) {
                if (spYear.getSelectedItemPosition() == 0) {
                    initClazz();
                } else {
                    spYear.setSelection(0);
                }
            }
        }
    }

    protected void initClazz() {
        Subject subject = (Subject) spSubject.getSelectedItem();
        Integer year = (Integer) spYear.getSelectedItem();
        if (subject != null && year != null) {
            ArrayAdapter<Clazz> adapter = (ArrayAdapter<Clazz>) spClazz.getAdapter();
            adapter.clear();
            adapter.addAll(subject.getClasses(year));
            adapter.notifyDataSetChanged();
            spClazz.setSelection(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                startSave();
        }
    }

    protected void startSave() {
        String msg = "\n学号：" + tilStudentId.getEditText().getText().toString()
                + "\n姓名：" + tilStudentName.getEditText().getText().toString()
                + "\n性别：" + (rgSex.getCheckedRadioButtonId() == R.id.rbMale ? "男" : "女")
                + "\n系部：" + spDepartment.getSelectedItem()
                + "\n专业：" + spSubject.getSelectedItem()
                + "\n入学年份：" + spYear.getSelectedItem()
                + "\n班级：" + spClazz.getSelectedItem();
        new MaterialDialog.Builder(this).title("确认提交吗？")
                .content("一旦提交，某些信息将不能再次修改，请核对信息是否准确无误。" + msg)
                .positiveText("提交").negativeText("修改")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveStudentInfo();
                    }
                })
                .show();
    }

    protected void saveStudentInfo() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在保存信息").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).build();
        String studentId = tilStudentId.getEditText().getText().toString();
        String name = tilStudentName.getEditText().getText().toString();
        int sex = rgSex.getCheckedRadioButtonId() == R.id.rbMale ? Constant.SEX_MALE : Constant.SEX_FEMALE;
        int clazzId = ((Clazz) spClazz.getSelectedItem()).getId();
        StudentApi.register(studentId, name, sex, clazzId, new SimpleServiceCallback<Student>(clRegister, dialog) {
            @Override
            public void onGetDataSuccessful(Student student) {
                IntentUtils.startActivity(RegisterStudentActivity.this, StudentMainUiActivity.class,
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
    }

    protected void checkStudentIdExistence() {
        final String studentId = tilStudentId.getEditText().getText().toString();
        httpHandler = StudentApi.hasExist(studentId, new SimpleServiceCallback<Boolean>(clRegister) {

            @Override
            public void onAfter() {
                refreshOperationEnable();
            }

            @Override
            public void onGetDataSuccessful(Boolean isExist) {
                existence.put(studentId, isExist);
                setupStudentIdExistence(isExist);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                tilStudentId.setError("我们无法检测该学号是否可用。");
            }

            @Override
            public void onGetDataFailed(int code, String msg) {
                tilStudentId.setError("我们无法检测该学号是否可用。");
            }

            @Override
            public void onJsonSyntaxException(String s) {
                tilStudentId.setError("我们无法检测该学号是否可用。");
            }
        });
    }

    private void setupStudentIdExistence(boolean isExist) {
        if (isExist) {
            CharSequence errorText = "该学号已被使用";
            tilStudentId.setError(errorText);
        } else {
            TextInputLayoutUtils.setErrorEnabled(tilStudentId, false);
        }
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
                        ActivityUtils.exitApp(RegisterStudentActivity.this, "在你完善信息之前，你将无法使用本系统。");
                    }
                })
                .show();
    }

    private void refreshOperationEnable() {
        btnSave.setEnabled(TextInputLayoutUtils.isInputComplete(tilStudentId)
                && TextInputLayoutUtils.isInputComplete(tilStudentName));
    }

}