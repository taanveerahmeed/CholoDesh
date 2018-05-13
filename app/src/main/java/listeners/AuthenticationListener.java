/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package listeners;

import model.Auth;
import model.AuthResult;
import model.Review;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import userDefinder.TailormadeSync;

/**
 * Created by Olivine on 6/9/2017.
 */

public interface AuthenticationListener {
    @POST("customer/login")
    Call<AuthResult> authenTication(@Body Auth auth);
    @POST("customer/signup")
    Call<String> signUp(@Body Auth auth);
    @POST("provider/tailormadeinfo")
    Call<String> testTailorMade (@Body TailormadeSync test);
    @POST("provider/review")
    Call<String> testReview (@Body Review review);


}
