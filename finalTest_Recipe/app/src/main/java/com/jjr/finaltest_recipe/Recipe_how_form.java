package com.jjr.finaltest_recipe;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.List;
import java.util.Locale;

public class Recipe_how_form extends AppCompatActivity {
    /* 레시피 */
    private DBHelper mydb;
    TextView recipeName;
    TextView recipeText;
    int id = 0;

    /* 타이머 */
    private TextView countdown;
    private Button countStart;
    private Button countReset;

    private EditText minuteText;
    private EditText secondsText;
    private TextView mi_seText;

    private long START_TIME_IN_MILLIS = 0; //타이머 시작 초


    private CountDownTimer countDownTimer;

    private boolean TimerRunning;

    private long TimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_how_form);


        /* 레시피 */
        recipeName = findViewById(R.id.recipeName);
        recipeText = findViewById(R.id.recipeText);

        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id = Value;
                rs.moveToFirst();
                String n = rs.getString(rs.getColumnIndex(DBHelper.RECIPE_COLUMN_NAME));
                String d = rs.getString(rs.getColumnIndex(DBHelper.RECIPE_COLUMN_TEXT));

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.recipeSave);
                b.setVisibility(View.INVISIBLE);
                recipeName.setText((CharSequence) n);
                recipeText.setText((CharSequence) d);
            }
        }

        /* 타이머 */
        countdown = findViewById(R.id.countdown);
        countStart = findViewById(R.id.countStart);
        countReset = findViewById(R.id.countReset);

        minuteText = findViewById(R.id.minuteText);
        secondsText = findViewById(R.id.secondsText);
        mi_seText = findViewById(R.id.textView4);


        countStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerRunning)
                    pauseTimer();
                else
                    startTimer();
            }
        });

        countReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDown();

    }


    /* 레시피 */
    public void insert(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateRecipe(id, recipeName.getText().toString(),
                        recipeText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정완료!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent =
                            new Intent(getApplicationContext(), com.jjr.finaltest_recipe.MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "수정 실패", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertRecipe(recipeName.getText().toString(),
                        recipeText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "추가 완료!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "추가 실패", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }

    public void delete(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                mydb.deleteRecipe(id);
                Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void edit(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");
            if (value > 0) {
                if (mydb.updateRecipe(id, recipeName.getText().toString(),
                        recipeText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정 완료!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "수정 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }





    /* 타이머 */

    private void startTimer() {
        try {
            //600000 = 10분
            minuteText.setVisibility(View.INVISIBLE);
            secondsText.setVisibility(View.INVISIBLE);
            mi_seText.setVisibility(View.INVISIBLE);
            countdown.setVisibility(View.VISIBLE);


            if ((TextUtils.isEmpty(minuteText.getText()))==true)
            {
                minuteText.setText("0");
            }
            if ((TextUtils.isEmpty(secondsText.getText()))==true)
            {
                secondsText.setText("0");
            }

            int MinuteText = (Integer.parseInt(minuteText.getText().toString()) * 60000);
            int SecondsText = (Integer.parseInt(secondsText.getText().toString()) * 1000);
            TimeLeftInMillis = (MinuteText + SecondsText);

            countDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TimeLeftInMillis = millisUntilFinished;
                    updateCountDown();
                }

                @Override
                public void onFinish() {
                    TimerRunning = false;
                    countStart.setVisibility(View.INVISIBLE);
                    countReset.setVisibility(View.VISIBLE);
                    minuteText.setText("");
                    secondsText.setText("");
                    Intent intent = new Intent(getApplicationContext(),Timer_bgm.class);
                    startActivity(intent);
                }
            }.start();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "시간을 제대로 입력해주세요!", Toast.LENGTH_LONG).show();
            countdown.setVisibility(View.INVISIBLE);
            minuteText.setVisibility(View.VISIBLE);
            secondsText.setVisibility(View.VISIBLE);
            mi_seText.setVisibility(View.VISIBLE);
        }

        TimerRunning = true;
        countStart.setText("일시정지");
        countReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        TimerRunning = false;
        countStart.setText("시작");
        countReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        countdown.setVisibility(View.INVISIBLE);
        minuteText.setVisibility(View.VISIBLE);
        secondsText.setVisibility(View.VISIBLE);
        minuteText.setText("");
        secondsText.setText("");
        mi_seText.setVisibility(View.VISIBLE);
        TimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDown();
        countReset.setVisibility(View.INVISIBLE);
        countStart.setVisibility(View.VISIBLE);
        countStart.setText("시작");
    }

    private void updateCountDown() {
        int minutes = (int) (TimeLeftInMillis / 1000) / 60;
        int seconds = (int) (TimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        countdown.setText(timeLeftFormatted);
    }
}
