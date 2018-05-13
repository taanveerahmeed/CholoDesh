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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import adapters.ReviewByUserAdapter;

import callbacks.ProvideCallback;
import constants.Travel;
import helpers.BaseURL;
import helpers.CustomCallBack;
import model.Food;
import model.PostedReview;
import model.ReviewByUser;
import retrofit2.Call;
import retrofit2.Response;

import static adapters.HotelListAdapter.SERVICE_ID;

public class FoodDetailsActivity extends AppCompatActivity {

    RatingBar ratingBar,ratingGiving;
    RecyclerView ratings;
    TextView category,provider,phone,email,address,description,showMore,description2,nofRevies,resinfoforlang,detailsforlang,wrtrevieforlang,tapforlang;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    float userRating;
    ProvideCallback provideCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingGiving = (RatingBar) findViewById(R.id.ratingGiving);
        provider = (TextView) findViewById(R.id.providername);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        nofRevies = (TextView) findViewById(R.id.nofRevies);
        address = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        description2 = (TextView) findViewById(R.id.description2);
        showMore = (TextView) findViewById(R.id.details);
        imageView = (ImageView) findViewById(R.id.image) ;
        resinfoforlang = (TextView) findViewById(R.id.resinfoforlang);
        detailsforlang = (TextView) findViewById(R.id.detailsforlang);
        wrtrevieforlang = (TextView) findViewById(R.id.wrtrevieforlang);
        tapforlang = (TextView) findViewById(R.id.tapforlang);
        category = (TextView) findViewById(R.id.category);

        if (!BaseURL.LANGUAGE_ENG)
        {
            this.setTitle("বিস্তারিত");
            resinfoforlang.setText("রেস্টুরেন্ট তথ্য");
            detailsforlang.setText("বিস্তারিত");
            wrtrevieforlang.setText("রিভিউ লিখুন");
            tapforlang.setText("রিভিউ লিখতে ট্যাপ করুন ");

        }
        else this.setTitle("Food Details");

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        String url= BaseURL.FOOD_IMAGE_BASE_URL+BaseURL.food.getDestinationFoodServiceProviderImage();
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(imageView);
        float gotRating = 0;
        String countRating = "0";
        if (BaseURL.food.getRatingCount()!=null)
        {
            gotRating = Float.valueOf(BaseURL.food.getAvgRating());
            countRating = BaseURL.food.getRatingCount();
        }
        nofRevies.setText(countRating+" Review(s)");
        ratingBar.setRating(gotRating);
        ratings = (RecyclerView) findViewById(R.id.ratings);

        ReviewByUser[] reviewByUsers = new ReviewByUser[2];
        ReviewByUser test = new ReviewByUser();
        ReviewByUser test2 = new ReviewByUser();
        test2.review = "Some demo text for testing";

        reviewByUsers[0] = test2;
        reviewByUsers[1] = test;
        provideCallback = new ProvideCallback(this);
        //Toast.makeText(FoodDetailsActivity.this,BaseURL.food.getDestinationFoodServiceId()+"",Toast.LENGTH_SHORT).show();

        loadreviews();
        //updateValue();
        //ratings.setAdapter(new ReviewByUserAdapter(this,reviewByUsers));

        //Toast.makeText(this, BaseURL.food.getDestinationFoodServiceProviderName(),Toast.LENGTH_SHORT).show();
//        try
//        {
//
//        }catch (Exception ex)
//        {
//
//        }

        if (!BaseURL.LANGUAGE_ENG && BaseURL.food.getDestinationFoodServiceProviderNameBn() != null)
        {
            provider.setText(BaseURL.food.getDestinationFoodServiceProviderNameBn());
        }
        else provider.setText(BaseURL.food.getDestinationFoodServiceProviderName());

        if (!BaseURL.LANGUAGE_ENG && BaseURL.food.getFoodCategoryNameBn() != null)
        {
            category.setText(BaseURL.food.getFoodCategoryNameBn());
        }
        else category.setText(BaseURL.food.getFoodCategoryName());

        phone.setText(BaseURL.food.getDestinationFoodServiceProviderPhone());

        if (!BaseURL.LANGUAGE_ENG && BaseURL.food.getDestinationFoodServiceProviderAddressBn() != null)
        {
            address.setText(BaseURL.food.getDestinationFoodServiceProviderAddressBn());
        }
        else address.setText(BaseURL.food.getDestinationFoodServiceProviderAddress());

        if (!BaseURL.LANGUAGE_ENG && BaseURL.food.getDestinationFoodServiceProviderDetailsBn() != null)
        {
            description.setText(Html.fromHtml(BaseURL.food.getDestinationFoodServiceProviderDetailsBn()));
        }
        else description.setText(Html.fromHtml(BaseURL.food.getDestinationFoodServiceProviderDetails()));


