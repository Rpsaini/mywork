package com.markrap.dashboard.samradhii_dashboard;

import static android.content.Intent.ACTION_DIAL;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.markrap.R;
import com.markrap.adapter.SamriddhiActionAdadpter;
import com.markrap.adapter.SamriddhiDEtailsDataAdadpter;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.util.AnimUtil;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SamraddhiActionDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView etStkID, etStockistName, etMobileNumber, etPASRName, etROCNAme, editunnatidDownlaodstatus, etUnnatiMTDOrderValue, EtUnnatiMTDINvoice;
    private RecyclerView deatilsRecyclerView;
    private TextView spinnerMonth;
    private SamraddhiActionDetailsActivity samraddhiActionaleActivity;
    private Spinner month;
    private ImageView back_btn, imagCall;
    SamriddhiDEtailsDataAdadpter SamriddhiDEtailsDataAdadpter;
    TextView txtTgt, txtStkName, txtBgtTarget, txtThresholdBZ, txtUnbilled,
            txtActive, txtRangeActive, txtRangeBgtTarget, txtRangeThreshold, txtRangeUnbilled,
            txtFpOneActive, txtFpOneBgtTarget, txtFpOneThreshold, txtFpOneUnbilled, txtFptwoActive, txtFptwoBgtTarget, txtFpTwoThreshold, txtFPTwoUnbilled, txtThresholdTgtActive, txtThresholdPending, txtThresholdtgtMaxIncent, txtIncentiveExpensive;
    TextView   txtBzTargets,txtRangeTargets,txtFP1Targets,txtFp2Target,txtThrehholdTarget;
    TextView txtFPOne,txtFPtw0;
    private TextView txtTillDate;

    @Override
    protected void setUp() {
        PrefHelper.getInstance().getSharedValue("selectedItemStr");

        PrefHelper.getInstance().getSharedValue("days");

        init();
        getSamriddhiEntryDashboardData();

        setupSamriddhiStockiestDetails(getIntent().getStringExtra(AppConstants.stockiest_id), PrefHelper.getInstance().getSharedValue("selectedItemStr"));
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
        getSamriddhiEntryDashboardData();

    }

    @Override
    protected int setLayout() {
        return R.layout.samriddhi_details;
    }

    private void init() {
        txtFPOne=(TextView)findViewById(R.id.txtFPOne);
        txtFPtw0=(TextView)findViewById(R.id.txtFPtw0);
        txtBzTargets = (TextView) findViewById(R.id.txtBzTargets);
        txtRangeTargets= (TextView) findViewById(R.id.txtRangeTargets);
        txtFP1Targets= (TextView) findViewById(R.id.txtFP1Targets);
        txtFp2Target= (TextView) findViewById(R.id.txtFp2Target);
        txtThrehholdTarget= (TextView) findViewById(R.id.txtThrehholdTarget);

        txtTillDate = (TextView) findViewById(R.id.txtTillDate);
        txtTgt = (TextView) findViewById(R.id.txtTgt);
        txtStkName = (TextView) findViewById(R.id.txtStkName);
        txtBgtTarget = (TextView) findViewById(R.id.txtBgtTarget);
        txtThresholdBZ = (TextView) findViewById(R.id.txtThresholdBZ);
        txtUnbilled = (TextView) findViewById(R.id.txtUnbilled);

        etStkID = (TextView) findViewById(R.id.etStkID);
        etStkID.setText(getIntent().getStringExtra(AppConstants.siffiId));

        etStockistName = (TextView) findViewById(R.id.etStockistName);
        etMobileNumber = (TextView) findViewById(R.id.etMobileNumber);
        etPASRName = (TextView) findViewById(R.id.etPASRName);
        etROCNAme = (TextView) findViewById(R.id.etROCNAme);
        //samraddhiiRecyclerView=(RecyclerView)findViewById(R.id.samraddhiiRecyclerView);
        editunnatidDownlaodstatus = (EditText) findViewById(R.id.editunnatidDownlaodstatus);
        // editunnatidDownlaodstatus.setText("No Data Found!.");
        etUnnatiMTDOrderValue = (EditText) findViewById(R.id.etUnnatiMTDOrderValue);
        // etUnnatiMTDOrderValue.setText("No Data Found!.");

        EtUnnatiMTDINvoice = (EditText) findViewById(R.id.EtUnnatiMTDINvoice);
        //   EtUnnatiMTDINvoice.setText("No Data Found!.");

      //  txtTillDate.setText(PrefHelper.getInstance().getSharedValue("days"));
        month = (Spinner) findViewById(R.id.month);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{PrefHelper.getInstance().getSharedValue("selectedItemStr")});
        month.setAdapter(aa);

        back_btn = (ImageView) findViewById(R.id.back_btn);
        imagCall = (ImageView) findViewById(R.id.imagCall);
        imagCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    actionCall(etMobileNumber.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtActive = (TextView) findViewById(R.id.txtActive);
        txtBgtTarget = (TextView) findViewById(R.id.txtBgtTarget);
        txtRangeActive = (TextView) findViewById(R.id.txtRangeActive);
        txtRangeBgtTarget = (TextView) findViewById(R.id.txtRangeBgtTarget);
        txtRangeThreshold = (TextView) findViewById(R.id.txtRangeThreshold);
        txtRangeUnbilled = (TextView) findViewById(R.id.txtRangeUnbilled);

        txtFpOneActive = (TextView) findViewById(R.id.txtFpOneActive);
        txtFpOneBgtTarget = (TextView) findViewById(R.id.txtFpOneBgtTarget);
        txtFpOneThreshold = (TextView) findViewById(R.id.txtFpOneThreshold);
        txtFpOneUnbilled = (TextView) findViewById(R.id.txtFpOneUnbilled);
        txtFptwoActive = (TextView) findViewById(R.id.txtFptwoActive);
        txtFptwoBgtTarget = (TextView) findViewById(R.id.txtFptwoBgtTarget);
        txtFPTwoUnbilled = (TextView) findViewById(R.id.txtFPTwoUnbilled);
        txtFpTwoThreshold = (TextView) findViewById(R.id.txtFpTwoThreshold);
        txtThresholdTgtActive = (TextView) findViewById(R.id.txtThresholdTgtActive);

        txtThresholdPending = (TextView) findViewById(R.id.txtThresholdPending);
        txtThresholdtgtMaxIncent = (TextView) findViewById(R.id.txtThresholdtgtMaxIncent);
        txtIncentiveExpensive = (TextView) findViewById(R.id.txtIncentiveExpensive);

    }

    @Override
    public void onClick(View v) {

    }

    private void setupSamriddhiStockiestDetails(String stockiest_id, String entryDates) {
        System.out.println("setupSamriddhiStockiestDetails" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("stockiest_id", stockiest_id);

        m.put("date", entryDates);


        System.out.println("@@getSamriddhiActionable" + AppConstants.apiUlr + "smriddhi_stockiest/" + m);


        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "smriddhi_stockiest/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("setupSamriddhiStockiestDetails====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");
                        etStockistName.setText(dataAr.getString("stockiest_name"));
                        etStockistName.setText(dataAr.getString("stockiest_name"));

                        etMobileNumber.setText(dataAr.getString("mobile"));
                        etPASRName.setText(dataAr.getString("psr_name"));

                        etROCNAme.setText(dataAr.getString("roc_name"));

                        //  month.setText(dataAr.getString("month_date"));
                        JSONObject month_wise_data = dataAr.getJSONObject("month_wise_data");
                        JSONObject month_wise = month_wise_data.getJSONObject("month_wise");
                        System.out.println("@@month_wise" + month_wise);
                        JSONObject BZTarget = month_wise.getJSONObject("BZ Target");
                        System.out.println("@@BZTarget" + BZTarget);
// i row

                        txtBzTargets.setText(BZTarget.optString("target"));
                        txtActive.setText(BZTarget.optString("achieve"));
                        txtBgtTarget.setText(BZTarget.optString("pending"));
                        txtThresholdBZ.setText(BZTarget.optString("max_incentive"));
                        txtUnbilled.setText(BZTarget.optString("incentive_achieve"));
                        //2nd row
                        JSONObject Range = month_wise.getJSONObject("Range");
                        System.out.println("@@BZTarget" + Range);
// i row
                        txtRangeTargets.setText(Range.optString("target"));
                        txtRangeActive.setText(Range.optString("achieve"));
                        txtRangeBgtTarget.setText(Range.optString("pending"));
                        txtRangeThreshold.setText(Range.optString("max_incentive"));
                        txtRangeUnbilled.setText(Range.optString("incentive_achieve"));
//3 row
                        JSONObject FP1 = month_wise.getJSONObject("FP1");
                        System.out.println("@@BZTarget" + FP1);

                        txtFPOne.setText("FP1 "+" - "+FP1.optString("name"));

                        txtFP1Targets.setText(FP1.optString("target"));
                        txtFpOneActive.setText(FP1.optString("achieve"));
                        txtFpOneBgtTarget.setText(FP1.optString("pending"));
                        txtFpOneThreshold.setText(FP1.optString("max_incentive"));
                        txtFpOneUnbilled.setText(FP1.optString("incentive_achieve"));
//3 row

//4th
                        JSONObject FP2 = month_wise.getJSONObject("FP2");
                        System.out.println("@@FP2" + FP2);
                        txtFPtw0.setText("FP2 "+" - "+FP2.optString("name"));

                        txtFp2Target.setText(FP2.optString("target"));

                        txtFptwoActive.setText(FP2.optString("achieve"));
                        txtFptwoBgtTarget.setText(FP2.optString("pending"));
                        txtFpTwoThreshold.setText(FP2.optString("max_incentive"));
                        txtFPTwoUnbilled.setText(FP2.optString("incentive_achieve"));
//3 row
                        JSONObject ThresholdTarget = month_wise.getJSONObject("Threshold Target");
                        System.out.println("@@Threshold Target" + ThresholdTarget);
                        txtThrehholdTarget.setText(ThresholdTarget.optString("target"));

                        txtThresholdTgtActive.setText(ThresholdTarget.optString("achieve"));
                        txtThresholdPending.setText(ThresholdTarget.optString("pending"));
                        txtThresholdtgtMaxIncent.setText(ThresholdTarget.optString("max_incentive"));
                        txtIncentiveExpensive.setText(ThresholdTarget.optString("incentive_achieve"));
                        System.out.println("@@@-1" + txtThresholdTgtActive.getText().toString() + "check" + ThresholdTarget.optString("achieve"));
                        System.out.println("@@@-1" + txtThresholdPending.getText().toString() + "check" + ThresholdTarget.optString("pending"));
                        System.out.println("@@@-1" + txtThresholdtgtMaxIncent.getText().toString() + "check" + ThresholdTarget.optString("max_incentive"));
                        System.out.println("@@@-1" + txtIncentiveExpensive.getText().toString() + "check" + ThresholdTarget.optString("incentive_achieve"));


                        editunnatidDownlaodstatus.setText(dataAr.getString("unnati_download_status"));
                        etUnnatiMTDOrderValue.setText(dataAr.getString("unnati_download_status"));
                        editunnatidDownlaodstatus.setText(dataAr.getString("unnati_mtd_order_value"));
                        EtUnnatiMTDINvoice.setText(dataAr.getString("unnati_mtd_invoice_value"));


                        //   showSamriddhiActionable(month_wise);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showSamriddhiActionable(JSONObject dataAr) {

        RecyclerView recyclerlistview = findViewById(R.id.deatilsRecyclerView);
        SamriddhiDEtailsDataAdadpter = new SamriddhiDEtailsDataAdadpter(dataAr, samraddhiActionaleActivity);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(samraddhiActionaleActivity, LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        //      recyclerlistview.setNestedScrollingEnabled(false);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(SamriddhiDEtailsDataAdadpter);
    }

    private void actionCall(String phoneNumber) {

        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setFilterData(ArrayList<String> wdArray, ArrayList<String> nameAr) {
        System.out.println("@@SetFilterData_SamvithiAcionble");
        Spinner spinnerMonth = findViewById(R.id.month);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, wdArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nameArR = nameAr.get(position).toString();
                String selectedItemStr = spinnerMonth.getSelectedItem().toString();
                String dayGet = spinnerMonth.getSelectedItem().toString();
                PrefHelper.getInstance().storeSharedValue("selectedItemStr", String.valueOf(selectedItemStr));

                txtTillDate.setText(nameArR);
                System.out.println("@@@Day"+nameArR);
              //  PrefHelper.getInstance().storeSharedValue("days", nameArR);

                // JSONArray filterArray = new JSONArray();
                //getSamriddhiDashboardDataNewOne(selectedItemStr);
                //     if(position>0) {
            }


            //   } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void getSamriddhiDashboardDataNewOne(String entryDates) {
        System.out.println("@@NEwOneActionSpinner" + entryDates);
        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));

        m.put("date", entryDates);
        //+mainActivity.getLoginData("id")
        System.out.println("@@NEwOneActionSpinner" + AppConstants.apiUlr + "smriddhi_dashboard/" + m);


        new ServerHandler().sendToServer(getApplicationContext(), AppConstants.apiUlr + "smriddhi_dashboard/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
             /*   try {

                    System.out.println("getSamriddhiDahboardData====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        JSONObject dataAr = obj.getJSONObject("data");



                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
                try {
                    System.out.println("getSamriddhiActionable====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {
                        //   JSONObject dataAr = obj.getJSONObject("data");
//                        JSONArray dataArAll = dataAr.getJSONArray("all");
                        //      System.out.println("@@getSamriddhiActionable====" + dataArAll);
                        System.out.println("@@NEwOneActionSpinner__3" + AppConstants.apiUlr + "smriddhi_dashboard/" + m);

                       // getSamriddhiActionable(PrefHelper.getInstance().getSharedValue("selectedItemStr"));


                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getSamriddhiEntryDashboardData() {

        ArrayList<String> wdIdAr = new ArrayList<>();
        ArrayList<String> storenameAr = new ArrayList<>();
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        m.put("user_id", getLoginData("id"));
        new ServerHandler().sendToServer(getApplicationContext(), AppConstants.apiUlr + "entrydates/", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {

            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    Date res = null;
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
                                storenameAr.add(dataObje.getString("day"));
                                System.out.println("--11enterdates====" + wdIdAr.toString());
                                System.out.println("--11enterdates__1====" + storenameAr.toString());
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

}