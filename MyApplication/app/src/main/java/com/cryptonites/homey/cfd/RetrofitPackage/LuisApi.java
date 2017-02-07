package com.cryptonites.homey.cfd.RetrofitPackage;

import com.cryptonites.homey.cfd.modelclasses.LuisResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by root on 7/2/17.
 */

public interface LuisApi {

    // * Adding URL Dynamically
    @GET
    Call<LuisResponse> getResponse(@Url String url);


}
