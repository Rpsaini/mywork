package com.markrap.dashboard.outlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.markrap.R;
import com.markrap.adapter.CategegoryAdapter;
import com.markrap.adapter.SearchStockistAdapter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchStockist extends BaseActivity {


ArrayList<String> searrchStrigAr=new ArrayList<>();
    @Override
    protected void setUp() {
        getStores();

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText adressText =findViewById(R.id.adressText);
        findViewById(R.id.clearText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                adressText.setText("");
                initRecycler(mainDataAr);

            }
        });

        adressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length()>1)
                {
                    System.out.println("Is conatine==="+s.toString().toLowerCase());
                    for(int x=0;x<searrchStrigAr.size();x++)
                    {
                        String srchStr=searrchStrigAr.get(x);
                        if (srchStr.contains(s.toString().toLowerCase())) {

                            int index = searrchStrigAr.indexOf(srchStr);
                            ArrayList<JSONObject> filterList = new ArrayList<>();
                            filterList.add(mainDataAr.get(index));
                            initRecycler(filterList);

                        }
                    }

                }
            }
        });



    }

    @Override
    protected int setLayout() {
        return R.layout.activity_search_stockist2;
    }


    private void initRecycler(ArrayList<JSONObject> dataAr)
    {
        RecyclerView recyclerlistview = findViewById(R.id.searchStockistRecycler);
        SearchStockistAdapter mAdapter = new SearchStockistAdapter(dataAr, this);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }


    ArrayList<JSONObject> mainDataAr=new ArrayList<>();
    public void getStores()
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"mystockistoutlets/"+getLoginData("id"), m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    searrchStrigAr.clear();
                    mainDataAr.clear();
                    System.out.println("Stockist Data===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        JSONArray dataAr =obj.getJSONArray("data");

                        for(int x=0;x<dataAr.length();x++) {
                            searrchStrigAr.add(dataAr.getJSONObject(x).getString("store_name").toLowerCase());
                            mainDataAr.add(dataAr.getJSONObject(x));
                        }

                        initRecycler(mainDataAr);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}