package com.redmadintern.mikhalevich.security.ui.view;

import android.view.View;
import android.view.ViewGroup;

import com.redmadintern.mikhalevich.security.R;

/**
 * Created by Alexander on 15.07.2015.
 */
public class KeyboardView implements View.OnClickListener{
    private OnClickListener clickListener;

    public KeyboardView(View v) {
        ViewGroup keyboardLayout = (ViewGroup)v.findViewById(R.id.keyboard_layout);
        int childCount = keyboardLayout.getChildCount();
        for (int i = 0; i < childCount; i++)
            keyboardLayout.getChildAt(i).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (clickListener == null)
            return;

        String tag = (String)view.getTag();
        if (tag.equals("Del")) {
            clickListener.onDelClicked();
        } else {
            clickListener.onNumberClicked(tag.charAt(0));
        }
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnClickListener {
        void onNumberClicked(char num);
        void onDelClicked();
    }
}
