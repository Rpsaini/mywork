package com.markrap.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.preferences.SavePreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;

import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Signup extends BaseActivity implements View.OnClickListener {
    TextView txtLogin;
    TextView verify_btn;
    TextInputLayout txtUsername ,txtPassword,text_input_layout_name,text_input_layout_mobile,text_input_layout_email;

    public static Intent getIntent(Context context) {
        return new Intent(context, Signup.class);
    }

    @Override
    protected void setUp() {
        init();
    }

    private void init() {
        txtLogin=(TextView) findViewById(R.id.txtLogin);
        verify_btn=(TextView) findViewById(R.id.verify_btn);

        txtUsername=(TextInputLayout)findViewById(R.id.txtUsername);
        txtPassword=(TextInputLayout)findViewById(R.id.txtPassword);
        text_input_layout_name=(TextInputLayout)findViewById(R.id.text_input_layout_name);
        text_input_layout_mobile=(TextInputLayout)findViewById(R.id.text_input_layout_mobile);
        text_input_layout_email=(TextInputLayout)findViewById(R.id.text_input_layout_email);

        txtLogin.setOnClickListener(this);
        verify_btn.setOnClickListener(this);

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_signup;
    }

    @Override
    public void onClick(View v) {
        if(v==txtLogin){
            startActivity(LoginActivity.getIntent(Signup.this));
        }
      else  if(v==verify_btn)
        {

            if(txtUsername.getEditText().getText().toString().length()==0)
            {
                showErrorDialog("Enter Username");
            }
            else if(txtPassword.getEditText().toString().length()==0)
            {
                showErrorDialog("Enter password");
            }

            else if(text_input_layout_name.getEditText().toString().length()==0)
            {
                showErrorDialog("Enter User Name");
            }
            else if(text_input_layout_mobile.getEditText().toString().length()==0)
            {
                showErrorDialog("Enter Your Mobile number");
            }
            else if(text_input_layout_email.getEditText().toString().length()==0)
            {
                showErrorDialog("Enter Your Email");
            }
            else
            {
                singUp(txtUsername.getEditText().getText().toString(),
                        text_input_layout_mobile.getEditText().getText().toString()
                        ,text_input_layout_email.getEditText().getText().toString(),
                        txtUsername.getEditText().getText().toString(),
                        txtPassword.getEditText().getText().toString()
                        );
            }



        }

    }
    public void singUp(String firstName,String Mobile,String email,String username,String password)
    {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        m.put("user_id", "0");
        m.put("first_name", firstName);
        m.put("last_name", "test");
        m.put("email", email);
        m.put("phone", Mobile);
        m.put("designation", "");
        m.put("username", username);

        m.put("password", password);
        m.put("short_desc", "");
        m.put("image", "");



        Map<String, String> headerMap = new HashMap<>();

        new ServerHandler().sendToServer(this, AppConstants.apiUlr+"signup", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Login===="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result")>0)
                    {
                        new SavePreferences().savePreferencesData(Signup.this,obj.getString("data"),AppConstants.logindata);

                        startActivity(new Intent(Signup.this, MainActivity.class));
                        finish();
//                   //"result":1,"msg":"Registration successfull, User will approve by Administrator!",
//        //"data":{"id":"17","first_name":"Ram","last_name":"test","email":"tespppp@gmail.com",
//       // "phone":"9098989890","designation":"","created_at":"2022-03-07 05:55:36"}}
                       // showErrorDialog(obj.getString("msg"));

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

