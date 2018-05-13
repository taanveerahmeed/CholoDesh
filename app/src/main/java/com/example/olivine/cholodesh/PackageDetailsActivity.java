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
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import helpers.CustomCallBack;

import adapters.InclusionAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import helpers.DynamicHeight;
import io.realm.Realm;
import model.PackageDetails;
import model.PackageGallery;
import model.PackageInclusion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import userDefinder.PackageReservation;

public class PackageDetailsActivity extends AppCompatActivity {
    // API Init
    PackageDetails tourPackage;
    Realm realm;
    ProvideCallback provideCallback;
    static String ImageUrl;

    @BindView(R.id.overviewforlang)
    TextView overviewforlang;
    @BindView(R.id.thingstodoforlang)
    TextView thingstodoforlang;
    @BindView(R.id.inclusionsforlang)
    TextView inclusionsforlang;

    @BindView(R.id.destinationIamge)
    ImageView DestinationImage;
    @BindView(R.id.PackageName)
    TextView PackageName;
    @BindView(R.id.tourShortDetails)
    TextView tourShortDetails;
    @BindView(R.id.dayNight)
    TextView dayNight;
    @BindView(R.id.tourCost)
    TextView tourCost;
    @BindView(R.id.providerName)
    TextView providerName;
    @BindView(R.id.providerAddress)
    TextView providerAddress;
    @BindView(R.id.providerEmail)
    TextView providerEmail;
    @BindView(R.id.providerHotLine)
    TextView providerHotLine;
    @BindView(R.id.tourActivities)
    WebView tourActivities;
    @BindView(R.id.packageOverview)
    TextView packageOverview;
    @BindView(R.id.mDemoSlider)
    SliderLayout mDemoSlider;
    @BindView(R.id.inclusions)
    RecyclerView inclusions;
    SharedPreferences sharedPreferences;

    @BindView(R.id.viewRooms)
    Button viewRooms;

