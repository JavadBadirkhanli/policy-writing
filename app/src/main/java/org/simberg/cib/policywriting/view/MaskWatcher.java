package org.simberg.cib.policywriting.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by javadbadirkhanly on 12/14/17.
 */

public class MaskWatcher implements TextWatcher {

    private boolean isRunning = false;
    private boolean isDeleting = false;
    private String mask;

    public MaskWatcher(String mask){
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
