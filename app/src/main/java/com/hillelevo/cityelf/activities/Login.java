package com.hillelevo.cityelf.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.hillelevo.cityelf.R;


public class Login extends Activity {

    private String phone;
    private String mail;
    private EditText pass;
    private EditText mailOrPhone;
    private android.content.Intent myIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pass = (EditText) findViewById(R.id.editText_Password);
        mailOrPhone = (EditText) findViewById(R.id.editText_mail);

    }


    public void onClick_Login(View view) {
        String str = String.valueOf(mailOrPhone.getText());
        if(str.matches("[-+]?\\d+")){
            phone=str;
            mail=null;
        }
        else{
            phone=null;
            mail=str;
        }
        System.out.println("phone="+phone);
        System.out.println("mail="+mail);
    }
}
