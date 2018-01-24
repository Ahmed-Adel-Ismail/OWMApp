package com.parent.owm.screens.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.actors.ActorSystem;
import com.actors.Message;
import com.parent.entities.City;
import com.parent.owm.R;

import static com.parent.owm.screens.main.MainActivity.MSG_SHOW_FORECAST;
import static com.parent.owm.screens.main.MainViewModel.MSG_REMOVE_CITY;

class FavoriteCitiesViewHolder extends RecyclerView.ViewHolder {

    private TextView cityNameTextView;
    private City city;

    FavoriteCitiesViewHolder(View itemView) {
        super(itemView);
        
        cityNameTextView = itemView.findViewById(R.id.view_favorite_city_label_text_view);

        itemView.findViewById(R.id.view_favorite_city_remove_button)
                .setOnClickListener(this::notifyRemoveButtonClicked);

        itemView.findViewById(R.id.view_favorite_city_show_forecast_button)
                .setOnClickListener(this::notifyShowForecastClicked);
    }

    private void notifyShowForecastClicked(View view) {
        ActorSystem.send(new Message(MSG_SHOW_FORECAST, city), MainActivity.class);
    }

    private void notifyRemoveButtonClicked(View view) {
        ActorSystem.send(new Message(MSG_REMOVE_CITY, city), MainViewModel.class);
    }

    void invalidate(City city) {
        this.city = city;
        this.cityNameTextView.setText(city.getName());
    }


}
