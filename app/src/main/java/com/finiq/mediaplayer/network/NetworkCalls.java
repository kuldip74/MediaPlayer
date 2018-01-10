package com.finiq.mediaplayer.network;

/**
 * Created by ADMIN on 1/10/2018.
 */
import com.finiq.mediaplayer.pojo.AddFavouriteResponse;
import com.finiq.mediaplayer.pojo.RegisterUserResponse;
import com.finiq.mediaplayer.pojo.RemoveFavouriteResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface NetworkCalls {
    @FormUrlEncoded
    @POST("/api/Users/RegisterUser")
    Call<RegisterUserResponse> registerUser(@Field("Name") String name,@Field("Username") String username, @Field("Password") String password);

    @FormUrlEncoded
    @POST("/api/Music/AddFavourite")
    Call<AddFavouriteResponse> addFavourite(@Field("Username") String username, @Field("Title") String title, @Field("Artist") String artist, @Field("Album") String album);

    @FormUrlEncoded
    @POST("/api/Music/RemoveFavourite/{username}/{id}")
    Call<RemoveFavouriteResponse> removeFavourite();

    @FormUrlEncoded
    @GET("/api/Users/GetName/{username}")
    Call<String> getUsername();

    @FormUrlEncoded
    @GET(" /api/Music/Favourites/{username}")
    Call<String> getFavourites();




}
