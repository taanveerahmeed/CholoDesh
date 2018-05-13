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
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import adapters.DestinationTagAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import helpers.BaseURL;
import helpers.CustomCallBack;
import model.DestinationGallery;
import model.NearByPlaceGallerInfo;
import model.NearByPlacesInfo;
import retrofit2.Call;
import retrofit2.Response;


public class NearByPlaceDetailsActivity extends AppCompatActivity {

    @BindView(R.id.mDemoSlider)
    SliderLayout mDemoSlider;
    ProvideCallback provideCallback;

    @BindView(R.id.placeName)
    TextView placeName;

    @BindView(R.id.aboutforlang)
    TextView aboutforlang;


    @BindView(R.id.placeDetails)
    TextView placeDetails;

    @BindView(R.id.tags)
    RecyclerView tags;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_place_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_clear_24);

        int placeId = getIntent().getIntExtra("NEAR_BY_PLACE_ID",0);
        name= getIntent().getStringExtra("NEAR_BY_PLACE_NAME");
        this.setTitle(name);



        //Toast.makeText(this,placeId+"",Toast.LENGTH_LONG).show();

        ButterKnife.bind(this);
        if (!BaseURL.LANGUAGE_ENG)
        {
            aboutforlang.setText("বিস্তারিত");
        }
        provideCallback = new ProvideCallback(this);
        loadTextViews (placeId);
        loadGallery (placeId);


    }

    private void loadGallery (int placeId)
    {
        provideCallback.getNearByPlaceGalleryInfo(placeId).enqueue(new CustomCallBack<NearByPlaceGallerInfo[]>(this) {
            @Override
            public void onResponse(Call<NearByPlaceGallerInfo[]> call, Response<NearByPlaceGallerInfo[]> response) {
                super.onResponse(call, response);
                if(response.body()!=null){
                    loadGallery(response.body());
                }
            }

            @Override
            public void onFailure(Call<NearByPlaceGallerInfo[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(NearByPlaceDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();


            }
        });
    }

    private void loadGallery (NearByPlaceGallerInfo[] galleries)
    {
        mDemoSlider.removeAllSliders();
        for(NearByPlaceGallerInfo nearByPlaceGallerInfo : galleries){
            //String name=destinationGallery.get();
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name) // image description
                    .image(BaseURL.DESTINATION_NEAR_BY_PLACE_GALLERY_BASE_URL+nearByPlaceGallerInfo.getDestinationNearbyPlaceGalleryImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra","Name");

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    private void loadTextViews (int placeId)
    {
        provideCallback.getNearByPlaceInfo(placeId).enqueue(new CustomCallBack<NearByPlacesInfo[]>(this) {
            @Override
            public void onResponse(Call<NearByPlacesInfo[]> call, Response<NearByPlacesInfo[]> response) {
                super.onResponse(call, response);
                if(response.body()!=null){
                    //loadGallery(response.body());
                    NearByPlacesInfo [] nearByPlaceInfo = response.body();
                    if (!BaseURL.LANGUAGE_ENG && nearByPlaceInfo[0].getDestinationNearbyPlaceNameBn() != null)
                    {
                        placeName.setText(nearByPlaceInfo[0].getDestinationNearbyPlaceNameBn());
                    }
                    else
                    {
                        placeName.setText(nearByPlaceInfo[0].getDestinationNearbyPlaceName());
                    }

                    if (!BaseURL.LANGUAGE_ENG && nearByPlaceInfo[0].getDestinationNearbyPlaceDetailsBn() != null)
                    {
                        placeDetails.setText(Html.fromHtml(nearByPlaceInfo[0].getDestinationNearbyPlaceDetailsBn()));
                    }
                    else
                    {
                        placeDetails.setText(Html.fromHtml(nearByPlaceInfo[0].getDestinationNearbyPlaceDetails()));
                    }




                    if (BaseURL.LANGUAGE_ENG && nearByPlaceInfo[0].getDestinationNearbyPlaceTag()!= null)
                    {
                        String [] destinationTags = nearByPlaceInfo[0].getDestinationNearbyPlaceTag().split(",");
                        // Toast.makeText(DestinationActivity.this,destinationNews[0].getDestinationId()+"",Toast.LENGTH_LONG).show();
                        if (destinationTags.length>0 && destinationTags[0] != "")
                        {
                            DestinationTagAdapter adapter = new DestinationTagAdapter(getApplicationContext(),destinationTags);
                            tags.setAdapter(adapter);
                        }

                    }
                    else if (!BaseURL.LANGUAGE_ENG && nearByPlaceInfo[0].getDestinationNearbyPlaceTagBn()!= null)
                    {
                        String [] destinationTags = nearByPlaceInfo[0].getDestinationNearbyPlaceTagBn().split(",");
                        // Toast.makeText(DestinationActivity.this,destinationNews[0].getDestinationId()+"",Toast.LENGTH_LONG).show();
                        if (destinationTags.length>0 && destinationTags[0] != "")
                        {
                            DestinationTagAdapter adapter = new DestinationTagAdapter(getApplicationContext(),destinationTags);
                            tags.setAdapter(adapter);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<NearByPlacesInfo[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(NearByPlaceDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();


            }
        });
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
