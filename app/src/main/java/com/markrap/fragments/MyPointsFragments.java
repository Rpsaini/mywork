package com.markrap.fragments;

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

import org.json.JSONArray;


public class MyPointsFragments extends Fragment {
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
         rootView = inflater.inflate(R.layout.mypoints_fragment, container, false);

        initView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@MainHome_ONResume---", "onResume---");
    }










    private void initView() {
        RecyclerView recyclerlistview=rootView.findViewById(R.id.recyclerlistview);
        NotificationAdapter mAdapter = new NotificationAdapter(new JSONArray(),(MainActivity)getActivity());
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }






}

