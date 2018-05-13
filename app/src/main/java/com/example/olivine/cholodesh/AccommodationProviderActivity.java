/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.olivine.cholodesh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.AccommodationListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import helpers.CustomCallBack;
import listeners.AccommodationListener;
import model.AccommodationProvider;
import model.AccommodationRoom;
import model.ListWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationProviderActivity extends AppCompatActivity implements AccommodationListener {

    private ProvideCallback provideCallback;
    private List<AccommodationProvider> accommodationProviders=new ArrayList<>();
    @BindView(R.id.acccommodationProvierList) RecyclerView acccommodationProvierList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_provider);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setActionBar();
        if (!BaseURL.LANGUAGE_ENG)
        {
            this.setTitle("হোটেল তালিকা");
        }
        else this.setTitle("Hotel List");
        //TODO init callback
        provideCallback=new ProvideCallback(this);
        //--------------------------------------------------
        int district_id=getIntent().getIntExtra("district_id",0);
        loadAccommodation(district_id);
    }





    private void loadAccommodation(int id){
        provideCallback.getDestinationWiseAccommodationList(id).enqueue(new CustomCallBack<AccommodationProvider[]>(this) {
            @Override
            public void onResponse(Call<AccommodationProvider[]> call, Response<AccommodationProvider[]> response) {
                super.onResponse(call,response);
                Log.e("Package Url",call.request().url().toString());
                if (response.body().length > 0)
                {
                    accommodationProviders= Arrays.asList(response.body());
                    AccommodationListAdapter accommodationListAdapter=new AccommodationListAdapter(AccommodationProviderActivity.this,accommodationProviders);
                    accommodationListAdapter.setAccommodationListener(AccommodationProviderActivity.this);
                    acccommodationProvierList.setAdapter(accommodationListAdapter);
                }
                else
                {
                    String meesage ="No Accomodations";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কিছুই পাওয়া যায়নি";

                    }
                    Toast.makeText(AccommodationProviderActivity.this,meesage,Toast.LENGTH_LONG).show();
                    AccommodationProviderActivity.this.onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<AccommodationProvider[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(AccommodationProviderActivity.this, meesage, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void calculateAccommodationCost(AccommodationRoom accommodationProvider, int action) {

    }

    @Override
    public void accommodationActivityResult(int requestCode) {
        // Redirect to room Acticity according to hotel
        Intent roomIntent=new Intent(AccommodationProviderActivity.this, AccommodationRoomlistActivity.class);
        roomIntent.putExtra(Travel.PROVIDER_KEY,requestCode);
        startActivityForResult(roomIntent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Pass the selected rooms to Accommodation activity
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=200){
            return;
        }
        String json=data.getExtras().getString(Travel.KEY_ROOM_LIST,null);
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString(Travel.KEY_ROOM_LIST,json);
        intent.putExtras(bundle);
        setResult(200,intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
