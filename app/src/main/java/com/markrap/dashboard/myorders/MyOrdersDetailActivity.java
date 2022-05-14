package com.markrap.dashboard.myorders;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.adapter.MyOrderDetailsAdapter;
import com.markrap.adapter.OutLetsAdapter;
import com.markrap.adapter.ViewCartItemsAdapter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.dashboard.outlet.AddOutlet;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyOrdersDetailActivity extends BaseActivity {
    private MainActivity mainActivity;
   private TextView order_date,txtTotalPrice,order_id,order_comment;

    @Override
    protected void setUp() {
        init();
    }


    private void init() {


        findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView customeToolbartext = findViewById(R.id.customeToolbartext);
         order_date = findViewById(R.id.order_date);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        order_id = findViewById(R.id.order_id);
        order_comment = findViewById(R.id.order_comment);
        customeToolbartext.setText("Order Details");


        getOrderDetails();


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_myorder_details;
    }



    private void showOrderData(JSONArray data) {
        RecyclerView cartItemRecycler = findViewById(R.id.cartItemRecycler);
        ViewCartItemsAdapter mAdapter = new ViewCartItemsAdapter(data, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartItemRecycler.setLayoutManager(horizontalLayoutManagaer);
        cartItemRecycler.setItemAnimator(new

                DefaultItemAnimator());
        cartItemRecycler.setAdapter(mAdapter);

    }


    public void getOrderDetails() {
        //  String id=getIntent().getStringExtra(AppConstants.id);
        LinkedHashMap<String, String> m = new LinkedHashMap<>();


        Map<String, String> headerMap = new HashMap<>();
      //  System.out.println("getOrderDetails===" + AppConstants.apiUlr + "order/" + getIntent().getStringExtra(AppConstants.id));

        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "order/" + getIntent().getStringExtra(AppConstants.id), m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getOrderDetails====" + dta);
                    JSONObject obj = new JSONObject(dta);

                    if (obj.getInt("result") > 0) {

                        JSONObject data = obj.getJSONObject("data");
                        txtTotalPrice.setText("Total Amount : "+data.getString("amount"));
                        order_date.setText("Order Date : "+data.getString("order_date"));
                        order_id.setText("Order ID : "+data.getString("id"));
                        order_comment.setText("Comments : "+data.getString("comments"));

                       JSONArray itemAr= data.getJSONArray("items");


                        showOrderData(itemAr);



//                        "id":"5",
//                                "order_by":"12",
//                                "order_id":"100005",
//                                "outlet_id":"1",
//                                "order_date":"2022-03-16",
//                                "amount":"10",
//                                "status":"new",
//                                "image":"",
//                                "comments":null,
//                                "add_date":null,
//                                "upd_date":null,
//                                "first_name":"Navin",
//                                "last_name":"Singh",
//                                "outlet_name":"Outlet 1",



                    } else {
                        mainActivity.showErrorDialog(obj.getString("msg"));


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
