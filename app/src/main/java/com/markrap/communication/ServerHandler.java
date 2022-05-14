package com.markrap.communication;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.markrap.R;


import java.util.HashMap;
import java.util.Map;

public class ServerHandler {
    private Dialog progressdlg;
    private UtilClass utilClass;
    private Context ct1;
    private int loaderLayout;


    public ServerHandler() {
    }

    public boolean CheckInternetState(Context ct, int sholoader) {
        try {
            utilClass = new UtilClass(ct);
            ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) {
                return true;
            } else {
                if (sholoader <= 0) {
                    utilClass.show_alert("Network", "Network Error.");
                }
                return false;
            }
        } catch (Exception e) {
            utilClass.show_alert("Error", e.getMessage());
        }
        return false;
    }

    public void sendToServer(final Context ct, String url, final Map<String, String> data, final int showloader, final Map<String, String> headerData, final int requestType, int loaderLayout, final CallBack cb) {
        ct1 = ct;
        this.loaderLayout = loaderLayout;
        if (progressdlg != null && progressdlg.isShowing()) {
            progressdlg.dismiss();
        }
        if (CheckInternetState(ct, showloader)) {
            try {
                data.put("device_info", getDeviceName());
                data.put("device_type", "Android");


                int requestMethod=Request.Method.POST;
                if(requestType==0)
                {
                    requestMethod=Request.Method.GET;
                }


                StringRequest stringRequest = new StringRequest(requestMethod, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    System.out.println("Response back======" + response);
                                    cb.getRespone(response, null);
                                    progressdlg.dismiss();

                                } catch (Exception e) {
                                    cb.getRespone("error", null);
                                    if (showloader <= 0)
                                        utilClass.show_alert("Network", "Network Error.");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                System.out.println("inside on error===="+error);
                                try {
                                    //  int statusCode= error.networkResponse.statusCode;
                                    if (progressdlg != null && progressdlg.isShowing()) {
                                        progressdlg.dismiss();
                                    }
                                    if (error != null) {
                                        byte[] data1 = error.networkResponse.data;
                                        char[] ch = new String(data1, "UTF-8").toCharArray();
                                        cb.getRespone(new String(ch), null);

                                    }
                                    else
                                    {
                                        System.out.println("Error=="+error);
                                        cb.getRespone("Error", null);
                                }

                                } catch (Exception e) {
                                    cb.getRespone("Error", null);
                                    e.printStackTrace();
                                }
                            }
                        }



                        ) {
                    @Override
                    protected Map<String, String> getParams() {
                        return data;
                    }


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        try {

                              params.put("Authorization", headerData.get("Authorization"));
//                            params.put("Content-Type", "application/json");

                            System.out.println("Heade data=="+params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return params;

                    }

                    ;
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                RequestQueue requestQueue = Volley.newRequestQueue(ct);
                requestQueue.add(stringRequest);

                stringRequest.setShouldCache(true);
                showProgressDialog();
                progressdlg.show();

                if (showloader >= 1) {
                    progressdlg.dismiss();
                }
            } catch (Exception e) {
                if (progressdlg != null)
                    progressdlg.dismiss();

            }
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    private void showProgressDialog() {
        progressdlg = new Dialog(ct1);
        progressdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdlg.setContentView(loaderLayout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = progressdlg.getWindow();
        lp.copyFrom(window.getAttributes());
        progressdlg.setCancelable(false);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        progressdlg.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        progressdlg.getWindow().setDimAmount(0);
        progressdlg.show();

    }
}