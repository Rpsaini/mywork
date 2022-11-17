package com.markrap.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.adapter.NotificationAdapter;
import com.markrap.dashboard.MainActivity;

import org.json.JSONArray;


public class SamriddhiDashoboard extends Fragment {
    public static final String TAG = "SamriddhiDashoboard";
    Spinner spinnerMonth;
    TextView txtTillDate, txtValueofMAppedStockist, txtValueTaget, txtValueAchievment, txtValueBilling, txtThresholdTargetPerc, txtBusinesssTargetPercent, txtRangePercent, txtFPPercent, txtFP2AchievmentsPercentage;
    private View rootView;
    private SeekBar seekBarBilling, seekbarThreshhold, seekBarBusinessTarget, seekBarRange, seekkBarFPOne, seekBarFBTwo;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                SamriddhiDashoboard.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.samriddhi_dashoboard, container, false);

        initView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@MainHome_ONResume---", "onResume---");
    }

    private void initView() {
        spinnerMonth = (Spinner) rootView.findViewById(R.id.spinnerMonth);
        txtTillDate = (TextView) rootView.findViewById(R.id.txtTillDate);
        txtValueofMAppedStockist = (TextView) rootView.findViewById(R.id.txtValueofMAppedStockist);
        txtValueTaget = (TextView) rootView.findViewById(R.id.txtValueTaget);
        txtValueAchievment = (TextView) rootView.findViewById(R.id.txtValueAchievment);

        txtValueBilling = (TextView) rootView.findViewById(R.id.txtValueBilling);
        txtThresholdTargetPerc = (TextView) rootView.findViewById(R.id.txtThresholdTargetPerc);
        txtBusinesssTargetPercent = (TextView) rootView.findViewById(R.id.txtBusinesssTargetPercent);
        txtRangePercent = (TextView) rootView.findViewById(R.id.txtRangePercent);
        txtFPPercent = (TextView) rootView.findViewById(R.id.txtFPPercent);
        txtFP2AchievmentsPercentage = (TextView) rootView.findViewById(R.id.txtFP2AchievmentsPercentage);
        seekBarBilling = (SeekBar) rootView.findViewById(R.id.seekBarBilling);
        seekbarThreshhold = (SeekBar) rootView.findViewById(R.id.seekbarThreshhold);
        seekBarBusinessTarget = (SeekBar) rootView.findViewById(R.id.seekBarBusinessTarget);
        seekBarRange = (SeekBar) rootView.findViewById(R.id.seekBarRange);
        seekkBarFPOne = (SeekBar) rootView.findViewById(R.id.seekkBarFPOne);
        seekBarFBTwo = (SeekBar) rootView.findViewById(R.id.seekBarFBTwo);


    }


}

