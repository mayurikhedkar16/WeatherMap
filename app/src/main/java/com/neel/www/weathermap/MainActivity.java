package com.neel.www.weathermap;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
   private static ArrayList<Days> days;
    private static ArrayList<WeatherdataModel> weatherdata;
CoordinatorLayout cordmain;
//62006d777f587f988dbbc1730db1caac
   // Sample 1. http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&cnt=14&APPID=xxxxx
 //   Sample 2. http://api.openweathermap.org/data/2.5/forecast/daily?q=Moscow&cnt=14&APPID=62006d777f587f988dbbc1730db1caac
   // http://openweathermap.org/API#forecast
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherdata = (ArrayList<WeatherdataModel>) getIntent().getSerializableExtra("weathermodel");
        days = (ArrayList<Days>) getIntent().getSerializableExtra("daytemp");

        cordmain=(CoordinatorLayout)findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Snackbar.make(cordmain, "Please scroll left for more>>>", Snackbar.LENGTH_LONG).show();


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView description = (TextView) rootView.findViewById(R.id.descdata);
            TextView speeddata = (TextView) rootView.findViewById(R.id.speedtxt);
            TextView clouddata = (TextView) rootView.findViewById(R.id.clouddatatxt);
            TextView userCity = (TextView) rootView.findViewById(R.id.usercity);
            TextView usertempdate = (TextView) rootView.findViewById(R.id.tempdate);
            Button btnmax = (Button) rootView.findViewById(R.id.maxtemp);
            Button btnmin = (Button) rootView.findViewById(R.id.mintemp);
            ImageView imgicon = (ImageView) rootView.findViewById(R.id.tempimage);

            Days daytemp=days.get(getArguments().getInt(ARG_SECTION_NUMBER));

            WeatherdataModel weathemodel=weatherdata.get(0);
            userCity.setText(weathemodel.getName());

            String finaldate= getcurrentDate(getArguments().getInt(ARG_SECTION_NUMBER));
            usertempdate.setText(finaldate);
            try {
                int resourceId = getResources().getIdentifier("p"+daytemp.getIcon().trim(), "drawable", "com.neel.www.weathermap");
                imgicon.setImageResource(resourceId);
            }
            catch(Exception er)
            {

            }

            NumberFormat nf = NumberFormat.getInstance(); // get instance
            nf.setMaximumFractionDigits(2); // set decimal places

            Double maxtemp=((Double.parseDouble(daytemp.getMax()))-273);
            String max = nf.format(maxtemp)+"\u00b0 C";
            btnmax.setText(max);

            Double mintemp=(Double.parseDouble(daytemp.getMin())-273);
            String min = nf.format(mintemp)+"\u00b0 C";

            btnmin.setText(min);

            description.setText(daytemp.getDescription());
            String finalspeed=daytemp.getSpeed()+"m/s";
            speeddata.setText(finalspeed);
            String cloudfinaldata="clouds: "+daytemp.getClouds()+"%, "+daytemp.getPressure()+" hpa";
            clouddata.setText(cloudfinaldata);



            return rootView;
        }
        public String getMonthfromDate(String datefield)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date myDate = null;
            try {
                myDate = dateFormat.parse(datefield);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", myDate); //Jun

            //SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MMdd");


            return stringMonth;
        }

        public String getcurrentDate(int days)
        {

            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);

            Date finaldate=cal.getTime();
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
            return df.format(finaldate);

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return days.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}
