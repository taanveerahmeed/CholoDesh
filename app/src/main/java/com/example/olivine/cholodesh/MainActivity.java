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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import adapters.PackageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import constants.Travel;
import fragments.AccomodationFragment;
import fragments.BookingFragment;
import fragments.DestinationFragment;
import fragments.HotelListFragment;
import fragments.PackageFragment;
import fragments.TailormadeListFragment;
import fragments.Transportation;
import fragments.TripPlannerFragment;
import fragments.MenuFragment;
import helpers.BanglaNumberParser;
import helpers.BaseURL;
import listeners.FragmentInteractionListener;
import model.Package;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this is the main activity of the application
 * this runs after the splash screen
 * consists of fragments of all options
 */

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener , FragmentInteractionListener{

    // Fragments initialization
    PackageFragment packageFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPreferences;
    boolean shouldExecuteOnResume;
    MenuItem menuItem;
    NavigationView navigationView;
    //ImageView logo;
    boolean LangEnglish = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);                 //binding the xml variables with java class variables
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        shouldExecuteOnResume = false;
        //logo = (ImageView) findViewById(R.id.imageViewCholoDesh);
        //logo.setOnClickListener(this);
        LangEnglish = BaseURL.LANGUAGE_ENG;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);  //slider
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavMenu ();

      // Init and Add fragment
        fragmentManager=getSupportFragmentManager();        //for controlling fragments
        fragmentTransaction=fragmentManager.beginTransaction();
        MenuFragment menuFragment=MenuFragment.newInstance();       //at first menu options will show thats why menu fragment
        fragmentTransaction.add(R.id.listFragment,menuFragment,"Sixth");
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }

    private void updateNavMenu() {

        Menu menu = navigationView.getMenu();
        MenuItem packageMenu = menu.findItem(R.id.nav_packages);
        MenuItem tripsMenu = menu.findItem(R.id.nav_create_plan);
        MenuItem myTripsMenu = menu.findItem(R.id.nav_my_trip);
        MenuItem destinationMenu = menu.findItem(R.id.nav_destination);
        MenuItem accomdationMenu = menu.findItem(R.id.nav_view_hotel);
        MenuItem transportMenu = menu.findItem(R.id.nav_transportation);
        MenuItem tokenMenu = menu.findItem(R.id.nav_tokens);
        MenuItem exitMenu = menu.findItem(R.id.exit);

        if (!BaseURL.LANGUAGE_ENG)
        {
            packageMenu.setTitle("প্যাকেজ");
            tripsMenu.setTitle("প্ল্যান তৈরী করুন");
            myTripsMenu.setTitle("আমার পরিকল্পনা");
            destinationMenu.setTitle("গন্তব্য");
            accomdationMenu.setTitle("বাসস্থান");
            transportMenu.setTitle("পরিবহন");
            tokenMenu.setTitle("টোকেন");
            exitMenu.setTitle("প্রস্থান");


        }
        else
        {
            packageMenu.setTitle("Packages");
            tripsMenu.setTitle("Create Trip Plan");
            myTripsMenu.setTitle("My Trips");
            destinationMenu.setTitle("Destination" );
            accomdationMenu.setTitle("Accommodation");
            transportMenu.setTitle("Transportation");
            tokenMenu.setTitle("View Token");
            exitMenu.setTitle("Exit");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuItem Home=menu.add(Menu.NONE,1,Menu.NONE,"Home");
//        Home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        Home.setIcon(R.drawable.ic_home_new);
//
        MenuItem lang =menu.add(Menu.NONE,2,Menu.NONE,"lang");
        menuItem = lang;
        lang.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //lang.setIcon(R.drawable.ic_user_new);
        if (LangEnglish)
        {
           lang.setIcon(R.drawable.ic_lang_bn);
        }
        else
        {
            lang.setIcon(R.drawable.ic_lang_en);
            //lang.getIcon().setTint(Color.WHITE);
        }

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu_new, menu);
//        if (LangEnglish)
//        {
//            menu.getItem(1).setIcon(R.drawable.ic_lang_en);
//        }
//        else
//        {
//            menu.getItem(1).setIcon(R.drawable.ic_lang_bn);
//        }
        return true;

        // return true so that the menu pop up is opened

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case 2:

                switchFragmentLanguage(item);
                updateNavMenu();
                    //this.recreate();
                return true;


        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //switchFragmentLanguage(menuItem);
        if (shouldExecuteOnResume)
        {
            if (!BaseURL.LANGUAGE_ENG)
            {
                menuItem.setIcon(R.drawable.ic_lang_en);
               // menuItem.getIcon().setTint(Color.WHITE);

            }
            else
            {
                menuItem.setIcon(R.drawable.ic_lang_bn);

            }
        }else
        {
            shouldExecuteOnResume = true;
        }


        //Toast.makeText(this,"Resumed",Toast.LENGTH_SHORT).show();

    }

    private void switchFragmentLanguage (MenuItem item)
    {
        if (BaseURL.LANGUAGE_ENG)
        {
            item.setIcon(R.drawable.ic_lang_en);
          //  item.getIcon().setTint(Color.WHITE);
            BaseURL.LANGUAGE_ENG = false;
            LangEnglish = false;
        }
        else
        {
            item.setIcon(R.drawable.ic_lang_bn);
            BaseURL.LANGUAGE_ENG = true;
            LangEnglish = true;
        }
        FragmentManager fm = this.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();

        }
        Fragment currentFragment = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.listFragment);

        if (currentFragment instanceof PackageFragment )
        {
            //Toast.makeText(MainActivity.this,"Package",Toast.LENGTH_SHORT).show();
            createPackageFragment();

        }

        else if (currentFragment instanceof TripPlannerFragment )
        {
           // Toast.makeText(MainActivity.this,"Trip",Toast.LENGTH_SHORT).show();
            createTripPlannerFragment();
        }
        else if (currentFragment instanceof TailormadeListFragment )
        {
            //Toast.makeText(MainActivity.this,"Tailormade",Toast.LENGTH_SHORT).show();
            createMyTripsFragment();
        }
        else if (currentFragment instanceof DestinationFragment )
        {
            //Toast.makeText(MainActivity.this,"Desination",Toast.LENGTH_SHORT).show();
            createDestinationFragment();
        }

        else if (currentFragment instanceof AccomodationFragment )
        {
            //Toast.makeText(MainActivity.this,"Accomodation",Toast.LENGTH_SHORT).show();
            createAccomodationFragment();
        }

        else if (currentFragment instanceof Transportation )
        {
            //Toast.makeText(MainActivity.this,"Trasportation",Toast.LENGTH_SHORT).show();
            createTransportFragment();
        }

        else if (currentFragment instanceof MenuFragment )
        {
            //Toast.makeText(MainActivity.this,"Menu",Toast.LENGTH_SHORT).show();
            createMenuFragment();

        }

        else if (currentFragment instanceof BookingFragment )
        {
            //Toast.makeText(MainActivity.this,"Booking",Toast.LENGTH_SHORT).show();
            //createMenuFragment();
            createBookingFragment();

        }


    }

    private void createMenuFragment ()
    {
        MenuFragment menuFragment=MenuFragment.newInstance();       //at first menu options will show thats why menu fragment
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,menuFragment,"Sixth");
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createPackageFragment ()
    {
        packageFragment=PackageFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,packageFragment,"First");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createTripPlannerFragment ()
    {
        TripPlannerFragment tripplannerFragment=TripPlannerFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,tripplannerFragment,"Third");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void createMyTripsFragment ()
    {
        TailormadeListFragment tailormadeListFragment=TailormadeListFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,tailormadeListFragment,"Second");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createDestinationFragment ()
    {
        DestinationFragment destinationFragment=DestinationFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,destinationFragment,"Seventh");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createAccomodationFragment ()
    {
        AccomodationFragment hotelListFragment=AccomodationFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,hotelListFragment,"Forth");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createTransportFragment ()
    {
        Transportation transportation=Transportation.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,transportation,"Seventh");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void createBookingFragment ()
    {
        BookingFragment bookingFragment=BookingFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.listFragment,bookingFragment,"Fifth");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //for different button clicks different fragments are opening
        int id = item.getItemId();

        if (id == R.id.nav_packages)
        {
            createPackageFragment();

        }
        else if (id == R.id.nav_my_trip)
        {
           String customerEmail = sharedPreferences.getString(Travel.USER_EMAIL,null);
            if (customerEmail != null) {
                createMyTripsFragment();
            }else
            {
                BaseURL.FRAGMENT_OF_MAIN = "MY_TRIPS";
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivityForResult(loginIntent, 110);
            }

        } else if (id == R.id.nav_create_plan) {

            createTripPlannerFragment();
        }
        else if (id == R.id.nav_view_hotel)
        {

            createAccomodationFragment();
        }
        else if (id == R.id.nav_tokens)
        {
            String customerEmail = sharedPreferences.getString(Travel.USER_EMAIL,null);
            if (customerEmail != null) {
                createBookingFragment();
            }
            else
            {
                BaseURL.FRAGMENT_OF_MAIN = "VIEW_TOKENS";
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivityForResult(loginIntent, 110);
            }

        }

        else if (id == R.id.nav_destination)
        {

            createDestinationFragment();

        }

        else if (id == R.id.nav_transportation)
        {

            createTransportFragment();
        }

        else  if (id == R.id.exit)
        {
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);
        }
//        else if (id == R.id.nav_menu) {
//            Toast.makeText(this, "This is my Toast message!",
//                    Toast.LENGTH_LONG).show();
//       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick (View v)
    {
        Toast.makeText(this, "This is my Toast message!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String customerEmail = sharedPreferences.getString(Travel.USER_EMAIL,null);
        if (customerEmail != null){
            //Toast.makeText(this,resultCode+"",Toast.LENGTH_SHORT).show();
            //Fragment currentFragment = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.listFragment);

            if (BaseURL.FRAGMENT_OF_MAIN.equals("VIEW_TOKENS") )
            {
                createBookingFragment();
            }
            else if (BaseURL.FRAGMENT_OF_MAIN.equals("MY_TRIPS") )
            {
                createMyTripsFragment();

            }


        }
    }



}
