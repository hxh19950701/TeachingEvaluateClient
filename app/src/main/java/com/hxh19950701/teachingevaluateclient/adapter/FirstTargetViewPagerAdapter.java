package com.hxh19950701.teachingevaluateclient.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.fragment.FirstTargetFragment;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;

public class FirstTargetViewPagerAdapter extends FragmentStatePagerAdapter {

    private final int FIRST_TARGET_COUNT = EvaluateTargetManager.getFirstTargets().size();

    private Fragment[] fragments = new Fragment[FIRST_TARGET_COUNT];
    private float[] score;

    public FirstTargetViewPagerAdapter(FragmentManager fm, float[] score, @Nullable FirstTargetFragment.OnItemScoreUpdateListener listener) {
        super(fm);
        this.score = score;
        for (int counter = 0; counter < FIRST_TARGET_COUNT; ++counter) {
            EvaluateFirstTarget firstTarget = EvaluateTargetManager.getFirstTargets().get(counter);
            fragments[counter] = new FirstTargetFragment(firstTarget, score, listener);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return FIRST_TARGET_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return EvaluateTargetManager.getFirstTargets().get(position).getName();
    }
}
