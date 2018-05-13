/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olivine.cholodesh.R;
import com.squareup.picasso.Picasso;

import helpers.BaseURL;
import model.HotelInclusion;
import model.PackageInclusion;
import model.TailorMadeAccommodation;
import model.TailorMadeItinerary;

/**
 * Created by rhythmshahriar on 9/13/17.
 */

public class TailorMadeAccommodationAdapter extends RecyclerView.Adapter<TailorMadeAccommodationViewHolder>{

    private Context mContext;
    private Object[] objects;
   // private PackageInclusion[] inclusionsOfPackage;

    public TailorMadeAccommodationAdapter(Context mContext, Object[] objects) {
        this.mContext = mContext;
        this.objects = objects;
    }
    @Override
    public TailorMadeAccommodationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_tailor_made_accommodation,parent,false);
        TailorMadeAccommodationViewHolder tailorMadeAccommodationViewHolder=new TailorMadeAccommodationViewHolder(view);
        return tailorMadeAccommodationViewHolder;
    }

    @Override
    public void onBindViewHolder(TailorMadeAccommodationViewHolder holder, int position) {

        Object object=objects[position];
        if(object instanceof TailorMadeAccommodation){



            TailorMadeAccommodation tailorMadeAccommodation= (TailorMadeAccommodation) object;
            String url= BaseURL.HOTEL_ROOM_IMAGE_BASE_URL+tailorMadeAccommodation.getImage();
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .fit()
                    .into(holder.image);
            holder.provider.setText(tailorMadeAccommodation.getTailormadeAccommodationAccommodationName());
            holder.occupancy.setText("Max Occupancy: "+tailorMadeAccommodation.getTailormadeAccommodationAccommodationRoomMaxOccupancy());
            holder.category.setText(tailorMadeAccommodation.getTailormadeAccommodationAccommodationCategoryName());
            holder.count.setText(tailorMadeAccommodation.getTailormadeAccommodationDistrictName());
            holder.price.setText("Cost: "+ tailorMadeAccommodation.getTailormadeAccommodationAccommodationRoomPrice()+ " ৳");

        }
        if(object instanceof TailorMadeItinerary){
            TailorMadeItinerary tailorMadeItinerary= (TailorMadeItinerary) object;
            holder.provider.setText(tailorMadeItinerary.getItineraryPlaceName());
            holder.occupancy.setVisibility(View.GONE);
            holder.category.setText("Plan of: "+tailorMadeItinerary.getItineraryDayplan());
            if (tailorMadeItinerary.getItineraryTime().equals("AM"))holder.count.setText("Plan of Morning");
            else if (tailorMadeItinerary.getItineraryTime().equals("PM"))
            {
                holder.count.setText("Plan of Noon");
            }
            else
            {
                holder.count.setText("Plan of Whole Day");
            }
            holder.price.setText("Cost: "+ tailorMadeItinerary.getItineraryPerPersonCost()+ " ৳");


        }

    }

    @Override
    public int getItemCount() {
        return objects.length;
    }
}

class TailorMadeAccommodationViewHolder  extends RecyclerView.ViewHolder{

    TextView provider,occupancy,category,count,price;
    ImageView image;

    public TailorMadeAccommodationViewHolder(View itemView) {
        super(itemView);

        provider= (TextView) itemView.findViewById(R.id.provider);
        occupancy= (TextView) itemView.findViewById(R.id.occupancy);
        category= (TextView) itemView.findViewById(R.id.category);
        count= (TextView) itemView.findViewById(R.id.count);
        price = (TextView) itemView.findViewById(R.id.price);
        image = (ImageView) itemView.findViewById(R.id.foodimage);


    }

}
