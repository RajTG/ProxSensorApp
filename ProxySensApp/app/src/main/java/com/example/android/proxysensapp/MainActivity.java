package com.example.android.proxysensapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager sm;
    Sensor sensor;
    TextView count, numberz;
    MediaPlayer mediaPlayer;
    String songId;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songId = "song";
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        count = (TextView)findViewById(R.id.count);
        id = getResources().getIdentifier(songId, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sky);
        numberz = (TextView)findViewById(R.id.numberz);

    }

    CountDownTimer cdt = new CountDownTimer(10000, 1000){
        @Override
        public void onTick(long millisUntilFinished) {
            numberz.setTextSize(300);
            numberz.setText(String.valueOf(millisUntilFinished/1000));
        }

        @Override
        public void onFinish() {
            numberz.setTextSize(50);
            numberz.setGravity(Gravity.CENTER);
            numberz.setTextColor(0xAA00FF00);
            numberz.setText("DONE!");
            numberz.setBackgroundResource(R.drawable.border2);
            threadStarter();

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            count = (TextView)findViewById(R.id.count);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sky);
            mediaPlayer.start();
        }
    };


    public void threadStarter(){
        Runnable timer = new Runnable(){
            public void run(){

                handler.sendEmptyMessage(0);
            }
        };

        Thread thread = new Thread(timer);
        thread.start();

    }


    @Override
    public void onResume(){
        super.onResume();
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            if (event.values[0]==0) {
                count.setText("Near");
                cdt.start();

            }

            else {
                count.setText("Far");
                cdt.cancel();
                numberz.setText(" ");
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }


            }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
