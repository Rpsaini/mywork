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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.communication.ApiProductionS;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.dashboard.outlet.CategoryList;
import com.markrap.dashboard.outlet.ProductList;
import com.markrap.dashboard.outlet.ViewOutLets;
import com.markrap.fragments.AppViewModel;
import com.markrap.gpstracter.GpsTracker;
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

public class OutLetsAdapter extends RecyclerView.Adapter<OutLetsAdapter.MyViewHolder> {

     ViewOutLets ira1;
    private JSONArray moviesList;
    Context mContext;
    ProgressDialog pd;
    String outlet_id = "";
    Uri imageUrli;
    String msg="";
    private String lat, lng;
    private GpsTracker gpsTracker;
    ViewOutLets mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskDescription, txtTaskName, address, txtLatitude, txtLongitude;
        ImageView taskImage;
        LinearLayout mainLinear;
        TextView updatestatus;

        public MyViewHolder(View view) {
            super(view);
            txtLatitude = view.findViewById(R.id.txtLatitude);
            txtLongitude = view.findViewById(R.id.txtLongitude);

            taskDescription = view.findViewById(R.id.taskDescription);
            taskImage = view.findViewById(R.id.taskImage);
            txtTaskName = view.findViewById(R.id.txtTaskName);
            address = view.findViewById(R.id.address);
            mainLinear = view.findViewById(R.id.mainLinear);
            txtLatitude = view.findViewById(R.id.txtLatitude);
            txtLongitude = view.findViewById(R.id.txtLongitude);
            updatestatus = view.findViewById(R.id.updatestatus);


        }
    }

    public OutLetsAdapter(JSONArray moviesList, ViewOutLets ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public OutLetsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_outlets_listrow, parent, false);
        mContext = parent.getContext();
        return new OutLetsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OutLetsAdapter.MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);
            holder.taskDescription.setText(jsonObject.getString("owner_name"));
            holder.txtTaskName.setText(jsonObject.getString("outlet_name"));
            holder.address.setText(jsonObject.getString("address"));

            holder.txtLatitude.setText("Latitude: " + jsonObject.getString("latitude"));
            holder.txtLongitude.setText("Longitude: " + jsonObject.getString("longitude"));

            holder.updatestatus.setTag(jsonObject + "");
            holder.updatestatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        outlet_id = jsonObject.getString("id");
                      //   msg="Update today visiting status for (" + jsonObject.getString("outlet_name") + ") Outlet";
                           msg="Market Visit at Outlet (" + jsonObject.getString("outlet_name") + ") Outlet";

                        checkPermissionGranted();
