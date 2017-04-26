package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.response.Clazz;
import com.hxh19950701.teachingevaluateclient.bean.response.Department;
import com.hxh19950701.teachingevaluateclient.bean.response.Student;
import com.hxh19950701.teachingevaluateclient.bean.response.Subject;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.manager.DepartmentInfoManager;
import com.hxh19950701.teachingevaluateclient.network.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.network.api.StudentApi;
import com.hxh19950701.teachingevaluateclient.utils.ActivityUtils;
import com.hxh19950701.teachingevaluateclient.utils.CheckTextExistenceUtils;
import com.hxh19950701.teachingevaluateclient.utils.IntentUtils;
import com.hxh19950701.teachingevaluateclient.utils.TextInputLayoutUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class StudentInfoCompleteActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.clRegister)
    /*package*/ CoordinatorLayout clRegister;
    @BindView(R.id.tilStudentId)
    /*package*/ TextInputLayout tilStudentId;
    @BindView(R.id.tilStudentName)
    /*package*/ TextInputLayout tilStudentName;
    @BindView(R.id.rgSex)
    /*package*/ RadioGroup rgSex;
    @BindView(R.id.spDepartment)
    /*package*/ Spinner spDepartment;
    @BindView(R.id.spSubject)
    /*package*/ Spinner spSubject;
    @BindView(R.id.spYear)
    /*package*/ Spinner spYear;
    @BindView(R.id.spClazz)
    /*package*/ Spinner spClazz;
    @BindView(R.id.btnSave)
    /*package*/ Button btnSave;

    private CheckTextExistenceUtils existenceUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_student;
    }

    @Override
    protected void initListener() {
        spDepartment.setOnItemSelectedListener(this);
        spSubject.setOnItemSelectedListener(this);
        spYear.setOnItemSelectedListener(this);
        existenceUtils = new CheckTextExistenceUtils(
                tilStudentId,
                "正在检测该学号是否可用，请稍后……",
                "该学号已被使用",
                "我们无法检测该学号是否可用。",
                new CheckTextExistenceUtils.CheckListener() {
                    @Override
                    public void onCheckFromServer(CheckTextExistenceUtils.CheckServiceCallback callback) {
                        StudentApi.hasExist(callback.getText().toString(), callback);
                    }

                    @Override
                    public void onComplete() {
                        refreshOperationEnable();
                    }
                });
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

    @SuppressWarnings("unchecked")
    private void initDepartment() {
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

    @SuppressWarnings("unchecked")
    private void initSubject() {
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

    @SuppressWarnings("unchecked")
    private void initYear() {
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

    @SuppressWarnings("unchecked")
    private void initClazz() {
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

    @SuppressWarnings("StringBufferReplaceableByString")
    @OnClick(R.id.btnSave)
    public void showSaveInfoDialog() {
        String msg = new StringBuilder().append("一旦提交，某些信息将不能再次修改，请核对信息是否准确无误。")
                .append("\n学号：").append(tilStudentId.getEditText().getText())
                .append("\n姓名：").append(tilStudentName.getEditText().getText())
                .append("\n性别：").append(rgSex.getCheckedRadioButtonId() == R.id.rbMale ? "男" : "女")
                .append("\n系部：").append(spDepartment.getSelectedItem())
                .append("\n专业：").append(spSubject.getSelectedItem())
                .append("\n入学年份：").append(spYear.getSelectedItem())
                .append("\n班级：").append(spClazz.getSelectedItem())
                .toString();
        new MaterialDialog.Builder(this).title("确认提交吗？").content(msg)
                .positiveText("提交").negativeText("修改")
                .onPositive((dialog, which) -> saveStudentInfo())
                .show();
    }

    private void saveStudentInfo() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在保存信息").content("请稍后...")
                .cancelable(false).progress(true, 0).build();
        String studentId = tilStudentId.getEditText().getText().toString();
        String name = tilStudentName.getEditText().getText().toString();
        int sex = rgSex.getCheckedRadioButtonId() == R.id.rbMale ? Constant.SEX_MALE : Constant.SEX_FEMALE;
        int clazzId = ((Clazz) spClazz.getSelectedItem()).getId();
        StudentApi.register(studentId, name, sex, clazzId, new SimpleServiceCallback<Student>(clRegister, dialog) {
            @Override
            public void onGetDataSuccessful(Student student) {
                IntentUtils.startActivity(StudentInfoCompleteActivity.this, StudentMainUiActivity.class,
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
    }

    @OnTextChanged(value = R.id.tilStudentId)
    public void checkStudentId() {
        existenceUtils.abortCurrentCheck();
        String studentId = tilStudentId.getEditText().getText().toString();
        if (studentId.isEmpty()) {
            TextInputLayoutUtils.setErrorEnabled(tilStudentId, false);
            refreshOperationEnable();
        } else if (studentId.length() < 6) {
            tilStudentId.setError("学号由6~16个数字和字母组成。");
            refreshOperationEnable();
        } else {
            existenceUtils.checkExistence();
        }
    }

    public void checkOtherInfo() {

    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this).content("你的信息尚未保存，现在退出吗？")
                .positiveText("退出").negativeText("取消")
                .onPositive((dialog, which) -> ActivityUtils.exitApp(StudentInfoCompleteActivity.this, "在你完善信息之前，你将无法使用本系统。"))
                .show();
    }

    private void refreshOperationEnable() {
        btnSave.setEnabled(TextInputLayoutUtils.isInputComplete(tilStudentId)
                && TextInputLayoutUtils.isInputComplete(tilStudentName));
    }

}