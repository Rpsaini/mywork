package com.markrap.dashboard.outlet;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markrap.R;
import com.markrap.adapter.OutLetsAdapter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.fragments.AllStoresFrg;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewOutLets extends BaseActivity {
    LinearLayout rrCheckedIn;
     SwipeRefreshLayout swipeRefreshLayout;
    private GpsTracker gpsTracker;
    ProgressDialog pd;
    double latitude, longitude;

    @Override
    protected void setUp() {

        init();
        initView();
    }


    private void init() {

        findViewById(R.id.ttolbarplus).setVisibility(View.VISIBLE);
        findViewById(R.id.ttolbarplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ViewOutLets.this,AddOutlet.class);
                intent.putExtra(AppConstants.storeName,getIntent().getStringExtra(AppConstants.storeName));
                intent.putExtra(AppConstants.id,getIntent().getStringExtra(AppConstants.id));
                startActivityForResult(intent,1001);
            }
        });

        findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String storename=getIntent().getStringExtra(AppConstants.storeName);

        TextView customeToolbartext=findViewById(R.id.customeToolbartext);
        customeToolbartext.setText(storename);

        getLocation();
        //getCategory();


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_view_out_lets;
    }


    private void initView() {
        try {


            JSONArray dataObj = AllStoresFrg.selectedSToreArray;//new JSONArray(getIntent().getStringExtra(AppConstants.outlets));
            System.out.println("Dataa==="+dataObj);
            RecyclerView recyclerlistview = findViewById(R.id.recyclerlistview);
            OutLetsAdapter mAdapter = new OutLetsAdapter(dataObj, this);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
            recyclerlistview.setItemAnimator(new DefaultItemAnimator());
            recyclerlistview.setAdapter(mAdapter);



            swipeRefreshLayout = findViewById(R.id.pullToRefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    getStores();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getLocation() {
        pd = new ProgressDialog(ViewOutLets.this);
        gpsTracker = new GpsTracker(ViewOutLets.this);
        if (gpsTracker.canGetLocation()) {

            pd.setMessage("Fetching your current location");
            pd.show();
            pd.setCancelable(false);
            callHandler();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    final Handler handler = new Handler();

    private void callHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                if (latitude > 0 && longitude > 0) {
                    pd.dismiss();
                    PrefHelper.getInstance().storeSharedValue("lat",String.valueOf(latitude));
                    PrefHelper.getInstance().storeSharedValue("lon",String.valueOf(longitude));

                    System.out.println("Lat lnt===AllStoresFg" + latitude + "AllStoresFg==" + longitude);
                }

            }
        }, 1000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("@@OnResue--ata hai","ViewOutlets");
        getStores();
    }

    public void getStores()
    {

        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"mystockistoutlets/"+getLoginData("id"), m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    System.out.println("Stockistdata===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    swipeRefreshLayout.setRefreshing(false);
                    if (obj.getInt("result")>0) {

                        JSONArray dataAr = obj.getJSONArray("data");
                        for (int x = 0; x < dataAr.length(); x++) {
                            JSONObject dataObje = dataAr.getJSONObject(x);

                            if (dataObje.getString("id").equalsIgnoreCase(getIntent().getStringExtra(AppConstants.id)))
                            {

                                System.out.println("Selected stockist==="+dataObje);
                                AllStoresFrg.selectedSToreArray=dataObje.getJSONArray("outlets");
                                initView();
                                break;
                            }


                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
