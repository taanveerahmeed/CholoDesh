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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.olivine.cholodesh.PackageDetailsActivity;
import com.example.olivine.cholodesh.R;
import com.squareup.picasso.Picasso;

import constants.Travel;
import helpers.BaseURL;
import model.Package;

/**
 * Created by Olivine on 6/12/2017.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageViewHolder> {
    private Context mContext;
    private Package [] packages;


    public PackageAdapter(Context mContext, Package[] packages) {
        this.mContext = mContext;
        this.packages = packages;
    }


    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_packagelist_new,parent,false);
        PackageViewHolder viewholder=new PackageViewHolder(view);
        //Toast.makeText(mContext,"this",Toast.LENGTH_LONG).show();
        return viewholder;
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {

        final Package tourPackage=packages[position];


        holder.packageName.setText(tourPackage.getPackageName());
        holder.tourPlace.setText("Dhaka"+" To "+tourPackage.getDestinationName());
        holder.destination.setText("In "+tourPackage.getDestinationName());
        holder.dayNight.setText(tourPackage.getPackageDay()+" Days "+tourPackage.getPackageNight()+" Night");
        holder.packageCost.setText(tourPackage.getPackagePrice()+"৳");
        holder.operatorName.setText("By "+tourPackage.getProviderName());
        String url= BaseURL.PACKAGE_IMAGE_BASE_URL+tourPackage.getPackageGalleryImage();


        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(holder.test);



        holder.packageParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent packageDetailsIntent=new Intent(mContext,PackageDetailsActivity.class);
                packageDetailsIntent.putExtra(Travel.PACKAGE_KEY,tourPackage.getPackageId());
                packageDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                packageDetailsIntent.putExtra("IMAGE_URL",tourPackage.getPackageGalleryImage());
                mContext.startActivity(packageDetailsIntent);
            }
        });


    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return packages.length;
    }

}

class PackageViewHolder extends RecyclerView.ViewHolder{
    TextView packageName;
    TextView tourPlace;
    TextView operatorName;
    TextView destination;
    TextView dayNight;
    TextView packageCost;
    ImageView test;
    LinearLayout packageParent;
    public PackageViewHolder(View itemView)
    {
        super(itemView);
        packageName = (TextView) itemView.findViewById(R.id.packageName);
        tourPlace= (TextView) itemView.findViewById(R.id.tourPlace);
        operatorName= (TextView) itemView.findViewById(R.id.operatorName);
        destination= (TextView) itemView.findViewById(R.id.destination);
        dayNight= (TextView) itemView.findViewById(R.id.dayNight);
        packageCost= (TextView) itemView.findViewById(R.id.packageCost);
        packageParent= (LinearLayout) itemView.findViewById(R.id.packageParent);
        test= (ImageView) itemView.findViewById(R.id.imageViewLoad);

    }


}
