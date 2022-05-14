package com.markrap.dashboard;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.adapter.ManageProductCartAdapter;
import com.markrap.base.BaseActivity;

import org.json.JSONArray;

public class ManageStoreProductCart extends BaseActivity {
    LinearLayout rrCheckedIn;



    @Override
    protected void setUp() {
        init();
        initView();

    }


    private  void init()
    {
        findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolText =findViewById(R.id.customeToolbartext);
      //  toolText.setText(getIntent().getStringExtra(AppConstants.storeName));


    }

    @Override
    protected int setLayout() {
        return R.layout.managestore_product_cart;
    }




    private void initView() {
        RecyclerView recyclerlistview=findViewById(R.id.recyclerlistview);
        ManageProductCartAdapter mAdapter = new ManageProductCartAdapter(new JSONArray(),ManageStoreProductCart.this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }




}
