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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivine.cholodesh.R;
import com.example.olivine.cholodesh.TailorMadeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import helpers.BaseURL;
import helpers.DynamicHeight;
import model.TailorMadeList;
import userDefinder.TailorMade;

/**
 * Created by Olivine on 6/7/2017.
 */

public class TailormadeListAdapter extends RecyclerView.Adapter<TailorMadeViewholder> {
    private Context mContext;
    private List<TailorMadeList> tailorMades;

    public TailormadeListAdapter(Context mContext, List<TailorMadeList> tailorMades) {
        this.mContext = mContext;
        this.tailorMades = tailorMades;
    }

    @Override
    public TailorMadeViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.layout_tailor_made_list_new,parent,false);
        TailorMadeViewholder tailorMadeViewholder=new TailorMadeViewholder(view);
        return tailorMadeViewholder;
    }

    @Override
    public void onBindViewHolder(TailorMadeViewholder holder, int position) {
        final TailorMadeList tailorMade=tailorMades.get(position);
        DynamicHeight.setHeight(mContext,holder.parent,4,3);
        final String url = BaseURL.DESTINATION_IMAGE_BASE_URL+tailorMade.getTailormadeDestinationDistrictImage();
                    Picasso.with(mContext)
                            .load(url)
                            .fit()
                            .placeholder(R.drawable.image_placeholder)
                            .into(holder.imageViewLoad);
        holder.cost.setText(tailorMade.getTailormadeTotalCost()+ " ৳");
        //Toast.makeText(mContext,tailorMade.getTailormadeDays()+" days Trip from "+ tailorMade.getTailormadeDepartDistrictName()+ " to "+tailorMade.getTailormadeDestinationDistrictName(),Toast.LENGTH_SHORT).show();
        holder.dayNight.setText(tailorMade.getTailormadeDays()+" DAYS TRIP FROM "+ tailorMade.getTailormadeDepartDistrictName()+ " TO "+tailorMade.getTailormadeDestinationDistrictName());
        holder.travellers.setText(tailorMade.getTailormadePerson()+ "Traveler(s)");

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TailorMadeActivity.class);
                intent.putExtra("TAILOR_MADE_ID",tailorMade.getTailormadeId()+"");
                intent.putExtra("DESTINATION_IMAGE_PATH",url);
                intent.putExtra("COST_TOTAL",tailorMade.getTailormadeTotalCost()+ " ৳");

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tailorMades.size();
    }
}
class TailorMadeViewholder extends RecyclerView.ViewHolder{
    RelativeLayout parent;
    TextView cost;
    TextView dayNight;
    TextView travellers;
    ImageView imageViewLoad;


    public TailorMadeViewholder(View itemView) {
        super(itemView);
        parent= (RelativeLayout) itemView.findViewById(R.id.parent);
        cost= (TextView) itemView.findViewById(R.id.cost);
        dayNight = (TextView) itemView.findViewById(R.id.dayNight);
        travellers= (TextView) itemView.findViewById(R.id.travellers);
        imageViewLoad = (ImageView) itemView.findViewById(R.id.imageViewLoad);

    }
}
