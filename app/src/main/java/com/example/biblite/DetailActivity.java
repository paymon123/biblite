package com.example.biblite;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private  MediaPlayer mPlayer;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private int mLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String title = getIntent().getExtras().getString("title");
        String url = getIntent().getExtras().getString("url");

        if (MainActivity.width > 400)
        mLeft = (int)((double)MainActivity.width/3) ;
        else mLeft = (int)((double)MainActivity.width/4) ;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        backwardbtn = findViewById(R.id.btnBackward);
        forwardbtn = findViewById(R.id.btnForward);
        playbtn = findViewById(R.id.btnPlay);
        pausebtn = findViewById(R.id.btnPause);
        songName = findViewById(R.id.txtSname);
        startTime = findViewById(R.id.txtStartTime);
        songTime = findViewById(R.id.txtSongTime);
        songName.setText(title);


        RelativeLayout.LayoutParams params1= (RelativeLayout.LayoutParams) backwardbtn.getLayoutParams();
        params1.setMargins(mLeft, 0, 0, mLeft/2); //bottom margin is 25 here (change it as u wish)
        backwardbtn.setLayoutParams(params1);

        RelativeLayout.LayoutParams params2= (RelativeLayout.LayoutParams) forwardbtn.getLayoutParams();
        params2.setMargins(mLeft, 0, 0, mLeft/2); //bottom margin is 25 here (change it as u wish)
        forwardbtn.setLayoutParams(params2);

        RelativeLayout.LayoutParams params3= (RelativeLayout.LayoutParams) playbtn.getLayoutParams();
        params3.setMargins(mLeft, 0, 0, mLeft/2); //bottom margin is 25 here (change it as u wish)
        playbtn.setLayoutParams(params3);


        RelativeLayout.LayoutParams params4= (RelativeLayout.LayoutParams) pausebtn.getLayoutParams();
        params4.setMargins(mLeft, 0, 0, mLeft/2); //bottom margin is 25 here (change it as u wish)
        pausebtn.setLayoutParams(params4);

        Uri myUri =  Uri.parse(url);// initialize Uri here
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource( url);
            mPlayer.prepare();
            //mPlayer.start();
        }catch(Exception e){

        }
        songPrgs = findViewById(R.id.sBar);
        songPrgs.setClickable(false);
        pausebtn.setEnabled(false);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                mPlayer.start();
                eTime = mPlayer.getDuration();
                sTime = mPlayer.getCurrentPosition();
                if(oTime == 0){
                    songPrgs.setMax(eTime);
                    oTime =1;
                }
                songTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
                songPrgs.setProgress(sTime);
                hdlr.postDelayed(UpdateSongTime, 100);
                pausebtn.setEnabled(true);
                playbtn.setEnabled(false);
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                pausebtn.setEnabled(false);
                playbtn.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime + fTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime - bTime) > 0)
                {
                    sTime = sTime - bTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            if( (TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) > 9)
            startTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
        else
            startTime.setText(String.format("%d:0%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) ) ;
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };
}