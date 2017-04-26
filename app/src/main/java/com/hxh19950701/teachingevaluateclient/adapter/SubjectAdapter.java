package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.activity.ClazzManageActivity;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.Subject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> data;

    public SubjectAdapter(List<Subject> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_single_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvText)
        /*package*/ TextView tvText;

        private Subject subject;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(Subject subject) {
            this.subject = subject;
            tvText.setText(subject.getName());
        }

        @OnClick(R.id.llText)
        public void lookupSubject() {
            Context context = itemView.getContext();
            context.startActivity(ClazzManageActivity.newIntent(context, subject.getId()));
        }
    }
}