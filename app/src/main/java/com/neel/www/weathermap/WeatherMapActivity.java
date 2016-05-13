package com.neel.www.weathermap;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Nilesh Shinde on 07-04-2016.
 */
public class WeatherMapActivity extends AppCompatActivity implements HeadlessFragment.TaskStatusCallback,
        View.OnClickListener {

    private HeadlessFragment mFragment;
   // private ProgressBar mProgressBar;
    private TextView mProgressvalue;
    private ProgressDialog pDialog;
    RelativeLayout rel;


    /**
     * Called when activity is starting. Most initialization part is done here.
     */
    EditText edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weathermap);

        rel=(RelativeLayout)findViewById(R.id.relmain);
      //  mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //mProgressvalue = (TextView) findViewById(R.id.progressValue);
         edt=(EditText)findViewById(R.id.edttext);
        Button btn=(Button)findViewById(R.id.btnweather);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null)
                    mFragment.startBackgroundTask(Constants.City_URL+edt.getText().toString().trim(),WeatherMapActivity.this);
            }
        });

        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress_value");
            mProgressvalue.setText(progress + "%");
         //   mProgressBar.setProgress(progress);


        }
        FragmentManager mMgr = getFragmentManager();
        mFragment = (HeadlessFragment) mMgr
                .findFragmentByTag(HeadlessFragment.TAG_HEADLESS_FRAGMENT);

        if (mFragment == null) {
            mFragment = new HeadlessFragment();
            mMgr.beginTransaction()
                    .add(mFragment, HeadlessFragment.TAG_HEADLESS_FRAGMENT)

                    .commit();
        }
    }

    /**
     * This method is called before an activity may be killed Store info in
     * bundle if required.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  outState.putInt("progress_value", mProgressBar.getProgress());

    }

    // Background task Callbacks

    @Override
    public void onPreExecute() {
        showpDialog();
      /*  Toast.makeText(getApplicationContext(), "onPreExecute",
                Toast.LENGTH_SHORT).show();
*/

    }

    @Override
    public void onPostExecute(Vector<WeatherdataModel> result,Vector<Days> daysweather,String msg) {
       // Toast.makeText(getApplicationContext(), "onPostExecute"+ daysweather,
        //        Toast.LENGTH_SHORT).show();
        hidepDialog();
        if (mFragment != null)
            mFragment.updateExecutingStatus(false);
        if(result!= null  && daysweather != null && daysweather.size()>0 ) {

            Intent inter = new Intent(WeatherMapActivity.this, MainActivity.class);

            inter.putExtra("daytemp", daysweather);
            inter.putExtra("weathermodel", result);
            startActivity(inter);
        }
        else{
            if(msg==null || msg.length()==0)
                msg="No City found";
            Snackbar.make(rel,msg,Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onCancelled() {
        /*Toast.makeText(getApplicationContext(), "onCancelled",
                Toast.LENGTH_SHORT).show();*/
        if (mFragment != null)
            mFragment.updateExecutingStatus(false);
        hidepDialog();
        Snackbar.make(rel, "No City found", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onProgressUpdate(int progress) {
        mProgressvalue.setText(progress + "%");
       // mProgressBar.setProgress(progress);
    }

    /**
     * Called when a view has been clicked
     */
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.start:



                if (mFragment != null)
              //      mFragment.startBackgroundTask(Constants.City_URL,this);


                break;

          /*  case R.id.cancel:

                if (mFragment != null)
                    mFragment.cancelBackgroundTask();
                break;*/


        }
    }

    // Background task Callbacks

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}