//                        ira1.confirmBox("Update today visiting status for (" + jsonObject.getString("outlet_name") + ") Outlet","outlet", new CallBack() {
//                            @Override
//                            public void getRespone(String dta, ArrayList<Object> respons) {
//                                if (dta.equalsIgnoreCase("true")) {
//
//                                    checkPermissionGranted();
//                                }
//                            }
//                        }, new ImageAttendanceCallBack() {
//                            @Override
//                            public void getImageUri(Uri uri) {
//
//                                try {
//                                    imageUrli = uri;
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("Leng===" + moviesList.length());
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





    private void checkPermissionGranted() {
        try {
            if (ContextCompat.checkSelfPermission(ira1, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ira1, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {

                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation() {
        pd = new ProgressDialog(ira1);
        gpsTracker = new GpsTracker(ira1);
        if (gpsTracker.canGetLocation()) {

            pd.setMessage("Fetching Outlet location");
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
                    markAtten(latitude + "", longitude + "");

                }

            }
        }, 1000);
    }


    private void markAtten(String latitude, String longitude) {

        outletVisit(msg,latitude,longitude);
     //   updateVisit(new File(getRealPathFromURI(imageUrli)), latitude, longitude);
    }


    public void updateVisit(String latitude, String longitude) {




        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("outlet_id", outlet_id);
        m.put("user_id", ira1.getLoginData("id"));
        m.put("latitude", latitude);
        m.put("longitude", longitude);

        System.out.println("Before data==="+m);

        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(mContext, AppConstants.apiUlr+"outletvisit", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("outletvisit===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        if(obj.has("errors")) {
                            ira1.showErrorDialog(obj.getString("errors"));
                        }
                        else{
                            ira1.showErrorDialog(obj.getString("msg"));
                        }

                    }
                    else
                    {
                        if(obj.has("errors")) {
                            ira1.showErrorDialog(obj.getString("errors"));
                        }
                        else{
                            ira1.showErrorDialog(obj.getString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



//
//        BuildRequestParms buildRequestParms = new BuildRequestParms();
//        AppViewModel apiParamsInterface = ApiProductionS.getInstance(ira1).provideService(AppViewModel.class);
//        Observable<JSONObject> observable = null;
//
////        ArrayList<Object> imageAr1 = buildRequestParms.getMultipart("outlet_image", file);
////        MultipartBody.Part body1 = (MultipartBody.Part) imageAr1.get(0);
//
//
//        observable = apiParamsInterface.outletvisit(
//                buildRequestParms.getRequestBody(outlet_id),
//                buildRequestParms.getRequestBody(ira1.getLoginData("id")),
//                buildRequestParms.getRequestBody(latitude),
//                buildRequestParms.getRequestBody(longitude)
//
//
//        );
//
//        final ProgressDialog mProgressDialog = new ProgressDialog(ira1);
//        mProgressDialog.show();
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.setTitle("Please wait..");
//
//        Disposable disposable = RxAPICallHelper.call(observable, new RxAPICallback<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject uploadFileResponse) {
//
//                try {
//
//
//                    if(uploadFileResponse.has("errors"))
//                    {
//                        ira1.showErrorDialog(uploadFileResponse.getString("errors"));
//                    }
//                    else {
//                        ira1.showErrorDialog("Your Outlet visit has been done successfully");
//                    }
//                    mProgressDialog.dismiss();
//                } catch (Exception e) {
//                    mProgressDialog.dismiss();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//                System.out.println("error===" + throwable.getMessage());
//                mProgressDialog.dismiss();
//
//
//            }
//        });


    }


    private String getRealPathFromURI(Uri uri) {
        File myFile;
        myFile = new File(uri.getPath());
        return myFile.getAbsolutePath();
    }



    public   void  outletVisit(String outletname,String lat,String lng)
    {

        final Dialog dialog = new Dialog(ira1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.outlet_markvisit_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        ImageButton imgCross=dialog.findViewById(R.id.imgCross);
        TextView txtOutletname=dialog.findViewById(R.id.txtOutletname);
        TextView txtlat=dialog.findViewById(R.id.txtlat);
        TextView txtlng=dialog.findViewById(R.id.txtlng);
        EditText edNumberOfProductsAvailOutlet=dialog.findViewById(R.id.edNumberOfProductsAvailOutlet);
        EditText edTotalOrderValue=dialog.findViewById(R.id.edTotalOrderValue);

        txtOutletname.setText(outletname);
        txtlat.setText("Captured Lat : "+lat);
        txtlng.setText("Captured Long :"+lng);
        dialog.findViewById(R.id.imgCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity=(ViewOutLets) ira1;
                if(edNumberOfProductsAvailOutlet.getText().toString().length()==0)
                {
                    ira1.showErrorDialog("Number of ITC Lines available in Outlet");

                }
                else if(edTotalOrderValue.getText().toString().length()==0)
                {
                    ira1.showErrorDialog("Today's Order Value in Rs.");
                }
                else
                {

                    outletVistedDataSubmit(mainActivity.getLoginData("id"),outlet_id,edNumberOfProductsAvailOutlet.getText().toString().trim(),edTotalOrderValue.getText().toString().trim(),dialog);               }



                }
        });

        dialog.findViewById(R.id.txtMarkVisit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVisit(lat,lng);
            }
        });
    }


    public void outletVistedDataSubmit(String user_id,String outlet_id,String no_product,String total_value,Dialog dialog)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id", user_id);
        m.put("outlet_id", outlet_id);
        m.put("no_product", no_product);
        m.put("total_value", total_value);
        Map<String, String> headerMap = new HashMap<>();
        new ServerHandler().sendToServer(mContext, AppConstants.apiUlr+"addoutletdetorder", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    Log.i("addoutletdetorder",""+AppConstants.apiUlr+"addoutletdetorder"+m);
                    System.out.println("addoutletdetorder===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        ira1.showErrorDialog("Submit done.");
                        dialog.dismiss();

                    }
                    else
                    {
                        if(obj.has("errors")) {
                            ira1.showErrorDialog(obj.getString("errors"));
                        }
                        else{
                            ira1.showErrorDialog(obj.getString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
