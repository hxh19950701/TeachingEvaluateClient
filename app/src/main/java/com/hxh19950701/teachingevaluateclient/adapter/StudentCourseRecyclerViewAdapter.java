package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.activity.StudentEvaluateActivity;
import com.hxh19950701.teachingevaluateclient.utils.CourseUtils;

import java.util.List;

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

    public static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCourse;
        private TextView tvTeacher;
        private TextView tvScore;
        private TextView tvTime;
        private TextView tvComment;
        private TextView tvReply;
        private CardView cvItem;

        private Course course;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView) itemView.findViewById(R.id.tvCourse);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore);
            tvTeacher = (TextView) itemView.findViewById(R.id.tvTeacher);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvReply = (TextView) itemView.findViewById(R.id.tvReply);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);
            cvItem.setOnClickListener(this);
        }

        public void bindData(StudentCourseInfo data) {
            this.course = data.getCourse();
            tvCourse.setText(course.getName());
            tvTeacher.setText(course.getTeacher().getName());
            tvScore.setVisibility(data.getScore() >= 0 ? View.VISIBLE : View.GONE);
            tvScore.setText(data.getScore() + "");
            tvTime.setText(CourseUtils.formatCourseTime(course.getYear(), course.getTerm()));
            tvComment.setVisibility(TextUtils.isEmpty(data.getComment()) ? View.GONE : View.VISIBLE);
            tvComment.setText("你：" + data.getComment());
            tvReply.setVisibility(TextUtils.isEmpty(data.getReply()) ? View.GONE : View.VISIBLE);
            tvReply.setText("教师回复：" + data.getReply());
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            int courseId = course.getId();
            Intent intent = new Intent(context, StudentEvaluateActivity.class);
            intent.putExtra(Constant.KEY_COURSE_ID, courseId);
            context.startActivity(intent);
        }
    }
}