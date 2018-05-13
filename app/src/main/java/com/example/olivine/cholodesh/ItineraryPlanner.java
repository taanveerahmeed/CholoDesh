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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import adapters.ItineraryListAdapter;
import adapters.LocalTourAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import callbacks.AuthCallback;
import callbacks.PlaceCallback;
import constants.Travel;
import helpers.BanglaNumberParser;
import helpers.BaseURL;
import helpers.CustomCallBack;
import helpers.RecyclerItemClickListener;
import helpers.RetrofitInitializer;
import io.realm.Realm;
import listeners.ItineraryListener;
import listeners.TailormadeListener;
import model.AccommodationRoom;
import model.Auth;
import model.Destination;
import model.Food;
import model.Itenerary;
import model.ListWrapper;
import model.LocalTour;
import model.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import userDefinder.TailorMade;
import userDefinder.TailormadeSync;

public class ItineraryPlanner extends AppCompatActivity {

    private static final int FINISH_MENU = 1;
    private static final int FOOD_MENU = 2;
    private static final int REQUEST_FOOD = 1004;
    int day=1;
    int maxDays;
    int totalItenaryCost=0;


    private AuthCallback authCallback;

    private Retrofit retrofit;
    private ItineraryListener itineraryListener;

    private LocalTour []localTours;
    private ArrayList<Itenerary> iteneraries=new ArrayList<>();

    // TODO Data storages declear
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Realm realm;

    // Adapters
    private ItineraryListAdapter itineraryListAdapter;
    private LocalTourAdapter localTourAdapter;

    // Callback
    PlaceCallback placeCallback;

