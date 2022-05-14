package com.markrap.dashboard.outlet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.communication.ApiProductionS;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.fragments.AllStoresFrg;
import com.markrap.fragments.AppViewModel;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.login.LoginActivity;
import com.markrap.placepicker.LocationPickerActivity;
import com.markrap.utility.AppConstants;
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

public class AddOutlet extends BaseActivity {

    private String lat = "", Lng = "";
    TextView outletlocation;
    TextInputLayout text_input_layout_latitude, text_input_layout_longitude;

    String stockistId = "", stockistname = "";
    private GpsTracker gpsTracker;
    ProgressDialog pd;
    Uri imageUrli;
    private EditText edaddress;


    TextInputLayout txtOutletname;
    TextInputLayout txtOwnerName;
    TextInputLayout text_input_layout_mobile;
    TextInputLayout text_input_layout_name;
    ImageView imgOutlet;

    @Override
    protected void setUp() {
        init();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_add_outlet;
    }

    private void init() {
        txtOutletname = findViewById(R.id.txtOutletname);
        txtOwnerName = findViewById(R.id.txtOwnerName);
        text_input_layout_mobile = findViewById(R.id.text_input_layout_mobile);
        text_input_layout_name = findViewById(R.id.text_input_layout_name);
        text_input_layout_latitude = findViewById(R.id.text_input_layout_latitude);
        text_input_layout_longitude = findViewById(R.id.text_input_layout_longitude);
        imgOutlet = findViewById(R.id.imgOutlet);
//        EditText edlandmark = findViewById(R.id.edlandmark);
        edaddress = findViewById(R.id.edaddress);

        outletlocation = findViewById(R.id.outletlocation);
        TextView txtwWelocome = findViewById(R.id.txtwWelocome);

        stockistname = getIntent().getStringExtra(AppConstants.storeName);
        txtwWelocome.setText("Add Outlet for : (" + stockistname + ")");
        stockistId = getIntent().getStringExtra(AppConstants.id);

        String first_name = getLoginData("first_name");
        String last_name = getLoginData("last_name");
        text_input_layout_name.getEditText().setText(first_name + " " + last_name);
        checkPermissionGranted();

        outletlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Data====>");
                Intent placepicker = new Intent(AddOutlet.this, LocationPickerActivity.class);
                placepicker.putExtra("text", "Chandigarh");
                startActivityForResult(placepicker, 1001);
            }
        });

        findViewById(R.id.takeaphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission(new ImageAttendanceCallBack() {
                    @Override
                    public void getImageUri(Uri uri) {
                        imageUrli = uri;
                        System.out.println("Image url==" + imageUrli);
                        imgOutlet.setVisibility(View.VISIBLE);

                        Glide.with(AddOutlet.this)
                                .load(imageUrli)
                                .placeholder(R.drawable.side_image)
                                .centerCrop()
                                .into(imgOutlet);
                    }
                });

            }
        });


        findViewById(R.id.verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtOutletname.getEditText().getText().length() == 0) {
                    showErrorDialog("Please enter Outlet Name");
                } else if (txtOwnerName.getEditText().getText().length() == 0) {
                    showErrorDialog("Please enter Outlet Owner name");
                } else if (text_input_layout_mobile.getEditText().getText().length() == 0) {
                    showErrorDialog("Please enter Outlet Mobile number");
                } else if (text_input_layout_name.getEditText().getText().length() == 0) {
                    showErrorDialog("Please enter your name");
                }
                else if (text_input_layout_latitude.getEditText().getText().length() == 0 || text_input_layout_longitude.getEditText().getText().length() == 0) {
                    showErrorDialog("Please add outlet Loctaion");
                } else if (edaddress.getText().length() == 0) {
                    showErrorDialog("Please enter outlet Address");
                }
               else if(imgOutlet.getVisibility()!=View.VISIBLE)
                {
                    showErrorDialog("Please select outlet image");
                }
                else
                    {

                    addOutlet();
//                    Map<String, String> map = new HashMap<>();
//                    map.put("user_id", getLoginData("id"));
//                    map.put("supplier_id", stockistId);
//                    map.put("outlet_name", txtOutletname.getEditText().getText().toString());
//                    map.put("owner_name", txtOwnerName.getEditText().getText().toString());
//                    map.put("mobile", text_input_layout_mobile.getEditText().getText().toString());
//                    map.put("location_name", outletlocation.getText().toString());
//                    map.put("latitude", lat);
//                    map.put("longitude", Lng);
////                    map.put("landmark", edlandmark.getText().toString());
//                    map.put("address", edaddress.getText().toString());
//                    map.put("outlet_image", imageUrli+"");
//                    saveOutlets(map);
                }

            }
        });


        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void checkPermissionGranted() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddOutlet.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {

                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void saveOutlets(Map<String, String> map) {
//        Map<String, String> headerMap = new HashMap<>();
//        new ServerHandler().sendToServer(this, AppConstants.apiUlr + "addoutlets", map, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
//            @Override
//            public void getRespone(String dta, ArrayList<Object> respons) {
//                try {
//                    System.out.println("addoutlets====" + dta);
//                    JSONObject obj = new JSONObject(dta);
//                    if (obj.getInt("result") > 0) {
//                        showErrorDialog(obj.getString("msg"));
//                    } else {
//                        showErrorDialog(obj.getString("errors"));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {
                if (data.hasExtra("sourcename")) {
                    outletlocation.setText(data.getStringExtra("sourcename"));
                    lat = data.getStringExtra("lat");
                    Lng = data.getStringExtra("lng");
                    text_input_layout_latitude.getEditText().setText(lat + "");
                    text_input_layout_longitude.getEditText().setText(Lng + "");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getLocation() {
        pd = new ProgressDialog(AddOutlet.this);
        gpsTracker = new GpsTracker(AddOutlet.this);
        if (gpsTracker.canGetLocation()) {
            pd.setMessage("Fetching your current location");
            pd.show();
            pd.setCancelable(false);
            callHandler();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    final Handler handler = new Handler();

    private void callHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                if (latitude > 0 && longitude > 0) {
                    pd.dismiss();
                    text_input_layout_latitude.getEditText().setText(String.valueOf(latitude));
                    text_input_layout_longitude.getEditText().setText(String.valueOf(longitude));
                }

            }
        }, 1000);
    }


    private void addOutlet() {


        UploadImage(new File(getRealPathFromURI(imageUrli)));


    }


    public void UploadImage(File file) {

        BuildRequestParms buildRequestParms = new BuildRequestParms();
        AppViewModel apiParamsInterface = ApiProductionS.getInstance(this).provideService(AppViewModel.class);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id", getLoginData("id"));
        m.put("on_date", date);
        Observable<JSONObject> observable = null;

        ArrayList<Object> imageAr1 = buildRequestParms.getMultipart("image", file);
        MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);

        System.out.println("Body==" + body1);


        observable = apiParamsInterface.addoutlets(
                buildRequestParms.getRequestBody(getLoginData("id")),
                buildRequestParms.getRequestBody(stockistId),
                buildRequestParms.getRequestBody(txtOutletname.getEditText().getText().toString()),
                buildRequestParms.getRequestBody(txtOwnerName.getEditText().getText().toString()),
                buildRequestParms.getRequestBody(text_input_layout_mobile.getEditText().getText().toString()),
                buildRequestParms.getRequestBody(outletlocation.getText().toString()),
                buildRequestParms.getRequestBody(text_input_layout_latitude.getEditText().getText().toString()),
                buildRequestParms.getRequestBody(text_input_layout_longitude.getEditText().getText().toString()),
                buildRequestParms.getRequestBody(edaddress.getText().toString()),
                body1
        );


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please wait..");


        Disposable disposable = RxAPICallHelper.call(observable, new RxAPICallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject uploadFileResponse) {

                try {

                    System.out.println("Uploaded response==="+uploadFileResponse);

                    if (uploadFileResponse.has("errors")) {
                        showErrorDialog(uploadFileResponse.getString("errors"));
                    } else {
                       // showErrorDialog("Outlet added.");
                        edaddress.setText("");
                        text_input_layout_latitude.getEditText().setText("");
                        text_input_layout_longitude.getEditText().setText("");
                        text_input_layout_name.getEditText().setText("");
                        text_input_layout_mobile.getEditText().setText("");

                        imgOutlet.setVisibility(View.GONE);
                        finish();


                    }


                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                System.out.println("error===" + throwable.getMessage());
                mProgressDialog.dismiss();


            }
        });


    }


    private String getRealPathFromURI(Uri uri) {
        File myFile;
        myFile = new File(uri.getPath());
        return myFile.getAbsolutePath();
    }








}