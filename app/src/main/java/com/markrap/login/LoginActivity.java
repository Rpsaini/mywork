package com.markrap.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.preferences.SavePreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.splash.SplashActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    TextView txtSignup;
    TextView verify_btn,tv_forgot_password;

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void setUp() {
        init();
    }

    private void init() {
        txtSignup = (TextView) findViewById(R.id.txtSignup);
        verify_btn = (TextView) findViewById(R.id.verify_btn);
        tv_forgot_password= (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);
        txtSignup.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v)
    {
        if(verify_btn==v)
        {
            TextInputLayout textinputUsername =findViewById(R.id.textinputUsername);
            TextInputLayout textPassword =findViewById(R.id.textPassword);

            if(textinputUsername.getEditText().getText().toString().length()<=0)
            {
                showErrorDialog("Enter Username");
                return;
            }
            if(textPassword.getEditText().getText().toString().length()<=0)
            {
                showErrorDialog("Enter Password");
                return;
            }
            else
            {
                login(textinputUsername.getEditText().getText().toString(),textPassword.getEditText().getText().toString());
            }
        }


        if (v == txtSignup)
        {
            startActivity(Signup.getIntent(LoginActivity.this));
        }
        if (v == tv_forgot_password) {
            //startActivity(ForgotPassword.getIntent(LoginActivity.this));
            Toast.makeText(getApplicationContext(),"To be reset by Area manager",Toast.LENGTH_SHORT).show();
        }
    }


    public void login(String username,String password)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("username", username);
        m.put("password", password);


        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"login", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        new SavePreferences().savePreferencesData(LoginActivity.this,obj.getString("data"),AppConstants.logindata);
//
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       finish();

//                        {"result":1,"msg":"Login successfully!","data":{"id":"15","first_name":"Ram","last_name":"Singh","email":"ram@gmail.com","phone":"9999999999","designation":"Developet","username":"Test","password":"Test123456","address":"","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/users\/11096044","short_desc":null,"publish":"1","created_at":"2022-03-06 09:40:29","date":""}}
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
