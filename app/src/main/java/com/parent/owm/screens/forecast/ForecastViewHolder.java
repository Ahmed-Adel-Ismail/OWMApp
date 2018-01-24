package com.parent.owm.screens.forecast;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.parent.entities.ForecastSummery;
import com.parent.owm.R;

class ForecastViewHolder extends RecyclerView.ViewHolder {

    private final TextView date;
    private final TextView cloudiness;
    private final TextView humidity;
    private final TextView temperature;
    private final TextView weather;
    private final TextView windSpeed;

    ForecastViewHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.view_forecast_item_date);
        cloudiness = itemView.findViewById(R.id.view_forecast_item_cloudiness);
        humidity = itemView.findViewById(R.id.view_forecast_item_humidity);
        temperature = itemView.findViewById(R.id.view_forecast_item_temperature);
        weather = itemView.findViewById(R.id.view_forecast_item_weather);
        windSpeed = itemView.findViewById(R.id.view_forecast_item_wind_speed);
    }

    void invalidate(ForecastSummery summery) {
        date.setText(summery.getDateText());
        cloudiness.setText(summery.getCloudiness());
        humidity.setText(summery.getHumidity());
        temperature.setText(summery.getTemperature());
        weather.setText(summery.getWeather());
        windSpeed.setText(summery.getWindSpeed());
    }
}
