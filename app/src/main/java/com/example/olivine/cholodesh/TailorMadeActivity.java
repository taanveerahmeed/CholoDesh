/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.olivine.cholodesh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stepstone.stepper.StepperLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.StepperAdapter;
import adapters.TailorMadeAccommodationAdapter;
import adapters.TailorMadeTransportAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import fragments.RouteSelectionFragment;
import helpers.BaseURL;
import helpers.CustomCallBack;
import helpers.DynamicHeight;
import listeners.FragmentInteractionListener;
import model.TailorMadeAccommodation;
import model.TailorMadeItinerary;
import model.TailorMadeTransport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import userDefinder.TailorMade;

public class TailorMadeActivity extends AppCompatActivity implements FragmentInteractionListener {
    // made for client requirement but not active
//    @BindView(R.id.stepperLayout) StepperLayout stepperLayout;
//    private RouteSelectionFragment routeSelectionFragment;
    TextView selectedtransports,selectedhotelrooms,selectedtours,totalcost;
    RecyclerView itineries,rooms,transports;
    ImageView destinationIamge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_made_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itineries = (RecyclerView) findViewById(R.id.itineries);
        rooms = (RecyclerView) findViewById(R.id.rooms);
        transports = (RecyclerView) findViewById(R.id.transports);
        destinationIamge = (ImageView) findViewById(R.id.destinationIamge);

        selectedtours = (TextView) findViewById(R.id.selectedtours);
        selectedtransports = (TextView) findViewById(R.id.selectedtransports);
        selectedhotelrooms = (TextView) findViewById(R.id.selectedhotelrooms);
        totalcost = (TextView) findViewById(R.id.totalcost);

        if (!BaseURL.LANGUAGE_ENG)
        {
            this.setTitle("টেইলার মেড তালিকাসমূহ");
            selectedtours.setText("নির্বাচিত ট্যুর সমূহ ");
            selectedtransports.setText("নির্বাচিত পরিবহন সমূহ");
            selectedhotelrooms.setText("নির্বাচিত হোটেল রুম সমূহ");


        }
        else this.setTitle("TailorMade Lists");


        String id = getIntent().getStringExtra("TAILOR_MADE_ID");
        totalcost.setText(getIntent().getStringExtra("COST_TOTAL"));
        

        final String url = getIntent().getStringExtra("DESTINATION_IMAGE_PATH");
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(destinationIamge);

        DynamicHeight.setHeight(this,destinationIamge,4,3);

        ProvideCallback provideCallback = new ProvideCallback(this);

        provideCallback.getTailorMadeItinerary(id).enqueue(new CustomCallBack<TailorMadeItinerary[]>(this) {
            @Override
            public void onResponse(Call<TailorMadeItinerary[]> call, Response<TailorMadeItinerary[]> response) {
                Log.e("Package Url",call.request().url().toString());
                super.onResponse(call,response);
                //Toast.makeText(TailorMadeActivity.this,response.body()[0].getItineraryPlaceName(),Toast.LENGTH_SHORT).show();
                if (response.body()!= null && response.body().length > 0)
                itineries.setAdapter(new TailorMadeAccommodationAdapter(TailorMadeActivity.this,response.body()));
                else
                {
                    selectedtours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TailorMadeItinerary[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(TailorMadeActivity.this,meesage,Toast.LENGTH_LONG).show();

            }
        });

        provideCallback.getTailorMadeAccommodation(id).enqueue(new Callback<TailorMadeAccommodation[]>() {
            @Override
            public void onResponse(Call<TailorMadeAccommodation[]> call, Response<TailorMadeAccommodation[]> response) {
                //Toast.makeText(TailorMadeActivity.this,response.body()[0].getTailormadeAccommodationAccommodationName(),Toast.LENGTH_SHORT).show();
                if (response.body()!= null && response.body().length > 0)
                rooms.setAdapter(new TailorMadeAccommodationAdapter(TailorMadeActivity.this,response.body()));
                else
                {
                    selectedhotelrooms.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TailorMadeAccommodation[]> call, Throwable t) {

            }
        });
        provideCallback.getTailorMadeTransport(id).enqueue(new Callback<TailorMadeTransport[]>() {
            @Override
            public void onResponse(Call<TailorMadeTransport[]> call, Response<TailorMadeTransport[]> response) {
                List<TailorMadeTransport> tailorMadeTransport = new ArrayList<TailorMadeTransport>();
                if (response.body()!=null && response.body().length > 0)
                {
                    for (int i =0 ;i<response.body().length;i++)
                    {
                        if (response.body()[i].getTailormadeRouteRouteName() != null)
                        {
                            tailorMadeTransport.add(response.body()[i]);
                        }
                    }
                    if (tailorMadeTransport.size() == 0)
                    {
                        selectedtransports.setVisibility(View.GONE);
                    }
                    transports.setAdapter(new TailorMadeTransportAdapter(TailorMadeActivity.this,tailorMadeTransport));

                }


                //Toast.makeText(TailorMadeActivity.this,response.body()[0].getTailormadeRouteTransportInfoOperatorName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TailorMadeTransport[]> call, Throwable t) {

            }
        });


        //ButterKnife.bind(this);
//        routeSelectionFragment=new RouteSelectionFragment();
//        StepperAdapter stepperAdapter=new StepperAdapter(getSupportFragmentManager(),this);
//        stepperLayout.setAdapter(stepperAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
