package com.hxh19950701.teachingevaluateclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.Bean.CourseListBean;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.application.TeachingEvaluateClientApplication;

public class StudentCourseAdapter extends BaseAdapter {

    protected CourseListBean data;

    public StudentCourseAdapter(CourseListBean data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        try {
            return data.getCourseList().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(TeachingEvaluateClientApplication.getApplication(), R.layout.item_course, null);
        }
        ViewHolder viewHolder = getViewHolder(convertView);
        viewHolder.tvCourseName.setText(data.getCourseList().get(position).getCourse().getName());
        viewHolder.tvScore.setText(data.getCourseList().get(position).getScore() < 0 ? "未完成评价" : data.getCourseList().get(position).getScore() + "");
        viewHolder.tvTeacher.setText(data.getCourseList().get(position).getCourse().getTeacher().getName());
        return convertView;
    }

    protected ViewHolder getViewHolder(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        return holder;
    }

    public static class ViewHolder {
        public final TextView tvCourseName;
        public final TextView tvTeacher;
        public final TextView tvScore;
        public final View root;

        public ViewHolder(View root) {
            tvCourseName = (TextView) root.findViewById(R.id.tvCourseName);
            tvScore = (TextView) root.findViewById(R.id.tvScore);
            tvTeacher = (TextView) root.findViewById(R.id.tvTeacher);
            this.root = root;
        }
    }
}
