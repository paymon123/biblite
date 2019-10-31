package com.example.biblite;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {


    /* ===========================================================================

    DEBUG PARAMS

    ============================================================================*/




    final static boolean TIMELINE_TESTING = false;
    final static int DATE_TESTING_WEEKS= 1;


    final static boolean RSS_TESTING = false;



/* ===========================================================================

    DEBUG PARAMS

    ============================================================================*/








    static int width = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        width = (size.x/3) ;


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if ( (!settings.contains("1"))) {


            //the app is being launched for first time, do something


            SharedPreferences.Editor editor = settings.edit();
            editor.putString("st", Long.toString(new Date().getTime()));


            editor.commit();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("1", false).commit();
        }

            else {
            try {
                Date d;
                if (TIMELINE_TESTING) {
                    if (settings.contains("test_anchor"))
                        d = new Date(settings.getLong("test_anchor", 0));
                    else d = new Date();

                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    c.add(Calendar.WEEK_OF_MONTH, DATE_TESTING_WEEKS);
                    d = c.getTime();
                    settings.edit().putLong("test_anchor", d.getTime()).commit();

                }
                else d = new Date();


                long diffInMillies = Math.abs(d.getTime() - Long.parseLong(settings.getString("st", Long.toString(new Date().getTime()))));



                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                PodcastEpisode.open = ((int)diff/7) + 3;
                PodcastEpisode.opens = 0 ;
            } catch (Exception e) {
            }

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }




}
