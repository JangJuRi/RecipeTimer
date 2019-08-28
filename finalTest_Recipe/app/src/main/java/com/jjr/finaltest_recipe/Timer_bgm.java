package com.jjr.finaltest_recipe;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Timer_bgm extends AppCompatActivity {

    private Button timer;
    public MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_bgm);

        timer = (Button)findViewById(R.id.timer_close);
        mediaPlayer = MediaPlayer.create(this,R.raw.timer_bgm);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() { }
}
