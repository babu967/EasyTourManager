package com.codex.easytourmanager.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codex.easytourmanager.R;
import com.codex.easytourmanager.api.ApiClient;
import com.codex.easytourmanager.api.ApiEndClient;
import com.codex.easytourmanager.forecast_data.ForecastWeather;
import com.codex.easytourmanager.weather_data.WeatherResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {
    private TextView curTempView, cityView, curConditionView;

    private TextView day1NameView, day1ConditionView, day1tempView, day2NameView, day2ConditionView, day2tempView;
    private TextView day3NameView, day3ConditionView, day3tempView, day4NameView, day4ConditionView, day4tempView;
    private TextView day5NameView, day5ConditionView, day5tempView;
    private ImageView day1IconView, day2IconView, day3IconView, day4IconView, day5IconView,curIconVIew;

    private String query = "dhaka,bd";
    ApiEndClient apiEndPoint;
    private String apiKey = "d31ac27d3e383a7baf33b5038b945844";
    ForecastWeather weatherResponse;
    int min=0,max=7;
    int dataSize ;
    ProgressDialog progressDialog;

    public WeatherFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Weather");

        curTempView = view.findViewById(R.id.temp_view);
        cityView = view.findViewById(R.id.city_view);
        curConditionView = view.findViewById(R.id.condition_view);
        curIconVIew = view.findViewById(R.id.icon_image_view);

        day1NameView = view.findViewById(R.id.day1_name_view);
        day1IconView = view.findViewById(R.id.day1_icon_view);
        day1ConditionView = view.findViewById(R.id.day1_condition_view);
        day1tempView = view.findViewById(R.id.day1_temp_view);

        day2NameView = view.findViewById(R.id.day2_name_view);
        day2IconView = view.findViewById(R.id.day2_icon_view);
        day2ConditionView = view.findViewById(R.id.day2_condition_view);
        day2tempView = view.findViewById(R.id.day2_temp_view);

        day3NameView = view.findViewById(R.id.day3_name_view);
        day3IconView = view.findViewById(R.id.day3_icon_view);
        day3ConditionView = view.findViewById(R.id.day3_condition_view);
        day3tempView = view.findViewById(R.id.day3_temp_view);

        day4NameView = view.findViewById(R.id.day4_name_view);
        day4IconView = view.findViewById(R.id.day4_icon_view);
        day4ConditionView = view.findViewById(R.id.day4_condition_view);
        day4tempView = view.findViewById(R.id.day4_temp_view);

        day5NameView = view.findViewById(R.id.day5_name_view);
        day5IconView = view.findViewById(R.id.day5_icon_view);
        day5ConditionView = view.findViewById(R.id.day5_condition_view);
        day5tempView = view.findViewById(R.id.day5_temp_view);


        apiEndPoint = ApiClient.getWeatherClient().create(ApiEndClient.class);
        setCurrentDayData();

        Call<ForecastWeather> call = apiEndPoint.getForeCastData(query, apiKey);

        progressDialog.show();
        call.enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {

                progressDialog.dismiss();
                weatherResponse = response.body();
                dataSize = weatherResponse.getList().size()-1;

                if (response != null) {

                    setday1Data();
                    setday2Data();
                    setday3Data();
                    setday4Data();

                    setday5Data();
                    // Toast.makeText(getActivity(), String.valueOf(weatherResponse.getList().size()), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {

            }
        });


        return view;
    }

    public void setday1Data() {


        //String day = weatherResponse.getList().get(0).retrieveDayName();
        //String temp = weatherResponse.getList().get(0).getMain().retrieveTemp();
        String icon = weatherResponse.getList().get(0).getWeather().get(0).retrieveIcon();
        String condition = weatherResponse.getList().get(0).getWeather().get(0).getDescription();


        day1NameView.setText("Today");
        day1tempView.setText(getMaxMinTemp());
        day1ConditionView.setText(condition);
        Picasso.get().load(icon).into(day1IconView);
    }

    public void setday2Data() {

        //String day = weatherResponse.getList().get(8).retrieveDayName();
        //String temp = weatherResponse.getList().get(8).getMain().retrieveTemp();
        String icon = weatherResponse.getList().get(8).getWeather().get(0).retrieveIcon();
        String condition = weatherResponse.getList().get(8).getWeather().get(0).getDescription();

        day2NameView.setText("Tomorrow");
        day2tempView.setText(getMaxMinTempDay2());
        day2ConditionView.setText(condition);
        Picasso.get().load(icon).into(day2IconView);
    }

    public void setday3Data() {

        String day = weatherResponse.getList().get(16).retrieveDayName();
        //String temp = weatherResponse.getList().get(16).getMain().retrieveTemp();
        String icon = weatherResponse.getList().get(16).getWeather().get(0).retrieveIcon();
        String condition = weatherResponse.getList().get(16).getWeather().get(0).getDescription();

        day3NameView.setText(day);
        day3tempView.setText(getMaxMinTempDay3());
        day3ConditionView.setText(condition);
        Picasso.get().load(icon).into(day3IconView);
    }

    public void setday4Data() {

        String day = weatherResponse.getList().get(24).retrieveDayName();
        //String temp = weatherResponse.getList().get(24).getMain().retrieveTemp();
        String icon = weatherResponse.getList().get(24).getWeather().get(0).retrieveIcon();
        String condition = weatherResponse.getList().get(24).getWeather().get(0).getDescription();

        day4NameView.setText(day);
        day4tempView.setText(getMaxMinTempDay4());
        day4ConditionView.setText(condition);
        Picasso.get().load(icon).into(day4IconView);
    }

    public void setday5Data() {

        String day = weatherResponse.getList().get(32).retrieveDayName();
        //String temp = weatherResponse.getList().get(32).getMain().retrieveTemp();
        String icon = weatherResponse.getList().get(32).getWeather().get(0).retrieveIcon();
        String condition = weatherResponse.getList().get(32).getWeather().get(0).getDescription();

        day5NameView.setText(day);
        day5tempView.setText(getMaxMinTempDay5());
        day5ConditionView.setText(condition);
        Picasso.get().load(icon).into(day5IconView);
    }


    private String getMaxMinTemp() {
        int i ;
        Double maxT =0.0;
        for (i=0;i<=7;i++){
            Double max = weatherResponse.getList().get(i).getMain().getTempMax();
            if (max>maxT){
                maxT=max;
            }
        }
        int j;
        Double minT=0.0;
        for (j=0;j<=7;j++){
            Double min = weatherResponse.getList().get(j).getMain().getTempMin();
            Double min2 =weatherResponse.getList().get(j+1).getMain().getTempMin();
            if (min<min2){
                minT = min;
            }else {
                minT = min2;
            }
        }

        Double min = minT-273.0;
        Double max = maxT-273.0;
        String mtemp = String.format("%.0f",min);
        String maxtemp = String.format("%.0f",max)+"\u2103";
        return mtemp+" / "+maxtemp;
    }

    private String getMaxMinTempDay2() {
        int i ;
        Double maxT =0.0;
        for (i=8;i<=15;i++){
            Double max = weatherResponse.getList().get(i).getMain().getTempMax();
            if (max>maxT){
                maxT=max;
            }
        }
        int j;
        Double minT=0.0;
        for (j=8;j<=15;j++){
            Double min = weatherResponse.getList().get(j).getMain().getTempMin();
            Double min2 =weatherResponse.getList().get(j+1).getMain().getTempMin();
            if (min<min2){
                minT = min;
            }else {
                minT = min2;
            }
        }

        Double min = minT-273.0;
        Double max = maxT-273.0;
        String mtemp = String.format("%.0f",min);
        String maxtemp = String.format("%.0f",max)+"\u2103";
        return mtemp+" / "+maxtemp;
    }
    private String getMaxMinTempDay3() {
        int i ;
        Double maxT =0.0;
        for (i=16;i<=23;i++){
            Double max = weatherResponse.getList().get(i).getMain().getTempMax();
            if (max>maxT){
                maxT=max;
            }
        }
        int j;
        Double minT=0.0;
        for (j=16;j<=23;j++){
            Double min = weatherResponse.getList().get(j).getMain().getTempMin();
            Double min2 =weatherResponse.getList().get(j+1).getMain().getTempMin();
            if (min<min2){
                minT = min;
            }else {
                minT = min2;
            }
        }

        Double min = minT-273.0;
        Double max = maxT-273.0;
        String mtemp = String.format("%.0f",min);
        String maxtemp = String.format("%.0f",max)+"\u2103";
        return mtemp+" / "+maxtemp;
    }
    private String getMaxMinTempDay4() {
        int i ;
        Double maxT =0.0;
        for (i=24;i<=31;i++){
            Double max = weatherResponse.getList().get(i).getMain().getTempMax();
            if (max>maxT){
                maxT=max;
            }
        }
        int j;
        Double minT=0.0;
        for (j=24;j<=31;j++){
            Double min = weatherResponse.getList().get(j).getMain().getTempMin();
            Double min2 =weatherResponse.getList().get(j+1).getMain().getTempMin();
            if (min<min2){
                minT = min;
            }else {
                minT = min2;
            }
        }

        Double min = minT-273.0;
        Double max = maxT-273.0;
        String mtemp = String.format("%.0f",min);
        String maxtemp = String.format("%.0f",max)+"\u2103";
        return mtemp+" / "+maxtemp;
    }
    private String getMaxMinTempDay5() {
        int i ;
        Double maxT =0.0;
        for (i=32;i<=dataSize;i++){
            Double max = weatherResponse.getList().get(i).getMain().getTempMax();
            if (max>maxT){
                maxT=max;
            }
        }
        int j;
        Double minT=0.0;
        for (j=32;j<dataSize;j++){
            Double min = weatherResponse.getList().get(j).getMain().getTempMin();
            Double min2 =weatherResponse.getList().get(j+1).getMain().getTempMin();
            if (min<min2){
                minT = min;
            }else {
                minT = min2;
            }
        }

        Double min = minT-273.0;
        Double max = maxT-273.0;
        String mtemp = String.format("%.0f",min);
        String maxtemp = String.format("%.0f",max)+"\u2103";
        return mtemp+" / "+maxtemp;
    }
    private void setCurrentDayData(){

        Call<WeatherResponse> call = apiEndPoint.getWeatherData(query,apiKey);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                if (weatherResponse!=null){
                    cityView.setText(weatherResponse.getName());
                    Double temp = weatherResponse.getMain().getTemp();
                    curTempView.setText(String.format("%.0f",temp)+"\u2103");
                    curConditionView.setText(weatherResponse.getWeather().get(0).getDescription());
                    Picasso.get().load(weatherResponse.getWeather().get(0).showIcon()).into(curIconVIew);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }


}

