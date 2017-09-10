package com.example.TulasProject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    Animation  animation;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //to hide action bar
        getSupportActionBar().hide();
        //to hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.splash_logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        logo.startAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try{
                    Thread.sleep(3000);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}