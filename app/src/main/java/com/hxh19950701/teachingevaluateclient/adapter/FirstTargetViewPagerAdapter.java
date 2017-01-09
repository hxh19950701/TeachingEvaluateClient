package com.hxh19950701.teachingevaluateclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hxh19950701.teachingevaluateclient.fragment.FirstTargetFragment;
import com.hxh19950701.teachingevaluateclient.manager.EvaluateTargetManager;

import java.util.Map;

public class FirstTargetViewPagerAdapter extends FragmentStatePagerAdapter {

    private int firstTargetCount = EvaluateTargetManager.getFirstTargets().size();
    private Fragment[] fragments = new Fragment[firstTargetCount];
    private Map<Integer, Float> score;

    public FirstTargetViewPagerAdapter(FragmentManager fm, Map<Integer, Float> score) {
        super(fm);
        this.score = score;
        for (int counter = 0; counter < firstTargetCount; ++counter) {
            fragments[counter] = new FirstTargetFragment(EvaluateTargetManager.getFirstTargets().get(counter), score);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return firstTargetCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return EvaluateTargetManager.getFirstTargets().get(position).getName();
    }
}
