package com.neel.www.weathermap;

/**
 * Created by Nilesh Shinde on 05-04-2016.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class HeadlessFragment extends Fragment {
    public static final String TAG_HEADLESS_FRAGMENT = "headless_fragment";


    public static interface TaskStatusCallback {
        void onPreExecute();

        void onProgressUpdate(int progress);

        void onPostExecute(Vector<WeatherdataModel> result,Vector<Days> dayweather,String message);

        void onCancelled();
    }

    TaskStatusCallback mStatusCallback;
    BackgroundTask mBackgroundTask;
    boolean isTaskExecuting = false;

    /**
     * Called when a fragment is first attached to its activity.
     * onCreate(Bundle) will be called after this.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mStatusCallback = (TaskStatusCallback) activity;
    }

    /**
     * Called to do initial creation of a fragment.
     * This is called after onAttach(Activity) and before onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    /**
     * Called when the fragment is no longer attached to its activity. This is called after onDestroy().
     */
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        mStatusCallback = null;
    }

    private class BackgroundTask extends AsyncTask<String, Integer, String>
    {
        String url;
        Context con;


        // temporary string to show the parsed response
        private String jsonResponse;

        private BackgroundTask(String url, Context con) {
            this.url = url;
            this.con = con;
        }

        @Override
        protected void onPreExecute() {


            if (mStatusCallback != null)
                mStatusCallback.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            url = url + "&cnt=14&APPID=62006d777f587f988dbbc1730db1caac";




            makeJsonObjectRequest(url);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
          /*  if (mStatusCallback != null)
                mStatusCallback.onPostExecute(result);*/
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mStatusCallback != null)
                mStatusCallback.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled(String result) {
            if (mStatusCallback != null)
                mStatusCallback.onCancelled();
        }

        private void makeJsonObjectRequest(String urlcity)
        {

           // showpDialog();
           // final ArrayList<String> weatherdata=new ArrayList<>();
          final  Vector<WeatherdataModel> weatherdata=new Vector<WeatherdataModel>();
            final Vector<Days> daysjson=new Vector<Days>();


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    urlcity, null, new Response.Listener<JSONObject>()
            {

                @Override
                public void onResponse(JSONObject response)
                {
                    //Log.d(TAG, response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        WeatherdataModel wtherdata=new WeatherdataModel();

                        String cod = response.getString("cod");

                        String message = response.getString("message");
                        if(cod.equalsIgnoreCase("404" )||cod.equalsIgnoreCase("400"))
                        {
                            if (mStatusCallback != null)
                                mStatusCallback.onPostExecute(weatherdata,daysjson,message);
                            // Snackbar.make(getView(),message,Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        String cnt = response.getString("cnt");
                        wtherdata.setCod(cod);
                        wtherdata.setMessage(message);
                        wtherdata.setCnt(cnt);



                        JSONObject city = response.getJSONObject("city");
                        String id1 = city.getString("id");
                        String name = city.getString("name");
                        wtherdata.setId(id1);
                        wtherdata.setName(name);



                        JSONObject coordinate = city.getJSONObject("coord");
                        String lat= coordinate.getString("lat");
                        String longt=coordinate.getString("lon");
                        wtherdata.setLatitude(lat);
                        wtherdata.setLongitude(longt);

                        JSONArray daysarray = response.getJSONArray("list");
                        for (int i = 0; i < daysarray.length(); i++) {

                            JSONObject list = (JSONObject) daysarray
                                    .get(i);
                            Days daysdata=new Days();
                            String pressure = list.getString("pressure");
                            String humidity = list.getString("humidity");
                            String speed = list.getString("speed");
                            String deg = list.getString("deg");
                            String clouds = list.getString("clouds");
                            daysdata.setPressure(pressure);
                            daysdata.setHumidity(humidity);
                            daysdata.setSpeed(speed);
                            daysdata.setDeg(deg);
                            daysdata.setClouds(clouds);


                            JSONObject temp = list.getJSONObject("temp");
                            String day = temp.getString("day");
                            String min = temp.getString("min");
                            String max = temp.getString("max");
                            String night = temp.getString("night");
                            String eve = temp.getString("eve");
                            String morn = temp.getString("morn");
                            daysdata.setDay(day);
                            daysdata.setMin(min);
                            daysdata.setMax(max);
                            daysdata.setNight(night);
                            daysdata.setEve(eve);
                            daysdata.setMorn(morn);



                            JSONArray weatherarray = list.getJSONArray("weather");
                            JSONObject weather=(JSONObject)weatherarray.get(0);
                            String id2 = weather.getString("id");
                            String main = weather.getString("main");
                            String description = weather.getString("description");
                            String icon = weather.getString("icon");
                            daysdata.setId(id2);
                            daysdata.setMain(main);
                            daysdata.setDescription(description);
                            daysdata.setIcon(icon);
                            daysjson.add(daysdata);

                        }

                      //  wtherdata.setDaysdata(daysjson);
                        weatherdata.add(wtherdata);
















                        if (mStatusCallback != null)
                            mStatusCallback.onPostExecute(weatherdata,daysjson,message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mStatusCallback != null)
                            mStatusCallback.onPostExecute(weatherdata,daysjson,"No City found");
                      //  Snackbar.make(getView(),"No City found.",Snackbar.LENGTH_LONG).show();
                        return;

                    }
                   // hidepDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("ERror", "Error: " + error.getMessage());
                    // hide the progress dialog
                   mStatusCallback.onCancelled();
                   // return;
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);

        }


    }



    public void startBackgroundTask(String url,Context con) {
        if(!isTaskExecuting){
            mBackgroundTask = new BackgroundTask(url,con);
            mBackgroundTask.execute(url);
            isTaskExecuting = true;
        }
    }

    public void cancelBackgroundTask() {
        if(isTaskExecuting){
            mBackgroundTask.cancel(true);
            isTaskExecuting = false;
        }
    }

    public void updateExecutingStatus(boolean isExecuting)
    {
        this.isTaskExecuting = isExecuting;
    }

}



