/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.olivine.cholodesh.NearByPlaceDetailsActivity;
import com.example.olivine.cholodesh.PackageDetailsActivity;
import com.example.olivine.cholodesh.R;
import com.example.olivine.cholodesh.ShowPopUp;
import com.squareup.picasso.Picasso;

import constants.Travel;
import helpers.BaseURL;
import model.NearByPlaces;
import model.Package;

/**
 * Created by rhythmshahriar on 7/19/17.
 */

public class NearByPlaceAdapter  extends RecyclerView.Adapter<ViewHolder>  {

    private Context mContext;
    private NearByPlaces[] nearByPlaces;

    public NearByPlaceAdapter(Context mContext, NearByPlaces[] nearByPlaces) {
        this.mContext = mContext;
        this.nearByPlaces = nearByPlaces;
    }


@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view= LayoutInflater.from(mContext).inflate(R.layout.layout_near_by_place,parent,false);
    ViewHolder viewHolder=new ViewHolder(view);
    return viewHolder;

}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Object object=nearByPlaces[position];
        if(object instanceof NearByPlaces){
            NearByPlaces nearByPlaces= (NearByPlaces) object;
            if (BaseURL.LANGUAGE_ENG)holder.nearByPlaceName.setText(nearByPlaces.getDestinationNearbyPlaceName());
            else if (!BaseURL.LANGUAGE_ENG && nearByPlaces.getDestinationNearbyPlaceNameBn() != null)
                holder.nearByPlaceName.setText(nearByPlaces.getDestinationNearbyPlaceNameBn());
            else
            {
                holder.nearByPlaceName.setText(nearByPlaces.getDestinationNearbyPlaceName());
            }

            String url= BaseURL.DESTINATION_NEAR_BY_PLACE_IMAGE_BASE_URL+nearByPlaces.getDestinationNearbyPlaceImage();

        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(holder.nearByPlaceImage);

            final int nearByPlaceId = nearByPlaces.getDestinationNearbyPlaceId();
            final String nearByPlaceName = nearByPlaces.getDestinationNearbyPlaceName();
            final String nearByPlaceNameBn = nearByPlaces.getDestinationNearbyPlaceNameBn();
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,NearByPlaceDetailsActivity.class);
                    intent.putExtra("NEAR_BY_PLACE_ID",nearByPlaceId);
                    if (!BaseURL.LANGUAGE_ENG && nearByPlaceNameBn != null)
                    {
                        intent.putExtra("NEAR_BY_PLACE_NAME",nearByPlaceNameBn);
                    }
                    else
                    {
                        intent.putExtra("NEAR_BY_PLACE_NAME",nearByPlaceName);
                    }

                    //popup.putExtra("DETAILS_ATTRACTION",details);
                    //popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return nearByPlaces.length ;
    }


}

class ViewHolder extends RecyclerView.ViewHolder
{
    TextView nearByPlaceName;
    ImageView nearByPlaceImage;
    LinearLayout parent;

    public ViewHolder(View itemView)
    {
        super(itemView);
        nearByPlaceName= (TextView) itemView.findViewById(R.id.nbPlacename);
        nearByPlaceImage = (ImageView) itemView.findViewById(R.id.imageViewNbPlace);
        parent = (LinearLayout) itemView.findViewById(R.id.parent);
    }



}
