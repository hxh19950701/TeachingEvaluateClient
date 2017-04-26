package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.Clazz;

import java.util.List;

import butterknife.BindView;

public class ClazzAdapter extends RecyclerView.Adapter<ClazzAdapter.ViewHolder> {

    private List<Clazz> data;

    public ClazzAdapter(List<Clazz> data) {
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

        private Clazz clazz;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(Clazz clazz) {
            this.clazz = clazz;
            tvText.setText(clazz.getYear() + "级 " + clazz.getName());
        }
    }
}