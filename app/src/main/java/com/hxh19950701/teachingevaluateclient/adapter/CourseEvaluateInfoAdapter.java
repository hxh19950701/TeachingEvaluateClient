package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;
import com.hxh19950701.teachingevaluateclient.bean.response.TeacherCourseEvaluate;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressWarnings("WeakerAccess")
public class CourseEvaluateInfoAdapter extends HeaderAndFooterRecyclerViewAdapter<
        CourseEvaluateInfoAdapter.HeaderViewHolder, CourseEvaluateInfoAdapter.ContentViewHolder, RecyclerView.ViewHolder> {

    private TeacherCourseEvaluate max;
    private TeacherCourseEvaluate min;
    private List<StudentCourseInfo> info;
    private ItemClickListener listener;

    public CourseEvaluateInfoAdapter(TeacherCourseEvaluate max,
                                     TeacherCourseEvaluate min,
                                     List<StudentCourseInfo> info,
                                     ItemClickListener listener) {
        this.max = max;
        this.min = min;
        this.info = info;
        this.listener = listener;
    }

    @Override
    public int getHeaderCount() {
        return 1;
    }

    @Override
    public int getContentItemCount() {
        return info == null ? 0 : info.size();
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.header_course_brief, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public ContentViewHolder onCreateContentItemViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_trend, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.bindData(max, min);
    }

    @Override
    public void onBindContentItemViewHolder(ContentViewHolder holder, int position) {
        holder.bindData(info.get(position), listener);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class HeaderViewHolder extends BaseViewHolder {

        @BindView(R.id.tvMaxThirdTargetName)
        TextView tvMaxThirdTargetName;
        @BindView(R.id.tvMaxScore)
        TextView tvMaxScore;
        @BindView(R.id.tvMaxTotalScore)
        TextView tvMaxTotalScore;
        @BindView(R.id.tvMinThirdTargetName)
        TextView tvMinThirdTargetName;
        @BindView(R.id.tvMinScore)
        TextView tvMinScore;
        @BindView(R.id.tvMinTotalScore)
        TextView tvMinTotalScore;

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(TeacherCourseEvaluate max, TeacherCourseEvaluate min) {
            tvMaxThirdTargetName.setText(max.getItem().getName());
            tvMaxScore.setText(max.getScore() + "分");
            tvMaxTotalScore.setText(max.getItem().getTotalScore() + "分");
            tvMinThirdTargetName.setText(min.getItem().getName());
            tvMinScore.setText(min.getScore() + "分");
            tvMinTotalScore.setText(min.getItem().getTotalScore() + "分");
        }

    }

    public static class ContentViewHolder extends BaseViewHolder {

        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvScore)
        TextView tvScore;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvReply)
        TextView tvReply;

        private StudentCourseInfo info;
        private ItemClickListener listener;

        public ContentViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(StudentCourseInfo data, ItemClickListener listener) {
            this.info = data;
            this.listener = listener;
            tvComment.setText(TextUtils.isEmpty(data.getComment()) ? "没有提交评论。" : data.getComment());
            tvScore.setText(data.getScore() + "");
            tvTime.setText(data.getCompleteTime());
            tvReply.setVisibility(TextUtils.isEmpty(data.getReply()) ? View.GONE : View.VISIBLE);
            tvReply.setText("你的回复：" + data.getReply());
        }

        @OnClick(R.id.cvTrend)
        public void reply() {
            listener.onItemClick(info);
        }
    }

    public interface ItemClickListener {
        void onItemClick(StudentCourseInfo item);
    }
}