    @OnClick(R.id.viewRooms)
    public void viewDetailsPlan(View view) {
        loadItineraries(getIntent().getIntExtra(Travel.PACKAGE_KEY, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        //setContentView(R.layout.activity_package_details_new);
        ButterKnife.bind(this);
        if (!BaseURL.LANGUAGE_ENG)
        {
            thingstodoforlang.setText("যা যা করা যাবে ");
            overviewforlang.setText("বিস্তারিত");
            inclusionsforlang.setText("অন্তর্ভুক্তি");
            this.setTitle("প্যাকেজ বিস্তারিতঃ ");
            viewRooms.setText("প্ল্যান");
        }
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        // Api init
        provideCallback = new ProvideCallback(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Realm.init(
                this
        );
        realm = Realm.getDefaultInstance();
        int packageId = getIntent().getIntExtra(Travel.PACKAGE_KEY, 0);
        ImageUrl = getIntent().getStringExtra("IMAGE_URL");
        loadPackageDetails(packageId);
        loadGallery(packageId);
       loadInclusion(packageId);


        com.shamanland.fab.FloatingActionButton fab = (com.shamanland.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setBackground(getResources().getDrawable(R.drawable.circle_button));
        //fab.setBackgroundColor(Color.BLUE);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder roomConfirmDialog=new AlertDialog.Builder(PackageDetailsActivity.this);
                String title = "Confirmation";
                String message = "Do You want to confirm Booking ?";
                String yes = "Yes";
                String no ="No";

                if (!BaseURL.LANGUAGE_ENG)
                {
                    title = "অনুমোদন";
                    message = "আপনি বুকিং নিশ্চিত করতে চান?";
                    yes = "হ্যা";
                     no ="না";
                }
                roomConfirmDialog.setTitle(title);
                roomConfirmDialog.setMessage(message );
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
                            Intent loginIntent=new Intent(PackageDetailsActivity.this,LoginActivity.class);
                            startActivityForResult(loginIntent,110);
                            //works are pending here login
                            //will show login page and return after successful login
                            return;
                        }
                        bookPackage();
                    }
                });
                roomConfirmDialog.show();
                //bookPackage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==200){
            bookPackage();
        }
    }

    private void bookPackage() {
        String url = BaseURL.BASE_URL + "provider/packageBooking";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(PackageDetailsActivity.this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("false"))
                {
                    String meesage ="Network Error";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" নেটওয়ার্ক ত্রুটি";

                    }
                    Toast.makeText(PackageDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();


                } else
                {
                    // Get Booking token from server and save to database
                    realm.beginTransaction();
                    PackageReservation par = new PackageReservation();
                    par.token = response;
                    par.packages = tourPackage;
                    realm.copyToRealm(par);
                    realm.commitTransaction();

                    String meesage ="Your Booking was succesful. Find your token in view token section.";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" আপনার বুকিং সফল হয়েছে ";

                    }
                    Toast.makeText(PackageDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();


                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PackageDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Static data for testing . This should load from login info
                String email = sharedPreferences.getString(Travel.USER_EMAIL,null);;
                //String email = "demo@demo.com";
                Map<String, String> params = new HashMap<String, String>();
                params.put("package_id", tourPackage.getPackageId() + "");
                params.put("customer_email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadItineraries(int packageId) {
        Intent packageIteneraryIntent = new Intent(PackageDetailsActivity.this, PackageItinerayActivity.class);
        packageIteneraryIntent.putExtra(Travel.INTENT_PACKAGE_ID, packageId);
        startActivity(packageIteneraryIntent);
    }

    private void loadInclusion(int packageId) {
        provideCallback.getPackageInclusions(packageId).enqueue(new Callback<PackageInclusion[]>() {
            @Override
            public void onResponse(Call<PackageInclusion[]> call, Response<PackageInclusion[]> response) {
                InclusionAdapter inclusionAdapter = new InclusionAdapter(getApplicationContext(), response.body());
                inclusions.setAdapter(inclusionAdapter);
            }

            @Override
            public void onFailure(Call<PackageInclusion[]> call, Throwable t) {

                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(PackageDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadPackageDetails(int packageid) {
        provideCallback.getPackageDetails(packageid).enqueue(new CustomCallBack<PackageDetails>(this) {
            @Override
            public void onResponse(Call<PackageDetails> call, Response<PackageDetails> response) {
                super.onResponse(call,response);
                //Log.e("Package Url",call.request().url().toString());
                PackageDetails packageDetails = response.body();
                if (packageDetails != null) {
                    tourPackage = packageDetails;
                    String url= BaseURL.PACKAGE_IMAGE_BASE_URL+PackageDetailsActivity.ImageUrl;
//        try {
//            Bitmap bitmap=new RetriveImage().execute(url).get();
//            BitmapDrawable background = new BitmapDrawable(bitmap);
//        viewHolder.packageParent.setBackgroundDrawable(background);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


                    Picasso.with(PackageDetailsActivity.this)
                            .load(url)
                            .placeholder(R.drawable.image_placeholder)
                            .fit()
                            .into(PackageDetailsActivity.this.DestinationImage);
//                    DynamicHeight.setHeight(PackageDetailsActivity.this,PackageDetailsActivity.this.DestinationImage,4,3);
//
//                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pckgdtls);
//
//                    DynamicHeight.setHeight(PackageDetailsActivity.this,linearLayout,4,2.5f);


                    PackageName.setText(packageDetails.getPackageName());
                    tourCost.setText( packageDetails.getPackagePrice() + " ৳");
                    providerName.setText(packageDetails.getProviderName());
                    providerAddress.setText("Address: " + packageDetails.getProviderAddress());
                    providerEmail.setText("Email: " + packageDetails.getProviderEmail());
                    //Html.
                    providerHotLine.setText("Hotline: " + packageDetails.getProviderHotline());
                    String message ="<font color=\"" + "#000000" + "\">" +packageDetails.getPackageActivities()+ "</font>";
                    //tourActivities.setText(Html.fromHtml(packageDetails.getPackageActivities()));
                    tourActivities.loadData(message,"text/html", "UTF-8");
                    packageOverview.setText(Html.fromHtml(packageDetails.getPackageOverview()));
                    dayNight.setText(packageDetails.getPackageDay() + " Day " + packageDetails.getPackageNight() + " Night");
                    tourShortDetails.setText(packageDetails.getPackageLocationFrom() + " - " + packageDetails.getPackageLocationTo());
                }
            }

            @Override
            public void onFailure(Call<PackageDetails> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(PackageDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();


                Log.e("Hotel Error", call.request().url().toString());
            }
        });
    }

    private void loadGallery(int packageId) {
        provideCallback.getPackageImages(packageId).enqueue(new Callback<PackageGallery[]>() {
            @Override
            public void onResponse(Call<PackageGallery[]> call, Response<PackageGallery[]> response) {
                PackageGallery[] packageGalleries = response.body();
                if (packageGalleries == null) {
                    return;
                }
                for (PackageGallery packageGallery : packageGalleries) {
                    String name = packageGallery.getPackageName();
                    TextSliderView textSliderView = new TextSliderView(PackageDetailsActivity.this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(BaseURL.PACKAGE_IMAGE_BASE_URL + packageGallery.getPackageGalleryImage())
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);

                    mDemoSlider.addSlider(textSliderView);
                }
                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(4000);
                DynamicHeight.setHeight(PackageDetailsActivity.this,PackageDetailsActivity.this.mDemoSlider,4,3);
            }

            @Override
            public void onFailure(Call<PackageGallery[]> call, Throwable t) {
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(PackageDetailsActivity.this,meesage,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
