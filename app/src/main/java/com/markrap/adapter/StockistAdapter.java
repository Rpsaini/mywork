package com.markrap.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;

import org.json.JSONArray;


import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markrap.communication.ApiProductionS;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.dashboard.outlet.AddOutlet;
import com.markrap.dashboard.outlet.ViewOutLets;
import com.markrap.fragments.AllStoresFrg;
import com.markrap.fragments.AppViewModel;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.login.LoginActivity;
import com.markrap.util.AnimUtil;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;
import com.wallet.retrofitapi.api.RxAPICallHelper;
import com.wallet.retrofitapi.api.RxAPICallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import Communication.BuildRequestParms;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

import static android.content.Intent.ACTION_DIAL;


public class StockistAdapter extends RecyclerView.Adapter<StockistAdapter.MyViewHolder> {

    private MainActivity ira1;
    private String lat, lng;
    private JSONArray moviesList;
    Context mContext;
    private GpsTracker gpsTracker;
    ProgressDialog pd;
    private String storeId = "";
    private AllStoresFrg allStoresFrg;
    Uri imageUrli;
    private MainActivity mainActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStoreNAme, txtno, txtStoreDescription, txtLatitude, txtLongitude, txtMaketNAme, txtMobileNumber;
        ImageView imageStoreLogo;
        LinearLayout mainLinear;
        TextView btnupdatelocation, txtDistance;
        LinearLayout llLatLng;
        ImageView imgCall;
        CircleImageView imagMAp;

