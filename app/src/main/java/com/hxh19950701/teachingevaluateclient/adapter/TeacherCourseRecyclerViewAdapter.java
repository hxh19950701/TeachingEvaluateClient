package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.CourseEvaluateInfoActivity;
import com.hxh19950701.teachingevaluateclient.activity.CourseInfoActivity;
import com.hxh19950701.teachingevaluateclient.activity.EvaluateActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.CourseUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TeacherCourseRecyclerViewAdapter extends RecyclerView.Adapter<TeacherCourseRecyclerViewAdapter.ContentViewHolder> {

    private List<Course> data;

    public TeacherCourseRecyclerViewAdapter(List<Course> data) {
        this.data = data;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_course, parent, false);
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
        @BindView(R.id.tvStatus)
        /*package*/ TextView tvStatus;
        @BindView(R.id.tvTime)
        /*package*/ TextView tvTime;
        @BindView(R.id.tvScore)
        /*package*/ TextView tvScore;

        private Course course;

        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(Course course) {
            this.course = course;
            tvCourse.setText(course.getName());
            tvStatus.setText(course.getEvaluatedPersonCount() >= course.getTotalPersonCount()
                    ? "评价已完成" : "已有 " + course.getEvaluatedPersonCount() + " 人评价");
            tvScore.setText(course.getScore() + "");
            tvTime.setText(CourseUtils.formatCourseTime(course.getYear(), course.getTerm()));
        }

        @OnClick(R.id.cvItem)
        public void viewDetail(View v) {
            if (course.getEvaluatedPersonCount() > 0) {
                Context context = itemView.getContext();
                context.startActivity(CourseEvaluateInfoActivity.newIntent(context, course.getId()));
            }
        }

        @OnLongClick(R.id.cvItem)
        public boolean showMoreOperationDialog() {
            Context context = itemView.getContext();
            new MaterialDialog.Builder(context).items("查看得分详情", "查看详细信息")
                    .itemsCallback((dialog, itemView, which, text) -> {
                        switch (which) {
                            case 0:
                                context.startActivity(EvaluateActivity.newIntent(context, course.getId(), Constant.IDENTITY_TEACHER, true));
                                break;
                            case 1:
                                context.startActivity(CourseInfoActivity.newIntent(context, course));
                                break;
                        }
                    })
                    .show();
            return true;
        }
    }
}