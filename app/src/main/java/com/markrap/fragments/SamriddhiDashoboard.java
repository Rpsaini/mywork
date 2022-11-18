package com.markrap.fragments;

import static com.markrap.utility.AppConstants.id;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.adapter.NotificationAdapter;
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


public class SamriddhiDashoboard extends Fragment {
    public static final String TAG = "SamriddhiDashoboard";
    Spinner spinnerMonth;
    TextView txtTillDate, txtValueofMAppedStockist, txtValueTaget, txtValueAchievment, txtValueBilling, txtThresholdTargetPerc, txtBusinesssTargetPercent, txtRangePercent, txtFPPercent, txtFP2AchievmentsPercentage;
    private View rootView;
    private SeekBar seekBarBilling, seekbarThreshhold, seekBarBusinessTarget, seekBarRange, seekkBarFPOne, seekBarFBTwo;
    private MainActivity mainActivity;

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
        mainActivity = (MainActivity) getActivity();

        initView();
        getSamriddhiEntryDashboardData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@SamriddhiDashoboard", "onResume---");
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

    // https://neptunesolution.in/marketstage/api/smriddhi_dashboard/24/2022-12-22
    private void getSamriddhiEntryDashboardData() {
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", mainActivity.getLoginData("id"));
        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr + "entrydates/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiDahboardData====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONArray dataAr = obj.getJSONArray("data");
                        //  JSONObject jsonObject = dataAr.getJSONObject(0);
                        //String enterdates=jsonObject.getString("enterdates");
                        //System.out.println("@@enterdates====" + enterdates);
                        //  wdIdAr.add("Search by Entry Dates");
                        for (int x = 0; x < dataAr.length(); x++) {
                            JSONObject dataObje = dataAr.getJSONObject(x);

                            if (!wdIdAr.contains(dataObje.getString("enterdates"))) {
                                wdIdAr.add(dataObje.getString("enterdates"));
                                System.out.println("@@enterdates====" + wdIdAr.toString());
                            }
                            setFilterData(wdIdAr, storenameAr);

                            //storenameAr.add(dataObje.getString("store_name"));


                        }
                        //   showTask(obj.getJSONArray("data"));

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setFilterData(ArrayList<String> wdArray, ArrayList<String> nameAr) {
        Spinner spinnerMonth = rootView.findViewById(R.id.spinnerMonth);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, wdArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String selectedItemStr = spinnerMonth.getSelectedItem().toString();
                txtTillDate.setText(selectedItemStr);
                // JSONArray filterArray = new JSONArray();
                getSamriddhiDashboardData(selectedItemStr);
                //     if(position>0) {
            }


            //   } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getSamriddhiDashboardData(String entryDates) {
        System.out.println("getSamriddhiDashboardData" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", mainActivity.getLoginData("id"));

        m.put("date", entryDates);
        //+mainActivity.getLoginData("id")
        System.out.println("@@getSamriddhiDashboardData" + AppConstants.apiUlr + "smriddhi_dashboard/" + m);


        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr + "smriddhi_dashboard/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    System.out.println("getSamriddhiDahboardData====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");

                        String stock_mapped = dataAr.getString("stock_mapped");
                        txtValueofMAppedStockist.setText(stock_mapped);

                        String total_target = dataAr.getString("total_target");
                        txtValueTaget.setText(total_target);
                        String achieve_target = dataAr.getString("achieve_target");
                        txtValueAchievment.setText(achieve_target);
                        //   showTask(obj.getJSONArray("data"));
                        JSONObject jsonnumeric_achievement = dataAr.getJSONObject("numeric_achievement");
                        txtValueBilling.setText(jsonnumeric_achievement.getString("Billing"));
                        // txtThresholdTargetPerc.setText(jsonnumeric_achievement.getString("Billing"));
                        txtBusinesssTargetPercent.setText(jsonnumeric_achievement.getString("Business Target"));
                        txtRangePercent.setText(jsonnumeric_achievement.getString("Range"));
                        txtFPPercent.setText(jsonnumeric_achievement.getString("FP1"));
                        txtFP2AchievmentsPercentage.setText(jsonnumeric_achievement.getString("FP2"));

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}

