/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.olivine.cholodesh.LoginActivity;
import com.example.olivine.cholodesh.R;
import com.example.olivine.cholodesh.RatingActivity;

import java.util.Arrays;
import java.util.List;

import adapters.PackageAdapter;
import adapters.TailormadeListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import helpers.CustomCallBack;
import io.realm.Realm;
import io.realm.RealmResults;
import listeners.FragmentInteractionListener;
import model.TailorMadeList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import userDefinder.TailorMade;

public class TailormadeListFragment extends Fragment {
    private FragmentInteractionListener mListener;

    // Componets initialization
    @BindView(R.id.tailormadeList) RecyclerView tailormadeList;
    ProvideCallback provideCallback;

    // Adapters
    TailormadeListAdapter tailormadeListAdapter;
    // data storage
    SharedPreferences sharedPreferences;
    Realm realm;
    public TailormadeListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TailormadeListFragment newInstance() {
        TailormadeListFragment fragment = new TailormadeListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!BaseURL.LANGUAGE_ENG)
        {
            getActivity().setTitle("টেইলর মেড তালিকা");
        }

        else getActivity().setTitle("Tailormade List");
        View view=inflater.inflate(R.layout.activity_tailormade_list,container,false);
        ButterKnife.bind(this,view);
        sharedPreferences=getActivity().getSharedPreferences(getString(R.string.preference_file_key),getActivity().MODE_PRIVATE);

        // Data storage init
//
//        Realm.init(getActivity());
//        realm=Realm.getDefaultInstance();
//        // Get Data from database
//        RealmResults<TailorMade> realm_tailormade=realm.where(TailorMade.class).findAll();
//
//        List<TailorMade> tailormadesToShow=realm.copyFromRealm(realm_tailormade);
//        // Adapter init
//        if (tailormadesToShow.size() > 0)
//        {
//            tailormadeListAdapter=new TailormadeListAdapter(getActivity(),tailormadesToShow);
//            tailormadeList.setAdapter(tailormadeListAdapter);
//        }
//        else
//        {
//            Toast.makeText(getActivity(),"No Tailormade Found",Toast.LENGTH_LONG).show();
//        }

        String customerEmail = sharedPreferences.getString(Travel.USER_EMAIL, null);
        if (customerEmail == null) {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(loginIntent, 110);
            //works are pending here login
            //will show login page and return after successful login
            //return;
        }
        else
        {
            LoadTailorMade();
        }


        return view;
    }


    private void LoadTailorMade()
    {
        String customerEmail = sharedPreferences.getString(Travel.USER_EMAIL, null);
        if (customerEmail != null)
        {
            provideCallback = new ProvideCallback(getActivity());
            provideCallback.getTailorMadeList(customerEmail).enqueue(new CustomCallBack<TailorMadeList[]>(getActivity()) {
                @Override
                public void onResponse(Call<TailorMadeList[]> call, Response<TailorMadeList[]> response) {
                    super.onResponse(call,response);
                    //Toast.makeText(getActivity(),response.body().length+"",Toast.LENGTH_LONG).show();
                    if (response.body()!= null && response.body().length > 0)
                    {
                        tailormadeList.setAdapter(new TailormadeListAdapter(getActivity(), Arrays.asList(response.body())));
                    }

                    else
                    {
                        String message = "No Tailormade Found";
                        if (!BaseURL.LANGUAGE_ENG)
                        {
                            message =" কোনো টেইলার মেড পাওয়া যায় নি ";

                        }
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TailorMadeList[]> call, Throwable t) {
                    super.onFailure(call,t);
                    String meesage ="Network Error";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" নেটওয়ার্ক ত্রুটি";

                    }
                    Toast.makeText(getActivity(), meesage, Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        DestinationFragment nextFrag= new DestinationFragment();
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.listFragment, nextFrag,"Seventh")
//                .addToBackStack(null)
//                .commit();
       // LoadTailorMade();

    }



}
