package com.hxh19950701.teachingevaluateclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hxh19950701.teachingevaluateclient.Bean.ItemBean;
import com.hxh19950701.teachingevaluateclient.ui.fragment.ItemFragment;

public class ItemAdapter extends FragmentPagerAdapter {

    ItemBean itemBean;

    public ItemAdapter(FragmentManager fm, ItemBean itemBean) {
        super(fm);
        this.itemBean = itemBean;
    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment itemFragment = new ItemFragment(itemBean.getTargetList().get(position).getName());
        return itemFragment;
    }

    @Override
    public int getCount() {
        return itemBean.getTargetList().size();
    }
}
