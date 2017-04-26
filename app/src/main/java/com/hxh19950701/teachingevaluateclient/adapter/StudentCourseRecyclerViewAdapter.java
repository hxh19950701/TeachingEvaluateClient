package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.CourseInfoActivity;
import com.hxh19950701.teachingevaluateclient.activity.EvaluateActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.CourseUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class StudentCourseRecyclerViewAdapter extends RecyclerView.Adapter<StudentCourseRecyclerViewAdapter.ContentViewHolder> {

    private List<StudentCourseInfo> data;

    public StudentCourseRecyclerViewAdapter(List<StudentCourseInfo> data) {
        this.data = data;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_course, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ContentViewHolder extends BaseViewHolder {

        @BindView(R.id.tvCourse)
        /*package*/ TextView tvCourse;
        @BindView(R.id.tvTeacher)
        /*package*/ TextView tvTeacher;
        @BindView(R.id.tvScore)
        /*package*/ TextView tvScore;
        @BindView(R.id.tvTime)
        /*package*/ TextView tvTime;
        @BindView(R.id.tvComment)
        /*package*/ TextView tvComment;
        @BindView(R.id.tvReply)
        /*package*/ TextView tvReply;

        private StudentCourseInfo data;

        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(StudentCourseInfo data) {
            this.data = data;
            tvCourse.setText(data.getCourse().getName());
            tvTeacher.setText(data.getCourse().getTeacher().getName());
            tvScore.setVisibility(data.getScore() >= 0 ? View.VISIBLE : View.GONE);
            tvScore.setText(data.getScore() + "");
            tvTime.setText(CourseUtils.formatCourseTime(data.getCourse().getYear(), data.getCourse().getTerm()));
            tvComment.setVisibility(TextUtils.isEmpty(data.getComment()) ? View.GONE : View.VISIBLE);
            tvComment.setText("你：" + data.getComment());
            tvReply.setVisibility(TextUtils.isEmpty(data.getReply()) ? View.GONE : View.VISIBLE);
            tvReply.setText("教师回复：" + data.getReply());
        }

        @OnClick(R.id.cvItem)
        public void viewDetail() {
            Context context = itemView.getContext();
            context.startActivity(EvaluateActivity.newIntent(context,
                    data.getCourse().getId(), Constant.IDENTITY_STUDENT, data.getScore() >= 0.0f));
        }

        @OnLongClick(R.id.cvItem)
        public boolean showMoreOperationDialog() {
            Context context = itemView.getContext();
            new MaterialDialog.Builder(context).items("查看详细信息")
                    .itemsCallback((dialog, itemView, which, text)
                            -> context.startActivity(CourseInfoActivity.newIntent(context, data.getCourse())))
                    .show();
            return true;
        }
    }
}