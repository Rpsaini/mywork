package com.markrap.dashboard.outlet;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markrap.R;
import com.markrap.adapter.ShowAllProducts;
import com.markrap.adapter.ViewCartItemsAdapter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ProductList extends BaseActivity {
    LinearLayout rrCheckedIn;
    //    TextView itemCount;
    ShowAllProducts mAdapter;
    JSONArray dataAr;

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

        TextView toolText = findViewById(R.id.customeToolbartext);

        toolText.setText(getIntent().getStringExtra(AppConstants.category));

        getProducts();
        listener();


    }

    double totalPrice;
    Map<String, String> mainObject = new HashMap<>();

    private void listener() {
        findViewById(R.id.placeOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice = 0;
                try {
                    mainObject.clear();

                    JSONArray itemSData = new JSONArray();

                    for (Map.Entry<Integer, Integer> map : mAdapter.cartItems.entrySet()) {

                        JSONObject dataObj = dataAr.getJSONObject(map.getKey());
                        JSONObject dataToServer = new JSONObject();
                        dataToServer.put("pid", dataObj.getString("id"));
                        dataToServer.put("qty", map.getValue() + "");
                        dataToServer.put("title", dataObj.getString("title"));
                        itemSData.put(dataToServer);
                        totalPrice = totalPrice + Double.parseDouble(dataObj.getString("price"));

                    }
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    mainObject.put("items", itemSData + "");
                    mainObject.put("user_id", getLoginData("id"));
                    mainObject.put("outlet_id", getIntent().getStringExtra(AppConstants.id));
                    mainObject.put("order_date", currentDate);
                    mainObject.put("amount", totalPrice + "");


                    placeOrder(totalPrice + "", itemSData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void submitOrder(Map<String, String> map) {
        System.out.println("Datato send===" + map);
        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "addorder", map, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("addorder====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        showErrorDialog(obj.getString("msg"), true);
                        ;
                    } else {
                        showErrorDialog(obj.getString("errors"), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_category2;
    }


    private void initView(JSONArray dataAr) {
        try {

            this.dataAr = dataAr;
            RecyclerView recyclerlistview = findViewById(R.id.recyclerlistview);
            mAdapter = new ShowAllProducts(dataAr, this);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
            recyclerlistview.setItemAnimator(new DefaultItemAnimator());
            recyclerlistview.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getProducts() {
        String id = getIntent().getStringExtra(AppConstants.id);
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "category_products/" + getIntent().getStringExtra(AppConstants.id), m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("products====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        initView(obj.getJSONArray("data"));
                    } else {
                        showErrorDialog(obj.getString("msg"), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void calculate(Map<Integer, Integer> map) {


        int count = 0;
        for (Map.Entry<Integer, Integer> ma : map.entrySet()) {
            count = ma.getValue() + count;

        }
        //  itemCount.setText(count + "");

        if (count > 0) {
            findViewById(R.id.llshowCart).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llshowCart).setVisibility(View.GONE);
        }


    }


    public void placeOrder(String amount, JSONArray dataAr) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_show_cart_items);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        TextView txtError = dialog.findViewById(R.id.txtTotalPrice);
        TextView customeToolbartext = dialog.findViewById(R.id.customeToolbartext);
        EditText edmytask = dialog.findViewById(R.id.edmytask);
        txtError.setText("Total Amount   Rs. " + amount);
        customeToolbartext.setText("Cart");

        RecyclerView cartItemRecycler = dialog.findViewById(R.id.cartItemRecycler);


        ViewCartItemsAdapter mAdapter = new ViewCartItemsAdapter(dataAr, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartItemRecycler.setLayoutManager(horizontalLayoutManagaer);
        cartItemRecycler.setItemAnimator(new DefaultItemAnimator());
        cartItemRecycler.setAdapter(mAdapter);


        dialog.findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mainObject.put("comments", edmytask.getText().toString());
                    submitOrder(mainObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        dialog.show();
    }


    public void showErrorDialog(String msg, boolean isTrue) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.showerror_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtError = dialog.findViewById(R.id.txtError);
        txtError.setText(msg);

        dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isTrue) {
                    finishAffinity();
                    startActivity(new Intent(ProductList.this, MainActivity.class));
                }

            }
        });

        dialog.show();
    }
}