        //description.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        if (BaseURL.food.getDestinationFoodServiceProviderEmail()!= null) email.setText(BaseURL.food.getDestinationFoodServiceProviderEmail());
        description2.setText(description.getText());
        description.post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(mContext,holder.review.getLineCount()+"",Toast.LENGTH_SHORT).show();
                if (description.getLineCount()>2)
                {
                    showMore.setVisibility(View.VISIBLE);
                }

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandCollaspe();

            }
        });
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandCollaspe();

            }
        });

        ratingGiving.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                String id =BaseURL.food.getDestinationFoodServiceId() +"";
                if (id.equals(BaseURL.REVIEWED_FOOD_ID))
                {
                    String meesage ="You Already Reviewed for this Food";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" আপনি ইতিমধ্যে এই খাবার এর জন্য রিভিউ করেছেন";

                    }
                    Toast.makeText(FoodDetailsActivity.this,meesage,Toast.LENGTH_SHORT).show();

                    ratingGiving.setRating(BaseURL.REVIEWED_FOOD_RATING);
                }
                else
                {
                    userRating = rating;
                    String customerEmail=sharedPreferences.getString(Travel.USER_EMAIL,null);
                    if(customerEmail==null){
                        Intent loginIntent=new Intent(FoodDetailsActivity.this,LoginActivity.class);
                        startActivityForResult(loginIntent,110);
                        //works are pending here login
                        //will show login page and return after successful login
                        //return;
                    }
                    else
                    {
                        startRatingActivity();
                    }
                    //Toast.makeText(RatingActivity.this,rating+ " Stars" ,Toast.LENGTH_SHORT).show();
                }




            }
        });


    }
    private void startRatingActivity ()
    {
        Intent intent = new Intent(FoodDetailsActivity.this,ShowPopUp.class);
        int serviceId=BaseURL.food.getDestinationFoodServiceId();
        String id = serviceId+"";
        intent.putExtra("DETAILS_ATTRACTION","oPEenFoRrAtInG");
        intent.putExtra("RATING",userRating);
        intent.putExtra("REVIEW_TRACK_ID",id);
        intent.putExtra("REVIEW_TYPE","Food");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user registered immediately  redirect to requested activity
        super.onActivityResult(requestCode, resultCode, data);

        String customerEmail=sharedPreferences.getString(Travel.USER_EMAIL,null);
        if (customerEmail == null)
        {
            String meesage ="Login Needed to Rate";
            if (!BaseURL.LANGUAGE_ENG)
            {
                meesage ="লগইন প্রয়োজন";

            }
            Toast.makeText(FoodDetailsActivity.this,meesage,Toast.LENGTH_SHORT).show();


        }
        else
        {
            startRatingActivity();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void expandCollaspe ()
    {


        ViewGroup.LayoutParams params = description.getLayoutParams();
        if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) (40 * scale + 0.5f);
            params.height = pixels;
            showMore.setText("Show More");
        }
        else
        {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            showMore.setText("Show Less");


        }

        //description.setLayoutParams(params);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(1000);
        v.startAnimation(a);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseURL.FoodReviewByUser != 0)
        {
            float gotRating = Float.valueOf(BaseURL.food.getAvgRating())*Integer.parseInt(BaseURL.food.getRatingCount()) + BaseURL.FoodReviewByUser;
            int countRating = Integer.parseInt(BaseURL.food.getRatingCount())+1;
            float newRating = gotRating/countRating;
           // Toast.makeText(this,"gotrating:" + gotRating + "countrating: "+ countRating+ "newRating :" + newRating,Toast.LENGTH_LONG).show();
            ratingBar.setRating(newRating);
            nofRevies.setText(countRating+" Review(s)");
            BaseURL.FoodReviewByUser = 0f;
            loadreviews();

        }


    }

    private void updateValue()
    {
        //Toast.makeText(FoodDetailsActivity.this,"count: "+ BaseURL.REVIEW_COUNT+" rating: "+ BaseURL.REVIEW,Toast.LENGTH_SHORT).show();
    }

    private void loadreviews ()
    {

        provideCallback.getFoodReview(BaseURL.food.getDestinationFoodServiceId()+"").enqueue(new CustomCallBack<PostedReview[]>(this) {
            @Override
            public void onResponse(Call<PostedReview[]> call, Response<PostedReview[]> response) {
                super.onResponse(call,response);
                if (response.body() != null && response.body().length>0)
                {
                    ratings.setAdapter(new ReviewByUserAdapter(FoodDetailsActivity.this,response.body(),"FOOD"));

                   // nofRevies.setText(BaseURL.REVIEW_COUNT+" Review(s)");
                     //ratingBar.setRating(BaseURL.REVIEW);
                }
                else
                {
                    String meesage ="No Review Found";
                    if (!BaseURL.LANGUAGE_ENG)
                    {
                        meesage =" কোন রিভিউ পাওয়া";

                    }
                    Toast.makeText(FoodDetailsActivity.this,meesage,Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<PostedReview[]> call, Throwable t) {
                super.onFailure(call,t);
                String meesage ="Network Error";
                if (!BaseURL.LANGUAGE_ENG)
                {
                    meesage =" নেটওয়ার্ক ত্রুটি";

                }
                Toast.makeText(FoodDetailsActivity.this,meesage,Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}





// class ExpandCollapseAnimation extends Animation {
//    private View mAnimatedView;
//    private int mEndHeight;
//    private int mType;
//     int targtetHeight;
//
//    /**
//     * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
//     * @param view The view to animate
//     * @param duration
//     * @param type The type of animation: 0 will expand from gone and 0 size to visible and layout size defined in xml.
//     * 1 will collapse view and set to gone
//     */
//    public ExpandCollapseAnimation(View view, int duration, int type,int height) {
//        setDuration(duration);
//        targtetHeight = height;
//        mAnimatedView = view;
//        mEndHeight = mAnimatedView.getLayoutParams().height;
//        mType = type;
//        if(mType == 0) {
//            mAnimatedView.getLayoutParams().height = 0;
//            mAnimatedView.setVisibility(View.VISIBLE);
//        }
//    }
//     @Override
//     protected void applyTransformation(float interpolatedTime, Transformation t) {
//         mAnimatedView.getLayoutParams().height = interpolatedTime == 1
//                 ? ViewGroup.LayoutParams.WRAP_CONTENT
//                 : (int)(targtetHeight * interpolatedTime);
//         mAnimatedView.requestLayout();
//     }
//
//     @Override
//     public boolean willChangeBounds() {
//         return false;
//     }
//
//
//}
