package com.hxh19950701.teachingevaluateclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by 绪浩 on 2016/11/17.
 * 含有头布局和脚布局的RecyclerViewAdapter
 *
 */

public abstract class HeaderAndFooterRecyclerViewAdapter
        <
            HeaderViewHolder extends RecyclerView.ViewHolder,
            ContentItemViewHolder extends RecyclerView.ViewHolder,
            FooterViewHolder extends RecyclerView.ViewHolder
        >
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOTER = 2;

    public abstract int getHeaderCount();

    public abstract int getContentItemCount();

    public abstract int getFooterCount();

    public abstract HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract ContentItemViewHolder onCreateContentItemViewHolder(ViewGroup parent);

    public abstract FooterViewHolder onCreateFooterViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(HeaderViewHolder holder, int position);

    public abstract void onBindContentItemViewHolder(ContentItemViewHolder holder, int position);

    public abstract void onBindFooterViewHolder(FooterViewHolder holder, int position);

    public boolean isHeaderView(int position) {
        int headerCount = getHeaderCount();
        return headerCount != 0 && position < headerCount;
    }

    public boolean isFooterView(int position) {
        return getFooterCount() != 0 && position >= getHeaderCount() + getContentItemCount();
    }

    public boolean isContentItemView(int position) {
        return !isHeaderView(position) && !isFooterView(position);
    }

    public int getHeaderPosition(int position) {
        if (isHeaderView(position)) {
            return position;
        } else {
            throw new IllegalArgumentException("position : " + position + " is not a header view!");
        }
    }

    public int getContentItemPosition(int position) {
        if (isContentItemView(position)) {
            return position - getHeaderCount();
        } else {
            throw new IllegalArgumentException("position : " + position + " is not a content item view!");
        }
    }

    public int getFooterPosition(int position) {
        if (isFooterView(position)) {
            return position - getHeaderCount() - getContentItemCount();
        } else {
            throw new IllegalArgumentException("position : " + position + " is not a footer view!");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return ITEM_TYPE_HEADER;
        } else if (isFooterView(position)) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);
            case ITEM_TYPE_CONTENT:
                return onCreateContentItemViewHolder(parent);
            case ITEM_TYPE_FOOTER:
                return onCreateFooterViewHolder(parent);
            default:
                throw new IllegalArgumentException("Unexpected view type!");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            int pos = getHeaderPosition(position);
            onBindHeaderViewHolder(headerViewHolder, pos);
        } else if (isFooterView(position)) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            int pos = getFooterPosition(position);
            onBindFooterViewHolder(footerViewHolder, pos);
        } else {
            ContentItemViewHolder contentItemViewHolder = (ContentItemViewHolder) holder;
            int pos = getContentItemPosition(position);
            onBindContentItemViewHolder(contentItemViewHolder, pos);
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getContentItemCount() + getFooterCount();
    }
}
