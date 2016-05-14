package com.example.user01.planit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpAPIWrapper extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private YelpAPI yelpAPI;
    private ArrayList<String> settings;
    private ArrayList<Event> morningEvents;
    private ArrayList<Event> afternoonEvents;
    private ArrayList<Event> eveningEvents;
    private CircularProgressView cpv;

    YelpAPIWrapper(Activity activity, ArrayList<String> settings) {
        this.activity = activity;
        this.settings = settings;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                activity.getString(R.string.consumer_key),
                        activity.getString(R.string.consumer_secret),
                        activity.getString(R.string.token),
                        activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        morningEvents = new ArrayList<>();
        afternoonEvents = new ArrayList<>();
        eveningEvents = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        cpv = (CircularProgressView) this.activity.findViewById(R.id.progress_circle);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Map<String, String> mornparam = new HashMap<>();
            Map<String, String> noonparam = new HashMap<>();
            Map<String, String> evenparam = new HashMap<>();
            mornparam.put("term", "Breakfast");
            noonparam.put("term", "Lunch");
            evenparam.put("term", "Dinner");
            Call<SearchResponse> call;

            Bitmap bitmap = BitmapFactory.decodeResource(
                    this.activity.getResources(),R.drawable.yelp_pin);
            bitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);
            EventData.setBitmap(bitmap);

            ArrayList<Business> breakfast, lunch, dinner;


            call = yelpAPI.search(settings.get(0), mornparam);
            SearchResponse morningResponse = call.execute().body();
            breakfast = morningResponse.businesses();

            call = yelpAPI.search(settings.get(0), noonparam);
            SearchResponse noonResponse = call.execute().body();
            lunch = noonResponse.businesses();

            call = yelpAPI.search(settings.get(0), evenparam);
            SearchResponse eveningResponse = call.execute().body();
            dinner = eveningResponse.businesses();



            EventData.setMorningRestaurant(breakfast);
            EventData.setAfternoonRestaurant(lunch);
            EventData.setEveningRestaurant(dinner);

//
//            switch(settings.get(1)) {
//                case "Morning":
//                    call = yelpAPI.search(settings.get(0), mornparam);
//                    SearchResponse morningResponse = call.execute().body();
//                    breakfast = morningResponse.businesses();
//                    morningEvents.add(new YelpEvent((breakfast.get((int) (Math.random() * 20))), this.activity));
//                    break;
//                case "Afternoon":
//                    call = yelpAPI.search(settings.get(0), noonparam);
//                    SearchResponse noonResponse = call.execute().body();
//                    lunch = noonResponse.businesses();
//                    afternoonEvents.add(new YelpEvent((lunch.get((int) (Math.random() * 20))), this.activity));
//                    break;
//                case "Evening":
//                    call = yelpAPI.search(settings.get(0), evenparam);
//                    SearchResponse eveningResponse = call.execute().body();
//                    dinner = eveningResponse.businesses();
//                    eveningEvents.add(new YelpEvent((dinner.get((int) (Math.random() * 20))), this.activity));
//                    break;
//                default:
//                    call = yelpAPI.search(settings.get(0), mornparam);
//                    morningResponse = call.execute().body();
//                    breakfast = morningResponse.businesses();
//
//                    call = yelpAPI.search(settings.get(0), noonparam);
//                    noonResponse = call.execute().body();
//                    lunch = noonResponse.businesses();
//
//                    call = yelpAPI.search(settings.get(0), evenparam);
//                    eveningResponse = call.execute().body();
//                    dinner = eveningResponse.businesses();
//
//                    for (int i = 0; i < 2; i++) {
//                        int random = (int) (Math.random() * breakfast.size() - 1);
//                        morningEvents.add(new YelpEvent(breakfast.get(random), this.activity));
//                        random = (int) (Math.random() * lunch.size() - 1);
//                        afternoonEvents.add(new YelpEvent(lunch.get(random), this.activity));
//                        random = (int) (Math.random() * dinner.size() - 1);
//                        eveningEvents.add(new YelpEvent(dinner.get(random), this.activity));
//                    }
//                    break;
//            }

//            Retrofit client = new Retrofit
//                    .Builder()
//                    .baseUrl("http://api.eventful.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            EventfulAPI eventfulAPI = client.create(EventfulAPI.class);
//            retrofit2.Call<EventfulModel> eventfulModelCall = eventfulAPI.EventfulList("music");
//            ArrayList<EventfulEvent> eventfulEvents = eventfulModelCall.execute().body().getEvents().getEvent();
//            for (int i = 0; i < 4; i++) {
//                eventfulEvents.get(i).setEventVariables(bitmap);
//                morningEvents.add(eventfulEvents.get(i));
//            }
//            EventData.setMorningEvents(morningEvents);
//            EventData.setAfternoonEvents(afternoonEvents);
//            EventData.setDinnerEvents(eveningEvents);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent intent = new Intent(activity,RecyclerActivity.class);

        activity.startActivity(intent);
        cpv.stopAnimation();
        cpv.setThickness(0);
        cpv.setColor(ContextCompat.getColor(this.activity.getApplicationContext(), R.color.grey));
    }

}
