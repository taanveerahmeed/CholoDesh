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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.AccommodationRoomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import helpers.CustomCallBack;
import model.AccommodationProvider;
import model.AccommodationRoom;
import model.ListWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationRoomlistActivity extends AppCompatActivity {
    private static final int DONE_ITEM = 1;
    @BindView(R.id.roomList) RecyclerView roomList;
    // Callbacks
    ProvideCallback provideCallback;
    //List
    List<AccommodationRoom> accommodationRooms=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_roomlist);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setActionBar();
        if (!BaseURL.LANGUAGE_ENG)
        {
            this.setTitle("রুম তালিকা");
        }
        else this.setTitle("Room List");

        //Callback init
        provideCallback=new ProvideCallback(this);
        //----------------------------
        Intent intent=getIntent();
        int providerId=intent.getIntExtra(Travel.PROVIDER_KEY,0);
        // show rooms according to hotel
        showRooms(providerId,provideCallback);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName = "Done";
        if (!BaseURL.LANGUAGE_ENG) menuName = "সম্পন্ন করুন ";
        MenuItem menuItem=menu.add(Menu.NONE,DONE_ITEM,Menu.NONE,menuName);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case DONE_ITEM:
                // Get all selected rooms and pass to the accommodation provider activity
                ArrayList<AccommodationRoom> rooms=new ArrayList<>();

                for (AccommodationRoom ar:accommodationRooms){
                    if(ar.isSelected()){
                        rooms.add(ar);
                        BaseURL.accommodationRooms.add(ar);
                    }
                }


                //Toast.makeText(this,rooms.size()+" activity",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                ListWrapper listWrapper =new ListWrapper();
                listWrapper.setAccommodationRooms(rooms);
                bundle.putString(Travel.KEY_ROOM_LIST,new Gson().toJson(listWrapper));
                intent.putExtras(bundle);
                setResult(200,intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showRooms(int providerId,ProvideCallback callback){
        callback.getAccommodationRoom(providerId).enqueue(new CustomCallBack<AccommodationRoom[]>(this) {
            @Override
            public void onResponse(Call<AccommodationRoom[]> call, Response<AccommodationRoom[]> response) {
                super.onResponse(call,response);

                if (response.body().length > 0)
                {
                    accommodationRooms= Arrays.asList(response.body());
                    AccommodationRoomListAdapter accommodationRoomListAdapter=new AccommodationRoomListAdapter(AccommodationRoomlistActivity.this,accommodationRooms);
                    roomList.setAdapter(accommodationRoomListAdapter);
                }
                else
                {
                    String meesage ="No Room Available";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কিছুই পাওয়া যায়নি";

                    }
                    Toast.makeText(AccommodationRoomlistActivity.this, meesage, Toast.LENGTH_SHORT).show();
                    AccommodationRoomlistActivity.this.onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<AccommodationRoom[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(AccommodationRoomlistActivity.this,meesage,Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
