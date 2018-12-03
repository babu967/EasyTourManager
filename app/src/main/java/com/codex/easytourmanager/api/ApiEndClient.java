package com.codex.easytourmanager.api;

import com.codex.easytourmanager.forecast_data.ForecastWeather;
import com.codex.easytourmanager.weather_data.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndClient {
    @GET("forecast")
    Call<ForecastWeather> getForeCastData(@Query("q") String city, @Query("appid") String key);

    @GET("weather")
    Call<WeatherResponse> getWeatherData(@Query("q") String city, @Query("appid") String key);

}
