package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.EvaluateActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;
import com.hxh19950701.teachingevaluateclient.utils.CourseUtils;

import java.util.List;

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

    public static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCourse;
        private TextView tvStatus;
        private TextView tvTime;
        private TextView tvScore;

        private Course course;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView) itemView.findViewById(R.id.tvCourse);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);
        }

        public void bindData(Course course) {
            this.course = course;
            tvCourse.setText(course.getName());
            tvStatus.setText(course.getEvaluatedPersonCount() == course.getTotalPersonCount()
                    ? "评价已完成"
                    : "已有 " + course.getEvaluatedPersonCount() + " 人评价");
            tvScore.setText(course.getScore()+"");
            tvTime.setText(CourseUtils.formatCourseTime(course.getYear(), course.getTerm()));
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            context.startActivity(EvaluateActivity.newIntent(context, course.getId(), Constant.IDENTITY_TEACHER, true));
        }
    }
}
