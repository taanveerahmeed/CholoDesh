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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivine.cholodesh.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import helpers.BaseURL;
import model.AccommodationProvider;
import model.AccommodationRoom;
import model.ListWrapper;

/**
 * Created by Tanveer on 5/27/2017.
 */

public class AccommodationRoomListAdapter extends RecyclerView.Adapter<AccommodationRoomViewholder> {

    private Context mContext;
    private List<AccommodationRoom> accommodationRooms;
    AccommodationRoomViewholder ref;

    public AccommodationRoomListAdapter(Context mContext, List<AccommodationRoom> accommodationRooms) {
        this.mContext = mContext;
        this.accommodationRooms = accommodationRooms;

    }
    @Override
    public AccommodationRoomViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.layout_hotel_room_list_new,parent,false);
        AccommodationRoomViewholder accommodationViewholder=new AccommodationRoomViewholder(itemView);
        return accommodationViewholder;
    }

    @Override
    public void onBindViewHolder(final AccommodationRoomViewholder holder, final int position) {

        final AccommodationRoom accommodationRoom=accommodationRooms.get(position);
        //Toast.makeText(mContext,accommodationRoom.UniqueId,Toast.LENGTH_SHORT).show();
        String url= BaseURL.HOTEL_ROOM_IMAGE_BASE_URL+accommodationRoom.getAccommodationRoomGalleryImage();
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(holder.roomImage);
        if(!BaseURL.LANGUAGE_ENG)
        {
            holder.select.setText("বাছাই করুন");
        }
        holder.accommodationName.setText(accommodationRoom.getProviderName());
        holder.occupancy.setText("Max Occupancy: " + accommodationRoom.getAccommodationRoomMaxOccupancy()+"");
        holder.roomType.setText(accommodationRoom.getAccommodationCategoryName());
        holder.roomPrice.setText(accommodationRoom.getAccommodationRoomPrice()+"৳");
        holder.count.setText("#"+(position+1)+" of "+getItemCount()+" Rooms " );
//        holder.bedType.setText("Single Bed");

        if (accommodationRoom.isSelected())
        {
            holder.roomListParent.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_border_bottom_selected));
        }
        else
        {
            holder.roomListParent.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_border_bottom));
        }
        ref = holder;
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleSelecion(view,accommodationRoom,holder);
               // Toast.makeText(mContext,accommodationRoom.getProviderName(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext,accommodationRoom.isSelected()+"",Toast.LENGTH_SHORT).show();

                if (doesExist(accommodationRoom))
                {

                    Toast.makeText(mContext,"Already booked",Toast.LENGTH_SHORT).show();
                    holder.select.setVisibility(View.INVISIBLE);

                   // Toast.makeText(mContext,accommodationRoom.getAccommodationRoomId()+"",Toast.LENGTH_SHORT).show();
                }

                else {


                    if (holder.select.getText().equals("Select") || holder.select.getText().equals("বাছাই করুন")) {
                        if (!BaseURL.LANGUAGE_ENG) holder.select.setText("নির্বাচিত");
                        else holder.select.setText("Selected");
                        holder.roomListParent.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_border_bottom_selected));
                        //BaseURL.accommodationRooms.add(accommodationRoom);


                        BaseURL.totalCost += Integer.parseInt(accommodationRoom.getAccommodationRoomPrice());
                        // Toast.makeText(mContext,BaseURL.totalCost+"",Toast.LENGTH_SHORT).show();
                    } else {
                        if (!BaseURL.LANGUAGE_ENG) holder.select.setText("বাছাই করুন");
                        else holder.select.setText("Select");
                        holder.roomListParent.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_border_bottom));
                        BaseURL.totalCost -= Integer.parseInt(accommodationRoom.getAccommodationRoomPrice());
                        //BaseURL.accommodationRooms.remove(accommodationRoom);
                        //Toast.makeText(mContext,BaseURL.totalCost+"",Toast.LENGTH_SHORT).show();
                    }
                    toggleSelecion(view,accommodationRoom);
                }

            }
        });

    }

    boolean doesExist(AccommodationRoom accommodationRoom)
    {
        boolean result = false;
        for (int i = 0; i< BaseURL.accommodationRooms.size();i++)
        {
            if (accommodationRoom.equals(BaseURL.accommodationRooms.get(i)))
            {
                //Toast.makeText(mContext,"matching : " +accommodationRoom.getAccommodationRoomId() + " with "+ BaseURL.accommodationRooms.get(i).getAccommodationRoomId(),Toast.LENGTH_SHORT).show();
                result = true;
            }
        }
        return  result;
    }

    @Override
    public int getItemCount() {
        return accommodationRooms.size();
    }
    // select rooms fom list and diselect
    private void toggleSelecion(View view,AccommodationRoom accommodationRoom){

        if(accommodationRoom.isSelected()){

            accommodationRoom.setSelected(false);
        }else{

            accommodationRoom.setSelected(true);
        }

    }
}
class AccommodationRoomViewholder extends RecyclerView.ViewHolder{
    LinearLayout roomListParent;
    TextView occupancy;
    TextView accommodationName; //// STOPSHIP: 6/2/2017  
    TextView roomType; //s
    TextView bedType;
    TextView roomPrice;
    TextView count;
    Button select;
    ImageView roomImage;
    CheckBox checkBox;

    public AccommodationRoomViewholder(View itemView) {
        super(itemView);
        roomListParent= (LinearLayout) itemView.findViewById(R.id.roomListParent);
        occupancy= (TextView) itemView.findViewById(R.id.occupancy);
        roomType= (TextView) itemView.findViewById(R.id.roomType);
        accommodationName= (TextView) itemView.findViewById(R.id.accommodationName);
        //bedType= (TextView) itemView.findViewById(R.id.bedType);
        roomPrice= (TextView) itemView.findViewById(R.id.roomPrice);
        count = (TextView) itemView.findViewById(R.id.count);
        select = (Button) itemView.findViewById(R.id.select);
        roomImage = (ImageView) itemView.findViewById(R.id.hotelroom);
         //checkBox = (CheckBox) itemView.findViewById(R.id.chekbox);

    }

}
