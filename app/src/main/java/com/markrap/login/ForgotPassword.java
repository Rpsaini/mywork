package com.markrap.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.app.preferences.SavePreferences;
import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {
    Button btnForgot;

    public static Intent getIntent(Context context) {
        return new Intent(context, ForgotPassword.class);
    }

    @Override
    protected void setUp() {
        init();
    }

    private void init() {
        btnForgot= (Button) findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.forgot_password;
    }

    @Override
    public void onClick(View v) {
        if (v == btnForgot) {
           // startActivity(Signup.getIntent(ForgotPassword.this));
        }


    }
    public void forgotPassword(String username)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("username", username);


        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"forgotPassword", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("forgotPassword===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {

                    }
                    else
                    {
                        showErrorDialog(obj.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