        public MyViewHolder(View view) {
            super(view);
            txtDistance = view.findViewById(R.id.txtDistance);
            txtno = view.findViewById(R.id.txtno);
            imgCall = view.findViewById(R.id.imagCall);
            txtMobileNumber = view.findViewById(R.id.txtMobileNumber);

            txtMaketNAme = view.findViewById(R.id.txtMaketNAme);
            txtStoreNAme = view.findViewById(R.id.txtStoreNAme);
            txtStoreDescription = view.findViewById(R.id.txtStoreDescription);
            imageStoreLogo = view.findViewById(R.id.imageStoreLogo);
            mainLinear = view.findViewById(R.id.mainLinear);
            txtLatitude = view.findViewById(R.id.txtLatitude);
            txtLongitude = view.findViewById(R.id.txtLongitude);
            btnupdatelocation = view.findViewById(R.id.btnupdatelocation);
            llLatLng = view.findViewById(R.id.llLatLng);
            imagMAp = view.findViewById(R.id.imagMAp);


        }
    }

    public StockistAdapter(JSONArray moviesList, MainActivity ira, AllStoresFrg allStoresFrg) {
        this.moviesList = moviesList;
        ira1 = ira;
        this.allStoresFrg = allStoresFrg;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_view, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);

            holder.txtMobileNumber.setText(jsonObject.getString("stockist_mobile"));
            holder.txtMaketNAme.setText(jsonObject.getString("market_name"));

            holder.txtStoreNAme.setText(jsonObject.getString("store_name"));
            holder.txtStoreDescription.setText(jsonObject.getString("organization"));
            showImage(jsonObject.getString("image"), holder.imageStoreLogo);
            holder.txtno.setTag(jsonObject);


            if (jsonObject.getString("latitude") == null || jsonObject.getString("latitude").equalsIgnoreCase("null")) {
                holder.llLatLng.setVisibility(View.GONE);
                holder.btnupdatelocation.setVisibility(View.VISIBLE);

            } else {

                holder.llLatLng.setVisibility(View.VISIBLE);
                holder.btnupdatelocation.setVisibility(View.VISIBLE);
                holder.txtLatitude.setText("Captured Lat: " + jsonObject.getString("latitude"));
                holder.txtLongitude.setText("Captured Long: " + jsonObject.getString("longitude"));


            }


            holder.btnupdatelocation.setTag(position);
            holder.btnupdatelocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int index = Integer.parseInt(v.getTag().toString());
                        JSONObject data = moviesList.getJSONObject(index);
                        checkPermissionGranted(data.getString("id"), "Woluld you like to update (" + data.getString("store_name") + ") location?");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


            holder.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        actionCall(holder.txtMobileNumber.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.imagMAp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openMaponlystoreLoc(jsonObject.getString("latitude"), jsonObject.getString("longitude"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.txtno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        JSONObject dataObj = new JSONObject(v.getTag().toString());
                        Intent intent = new Intent(ira1, ViewOutLets.class);
                        intent.putExtra(AppConstants.storeName, dataObj.getString("store_name"));
                        intent.putExtra(AppConstants.id, dataObj.getString("id"));
                        intent.putExtra(AppConstants.outlets, dataObj.getString("outlets"));
                        AllStoresFrg.selectedSToreArray = dataObj.getJSONArray("outlets");
                        ira1.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

//            holder.chSelected.setText(jsonObject.getString("name"));
//            holder.chSelected.setTag(jsonObject);

//
//            "data": [{
//                "id": "1",
//                        "store_name": "GE Electronic",
//                        "organization": "Gagan Enterprise Pvt. ltd.",
//                        "route_name": "test route",
//                        "gst_no": "ANND4545541",
//                        "lic_no": "Unn15522ss622",
//                        "supp_name": "Gagan Enterprise",
//                        "supp_type": "Wholesale",
//                        "supp_email": "geelec@gmail.com",
//                        "pan_no": "CEKJK5587",
//                        "address": "Mohali 5 Ph",
//                        "image": "https:\/\/neptunesolution.in\/marketing\/uploads\/supplier\/169827787.jpg",
//                        "publish": "1",
//                        "created_at": "2022-02-22 13:23:38"
//            }]

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return moviesList.length();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void showImage(String image_url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage);
        Glide.with(ira1).load(image_url).apply(requestOptions).into(imageView);
    }

    Dialog dialog;
    ImageView imgOutlet;

    private void updateLocationLoc(String id, String msg, String lat, String lng) {
        imageUrli = null;
        storeId = id;
        dialog = new Dialog(ira1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_loc_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        imgOutlet = dialog.findViewById(R.id.imgOutlet);
        TextView txtError = dialog.findViewById(R.id.txtMsg);
        txtError.setText(msg);
        ImageButton imgCross = dialog.findViewById(R.id.imgCross);
        TextView txtLatitude = dialog.findViewById(R.id.txtLatitude);
        TextView txtLongitude = dialog.findViewById(R.id.txtLongitude);

        txtLatitude.setText("Lat : " + lat);
        txtLongitude.setText("Lng : " + lng);

        System.out.println("Long===" + lng);
        dialog.findViewById(R.id.takeaphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ira1.checkPermission(new ImageAttendanceCallBack() {
                    @Override
                    public void getImageUri(Uri uri) {
                        imageUrli = uri;
                        System.out.println("Image url==" + imageUrli);
                        imgOutlet.setVisibility(View.VISIBLE);

                        Glide.with(mContext)
                                .load(imageUrli)
                                .placeholder(R.drawable.side_image)
                                .centerCrop()
                                .into(imgOutlet);
                    }
                });

            }
        });

        dialog.findViewById(R.id.txtyes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imageUrli == null) {
                    ira1.showErrorDialog("Please take stockist image");
                } else
                    {
                       // updateLoc(lat,lng);
                    updateLoc(new File(getRealPathFromURI(imageUrli)), lat, lng);
                }


            }
        });

        dialog.findViewById(R.id.txtno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.imgCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


    private void checkPermissionGranted(String id, String msg) {
        try {
            if (ContextCompat.checkSelfPermission(ira1, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ira1, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {

                getLocation(id, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getLocation(String id, String msg) {
        pd = new ProgressDialog(ira1);
        gpsTracker = new GpsTracker(ira1);
        if (gpsTracker.canGetLocation()) {

            pd.setMessage("Fetching your current location");
            pd.show();
            pd.setCancelable(false);
            callHandler(id, msg);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    final Handler handler = new Handler();

    private void callHandler(String id, String msg) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                if (latitude > 0 && longitude > 0) {
                    pd.dismiss();
                    updateLocationLoc(id, msg, latitude + "", longitude + "");
                    System.out.println("Lat lnt===" + latitude + "==" + longitude);
                }

            }
        }, 1000);
    }


//    public void updateLoc(String lat,String lng)
//    {
//        LinkedHashMap<String, String> m = new LinkedHashMap<>();
//        m.put("stockist_id", storeId);
//        m.put("user_id", ira1.getLoginData("id"));
//        m.put("latitude", lat);
//        m.put("longitude", lng);
//        m.put("image", "");
//
//        System.out.println("Beforrrr==="+m);
//
//
//        Map<String, String> headerMap = new HashMap<>();
//        new ServerHandler().sendToServer(ira1, AppConstants.apiUlr+"stockistvisit", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
//            @Override
//            public void getRespone(String dta, ArrayList<Object> respons) {
//                try {
//                    System.out.println("Stockist loc"+dta);
//                    JSONObject obj = new JSONObject(dta);
//                    if (obj.getInt("result")>0)
//                    {
//                     allStoresFrg.getStores();
//                        ira1.showErrorDialog("Captured Stockist location done successfully.");
//                    }
//                    else
//                    {
//                        if(obj.has("errors")) {
//                            ira1.showErrorDialog(obj.getString("errors"));
//                        }
//                        else{
//                            ira1.showErrorDialog(obj.getString("msg"));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    public void updateLoc(File file, String lat, String lng) {
        mainActivity = (MainActivity) ira1;
        BuildRequestParms buildRequestParms = new BuildRequestParms();
        AppViewModel apiParamsInterface = ApiProductionS.getInstance(ira1).provideService(AppViewModel.class);


        Observable<JSONObject> observable = null;
        ArrayList<Object> imageAr1 = buildRequestParms.getMultipart("image", file);
        MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);

        System.out.println("Data===="+ira1.getLoginData("id")+"==="+storeId);
        observable = apiParamsInterface.updateStockistLatLng(
                buildRequestParms.getRequestBody(ira1.getLoginData("id")),
                buildRequestParms.getRequestBody(storeId),
                buildRequestParms.getRequestBody(lat),
                buildRequestParms.getRequestBody(lng),
                body1
        );



        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please wait..");


        Disposable disposable = RxAPICallHelper.call(observable, new RxAPICallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject uploadFileResponse) {

                try {

                    System.out.println("Uploaded response===" + uploadFileResponse);

                    if (uploadFileResponse.has("errors")) {
                        allStoresFrg.getStores();
                    //    ira1.showErrorDialog("Captured Stockist location done successfully.");
                        ira1.showErrorDialog("Marked successfully.");

                        dialog.dismiss();
                    } else {
                        allStoresFrg.getStores();
                    //    ira1.showErrorDialog("Captured Stockist location done successfully.");
                        ira1.showErrorDialog("Marked successfully.");

                        dialog.dismiss();
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

    private void actionCall(String phoneNumber) {

        try {
            PackageManager pm = ira1.getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                ira1.startActivity(intent);
                AnimUtil.slideFromRightAnim(ira1);
            } else {
                Toast.makeText(ira1, "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openMaponlystoreLoc(String des_lat, String des_ltg) {
        try {
            //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + src_lat + "," + src_ltg + "&daddr=" + des_lat + "," + des_ltg));

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + des_lat + "," + des_ltg));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            ira1.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    private void loadData()
//    {
//        BuildRequestParms buildRequestParms = new BuildRequestParms();
//        AppViewModel apiParamsInterface = ApiProductionS.getInstance(ira1).provideService(AppViewModel.class);
//
//
//        ArrayList<Object> imageAr1 = buildRequestParms.getMultipart(side, file);
//        MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);
//
//
//        Observable<String> observable = apiParamsInterface.uploadImage(
//                buildRequestParms.getRequestBody("UploadKYC"),
//                buildRequestParms.getRequestBody(appCompatActivity.getLoginData("MemberId")),
//                buildRequestParms.getRequestBody(appCompatActivity.getUsername().toString()),
//                buildRequestParms.getRequestBody(appCompatActivity.getPassword().toString()),
//                buildRequestParms.getRequestBody(proofType),
//                buildRequestParms.getRequestBody(side),
//                body1,
//                "Bearer " + appCompatActivity.savePreferences.reterivePreference(appCompatActivity, AppConstants.token).toString()
//
//        );
//        final ProgressDialog mProgressDialog = new ProgressDialog(appCompatActivity);
//        mProgressDialog.show();
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.setTitle(appCompatActivity.getResources().getString(R.string.pleasewait));
//
//
//        Disposable disposable = RxAPICallHelper.call(observable, new RxAPICallback<String>() {
//            @Override
//            public void onSuccess(String uploadFileResponse) {
//                System.out.println("Inside on sucess=====>" + uploadFileResponse);
//                try {
//                    mProgressDialog.dismiss();
//                    appCompatActivity.setImageUri(imageView1,uri);
//                }
//                catch (Exception e) {
//                    mProgressDialog.dismiss();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//
//                mProgressDialog.dismiss();
//                appCompatActivity.showErrorDialog(new JSONObject(), "Images not uploaded", "202", new CallBack() {
//                    @Override
//                    public void getRespone(String dta, ArrayList<Object> respons) {
//                        if (dta.equalsIgnoreCase("true")) {
//
//                        }
//                    }
//                });
//
//            }
//        });
//
//    }












}
