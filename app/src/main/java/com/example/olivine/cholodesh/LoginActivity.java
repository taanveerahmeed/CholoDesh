/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.olivine.cholodesh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import constants.Travel;
import helpers.BaseURL;
import helpers.RetrofitInitializer;
import listeners.AuthenticationListener;
import model.Auth;
import model.AuthResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Olivine on 6/9/2017.
 */

public class LoginActivity extends AppCompatActivity {
    Retrofit retrofit;
    AuthenticationListener authenticationListener;

    @BindView(R.id.email)  EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.signup_tv)
    TextView signup_tv;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    // Data storage
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        if (!BaseURL.LANGUAGE_ENG)
        {
            email.setHint("মোবাইল নম্বর");
            password.setHint("পাসওয়ার্ড");
            signup_tv.setText("এখনও কোন অ্যাকাউন্ট নেই? এখানে নিবন্ধন করুন.");
            btnLogin.setText("লগইন");

        }
        // Storeage init
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        editor=sharedPreferences.edit();
        // APi init;
        retrofit= RetrofitInitializer.initNetwork(this);
        authenticationListener=retrofit.create(AuthenticationListener.class);
        setupWindowAnimations();
        checkLogin();


    }

    private void checkLogin(){
        String email=sharedPreferences.getString(Travel.USER_EMAIL,null);
        String password=sharedPreferences.getString(Travel.USER_PASSWORD,null);

        if(email!=null){
            setResult(200);
            finish();
        }
    }
    @OnClick(R.id.signup_tv)
    public void gotoRegistration(){
        Intent intent=new Intent(this,RegistrationActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user registered immediately  redirect to requested activity
        super.onActivityResult(requestCode, resultCode, data);
        if(email!=null){
            setResult(200);
            //finish();
        }

    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin(View view) {
        final String emailString=email.getText().toString();
        String passwordString=password.getText().toString();
        final Auth auth=new Auth();
        auth.setCustomerPhone(emailString).setPassword(passwordString);
        Call<AuthResult> call=authenticationListener.authenTication(auth);
        //Toast.makeText(LoginActivity.this, "Email" + auth.getCustomerEmail(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(LoginActivity.this, "pass" + auth.getPassword(), Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                AuthResult authResult=response.body();
                if(authResult.getAuthentication()){
                    editor.putString(Travel.USER_EMAIL,emailString);
                    editor.commit();

                   // Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    setResult(200);

                    finish();
                }
                else{
                    String meesage ="Login Failed";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" লগইন ব্যর্থ";

                    }
                    Toast.makeText(LoginActivity.this,meesage,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(LoginActivity.this,meesage,Toast.LENGTH_LONG).show();


            }
        });


    }
    private void setupWindowAnimations() {
        //Fade slide = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.fade_animation);
        //getWindow().setExitTransition(slide);
    }
    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(5000);
        // This view will not be affected by enter transition animation
        enterTransition.excludeTarget(R.id.email, true);
        return enterTransition;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
