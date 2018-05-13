/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivine.cholodesh.TripRouteActivity;
import com.example.olivine.cholodesh.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import callbacks.PlaceCallback;
import constants.Travel;
import helpers.BanglaNumberParser;
import helpers.BaseURL;
import helpers.CustomCallBack;
import helpers.ViewAssist;
import io.realm.Realm;
import io.realm.RealmResults;
import model.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import userDefinder.TailorMade;

/**
 * Created by Olivine on 6/21/2017.
 */

public class TripPlannerFragment extends Fragment {
    @BindView(R.id.editText_no_of_tourist)
    EditText editText_no_of_tourist;
    @BindView(R.id.editText_no_of_days) EditText editText_no_of_days;
    @BindView(R.id.departDateTextView)
    TextView departDateTextView;
    @BindView(R.id.returnDateTextView) TextView returnDateTextView;
    @BindView(R.id.locationAutoComplete)
    AutoCompleteTextView locationAutoComplete;
    @BindView(R.id.destinationAutoComplete) AutoCompleteTextView destinationAutoComplete;


    @BindView(R.id.destionforlang)
    TextView destionforlang;
    @BindView(R.id.fromlang)
    TextView fromlang;
    @BindView(R.id.toforlang)
    TextView toforlang;
    @BindView(R.id.trvlrforlang)
    TextView trvlrforlang;
    @BindView(R.id.trpdtforlang)
    TextView trpdtforlang;
    @BindView(R.id.deprtforlang)
    TextView deprtforlang;
    @BindView(R.id.returnforlang)
    TextView returnforlang;
    @BindView(R.id.searchButton)
    Button searchButton;





    // callback
    PlaceCallback placeCallback;
    // TODO local storage
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private List<Place> places;
    private Realm realm;

    @OnClick(R.id.departDateLayout)
    public void setTripDate(View view){


        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                int days=0;
                String selected_days=editText_no_of_days.getText().toString();
                if(selected_days.length()!=0){
                    days=Integer.parseInt(selected_days);
                }

                departDateTextView.setText(sdf.format(myCalendar.getTime()));
                myCalendar.add(Calendar.DATE, days);
                String newDate=sdf.format(myCalendar.getTime());
                returnDateTextView.setText(newDate);

                if (!BaseURL.LANGUAGE_ENG)
                {
                    departDateTextView.setText(BanglaNumberParser.getBangla(departDateTextView.getText().toString()));
                    returnDateTextView.setText(BanglaNumberParser.getBangla(newDate));
                }

            }

        };

        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