    // viewsinitiallization
    @BindView(R.id.spinnerDestinations) Spinner spinnerDestinations;
    @BindView(R.id.spinnertourTime) Spinner spinnertourTime;
    @BindView(R.id.localtripListView) RecyclerView localtripListView;
    @BindView(R.id.itineraryListView) RecyclerView itineraryListView;
    @BindView(R.id.itineraryPlaceholderText) TextView itineraryPlaceholderText;
    @BindView(R.id.dayplanSelector) TextView dayplanSelector;
    @BindView(R.id.totalCostView) TextView totalCostView;
    private TailorMade tailorMade;
    private int tailormade_id;
    // change day
    @OnClick(R.id.dayplus)
    public void nextDay(View view){
        if(day==maxDays){
            return;
        }
        day++;
//        if (!BaseURL.LANGUAGE_ENG)
//        {
//            dayplanSelector.setText("দিন  "+ BanglaNumberParser.getBangla(day+""));
//        }
         dayplanSelector.setText("Day "+day);
    }
    @OnClick(R.id.dayminus)
    public void previousDay(View view){
        if(day==1){
            return;
        }
        day--;
//        if (!BaseURL.LANGUAGE_ENG)
//        {
//            dayplanSelector.setText("দিন  "+BanglaNumberParser.getBangla(day+""));
//        }
         dayplanSelector.setText("Day "+day);
    }
    // end change day
    // If Destination change from spinner load data according to selection
    public void onDestinationSelectedListener(){
        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadLocaltour();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    // If time change load data according to selection
    private void onTimeSelectedListener() {
        spinnertourTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadLocaltour();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public  void loadLocaltour(){
        Destination destination= (Destination) spinnerDestinations.getSelectedItem();
        String time= (String) spinnertourTime.getSelectedItem();
        if(destination!=null){
            loadLocaltrips(destination.getDestinationId()+"",time);
           // Toast.makeText(this,destination.getDestinationId()+""+time,Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_planner);
        ButterKnife.bind(this);
        //getSupportActionBar().setTitle("Itinerary Plan");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!BaseURL.LANGUAGE_ENG)
        {

            this.setTitle("ভ্রমণ পরিকল্পনা");
        }
        else this.setTitle("Itinerary Plan");

        authCallback=new AuthCallback(this);
        // TODO Storage init
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Realm.init(this);
        realm=Realm.getDefaultInstance();
        //var init
        maxDays=sharedPreferences.getInt(Travel.NUMBER_OF_DAYS,1);
        // Api initialize
        placeCallback=new PlaceCallback(this);
        retrofit=RetrofitInitializer.initNetwork(this);
        itineraryListener=retrofit.create(ItineraryListener.class);
        loadDestinationsByDistrict(sharedPreferences.getInt(Travel.TO_LOCATION,26));
        onDestinationSelectedListener();
        onTimeSelectedListener();
        //TODO
        // Get current tailormade ID
        tailormade_id=sharedPreferences.getInt(Travel.CURRENT_TAILORMADE_ID,0);
        tailorMade=realm.where(TailorMade.class).equalTo("tailormade_id",tailormade_id).findFirst();

        ArrayList realm_planning= (ArrayList) realm.copyFromRealm(tailorMade.iteneraries);
        // Check for previously added plan
        if(realm_planning!=null && realm_planning.size()>0){
            iteneraries=realm_planning;
            itineraryListAdapter=new ItineraryListAdapter(ItineraryPlanner.this,realm_planning);
            itineraryListView.setAdapter(itineraryListAdapter);
            itineraryPlaceholderText.setVisibility(View.GONE);
            itineraryListView.setVisibility(View.VISIBLE);
        }

        // Create Itinerary
        localtripListView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, localtripListView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        LocalTour localTour=localTours[position];
                        String day=dayplanSelector.getText().toString();
                        int localtour_id=localTour.getLocalTourId();
                        // Check for duplicate entry
                        for(Itenerary tmp_itenarery:iteneraries){


                            if(tmp_itenarery.getLocalTourId()==localtour_id  && tmp_itenarery.getDayplan().equals(day)){

                                String meesage ="You Already planned for this place";
                                if (!BaseURL.LANGUAGE_ENG)
                                {
                                    meesage =" আপনি ইতিমধ্যে এই জায়গার  জন্য পরিকল্পনা করেছেন ";

                                }
                                Toast.makeText(getApplicationContext(),meesage,Toast.LENGTH_LONG).show();

                                return;
                            }
                        }
                        // manage Cost
                        totalItenaryCost+=Integer.parseInt(localTour.getLocalTourPerPersonCost());
                        totalCostView.setText("Local Tour Cost: "+totalItenaryCost);
                        // Add selected plan to Itenerary list
                        Itenerary itenerary=new Itenerary();
                        itenerary.setDayplan(day)
                                .setLocalTourId(localTour.getLocalTourId())
                                .setPlaceName(localTour.getDestinationNearbyPlaceName())
                                .setPerPersonCost(localTour.getLocalTourPerPersonCost())
                                .setTime(spinnertourTime.getSelectedItem().toString())
                                .setTransport(localTour.getLocalTourTransportType());


                        iteneraries.add(0,itenerary);
                        // View Itenerary plan to list
                        if(itineraryListAdapter==null){
                            itineraryListAdapter=new ItineraryListAdapter(getApplicationContext(),iteneraries);
                            itineraryListView.setAdapter(itineraryListAdapter);
                        }
                        itineraryListAdapter.notifyDataSetChanged();
                        String meesage ="Added to plan";
                        if (!BaseURL.LANGUAGE_ENG)
                        {
                            meesage =" পরিকল্পনা যোগ করা হয়েছে";

                        }
                        Toast.makeText(ItineraryPlanner.this,meesage,Toast.LENGTH_LONG).show();



                        itineraryListView.setVisibility(View.VISIBLE);
                        itineraryPlaceholderText.setVisibility(View.GONE);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



    }


    private void loadDestinationsByDistrict(int districtId){

        placeCallback.getDestinations(districtId).enqueue(new CustomCallBack<Destination[]>(this) {
            @Override
            public void onResponse(Call<Destination[]> call, Response<Destination[]> response) {
                super.onResponse(call,response);
                Log.e("Package Url",call.request().url().toString());
                if (response.body().length > 0)
                {
                    ArrayAdapter<Destination> destinationAdapter=new ArrayAdapter<Destination>(getApplicationContext(),R.layout.layout_spinner,R.id.spinnerText,response.body());
                    spinnerDestinations.setAdapter(destinationAdapter);
                }
                else

                {
                    String meesage ="Nothing Found";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কিছুই পাওয়া যায়নি";

                    }
                    Toast.makeText(ItineraryPlanner.this,meesage,Toast.LENGTH_LONG).show();

                }



            }

            @Override
            public void onFailure(Call<Destination[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="No Accomodations";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" কিছুই পাওয়া যায়নি";

                }
                Toast.makeText(ItineraryPlanner.this,meesage,Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadLocaltrips(String destination, String time){
        final Call<LocalTour[]> getLocalTour=itineraryListener.getLocalTourList(destination,time);
        //Toast.makeText(this,destination+" " +time, Toast.LENGTH_LONG).show();
        getLocalTour.enqueue(new CustomCallBack<LocalTour[]>(this) {
            @Override
            public void onResponse(Call<LocalTour[]> call, Response<LocalTour[]> response) {
                super.onResponse(call,response);
                if(response.body() != null && response.body().length > 0  ){
                   localTourAdapter=new LocalTourAdapter(getApplicationContext(),response.body());
                    localTours=response.body();
                    localtripListView.setAdapter(localTourAdapter);

                }else{

                    String meesage ="No trip found";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কোন ট্রিপ খুঁজে পাওয়া যায় নি";

                    }
                    Toast.makeText(ItineraryPlanner.this,meesage,Toast.LENGTH_LONG).show();


                    localtripListView.setAdapter(null);
                    Log.e("Api url", getLocalTour.request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<LocalTour[]> call, Throwable t) {
                super.onFailure(call,t);

                Log.e("Api url", getLocalTour.request().url().toString());
                Log.e("Api url",t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem foodItem=menu.add(Menu.NONE,FOOD_MENU,Menu.NONE,"Food");
        foodItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        foodItem.setIcon(R.drawable.icon_restaurent);
        String menuName = "Save";
        if (!BaseURL.LANGUAGE_ENG)
        {
            menuName = "সংরক্ষণ";
        }
        MenuItem finishItem=menu.add(Menu.NONE,FINISH_MENU,Menu.NONE,menuName);
        finishItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case FINISH_MENU :
                // TODO Save Itinerary
                realm.beginTransaction();
                tailorMade.iteneraries.clear();
                tailorMade.itineraryCost=totalItenaryCost;
                for(Itenerary itenerary:iteneraries){
                    tailorMade.iteneraries.add(itenerary);
                }
                realm.commitTransaction();

                final int tmp_id=tailormade_id;
                String title = "Confirmation";
                String message = "Do You Want to Save your Tailormade?";
                String yes = "Yes";
                String no = "No";

                if (!BaseURL.LANGUAGE_ENG)
                {
                    title = "অনুমোদন";
                    message = "আপনি আপনার টেইলর মেডটি  সংরক্ষণ করতে চান?";
                    yes = "হ্যা";
                    no = "না";
                }
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent loginIntent=new Intent(ItineraryPlanner.this,LoginActivity.class);
                        startActivityForResult(loginIntent,tmp_id);
                    }
                });
                dialog.setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Intent intent=new Intent(ItineraryPlanner.this,MainActivity.class);
                        //startActivity(intent);
                    }
                });
                dialog.setMessage(message);
                dialog.setTitle(title);
                dialog.show();
//                tailormade_id++;
//                editor.putInt(Travel.CURRENT_TAILORMADE_ID,tailormade_id);
//                editor.commit();

                //===========================================================

                break;
            case FOOD_MENU:
                Intent foodIntent=new Intent(ItineraryPlanner.this,FoodListActivity.class);
                startActivityForResult(foodIntent,REQUEST_FOOD);
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_FOOD){
//            for(Food food:){
//                tailorMade.iteneraries.add(itenerary);
//            }
            String json = "";
            try
            {
                json  = data.getExtras().getString("FOOD_LIST","");
                ListWrapper listWrapper=new Gson().fromJson(json,ListWrapper.class);
                List <Food> selectedFoods = listWrapper.getFoods();
                for (int i = 0; i<selectedFoods.size();i++)
                {
                    totalItenaryCost+= Integer.parseInt(selectedFoods.get(i).getDestinationFoodServiceProviderPriceRange());
                }
            }
            catch (Exception e)
            {

            }

            return;
        }
        int tailormade_id= requestCode;
        TailorMade tailorMade=realm.where(TailorMade.class).equalTo("tailormade_id",tailormade_id).findFirst();

        saveTailormadeToServer(tailorMade);


        Intent intent=new Intent(ItineraryPlanner.this,MainActivity.class);
        startActivity(intent);

    }

    private void saveTailormadeToServer(TailorMade tailorMade){
        String user=sharedPreferences.getString(Travel.USER_EMAIL,null);


        final TailormadeSync testtailormade=new TailormadeSync();
        testtailormade.setCustomerId(user)
                .setTailormade_id(tailorMade.tailormade_id)
                .setDeapartDistrictId(tailorMade.deapartDistrictId)
                .setDepartDistrictName(tailorMade.departDistrictName)
                .setDestinationDistrictId(tailorMade.destinationDistrictId)
                .setDestinationDistrictName(tailorMade.destinationDistrictName)
                .setDepartDate(tailorMade.departDate)
                .setReturnDate(tailorMade.returnDate)
                .setNumberOFDays(tailorMade.numberOFDays)
                .setNumberOFTourists(tailorMade.numberOFTourists)
                .setTotalCost(tailorMade.transportCost+ BaseURL.totalCost+tailorMade.itineraryCost+"");


        List<Route> realm_transport_provider=realm.copyFromRealm(tailorMade.routes);
        List<AccommodationRoom> realm_acc_provider=realm.copyFromRealm(tailorMade.accommodationRooms);
        List<Itenerary> realm_itinerary=realm.copyFromRealm(tailorMade.iteneraries);

        testtailormade.setTransportProviders(realm_transport_provider);
        testtailormade.setAccommodationProviders(realm_acc_provider);
        testtailormade.setIteneraries(realm_itinerary);
        Retrofit retrofit= RetrofitInitializer.initNetwork(this);
        TailormadeListener tailormadeListener=retrofit.create(TailormadeListener.class);
        Gson gson = new Gson();
        String json = gson.toJson(testtailormade);
        Log.e("Url","Null");
        Log.e("JSON",json );
        TailormadeSync test = new TailormadeSync();
        authCallback.SendTailorMade(testtailormade).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Url",response.body());
                Gson gson = new Gson();
                String json = gson.toJson(testtailormade);
                Log.e("Url","Null");
                Log.e("JSON",json );

                String meesage ="Your TailorMade has been Saved. Please Check \"My Trips\" for Details.";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" আপনার টেইলর মেডটি সংরক্ষিত হয়েছে।  বিস্তারিত জানতে \"আমার পরিকল্পনা\" অপশন দেখুন ";

                }
                Toast.makeText(ItineraryPlanner.this,meesage,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
//        realm.beginTransaction();
//        this.tailorMade.accommodationRooms = null;
//        realm.close();

//        tailormadeListener.getReturn(testtailormade).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body() == null)
//                        {
//                            Gson gson = new Gson();
//                            String json = gson.toJson(testtailormade);
//                            Log.e("Url","Null");
//                            Log.e("JSON",json );
//                        }
//                        else
//                        {
//                            Log.e("Url","!Null");
//                        }
//                Log.e("Url",call.request().url().toString());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//               // Log.e("error",t.getMessage());
//            }
//        });
        Intent intent=new Intent(ItineraryPlanner.this,MainActivity.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
