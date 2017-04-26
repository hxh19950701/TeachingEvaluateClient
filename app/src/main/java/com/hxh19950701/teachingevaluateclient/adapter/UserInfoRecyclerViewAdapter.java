package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.LoginRecord;
import com.hxh19950701.teachingevaluateclient.bean.response.Student;
import com.hxh19950701.teachingevaluateclient.bean.response.Teacher;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.UserUtils;

import java.util.List;

import butterknife.BindView;

@SuppressWarnings("WeakerAccess")
public class UserInfoRecyclerViewAdapter extends HeaderAndFooterRecyclerViewAdapter<
        UserInfoRecyclerViewAdapter.HeaderViewHolder, UserInfoRecyclerViewAdapter.ContentViewHolder, RecyclerView.ViewHolder> {

    private User user;
    private Student student;
    private Teacher teacher;
    private List<LoginRecord> records;

    public void setUser(User user) {
        this.user = user;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setRecords(List<LoginRecord> records) {
        this.records = records;
    }

    @Override
    public int getHeaderCount() {
        return 1;
    }

    @Override
    public int getContentItemCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.header_user_info, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public ContentViewHolder onCreateContentItemViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_logion_record, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.bindUser(user);
        if (user.getIdentity() == Constant.IDENTITY_STUDENT) {
            holder.bindStudent(student);
        } else if (user.getIdentity() == Constant.IDENTITY_TEACHER) {
            holder.bindTeacher(teacher);
        }
    }

    @Override
    public void onBindContentItemViewHolder(ContentViewHolder holder, int position) {
        holder.bindData(records.get(position));
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class HeaderViewHolder extends BaseViewHolder {

        @BindView(R.id.tvUsername)
        /*package*/ TextView tvUsername;
        @BindView(R.id.tvIdentity)
        /*package*/ TextView tvIdentity;
        @BindView(R.id.tvEnable)
        /*package*/ TextView tvEnable;
        @BindView(R.id.tvCreateTime)
        /*package*/ TextView tvCreateTime;
        @BindView(R.id.tvUpdateTime)
        /*package*/ TextView tvUpdateTime;
        @BindView(R.id.tvStudentId)
        /*package*/ TextView tvStudentId;
        @BindView(R.id.tvStudentName)
        /*package*/ TextView tvStudentName;
        @BindView(R.id.tvStudentSex)
        /*package*/ TextView tvStudentSex;
        @BindView(R.id.tvDepartment)
        /*package*/ TextView tvDepartment;
        @BindView(R.id.tvSubject)
        /*package*/ TextView tvSubject;
        @BindView(R.id.tvClazz)
        /*package*/ TextView tvClazz;
        @BindView(R.id.tvYear)
        /*package*/ TextView tvYear;
        @BindView(R.id.llStudentInfo)
        /*package*/ LinearLayout llStudentInfo;
        @BindView(R.id.tvTeacherId)
        /*package*/ TextView tvTeacherId;
        @BindView(R.id.tvTeacherName)
        /*package*/ TextView tvTeacherName;
        @BindView(R.id.tvTeacherSex)
        /*package*/ TextView tvTeacherSex;
        @BindView(R.id.llTeacherInfo)
        /*package*/ LinearLayout llTeacherInfo;

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public void bindUser(User user) {
            tvUsername.setText(user.getUsername());
            tvIdentity.setText(UserUtils.getIdentityString(user.getIdentity()));
            tvEnable.setText(UserUtils.getEnableString(user.isEnable()));
            tvCreateTime.setText(user.getCreateTime());
            tvUpdateTime.setText(user.getUpdateTime());
        }

        public void bindStudent(Student student) {
            if (student == null) {
                return;
            }
            llStudentInfo.setVisibility(View.VISIBLE);
            tvStudentId.setText(student.getStudentId());
            tvStudentName.setText(student.getName());
            tvStudentSex.setText(UserUtils.getSexString(student.getSex()));
            tvDepartment.setText(student.getClazz().getSubject().getDepartment().getName());
            tvSubject.setText(student.getClazz().getSubject().getName());
            tvClazz.setText(student.getClazz().getName());
            tvYear.setText(student.getClazz().getYear() + "");
        }

        public void bindTeacher(Teacher teacher) {
            if (teacher == null) {
                return;
            }
            llTeacherInfo.setVisibility(View.VISIBLE);
            tvTeacherId.setText(teacher.getTeacherId());
            tvTeacherName.setText(teacher.getName());
            tvTeacherSex.setText(UserUtils.getSexString(teacher.getSex()));
        }
    }

    public static class ContentViewHolder extends BaseViewHolder {

        @BindView(R.id.tvTime)
        /*package*/ TextView tvTime;
        @BindView(R.id.tvIp)
        /*package*/ TextView tvIp;

        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(LoginRecord data) {
            tvTime.setText(data.getTime());
            tvIp.setText(data.getIp());
        }
    }
}
