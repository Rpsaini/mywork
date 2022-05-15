package com.markrap.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;
import com.markrap.adapter.CategegoryAdapter;
import com.markrap.adapter.OrderListAdapter;
import com.markrap.adapter.PsrSalesAdapter;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.login.LoginActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class HomeFragments extends Fragment {
    public static final String TAG = "HomeFragments";
    MainActivity mainActivity;
    View rootView;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                HomeFragments.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        mainActivity = (MainActivity) getActivity();
        getUserDetails();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@MainHome_ONResume---", "onResume---");
    }


    public void getUserDetails() {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();


        Map<String, String> headerMap = new HashMap<>();

        System.out.println("Prole==="+AppConstants.apiUlr + "profile/" + mainActivity.getLoginData("id"));
        new ServerHandler().sendToServer(mainActivity, AppConstants.apiUlr + "profile/" + mainActivity.getLoginData("id"), m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0)
                    {
                        JSONObject dataObj = obj.getJSONObject("data");
                        new SavePreferences().savePreferencesData(mainActivity, dataObj, AppConstants.logindata);
                        setUserData();
                    } else {
                        mainActivity.showErrorDialog(obj.getString("msg"));
                    }
                    getDashboardStat();

                } catch (Exception e) {
                    e.printStackTrace();
                    getDashboardStat();
                }

            }
        });
    }


    public void getDashboardStat() {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();


        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(mainActivity, AppConstants.apiUlr + "dashboard/" + mainActivity.getLoginData("id"), m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

     //   new ServerHandler().sendToServer(mainActivity, AppConstants.apiUlr + "dashboard/" + headerMap, m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("dashboard==="+AppConstants.apiUlr + "dashboard/" + mainActivity.getLoginData("id"));

                        System.out.println("Dashbaord====" + dta);
                        JSONObject obj = new JSONObject(dta);
                        if (obj.getInt("result") > 0) {

                            JSONObject dataObj=obj.getJSONObject("data");
                            String monthly_attend= dataObj.getString("mtd_billed_value");
                            String attendance_days= dataObj.getString("attendance_days");
                            System.out.println("attendance_days====" + attendance_days);
                            String NumberOfStockistBilled= dataObj.getString("stockist_billed");
                            String total_sales= dataObj.getString("total_sales");
                            String MappedStockist =dataObj.getString("my_stockist");
                            System.out.println("NumberOfStockistBilled====" + NumberOfStockistBilled);
                            System.out.println("MappedStockist====" + MappedStockist);
                            String monthly_target =dataObj.getString("monthly_target");
                            System.out.println("monthly_target====" + monthly_target);
                            TextView txtMonthlyTarget=rootView.findViewById(R.id.txtMonthlyTarget);
                            TextView numberofMAppedStockist=rootView.findViewById(R.id.numberofMAppedStockist);
                            TextView txtNumberOfStockistBilled= rootView.findViewById(R.id.txtNumberOfStockistBilled);
                            TextView totalAttendance= rootView.findViewById(R.id.totalAttendance);
//                            TextView totalsaleText= rootView.findViewById(R.id.totalsale);
                            if(!monthly_target.equalsIgnoreCase(""))
                            {
                                txtMonthlyTarget.setText(monthly_target);
                            }
                            if(!MappedStockist.equalsIgnoreCase("null"))
                            {
                                numberofMAppedStockist.setText(MappedStockist);
                            }
//                            if(!total_sales.equalsIgnoreCase("null"))
//                            {
//                                totalsaleText.setText(total_sales);
//                            }

                            if(!attendance_days.equalsIgnoreCase("null"))
                            {
                                totalAttendance.setText(monthly_attend);
                            }
                            if(!NumberOfStockistBilled.equalsIgnoreCase("null"))
                            {
                                txtNumberOfStockistBilled.setText(NumberOfStockistBilled);
                            }
                             JSONArray result_array = dataObj.getJSONArray("psr_sales");
                            System.out.println("@@@result_array"+result_array.length());
                            showPSrData(result_array);
                        /*    for (int i = 0; i < result_array.length(); i++) {
                                JSONArray jDataAr=new JSONArray();
                                JSONObject jsonObject=new JSONObject();

                                JSONObject joObject = result_array.getJSONObject(i);
                                System.out.println("@@@"+joObject.length());
                             //   String jName = joObject.get("referral_fullname").toString();
                               // String jbalance = joObject.get("referral_balance").toString();
                               // joObject.put("psr_name",joObject.getString("psr_name"));
                            *//*    if(joObject.has("psr_name"))
                                {
                                    jsonObject.put("psr_name", joObject.getString("psr_name"));
                                }
                                else
                                {
                                    jsonObject.put("psr_name", "");
                                }
                                if(joObject.has("stockiest"))
                                {
                                    jsonObject.put("stockiest", joObject.getString("stockiest"));
                                }
                                else
                                {
                                    jsonObject.put("stockiest", "");
                                }
                                if(joObject.has("mtd_value")) {

                                    jsonObject.put("mtd_value", joObject.getString("mtd_value"));
                                }
                                else
                                {
                                    jsonObject.put("mtd_value", "");
                                }*//*
                                jDataAr.put(jsonObject);
                                showPSrData(jDataAr);
                            }*/

                     //       JSONArray data=dataObj.getJSONArray("psr_sales");
                       //     System.out.println("Psr sales==="+data);


                          /*  for(int i=0;i<data.length();i++){
                                JSONArray jDataAr=new JSONArray();
                                JSONObject jsonObject=new JSONObject();
                                JSONObject nameData=data.getJSONObject(i);
                                System.out.println("Psr jsonObject==="+jsonObject);





                                  //  JSONObject nameData=data.getJSONObject(key);
                                    System.out.println("Name data==="+nameData);
                                    jsonObject.put("psr_name",nameData.getString("psr_name"));
                                    if(nameData.has("stockiest"))
                                    {
                                        jsonObject.put("stockiest", nameData.getString("stockiest"));
                                    }
                                    else
                                    {
                                        jsonObject.put("stockiest", "");
                                    }
                                    if(nameData.has("mtd_value")) {

                                        jsonObject.put("mtd_value", nameData.getString("mtd_value"));
                                    }
                                    else
                                    {
                                        jsonObject.put("mtd_value", "");
                                    }
                                    jDataAr.put(jsonObject);

                                showPSrData(jDataAr);

                            }*/
                          /*  if(data!=null)
                            {

                                Iterator<String> dataStr =data.keys();
                                while (dataStr.hasNext())
                                {
                                    JSONObject jsonObject=new JSONObject();

                                    String key=dataStr.next();

                                    JSONObject nameData=data.getJSONObject(key);
                                    System.out.println("Name data==="+nameData);
                                    jsonObject.put("name",key);
                                    if(nameData.has("stockiest"))
                                    {
                                        jsonObject.put("stockiest", nameData.getString("stockiest"));
                                    }
                                    else
                                    {
                                        jsonObject.put("stockiest", "");
                                    }
                                    if(nameData.has("mtd_value")) {

                                        jsonObject.put("mtd_value", nameData.getString("mtd_value"));
                                    }
                                    else
                                    {
                                        jsonObject.put("mtd_value", "");
                                    }
                                    jDataAr.put(jsonObject);
                                }

                                showPSrData(jDataAr);

                            }*/





                        } else {
                            mainActivity.showErrorDialog(obj.getString("msg"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }


        private void setUserData()
        {
            String first_name = mainActivity.getLoginData("first_name");
            String last_name = mainActivity.getLoginData("last_name");
            String email = mainActivity.getLoginData("email");
            String phone = mainActivity.getLoginData("phone");
            String designation = mainActivity.getLoginData("designation");
            String image = mainActivity.getLoginData("image");

            TextView user_name_txt =rootView.findViewById(R.id.user_name_txt);
            user_name_txt.setText("Hi, "+first_name+" "+last_name);
            TextView txtUsernavname=mainActivity.findViewById(R.id.txtUsernavname);
            txtUsernavname.setText(first_name+" "+last_name);
            showImage(image,(ImageView) rootView.findViewById(R.id.user_profile));


            showImage(image,mainActivity.findViewById(R.id.user_profile_nav));


        }

        private void showImage(String image_url, ImageView imageView)
        {
            RequestOptions requestOptions=new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.side_image)
                    .error(R.drawable.side_image);
            Glide.with(mainActivity).load(image_url).apply(requestOptions).into(imageView);
        }


        private void showPSrData(JSONArray dataAr)
        {

            RecyclerView recyclerlistview = rootView.findViewById(R.id.psrRecyclerView);
            PsrSalesAdapter mAdapter = new PsrSalesAdapter(dataAr, mainActivity);
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);
            recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
      //      recyclerlistview.setNestedScrollingEnabled(false);
            recyclerlistview.setItemAnimator(new DefaultItemAnimator());
            recyclerlistview.setAdapter(mAdapter);
        }


    }

