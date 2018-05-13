/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.olivine.cholodesh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import callbacks.BookingCallback;
import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import io.realm.Realm;
import model.AccommodationRoom;
import model.HotelBooking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import helpers.CustomCallBack;

import static adapters.HotelListAdapter.SERVICE_ID;

public class HotelRoomListActivity extends AppCompatActivity {
    // For Separate Hotel Room Booking outside Tailor made.
    private List<AccommodationRoom> accommodationRooms;
    private static final int BOOK_MENU = 1;
    ProvideCallback provideCallback;
    BookingCallback bookingCallback;
    // Data strorage
    SharedPreferences sharedPreferences;
    Realm realm;
    //View
    @BindView(R.id.hotelRooms) RecyclerView hotelRooms;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName = "Confirm Room";
        if (!BaseURL.LANGUAGE_ENG)
        {
            menuName = "কনফার্ম ";
        }
        MenuItem menuItem=menu.add(Menu.NONE,BOOK_MENU,Menu.NONE,menuName);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case BOOK_MENU:
                final AlertDialog.Builder roomConfirmDialog=new AlertDialog.Builder(this);

                String no = "No";
                String yes = "Yes";
                String title = "Confirmation";
                String message = "Do You want to confirm Booking ?";

                if (!BaseURL.LANGUAGE_ENG)
                {
                    title = "অনুমোদন";
                    message = "আপনি বুকিং নিশ্চিত করতে চান?";
                    yes = "হ্যা";
                    no = "না";
                }
                roomConfirmDialog.setTitle(title);
                roomConfirmDialog.setMessage(message);
                roomConfirmDialog.setNegativeButton(no,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                // If user select YES then redirect to the login if not logged in or confirm booking
                roomConfirmDialog.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String customerEmail=sharedPreferences.getString(Travel.USER_EMAIL,null);
                        if(customerEmail==null){
                            Intent loginIntent=new Intent(HotelRoomListActivity.this,LoginActivity.class);
                            startActivityForResult(loginIntent,110);

                            return;
                        }
                        reserveRoom();
                    }
                });
                boolean flag = false;

                for (int itr = 0; itr< accommodationRooms.size();itr++ )
                {
                    if (accommodationRooms.get(itr).isSelected())
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)roomConfirmDialog.show();
                else
                {
                    String meesage ="No Room is Selected";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কোন রুম নির্বাচন করা হয়নি ";

                    }
                    Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();

                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==200){
            reserveRoom();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_room);
        ButterKnife.bind(this);
        if (!BaseURL.LANGUAGE_ENG)
        {
            this.setTitle("রুম সমূহ");
        }
        else this.setTitle("Hotel Rooms");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Storage init
        Realm.init(this);
        realm=Realm.getDefaultInstance();
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        int serviceId= getIntent().getIntExtra(SERVICE_ID,0);
        // callback init
        provideCallback=new ProvideCallback(this);
        bookingCallback=new BookingCallback(this);
        // get hotel rooms
        provideCallback.getAccommodationRoom(serviceId).enqueue(new CustomCallBack<AccommodationRoom[]>(this) {
            @Override
            public void onResponse(Call<AccommodationRoom[]> call, Response<AccommodationRoom[]> response) {
                super.onResponse(call,response);
                Log.e("Package Url",call.request().url().toString());
                if(response.body().length > 0)
                {
                    accommodationRooms= Arrays.asList(response.body());
                    AccommodationRoomListAdapter accommodationRoomListAdapter=new AccommodationRoomListAdapter(HotelRoomListActivity.this,accommodationRooms);
                    hotelRooms.setAdapter(accommodationRoomListAdapter);
                }
                else
                {
                    String meesage ="Nothing Found";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কিছুই পাওয়া যায়নি";

                    }
                    Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();

                    HotelRoomListActivity.this.onBackPressed();
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
                Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();
            }
        });

    }
    private void reserveRoom(){
        final List<AccommodationRoom> selectedAccommodationRooms=new ArrayList<AccommodationRoom>();
        // Get selected Roooms
        for (AccommodationRoom selected_room:accommodationRooms){
            if (selected_room.isSelected()){
                selectedAccommodationRooms.add(selected_room);
            }
        }
        String customerEmail=sharedPreferences.getString(Travel.USER_EMAIL,null);
        final HotelBooking rooms=new HotelBooking();
        rooms.setCustomerEmail(customerEmail);
        rooms.setAccommodationRoomList(selectedAccommodationRooms);
        Gson gson = new Gson();
        String json = gson.toJson(rooms);
        Log.e("Url","Null");
        Log.e("JSON",json );
        bookingCallback.confirmBooking(rooms).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //realm.beginTransaction();
                //Booking booking=new Booking();


                // Get Booking token from server
                // booking.tokenNO=response.body();
                if (response.body() != null)
                {
                    String meesage ="Your Booking was succesful. Find your token in view token section.";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" আপনার বুকিং সফল হয়েছে ";

                    }
                    Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();
                    Intent bookinListintent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(bookinListintent);
                    //Toast.makeText(HotelRoomListActivity.this,response.body(),Toast.LENGTH_LONG).show();
                }
                else
                {
                    String meesage ="Something Went Wrong";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কিছু ভুল হয়েছে";

                    }
                    Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();
                    //Toast.makeText(HotelRoomListActivity.this,"Null",Toast.LENGTH_LONG).show();
                }



                // Save to database
                //booking.accommodationRooms=new RealmList<>();
                //booking.accommodationRooms.addAll(selectedAccommodationRooms);
                //realm.copyToRealm(booking);
                //realm.commitTransaction();



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(HotelRoomListActivity.this,meesage,Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
