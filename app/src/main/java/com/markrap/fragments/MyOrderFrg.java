package com.markrap.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markrap.R;
import com.markrap.adapter.MyOrderAdapter;
import com.markrap.adapter.NotificationAdapter;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;

import org.json.JSONArray;



import android.content.Context;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.markrap.R;
        import com.markrap.adapter.NotificationAdapter;
        import com.markrap.adapter.OrderListAdapter;
        import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class MyOrderFrg extends Fragment {
    public static final String TAG = "HomeFragments";

    private View rootView;
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                MyPointsFragments.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_order_frg, container, false);

        System.out.println("Hereeeeeee=====");

        getMyOders();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void initView(JSONArray dataAr) {
        RecyclerView recyclerlistview=rootView.findViewById(R.id.recyclerlistview);
        MyOrderAdapter mAdapter = new MyOrderAdapter(dataAr,(MainActivity)getActivity());
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }


    private void getMyOders()
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr + "myorders/"+((MainActivity)getActivity()).getLoginData("id"), m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("myorders====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0)
                    {

                        initView(obj.getJSONArray("data"));

                    } else {
                        ((MainActivity) getActivity()).showErrorDialog(obj.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }



}

