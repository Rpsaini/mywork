package com.markrap.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;
import com.markrap.communication.ApiProductionS;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.gpstracter.GpsTracker;
import com.markrap.login.LoginActivity;
import com.markrap.utility.AppConstants;
import com.wallet.retrofitapi.api.RxAPICallHelper;
import com.wallet.retrofitapi.api.RxAPICallback;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

import Communication.BuildRequestParms;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;


public class AttendanceFragments extends Fragment {
    public static final String TAG = "HomeFragments";
    View rootView;
    Uri imageUrli;
    private String dataCheckIn = "";
    private MainActivity mainActivity;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int IMAGE_REQUEST_GALLERY = 328;
    public static final int IMAGE_REQUEST_CAMERA = 329;
    private static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    String imagePathUrl;
    File file;
    private Uri cameraImageUri;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                AttendanceFragments.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.attendance_fragment, container, false);

        mainActivity = (MainActivity) getActivity();
        setUserData();
        rootView.findViewById(R.id.rr_checkIn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mainActivity.confirmBox("Do you want to CheckIn ?","attendance", new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons) {
                        if (dta.equalsIgnoreCase("true")) {
                            dataCheckIn = "in";
                            checkPermissionGranted();
                        }
                    }
                }, new ImageAttendanceCallBack() {
                    @Override
                    public void getImageUri(Uri uri) {

                        try {


                            imageUrli = uri;


                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


        rootView.findViewById(R.id.imageChekdOut).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.confirmBox("Do you want to CheckOut ?","attendance", new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons) {
                        if (dta.equalsIgnoreCase("true")) {
                            dataCheckIn = "out";
                            checkPermissionGranted();
                        }
                    }
                }, new ImageAttendanceCallBack() {
                    @Override
                    public void getImageUri(Uri uri) {
                        imageUrli=uri;
                    }
                });

            }
        });

        return rootView;
    }


    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private GpsTracker gpsTracker;

    private void checkPermissionGranted() {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {

                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ProgressDialog pd;

    public void getLocation() {
        pd = new ProgressDialog(getActivity());
        gpsTracker = new GpsTracker(getActivity());
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

    private void callHandler()
      {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                if (latitude > 0 && longitude > 0) {
                    pd.dismiss();
                    markAtten(latitude + "", longitude + "");
                    System.out.println("Lat lnt===" + latitude + "==" + longitude);
                }

            }
        }, 1000);
    }


    private void markAtten(String latitude, String longitude) {


       UploadImage(new File(getRealPathFromURI(imageUrli)),latitude,longitude);


    }



    public void UploadImage(File file,String latitude,String longitude)
    {

        BuildRequestParms buildRequestParms = new BuildRequestParms();
        AppViewModel apiParamsInterface = ApiProductionS.getInstance(mainActivity).provideService(AppViewModel.class);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id", mainActivity.getLoginData("id"));
        m.put("on_date", date);
        Observable<JSONObject> observable =null;
        if (dataCheckIn.equalsIgnoreCase("in")) {
            ArrayList<Object> imageAr1 = buildRequestParms.getMultipart("in_image", file);
            MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);

            System.out.println("Body=="+body1);
            observable= apiParamsInterface.uploadImageIn(
                    buildRequestParms.getRequestBody(mainActivity.getLoginData("id")),
                    buildRequestParms.getRequestBody(date),
                    buildRequestParms.getRequestBody(latitude),
                    buildRequestParms.getRequestBody(longitude),
                    buildRequestParms.getRequestBody("image/jpeg"),
                    body1
            );

        } else {
            ArrayList<Object> imageAr1 = buildRequestParms.getMultipart("out_image", file);
            MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);
            observable= apiParamsInterface.uploadImageout(
                    buildRequestParms.getRequestBody(mainActivity.getLoginData("id")),
                    buildRequestParms.getRequestBody(date),
                    buildRequestParms.getRequestBody(latitude),
                    buildRequestParms.getRequestBody(longitude),
                    buildRequestParms.getRequestBody("image/jpeg"),
                    body1
            );
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(mainActivity);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Please wait..");







        Disposable disposable = RxAPICallHelper.call(observable, new RxAPICallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject uploadFileResponse) {

                try
                {
                    if(dataCheckIn.equalsIgnoreCase("in"))
                    {
                        if(uploadFileResponse.has("errors"))
                        {
                            mainActivity.showErrorDialog(uploadFileResponse.getString("errors"));
                        }
                        else
                        {
                            mainActivity.showErrorDialog("Checked in successfully.");
                        }

                    }
                    else
                    {

                        if(uploadFileResponse.has("errors"))
                        {
                            mainActivity.showErrorDialog(uploadFileResponse.getString("errors"));
                        }
                        else {
                            mainActivity.showErrorDialog("Checked out successfully.");
                        }
                    }

                    System.out.println("Inside on sucess=====>" + uploadFileResponse.toString());
                    mProgressDialog.dismiss();
                }
                catch(Exception e)
                {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                System.out.println("error==="+throwable.getMessage());
                mProgressDialog.dismiss();


            }
        });


    }


    private String getRealPathFromURI(Uri uri) {
        File myFile;
        myFile = new File(uri.getPath());
        return myFile.getAbsolutePath();
    }


    private void setUserData()
    {
        String first_name = mainActivity.getLoginData("first_name");
        String last_name = mainActivity.getLoginData("last_name");
        String image = mainActivity.getLoginData("image");

        TextView user_name_txt =rootView.findViewById(R.id.user_name_txt);
        user_name_txt.setText("Hi, "+first_name+" "+last_name);
        TextView txtUsernavname=mainActivity.findViewById(R.id.txtUsernavname);
        txtUsernavname.setText(first_name+" "+last_name);
        showImage(image,(ImageView) rootView.findViewById(R.id.user_profile));

    }

    private void showImage(String image_url, ImageView imageView)
    {
        RequestOptions requestOptions=new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.side_image)
                .error(R.drawable.side_image);
        Glide.with(mainActivity).load(image_url).apply(requestOptions).into(imageView);
    }
}

