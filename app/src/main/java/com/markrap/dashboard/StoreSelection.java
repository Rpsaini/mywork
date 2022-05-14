package com.markrap.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.login.Signup;

public class StoreSelection extends BaseActivity implements View.OnClickListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, StoreSelection.class);
    }

    @Override
    protected void setUp() {
        init();
    }

    private void init() {
    }

    @Override
    protected int setLayout() {
        return R.layout.itemview_store_list_selection;
    }

    @Override
    public void onClick(View v) {


    }
}
