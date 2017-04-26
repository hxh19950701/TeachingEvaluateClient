package com.hxh19950701.teachingevaluateclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseViewHolder;
import com.hxh19950701.teachingevaluateclient.bean.response.User;
import com.hxh19950701.teachingevaluateclient.utils.UserUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private List<User> data;
    private ItemClickListener listener;

    public List<User> getData() {
        return data;
    }

    public UserRecyclerViewAdapter(List<User> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvUsername)
        /*package*/ TextView tvUsername;
        @BindView(R.id.tvEnable)
        /*package*/ TextView tvEnable;
        @BindView(R.id.tvIdentity)
        /*package*/ TextView tvIdentity;
        @BindView(R.id.tvCreateTime)
        /*package*/ TextView tvCreateTime;

        private User user;
        private ItemClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(User user, ItemClickListener listener) {
            this.user = user;
            this.listener = listener;
            tvUsername.setText(user.getUsername());
            tvEnable.setText(user.isEnable() ? "已启用" : "未启用");
            tvEnable.setTextColor(user.isEnable() ? Color.BLUE : Color.RED);
            tvIdentity.setText(UserUtils.getIdentityString(user.getIdentity()));
            tvCreateTime.setText(user.getCreateTime().toString());
        }

        @OnClick(R.id.llUser)
        public void click() {
            listener.onItemClick(getAdapterPosition(), user);
        }

        @OnLongClick(R.id.llUser)
        public boolean longClick(){
            listener.onItemLongClick(getAdapterPosition(), user);
            return true;
        }
    }

    public static interface ItemClickListener {
        void onItemClick(int position, User user);
        void onItemLongClick(int position, User user);
    }
}