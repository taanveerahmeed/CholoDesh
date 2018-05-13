/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.olivine.cholodesh.AccommodationRoomlistActivity;
import com.example.olivine.cholodesh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import helpers.BaseURL;
import listeners.AccommodationListener;
import model.AccommodationProvider;

/**
 * Created by Olivine on 5/27/2017.
 */

public class AccommodationListAdapter extends RecyclerView.Adapter<AccommodationListViewholder> {
    private Context mContext;
    private List<AccommodationProvider> accommodationProviders;
    private AccommodationListener accommodationListener;

    public AccommodationListAdapter setAccommodationListener(AccommodationListener accommodationListener) {
        this.accommodationListener = accommodationListener;
        return this;
    }

    public AccommodationListAdapter(Context mContext, List<AccommodationProvider> accommodationProviders) {
        this.mContext = mContext;
        this.accommodationProviders = accommodationProviders;
    }

    @Override
    public AccommodationListViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.layout_accomodation_list_new,parent,false);
        AccommodationListViewholder accommodationViewholder=new AccommodationListViewholder(itemView);
        return accommodationViewholder;
    }

    @Override
    public void onBindViewHolder(AccommodationListViewholder holder, int position) {
        final AccommodationProvider accommodationProvider=accommodationProviders.get(position);
        holder.hotelNameTextView.setText(accommodationProvider.getProviderName());
        String url= BaseURL.IMAGE_BASE_URL+"provider_image/"+accommodationProvider.getProviderImage();
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(holder.hotelImage);



        holder.hotelAddress.setText(accommodationProvider.getAccommodationTypeName());
        holder.hotelCount.setText("#" + (position+1) + " of " + getItemCount()+" Hotels in "+ accommodationProvider.getDestinationName());

        try {
            String rating = accommodationProvider.getAccommodationTypeName();
            rating = rating.substring(0,1);

            holder.hotelRating.setRating(Integer.parseInt(rating));
        }catch (Exception ex)
        {
            holder.hotelRating.setRating(0);
        }
        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accommodationListener.accommodationActivityResult(accommodationProvider.getAccommodationServiceId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return accommodationProviders.size();
    }
}
class AccommodationListViewholder extends RecyclerView.ViewHolder{
    TextView hotelNameTextView;
    //TextView accommodationCategory;
    TextView hotelAddress;
    TextView hotelCount;
    ImageView hotelImage;
    RatingBar hotelRating;
    Button viewDetails;
    TextView hotline;
   // RatingBar hotelRating;

    public AccommodationListViewholder(View itemView) {
        super(itemView);
        hotelNameTextView = (TextView) itemView.findViewById(R.id.hotelNameTextView);

        hotelAddress = (TextView) itemView.findViewById(R.id.address);
        hotelCount = (TextView) itemView.findViewById(R.id.hotelCount);
        hotelImage = (ImageView) itemView.findViewById(R.id.hotelImage);
        hotelRating = (RatingBar) itemView.findViewById(R.id.hotelRating);
        viewDetails = (Button) itemView.findViewById(R.id.viewDetails);
        hotline = (TextView) itemView.findViewById(R.id.hotline);
        if (!BaseURL.LANGUAGE_ENG)
        {
            viewDetails.setText("বিস্তারিত");
        }
    }

}
