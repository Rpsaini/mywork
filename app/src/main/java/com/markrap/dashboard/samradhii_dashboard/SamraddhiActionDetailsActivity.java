package com.markrap.dashboard.samradhii_dashboard;

import static android.content.Intent.ACTION_DIAL;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SamraddhiActionDetailsActivity extends BaseActivity implements View.OnClickListener {
    private EditText etStkID, etStockistName, etMobileNumber, etPASRName, etROCNAme, editunnatidDownlaodstatus, etUnnatiMTDOrderValue, EtUnnatiMTDINvoice;
    private RecyclerView deatilsRecyclerView;
    private Spinner spinnerMonth;
    private SamraddhiActionDetailsActivity samraddhiActionaleActivity;
    private EditText month;
    private ImageView back_btn,imagCall;
    SamriddhiDEtailsDataAdadpter SamriddhiDEtailsDataAdadpter;
    TextView txtTgt, txtStkName, txtBgtTarget, txtThreshold, txtUnbilled,
            txtActive, txtRangeActive, txtRangeBgtTarget, txtRangeThreshold, txtRangeUnbilled,
            txtFpOneActive, txtFpOneBgtTarget, txtFpOneThreshold, txtFpOneUnbilled, txtFptwoActive, txtFptwoBgtTarget, txtFpTwoThreshold, txtFPTwoUnbilled, txtThresholdTgtActive, txtThresholdPending, txtThresholdtgtMaxIncent, txtIncentiveExpensive;

    @Override
    protected void setUp() {
        PrefHelper.getInstance().getSharedValue("selectedItemStr");
        init();
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
    }

    @Override
    protected int setLayout() {
        return R.layout.samriddhi_details;
    }

    private void init() {
        txtTgt = (TextView) findViewById(R.id.txtTgt);
        txtStkName = (TextView) findViewById(R.id.txtStkName);
        txtBgtTarget = (TextView) findViewById(R.id.txtBgtTarget);
        txtThreshold = (TextView) findViewById(R.id.txtThreshold);
        txtUnbilled = (TextView) findViewById(R.id.txtUnbilled);

        etStkID = (EditText) findViewById(R.id.etStkID);
        etStkID.setText(getIntent().getStringExtra(AppConstants.stockiest_id));

        etStockistName = (EditText) findViewById(R.id.etStockistName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etPASRName = (EditText) findViewById(R.id.etPASRName);
        etROCNAme = (EditText) findViewById(R.id.etROCNAme);
        //samraddhiiRecyclerView=(RecyclerView)findViewById(R.id.samraddhiiRecyclerView);
        editunnatidDownlaodstatus = (EditText) findViewById(R.id.editunnatidDownlaodstatus);
       // editunnatidDownlaodstatus.setText("No Data Found!.");
        etUnnatiMTDOrderValue = (EditText) findViewById(R.id.etUnnatiMTDOrderValue);
       // etUnnatiMTDOrderValue.setText("No Data Found!.");

        EtUnnatiMTDINvoice = (EditText) findViewById(R.id.EtUnnatiMTDINvoice);
     //   EtUnnatiMTDINvoice.setText("No Data Found!.");


        month = (EditText) findViewById(R.id.month);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        imagCall=(ImageView)findViewById(R.id.imagCall);
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

                        month.setText(dataAr.getString("month_date"));
                        JSONObject month_wise_data = dataAr.getJSONObject("month_wise_data");
                        JSONObject month_wise = month_wise_data.getJSONObject("month_wise");
                        System.out.println("@@month_wise" + month_wise);
                        JSONObject BZTarget = month_wise.getJSONObject("BZ Target");
                        System.out.println("@@BZTarget" + BZTarget);
// i row
                        txtActive.setText(BZTarget.optString("achieve"));
                        txtBgtTarget.setText(BZTarget.optString("pending"));
                        txtThreshold.setText(BZTarget.optString("max_incentive"));
                        txtUnbilled.setText(BZTarget.optString("incentive_achieve"));
                        //2nd row
                        JSONObject Range = month_wise.getJSONObject("Range");
                        System.out.println("@@BZTarget" + Range);
// i row
                        txtRangeActive.setText(Range.optString("achieve"));
                        txtRangeBgtTarget.setText(Range.optString("pending"));
                        txtRangeThreshold.setText(Range.optString("max_incentive"));
                        txtRangeUnbilled.setText(Range.optString("incentive_achieve"));
//3 row
                        JSONObject FP1 = month_wise.getJSONObject("FP1");
                        System.out.println("@@BZTarget" + FP1);

                        txtFpOneActive.setText(FP1.optString("achieve"));
                        txtFpOneBgtTarget.setText(FP1.optString("pending"));
                        txtFpOneThreshold.setText(FP1.optString("max_incentive"));
                        txtFpOneUnbilled.setText(FP1.optString("incentive_achieve"));
//3 row

//4th
                        JSONObject FP2 = month_wise.getJSONObject("FP2");
                        System.out.println("@@FP2" + FP2);

                        txtFptwoActive.setText(FP2.optString("achieve"));
                        txtFptwoBgtTarget.setText(FP2.optString("pending"));
                        txtFpTwoThreshold.setText(FP2.optString("max_incentive"));
                        txtFPTwoUnbilled.setText(FP2.optString("incentive_achieve"));
//3 row
                        JSONObject ThresholdTarget = month_wise.getJSONObject("Threshold Target");
                        System.out.println("@@Threshold Target" + ThresholdTarget);

                        txtThresholdTgtActive.setText(ThresholdTarget.optString("achieve"));
                        txtThresholdPending.setText(ThresholdTarget.optString("pending"));
                        txtThresholdtgtMaxIncent.setText(ThresholdTarget.optString("max_incentive"));
                        txtIncentiveExpensive.setText(ThresholdTarget.optString("incentive_achieve"));

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
}