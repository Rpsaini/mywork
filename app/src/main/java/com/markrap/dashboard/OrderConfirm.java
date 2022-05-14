package com.markrap.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.markrap.R;
import com.markrap.base.BaseActivity;

public class OrderConfirm extends BaseActivity implements View.OnClickListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, OrderConfirm.class);
    }

    @Override
    protected void setUp() {
        init();
    }

    private void init() {
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public void onClick(View v) {


    }
}
