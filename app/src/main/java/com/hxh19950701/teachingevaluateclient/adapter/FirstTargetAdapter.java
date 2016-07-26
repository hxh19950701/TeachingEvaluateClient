package com.hxh19950701.teachingevaluateclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hxh19950701.teachingevaluateclient.Bean.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.Bean.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.ui.fragment.FirstTargetFragment;

import java.util.ArrayList;
import java.util.List;

public class FirstTargetAdapter extends FragmentStatePagerAdapter {

    List<First> first = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    public FirstTargetAdapter(FragmentManager fm, List<EvaluateThirdTarget> evaluateThirdTargets, float[] score) {
        super(fm);
        for (int i = 0; i < evaluateThirdTargets.size(); ++i) {
            int pos = find(evaluateThirdTargets.get(i).getSecondTarget().getFirstTarget().getId());
            if (pos == first.size()) {
                first.add(new First(evaluateThirdTargets.get(i).getSecondTarget().getFirstTarget()));
            }
            first.get(pos).third.add(evaluateThirdTargets.get(i));
        }
        for (int i = 0; i < first.size(); ++i) {
            fragments.add(new FirstTargetFragment(first.get(i).third, score));
        }
    }


    protected int find(int id) {
        for (int i = 0; i < first.size(); ++i) {
            if (first.get(i).evaluateFirstTarget.getId() == id) {
                return i;
            }
        }
        return first.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return first.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return first.get(position).evaluateFirstTarget.getName();
    }

    private static class First {
        public EvaluateFirstTarget evaluateFirstTarget;
        public List<EvaluateThirdTarget> third = new ArrayList<>();

        public First(EvaluateFirstTarget evaluateFirstTarget) {
            this.evaluateFirstTarget = evaluateFirstTarget;
        }
    }
}
