package com.example.alexandra.expendablechecklist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMaps {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyAFXeGAfW8NsbhT8yXj0fhsVmvUEnP3AsA&language=ru")
    Call<Example> getNearbyPlaces(@Query("location") String location, @Query("radius") int radius);

    @GET("api/directions/json?key=AIzaSyAFXeGAfW8NsbhT8yXj0fhsVmvUEnP3AsA&units=metric&mode=walking")
    Call<Example2> getDistanceDuration( @Query("origin") String origin, @Query("destination") String destination, @Query("waypoints") String waypoints);
    // Call<Example2> getDistanceDuration( @Query("origin") String origin, @Query("destination") String destination);
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyAFXeGAfW8NsbhT8yXj0fhsVmvUEnP3AsA&language=ru")
    Call<Example> getNearbyPlaces2(@Query("location") String location, @Query("radius") int radius, @Query("pagetoken") String page);

}