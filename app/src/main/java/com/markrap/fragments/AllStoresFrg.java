package com.markrap.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;

import com.markrap.adapter.StockistAdapter;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class AllStoresFrg extends Fragment
{
    public static JSONArray selectedSToreArray;
    public static final String TAG = "HomeFragments";
     private View rootView;
    private GpsTracker gpsTracker;
    ProgressDialog pd;
    double latitude, longitude;
    JSONArray mainJsonArray;
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                AllStoresFrg.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.manageorder_fragment, container, false);


        getStores();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private  void setFilterData(ArrayList<String> wdArray,ArrayList<String> nameAr) {
        Spinner spinnerWdcode = rootView.findViewById(R.id.spinnerWdcode);
//        Spinner spinnerByName = rootView.findViewById(R.id.spinnerByName);
        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.autoCompleteTextView);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, wdArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWdcode.setAdapter(adapter);

//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, nameAr);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerByName.setAdapter(adapter2);

        spinnerWdcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String selectedItemStr = spinnerWdcode.getSelectedItem().toString();
                JSONArray filterArray = new JSONArray();

                if(position>0) {
                    for (int x = 0; x < mainJsonArray.length(); x++) {
                        try {
                            JSONObject dataObj = mainJsonArray.getJSONObject(x);
                            String idStr = dataObj.getString("wd_dest_code");

                            if (selectedItemStr.equalsIgnoreCase(idStr))
                            {
                                filterArray.put(dataObj);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        initRecycler(filterArray);
                    }
                }


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        spinnerByName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position>0) {
//                    String selectedItemStr = spinnerByName.getSelectedItem().toString();
//                    JSONArray filterArray = new JSONArray();
//                    for (int x = 0; x < mainJsonArray.length(); x++) {
//                        try {
//                            JSONObject dataObj = mainJsonArray.getJSONObject(x);
//                            String idStr = dataObj.getString("store_name");
//
//                            if (selectedItemStr.equalsIgnoreCase(idStr)) {
//                                filterArray.put(dataObj);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        initRecycler(filterArray);
//                    }
//                }
//
//            } // to close the onItemSelected
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, nameAr);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if(s.toString().length()>0) {
                    String selectedItemStr = s.toString();
                    JSONArray filterArray = new JSONArray();
                    for (int x = 0; x < mainJsonArray.length(); x++) {
                        try {
                            JSONObject dataObj = mainJsonArray.getJSONObject(x);
                            String idStr = dataObj.getString("store_name");

                            if (selectedItemStr.equalsIgnoreCase(idStr)) {
                                filterArray.put(dataObj);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        initRecycler(filterArray);
                    }
                }
                else
                {
                    initRecycler(mainJsonArray);
                }
            }

        });

    }


    private void initView(JSONArray dataAr)
    {
        mainJsonArray=dataAr;
        initRecycler(mainJsonArray);
    }
    private void initRecycler(JSONArray dataAr)
    {


        RecyclerView recyclerlistview=rootView.findViewById(R.id.recyclerlistview);
        StockistAdapter mAdapter = new StockistAdapter(dataAr,(MainActivity)getActivity(),this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }



    public void getStores()
    {
        ArrayList<String> wdIdAr=new ArrayList<>();
        ArrayList<String> storenameAr=new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr+"mystockistoutlets/"+((MainActivity)getActivity()).getLoginData("id"), m, 0, headerMap, 0, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
Log.i("StockistAPi___",""+AppConstants.apiUlr+"mystockistoutlets/"+((MainActivity)getActivity()).getLoginData("id"));
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        JSONArray dataAr=obj.getJSONArray("data");
                        initView(dataAr);

                        wdIdAr.add("Search by WD code");
                        storenameAr.add("Search by Stockist Name");
                        for(int x=0;x<dataAr.length();x++)
                        {
                            JSONObject dataObje=dataAr.getJSONObject(x);

                            if(!wdIdAr.contains(dataObje.getString("wd_dest_code"))) {
                                wdIdAr.add(dataObje.getString("wd_dest_code"));
                            }
                            storenameAr.add(dataObje.getString("store_name"));


                        }
                        setFilterData(wdIdAr,storenameAr);




                    }
                    else
                    {
                        ((MainActivity)getActivity()).showErrorDialog(obj.getString("errors"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}