//        ViewAssist.setDate(getActivity(),departDateTextView,returnDateTextView,days,"Select Trip date");
    }
    @OnClick(R.id.returnDateLayout)
    public void setReturnDate(View view){

        //ViewAssist.setDate(getActivity(),returnDateTextView,"Select Return date");
    }
    public TripPlannerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TripPlannerFragment newInstance() {
        TripPlannerFragment fragment = new TripPlannerFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.trip_planner_new,container,false);
        ButterKnife.bind(this,view);
        BaseURL.accommodationRooms.clear();

        if (!BaseURL.LANGUAGE_ENG)
        {
            //Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "siyamrupali.ttf");
           // destionforlang .setTypeface(face);
            getActivity().setTitle("আপনার প্ল্যান তৈরী করুন ");
           // destionforlang.setText("FONT CHECK");
            destionforlang.setText("গন্তব্য ");
            fromlang.setText("কোথা থেকে শুরু করবেন?");
            toforlang.setText("কোথায় যাবেন?");
            trvlrforlang.setText("ভ্রমণ বিবরণ");
            trpdtforlang.setText("সময়কাল");
            deprtforlang.setText("যাত্রা শুরুর তারিখ");
            returnforlang.setText("ফেরার তারিখ");
            editText_no_of_days.setHint("দিন");
            editText_no_of_tourist.setHint("পর্যটক");
            returnDateTextView.setText("দিন-মাস-বসর");
            departDateTextView.setText("দিন-মাস-বসর");
            destinationAutoComplete.setHint("কক্সবাজার");
            locationAutoComplete.setHint("ঢাকা");
            searchButton.setText("খুজুন");



        }
        else getActivity().setTitle("Create Your Plan");
        // Data storage init
        sharedPreferences=getActivity().getSharedPreferences(getString(R.string.preference_file_key),getActivity().MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Realm.init(getActivity());
        realm=Realm.getDefaultInstance();
        // TODO callback init
        placeCallback=new PlaceCallback(getActivity());
        BaseURL.totalCost = 0;
        loadLocations();

        return view;
    }
    @OnClick(R.id.searchButton)
    public void searchRoute(View view)
    {

        Intent intent=new Intent(getActivity(),TripRouteActivity.class);
        //get data from user
        String destinationlocation=destinationAutoComplete.getText().toString();
        String departlocation=locationAutoComplete.getText().toString();

        String tmp_tourist=editText_no_of_tourist.getText().toString();
        String tmp_days=editText_no_of_days.getText().toString();
        String fromDate=departDateTextView.getText().toString();
        String toDate=returnDateTextView.getText().toString();
        if(!validate()){
            return;
        }
        int numberOfTourist=Integer.parseInt(tmp_tourist);
        int numberOfDays=Integer.parseInt(tmp_days);
        int destinationDistrictId=0;
        String destinationDistrictName="";
        int departDistrictId=0;
        String DepartDistrictName="";

        for (Place place:places){
            if (place.getDistrictName().equalsIgnoreCase(destinationlocation) || place.getDistrictNameBn().equalsIgnoreCase(destinationlocation)){
                destinationDistrictId=place.getDistrictId();
                destinationDistrictName=place.getDistrictName();
                break;
            }
        }
        for (Place place:places){
            if (place.getDistrictName().equalsIgnoreCase(departlocation)|| place.getDistrictNameBn().equalsIgnoreCase(departlocation)){
                departDistrictId=place.getDistrictId();
                DepartDistrictName=place.getDistrictName();
                break;
            }
        }
        // save user inputs
        editor.putInt(Travel.DEPART_LOCATION,departDistrictId);
        editor.putInt(Travel.TO_LOCATION,destinationDistrictId);
        editor.putInt(Travel.NUMBER_OF_TOUIST,numberOfTourist);
        editor.putInt(Travel.NUMBER_OF_DAYS,numberOfDays);
        editor.putString(Travel.DEPART_DATE,BanglaNumberParser.getEnglish(fromDate));
        editor.putString(Travel.RETURN_DATE,BanglaNumberParser.getEnglish(toDate));
        editor.commit();
        // TODO save tailormade data
        int current_tailormade_id =sharedPreferences.getInt(Travel.CURRENT_TAILORMADE_ID,0);
        RealmResults<TailorMade> tailormades=realm.where(TailorMade.class).equalTo("tailormade_id",current_tailormade_id).findAll();
        TailorMade tailorMade=new TailorMade();
        tailorMade.tailormade_id=current_tailormade_id;
        tailorMade.deapartDistrictId=departDistrictId;
        tailorMade.destinationDistrictId=destinationDistrictId;
        tailorMade.departDistrictName=DepartDistrictName;
        tailorMade.destinationDistrictName=destinationDistrictName;
        tailorMade.departDate=BanglaNumberParser.getEnglish(fromDate);
        tailorMade.returnDate=BanglaNumberParser.getEnglish(toDate);
        tailorMade.numberOFDays=numberOfDays;
        tailorMade.numberOFTourists=numberOfTourist;
        // check if data is already saved
        if(tailormades.size()==0){

        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tailorMade);
        realm.commitTransaction();

        startActivity(intent);
    }
    public boolean validate(){
        String tmp_tourist=editText_no_of_tourist.getText().toString();
        String tmp_days=editText_no_of_days.getText().toString();
        String fromDate=departDateTextView.getText().toString();
        String toDate=returnDateTextView.getText().toString();
        String destinationlocation=destinationAutoComplete.getText().toString();
        String departlocation=locationAutoComplete.getText().toString();
        if(departlocation==null|| departlocation==""){
            String error = "Enter Depart Location";
            if (!BaseURL.LANGUAGE_ENG) error = "যাত্রা শুরুর স্থান লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

            return false;
        }

        if(destinationlocation==null|| destinationlocation==""){
            String error = "Enter Destination";
            if (!BaseURL.LANGUAGE_ENG) error = "গন্তব্য লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

            return false;
        }

        if(tmp_tourist.length()==0|| tmp_tourist==""){
            String error = "Enter Number of tourist";
            if (!BaseURL.LANGUAGE_ENG) error = "পর্যটক সংখ্যা লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tmp_days.length()==0|| tmp_days==""){
            String error = "Enter Number of days";
            if (!BaseURL.LANGUAGE_ENG) error = "দিনের সংখ্যা লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(fromDate==null|| fromDate==""){
            String error = "Enter depart date";
            if (!BaseURL.LANGUAGE_ENG) error = "প্রস্থান তারিখ লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

            return false;
        }
        if(toDate==null|| toDate==""){
            String error = "Enter return date";
            if (!BaseURL.LANGUAGE_ENG) error = "ফেরার তারিখ লিখুন";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

            return false;
        }


        return true;
    }
    // load all districts in spinner
    private void loadLocations(){
        placeCallback.getLocations().enqueue(new CustomCallBack<Place[]>(getActivity()) {
            @Override
            public void onResponse(Call<Place[]> call, Response<Place[]> response) {
                super.onResponse(call,response);
                places= Arrays.asList(response.body());
                ArrayAdapter<Place> placeAdapter=new ArrayAdapter<Place>(getActivity().getApplicationContext(),R.layout.layout_spinner,R.id.spinnerText,response.body());
                locationAutoComplete.setAdapter(placeAdapter);
                destinationAutoComplete.setAdapter(placeAdapter);
            }

            @Override
            public void onFailure(Call<Place[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(getActivity(), meesage, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), "Locations Could not be loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
