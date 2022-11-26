package com.markrap.dashboard.samradhii_dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.markrap.R;
import com.markrap.adapter.PsrSalesAdapter;
import com.markrap.adapter.SamriddhiActionAdadpter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.ApiProductionS;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.fragments.AppViewModel;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.placepicker.LocationPickerActivity;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;
import com.wallet.retrofitapi.api.RxAPICallHelper;
import com.wallet.retrofitapi.api.RxAPICallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import Communication.BuildRequestParms;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

public class SamraddhiActionaleActivity extends BaseActivity implements View.OnClickListener {
    private TextView txtMonth, txtTillDate, txtAllFilter, txtUnbilledFilter, txtBuildButNotThresholdActive, txtThreshholdActivebutTargetNot;
    private RecyclerView samraddhiiRecyclerView;
    private Spinner spinnerMonth;
    private SamraddhiActionaleActivity samraddhiActionaleActivity;
    private Spinner spinnerWDCode;
    private ImageView back_btn;
    private SamriddhiActionAdadpter samriddhiActionAdadpter;
    private TextView txtTillDatesss;
    private String tillDay;

    @Override
    protected void setUp() {
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String allData = bundle.getString("ALL");
        tillDay = bundle.getString("day");

        init();

        if (bundle != null) {
            if (allData.equalsIgnoreCase("")) {
                getSamriddhiActionable(PrefHelper.getInstance().getSharedValue("selectedItemStr"));
                getWDCodesValues(PrefHelper.getInstance().getSharedValue("selectedItemStr"));

            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
        // setItemClickListner();

    }

    public String getLoginData(String dataType) {
        try {
            JSONObject data = new JSONObject(new SavePreferences().reterivePreference(this, AppConstants.logindata).toString());
            return data.getString(dataType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSamriddhiActionable(PrefHelper.getInstance().getSharedValue("selectedItemStr"));

       getWDCodesValues(PrefHelper.getInstance().getSharedValue("selectedItemStr"));

    }

    @Override
    protected int setLayout() {
        return R.layout.samriddhi_actionable;
    }

    private void init() {
        txtTillDatesss = (TextView) findViewById(R.id.txtTillDatesss);
        txtTillDatesss.setText(tillDay);
        txtAllFilter = (TextView) findViewById(R.id.txtAllFilter);
        txtUnbilledFilter = (TextView) findViewById(R.id.txtUnbilledFilter);
        txtBuildButNotThresholdActive = (TextView) findViewById(R.id.txtBuildButNotThresholdActive);
        txtThreshholdActivebutTargetNot = (TextView) findViewById(R.id.txtThreshholdActivebutTargetNot);
        spinnerWDCode = (Spinner) findViewById(R.id.spinnerWDCode);
        txtMonth = (TextView) findViewById(R.id.txtMonth);
        txtMonth.setText(PrefHelper.getInstance().getSharedValue("selectedItemStr"));
        txtTillDate = (TextView) findViewById(R.id.txtTillDate);
        txtAllFilter = (TextView) findViewById(R.id.txtAllFilter);
        txtUnbilledFilter = (TextView) findViewById(R.id.txtUnbilledFilter);
        txtBuildButNotThresholdActive = (TextView) findViewById(R.id.txtBuildButNotThresholdActive);

        txtThreshholdActivebutTargetNot = (TextView) findViewById(R.id.txtThreshholdActivebutTargetNot);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtAllFilter.setOnClickListener(this);
        txtUnbilledFilter.setOnClickListener(this);
        txtBuildButNotThresholdActive.setOnClickListener(this);
        txtBuildButNotThresholdActive.setOnClickListener(this);
        txtThreshholdActivebutTargetNot.setOnClickListener(this);


    }

    private void getSamriddhiActionable(String entryDates) {
        System.out.println("getSamriddhiActionable" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                        JSONArray dataArAll = dataAr.getJSONArray("all");
                        System.out.println("@@dataArAll====" + dataArAll);
                        showSamriddhiActionable(dataArAll);

                       /* JSONArray unbilled = dataAr.getJSONArray("unbilled");
                        System.out.println("@@unbilled====" + unbilled);

                        JSONArray billedbutthresholdnotachieved = dataAr.getJSONArray("billed but threshold not achieved");
                        System.out.println("@@billedbutthresholdnotachieved====" + billedbutthresholdnotachieved);

                        JSONArray thresholdachbuttarnotachieved = dataAr.getJSONArray("threshold ach but tar not achieved");
                        System.out.println("@@thresholdachbuttarnotachieved====" + thresholdachbuttarnotachieved);
*/
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getSamriddhiActionableAllFilter(String entryDates) {
        System.out.println("getSamriddhiActionable" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                        JSONArray dataArAll = dataAr.getJSONArray("all");
                        System.out.println("@@dataArAll====" + dataArAll);
                        showSamriddhiActionable(dataArAll);

                        /*JSONArray unbilled = dataAr.getJSONArray("unbilled");
                        System.out.println("@@unbilled====" + unbilled);

                        JSONArray billedbutthresholdnotachieved = dataAr.getJSONArray("billed but threshold not achieved");
                        System.out.println("@@billedbutthresholdnotachieved====" + billedbutthresholdnotachieved);

                        JSONArray thresholdachbuttarnotachieved = dataAr.getJSONArray("threshold ach but tar not achieved");
                        System.out.println("@@thresholdachbuttarnotachieved====" + thresholdachbuttarnotachieved);*/

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getSamriddhitxtUnbilledFilter(String entryDates) {
        System.out.println("getSamriddhiActionable" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                     /*   JSONArray dataArAll = dataAr.getJSONArray("all");
                        System.out.println("@@dataArAll====" + dataArAll);*/

                        JSONArray unbilled = dataAr.getJSONArray("unbilled");
                        System.out.println("@@unbilled====" + unbilled);
                        showSamriddhiActionable(unbilled);

                        /*JSONArray billedbutthresholdnotachieved = dataAr.getJSONArray("billed but threshold not achieved");
                        System.out.println("@@billedbutthresholdnotachieved====" + billedbutthresholdnotachieved);

                        JSONArray thresholdachbuttarnotachieved = dataAr.getJSONArray("threshold ach but tar not achieved");
                        System.out.println("@@thresholdachbuttarnotachieved====" + thresholdachbuttarnotachieved);*/

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void txtBuildButNotThresholdActive(String entryDates) {
        System.out.println("getSamriddhiActionable" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                       /* JSONArray dataArAll = dataAr.getJSONArray("all");
                        System.out.println("@@dataArAll====" + dataArAll);
                        showSamriddhiActionable(dataArAll);*/


                        JSONArray billedbutthresholdnotachieved = dataAr.getJSONArray("billed but threshold not achieved");
                        System.out.println("@@billedbutthresholdnotachieved====" + billedbutthresholdnotachieved);
                        showSamriddhiActionable(billedbutthresholdnotachieved);


                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void txtThreshholdActivebutTargetNot(String entryDates) {
        System.out.println("getSamriddhiActionable" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");

                        JSONArray thresholdachbuttarnotachieved = dataAr.getJSONArray("threshold ach but tar not achieved");
                        System.out.println("@@thresholdachbuttarnotachieved====" + thresholdachbuttarnotachieved);
                        showSamriddhiActionable(thresholdachbuttarnotachieved);

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void showSamriddhiActionable(JSONArray dataAr) {

        RecyclerView recyclerlistview = findViewById(R.id.samraddhiiRecyclerView);
        samriddhiActionAdadpter = new SamriddhiActionAdadpter(dataAr, samraddhiActionaleActivity);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(samraddhiActionaleActivity, LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        //      recyclerlistview.setNestedScrollingEnabled(false);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(samriddhiActionAdadpter);
    }


    @Override
    public void onClick(View v) {
        if (v == txtAllFilter) {
            getSamriddhiActionableAllFilter(PrefHelper.getInstance().getSharedValue("selectedItemStr"));
        }
        if (v == txtUnbilledFilter) {
            getSamriddhitxtUnbilledFilter(PrefHelper.getInstance().getSharedValue("selectedItemStr"));

        }
        if (v == txtBuildButNotThresholdActive) {
            txtBuildButNotThresholdActive(PrefHelper.getInstance().getSharedValue("selectedItemStr"));
        }
        if (v == txtThreshholdActivebutTargetNot) {
            txtThreshholdActivebutTargetNot(PrefHelper.getInstance().getSharedValue("selectedItemStr"));

        }
    }

    private void getWDCodesValues(String entryDates) {
        System.out.println("@@getWDCodesValues---" + entryDates);
        ArrayList<String> Wdcodes = new ArrayList<>();
        ArrayList<String> wdcodesExtra = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        System.out.println("@@getWDCodesValues" + AppConstants.apiUlr + "wdcodes/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "wdcodes/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("wdcodes====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONArray dataAr = obj.getJSONArray("data");
                        Wdcodes.add("WD Code");

                        for (int x = 0; x < dataAr.length(); x++) {
                            JSONObject dataObje = dataAr.getJSONObject(x);

                            if (!Wdcodes.contains(dataObje.getString("wd_code"))) {

                                Wdcodes.add(dataObje.getString("wd_code"));
                                //  wdcodesExtra.add(dataObje.getString("day"));
                                System.out.println("@@Wdcodes====" + Wdcodes.toString());
                                System.out.println("@@Wdcodes====" + wdcodesExtra.toString());
                            }
                            setFilterWdCode(Wdcodes, wdcodesExtra);


                        }

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setFilterWdCode(ArrayList<String> wdArray, ArrayList<String> wdcodes) {
        Spinner spinnerMonth = findViewById(R.id.spinnerWDCode);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, wdArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String spinSelectWDCode = spinnerWDCode.getSelectedItem().toString();
              //  PrefHelper.getInstance().storeSharedValue("selectedItemStr", String.valueOf(spinSelectWDCode));


               getSamriddhiActionableWDCode(PrefHelper.getInstance().getSharedValue("selectedItemStr"),spinSelectWDCode);
            }


            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void getSamriddhiActionableWDCode(String entryDates,String wdcodesSelect) {
        System.out.println("@@Data using wdcodw" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("date", entryDates);
        m.put("wd_code", wdcodesSelect);

        System.out.println("@@getSamriddhiActionableWDCode" + AppConstants.apiUlr + "smriddhi_actionable/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_actionable/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                        JSONArray dataArAll = dataAr.getJSONArray("all");
                        System.out.println("@@dataArAll====" + dataArAll);
                        showSamriddhiActionable(dataArAll);

                        /*JSONArray unbilled = dataAr.getJSONArray("unbilled");
                        System.out.println("@@unbilled====" + unbilled);

                        JSONArray billedbutthresholdnotachieved = dataAr.getJSONArray("billed but threshold not achieved");
                        System.out.println("@@billedbutthresholdnotachieved====" + billedbutthresholdnotachieved);

                        JSONArray thresholdachbuttarnotachieved = dataAr.getJSONArray("threshold ach but tar not achieved");
                        System.out.println("@@thresholdachbuttarnotachieved====" + thresholdachbuttarnotachieved);*/

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}