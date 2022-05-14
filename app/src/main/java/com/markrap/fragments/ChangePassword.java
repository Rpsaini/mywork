package com.markrap.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.markrap.R;
import com.markrap.adapter.NotificationAdapter;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.login.LoginActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ChangePassword extends Fragment implements View.OnClickListener {
    public static final String TAG = "HomeFragments";
    TextView txtChangePassword;
    private MainActivity mainActivity;
    TextInputLayout textinputoldpassword,textNewPassword,confirmTextPassword;

    private View rootView;
    String id;
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                ChangePassword.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_changepassword, container, false);

       mainActivity= (MainActivity)getActivity();
        init();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@MainHome_ONResume---", "onResume---");
    }

    private void init() {
        txtChangePassword = (TextView) rootView.findViewById(R.id.txtChangePassword);
        txtChangePassword.setOnClickListener(this);
    }


    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id",mainActivity.getLoginData("id"));
        m.put("current_password", oldPassword);
        m.put("password", newPassword);
        m.put("cpassword", confirmPassword);

        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr + "changepassword", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0)
                    {

                        showErrorDialog1(obj.getString("msg"));

                        textinputoldpassword.getEditText().setText("");
                        textNewPassword.getEditText().setText("");
                        confirmTextPassword.getEditText().setText("");



                    } else
                        {
                            System.out.println("Inside else==");

                             mainActivity.showErrorDialog(obj.getString("msg"));


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public   void  showErrorDialog1(String msg)
    {
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.showerror_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtError=dialog.findViewById(R.id.txtError);
        txtError.setText(msg);

        dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mainActivity.setupHomeFragment();
            }
        });

        dialog.show();
    }


    @Override
    public void onClick(View view) {
        if (txtChangePassword == view) {
            TextInputLayout textinputoldpassword = rootView.findViewById(R.id.textinputoldpassword);
            TextInputLayout textNewPassword = rootView.findViewById(R.id.textNewPassword);
            TextInputLayout confirmTextPassword = rootView.findViewById(R.id.confirmTextPassword);

            if (textinputoldpassword.getEditText().getText().toString().length() <= 0) {
                mainActivity.showErrorDialog("Enter Old password");
                return;
            }
            if (textNewPassword.getEditText().getText().toString().length() <= 0) {
                mainActivity.showErrorDialog("Enter New Password");
                return;
            }
            if (confirmTextPassword.getEditText().getText().toString().length() <= 0) {
                mainActivity.showErrorDialog("Enter Confirm Password");
                return;
            }
            if (!confirmTextPassword.getEditText().getText().toString().equalsIgnoreCase(textNewPassword.getEditText().getText().toString()))
            {
                mainActivity.showErrorDialog("Enter Password does not matched");
                return;
            }

            else {
                changePassword(textinputoldpassword.getEditText().getText().toString(),textNewPassword.getEditText().getText().toString(),confirmTextPassword.getEditText().getText().toString());

            }
        }
    }



}

