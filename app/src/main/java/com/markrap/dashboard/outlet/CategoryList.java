package com.markrap.dashboard.outlet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.markrap.R;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markrap.R;
import com.markrap.adapter.CategegoryAdapter;
import com.markrap.adapter.OutLetsAdapter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryList extends BaseActivity {
    LinearLayout rrCheckedIn;


    @Override
    protected void setUp() {

        init();
        getCategory();
    }


    private void init() {

        findViewById(R.id.ttolbarplus).setVisibility(View.GONE);


        findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String storename = getIntent().getStringExtra(AppConstants.outlet_name);


        TextView customeToolbartext = findViewById(R.id.customeToolbartext);
        customeToolbartext.setText(storename);


        //getCategory();


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_category_list2;
    }


    private void initView(JSONArray dataAr) {
        try {

            RecyclerView recyclerlistview = findViewById(R.id.recyclerlistview);
            CategegoryAdapter mAdapter = new CategegoryAdapter(dataAr, this);
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
            recyclerlistview.setItemAnimator(new DefaultItemAnimator());
            recyclerlistview.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getCategory()
    {

        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();

        //"+getIntent().getStringExtra(AppConstants.id)
        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"categories", m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        initView(obj.getJSONArray("data"));
                    }
                    else
                    {
                        showErrorDialog(obj.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}





