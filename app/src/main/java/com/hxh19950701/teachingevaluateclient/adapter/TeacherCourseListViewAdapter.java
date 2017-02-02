package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.common.Constant;

import java.util.List;

public class TeacherCourseListViewAdapter extends BaseAdapter {

    private List<Course> data;

    public TeacherCourseListViewAdapter(List<Course> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_course, parent, false);
        }
        ViewHolder viewHolder = getViewHolder(convertView);
        viewHolder.tvCourseName.setText(data.get(position).getName());
        viewHolder.tvScore.setText(data.get(position).getScore() + "");
        viewHolder.tvCourseInfo.setText(data.get(position).getYear() + "年" + (data.get(position).getTerm() == Constant.TERM_FIRST ? "上学期" : "下学期"));
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
        public final TextView tvCourseInfo;
        public final TextView tvScore;
        public final View root;

        public ViewHolder(View root) {
            tvCourseName = (TextView) root.findViewById(R.id.tvCourseName);
            tvScore = (TextView) root.findViewById(R.id.tvScore);
            tvCourseInfo = (TextView) root.findViewById(R.id.tvCourseInfo);
            this.root = root;
        }
    }
}
