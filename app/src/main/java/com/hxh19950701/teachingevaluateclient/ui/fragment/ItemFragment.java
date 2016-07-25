package com.hxh19950701.teachingevaluateclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxh19950701.teachingevaluateclient.R;

/**
 * Created by hxh19950701 on 2016/7/13.
 */
public class ItemFragment extends Fragment {

    protected String name;

    public ItemFragment(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adapter_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setText(name);
        return view;
    }

}
