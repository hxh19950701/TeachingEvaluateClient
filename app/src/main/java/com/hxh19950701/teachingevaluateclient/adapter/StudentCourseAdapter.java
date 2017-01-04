package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.bean.service.StudentCourseInfo;

import java.util.List;

public class StudentCourseAdapter extends BaseAdapter {

    protected List<StudentCourseInfo> data;

    public StudentCourseAdapter(List<StudentCourseInfo> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        try {
            return data.size();
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
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        }
        ViewHolder viewHolder = getViewHolder(convertView);
        viewHolder.tvCourseName.setText(data.get(position).getCourse().getName());
        viewHolder.tvScore.setText(data.get(position).getScore() < 0 ? "未完成评价" : data.get(position).getScore() + "");
        viewHolder.tvTeacher.setText(data.get(position).getCourse().getTeacher().getName());
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
