package com.redmadintern.mikhalevich.security.ui.view;

import android.view.View;
import android.widget.CheckBox;

import com.redmadintern.mikhalevich.security.R;

/**
 * Created by Alexander on 15.07.2015.
 */
public class PinView {
    private CheckBox[] pins;

    public PinView(View parent) {
        pins = new CheckBox[4];
        pins[0] = (CheckBox)parent.findViewById(R.id.pin_1);
        pins[1] = (CheckBox)parent.findViewById(R.id.pin_2);
        pins[2] = (CheckBox)parent.findViewById(R.id.pin_3);
        pins[3] = (CheckBox)parent.findViewById(R.id.pin_4);
    }

    public void check(int index) {
        pins[index].setChecked(true);
    }

    public void uncheck(int index) {
        pins[index].setChecked(false);
    }
}
